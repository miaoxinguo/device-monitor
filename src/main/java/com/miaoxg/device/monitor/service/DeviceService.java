package com.miaoxg.device.monitor.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaoxg.device.monitor.core.Constants;
import com.miaoxg.device.monitor.core.MonitorValueCache;
import com.miaoxg.device.monitor.dao.DeviceDao;
import com.miaoxg.device.monitor.dao.HotelDao;
import com.miaoxg.device.monitor.entity.Device;
import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.DeviceVo;
import com.miaoxg.device.monitor.vo.MonitorValueVo;

@Service
@Transactional
public class DeviceService {
    private final static Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private final String FLAG_OFFLINE       = "&1";
    private final String FLAG_ID            = "&A";
    private final String FLAG_NAME          = "&U";       // 设备名
    private final String FLAG_TEMPERATURE   = "&B";       // 温度
    private final String FLAG_HUMIDITY      = "&C";       // 湿度
    private final String FLAG_CO2           = "&D";       // CO2
    private final String FLAG_NH3           = "&E";       // NH3
    private final String FLAG_IS_OPEN       = "&H";       // 是否开机
    private final String FLAG_USED_HOURS    = "&S";       // N滤网使用时间
    
    private final String DEVICE_CLOSED      = "0";        // 关机
    
    @Resource
    private DeviceDao deviceDao;
    @Resource
    private HotelDao hotelDao;
    
    /**
     * 新增设备
     */
    public void addDevice(Device device) {
        /*
         * 验证+更新缓存
         */
        deviceDao.insertDevice(device);
        
        getRemoteMonitorValue(device.getSid());
    }
    
    /**
     * 删除设备
     */
    public void removeDevice(String sid) {
        deviceDao.deleteDevice(sid);
    }
    
    /**
     * 修改设备
     */
    public void modifyDevice(Device device) {
        deviceDao.updateDevice(device);
    }
    
    /**
     * 批量更新滤网已用时长
     */
    public void updateUsedHours(Collection<MonitorValue> values) {
        deviceDao.updateUsedHours(values);
    }
    
    /**
     * 修改设备名并推动到平台
     */
    public void rename(String deviceId, String newName) {
        
    }
    
    /**
     * 分页获取设备的监测数据
     */
    public DataTablesVo<MonitorValue> getMonitorValue(MonitorValueVo vo) {
        List<String> sidList = null;
        int count = 0;
        
        if(vo.getUserId() != null && vo.getUserId()== 0){        // TODO 如果管理员不止一个，这里要改为根据权限是否为管理员
            sidList = deviceDao.selectAllDeviceSid(vo);
            count = deviceDao.selectAllDeviceSidCount(vo);
        }
        else if(vo.getHotelId() == 0){  // 取该用户所有酒店的所有设备
            sidList = deviceDao.selectDeviceSidByUser(vo);
            count = deviceDao.selectDeviceCountByUser(vo);
        }
        else{                      // 取指定酒店的所有设备   
            sidList = deviceDao.selectDeviceSidByHotel(vo);
            count = deviceDao.selectDeviceCountByHotel(vo);
        }
        logger.debug("device is {}, count is {}", sidList, count);
        
        // 从缓存查监测值
        List<MonitorValue> mvList = count>0 ? MonitorValueCache.INSTANCE.get(sidList) : new ArrayList<MonitorValue>();
        return new DataTablesVo<MonitorValue>(count, mvList);
       
    }

    /**
     * 分页获取设备信息
     */
    public DataTablesVo<Device> getDevices(DeviceVo vo) {
        List<Device> deviceList = deviceDao.selectDevices(vo);
        int count = deviceDao.selectDevicesCount(vo);
        
        return new DataTablesVo<Device>(count, deviceList);
    }
    
    /**
     * 根据sid获取设备信息
     */
    public Device getDeviceInfo(String sid) {
        return deviceDao.selectDevice(sid);
    }

    /**
     * 从平台获取设备信息，如果存在取监测数值
     * 
     * TODO 现在的接口无法判断设备是否存在
     */
    public void getRemoteMonitorValue(String sid) {
        DatagramSocket client = null;
        try {
            client = new DatagramSocket();
            client.setSoTimeout(20000);
        } catch (SocketException e) {
           throw new RuntimeException("连接服务器失败");
        }
        
        // 构造地址
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(Constants.SERVER_IP);
        } catch (UnknownHostException e) {
            client.close();
            throw new RuntimeException("连接服务器失败");
        }
        
        // 在缓存中取到所有的设备标识
        byte[] request = ("LNGNUM&" + sid).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(request, request.length, addr, Constants.SERVER_PORT);
        
        MonitorValue obj = null;
        try {
            client.send(sendPacket);
        
            byte[] recvBuf = new byte[256];
            DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
            client.receive(recvPacket);
            String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
            
            logger.debug("收到:{}", recvStr);
            obj = analyzeMessage(recvStr);
        } 
        catch (IOException e) {
            // 获取某设备监测值失败，继续处理下一设备
            logger.error("获取设备：{}监测值失败", sid, e);
        } 
        client.close();
        
        // 存入数据库
        logger.debug("持久化最新数据到数据库");
//        deviceDao.insertMonitorValue(tempList);
        
        // 同步到缓存
        logger.debug("同步最新数据到缓存");
        MonitorValueCache.INSTANCE.update(sid, obj);
    }
    
    /**
     * 从从平台获取监测数值，存入数据库和缓存
     */
    public void getRemoteMonitorValue() {
        /*
         *  创建socket客户端
         *  
         *  现在的问题是： 平台不支持多线程, 且每一次socket通信都很慢，如果设备多，必然超过前台页面刷新周期（30s）
         *  
         *  TODO 平台必须提供批量查询或支持多线程，按平台接口修改方法
         */
        DatagramSocket client = null;
        try {
            client = new DatagramSocket();
            client.setSoTimeout(20000);
        } catch (SocketException e) {
           throw new RuntimeException("连接服务器失败");
        }
        
        // 构造地址
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(Constants.SERVER_IP);
        } catch (UnknownHostException e) {
            client.close();
            throw new RuntimeException("连接服务器失败");
        }
        
        // 从平台获取数据，存入 数据库 和缓存
        List<MonitorValue> tempList = new ArrayList<MonitorValue>();
        
        // 在缓存中取到所有的设备标识
        for(String devId : MonitorValueCache.INSTANCE.getAllDeviceId()){
            byte[] request = ("LNGNUM&" + devId).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(request, request.length, addr, Constants.SERVER_PORT);
            try {
                client.send(sendPacket);
            
                byte[] recvBuf = new byte[256];
                DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
                client.receive(recvPacket);
                String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
                
                logger.debug("收到:{}", recvStr);
                MonitorValue obj = analyzeMessage(recvStr);
                tempList.add(obj);
            } 
            catch (IOException e) {
                // 获取某设备监测值失败，继续处理下一设备
                logger.error("获取设备：{}监测值失败", devId, e);
                continue;
            } 
        }
        client.close();
        
        // 存入数据库
        logger.debug("持久化最新数据到数据库");
        deviceDao.insertMonitorValue(tempList);
        
        // 同步到缓存
        logger.debug("同步最新数据到缓存");
        MonitorValueCache.INSTANCE.update(tempList);
    }
    
    /**
     * 解析服务器返回的数据
     */
    private MonitorValue analyzeMessage(String msg) {
        MonitorValue obj = new MonitorValue();
        
        // sid
        int idBeginIndex = msg.indexOf(FLAG_ID)+3;
        obj.setDeviceSid(msg.substring(idBeginIndex, msg.indexOf("&", idBeginIndex)));
        
        // 设备名默认设为sid
        obj.setDeviceName(obj.getDeviceSid());
        
        // 是否在线
        obj.setOnLine(!msg.contains(FLAG_OFFLINE));
        
        // 如果在线，继续封装信息
        if(obj.isOnLine()){
            // 设备名
            if(msg.contains(FLAG_NAME)){
                int nameBeginIndex = msg.indexOf(FLAG_NAME) + 3;
                obj.setDeviceName(msg.substring(nameBeginIndex, msg.indexOf("&", nameBeginIndex)));
            }
            
            // 温度
            int temperatureBeginIndex = msg.indexOf(FLAG_TEMPERATURE) + 3;
            obj.setTemperature(Float.valueOf( msg.substring(temperatureBeginIndex, msg.indexOf("&", temperatureBeginIndex))));
            
            // 湿度
            int humidityBeginIndex = msg.indexOf(FLAG_HUMIDITY) + 3;
            obj.setHumidity(Float.valueOf(msg.substring(humidityBeginIndex, msg.indexOf("&", humidityBeginIndex))));
            
            // CO2
            int co2BeginIndex = msg.indexOf(FLAG_CO2) + 3;
            obj.setCo2(Float.valueOf(msg.substring(co2BeginIndex, msg.indexOf("&", co2BeginIndex))));
            
            // NH3
            int nh3BeginIndex = msg.indexOf(FLAG_NH3) + 3;
            obj.setNh3(Float.valueOf(msg.substring(nh3BeginIndex, msg.indexOf("&", nh3BeginIndex))));
            
            // 开机/关机
            int isOpenBeginIndex = msg.indexOf(FLAG_IS_OPEN) + 3;
            String isDeviceClosed = msg.substring(isOpenBeginIndex, msg.indexOf("&", isOpenBeginIndex));
            obj.setOpen(!DEVICE_CLOSED.equals(isDeviceClosed));
            
            // 滤网使用时长
            int usedHoursBeginIndex = msg.indexOf(FLAG_USED_HOURS) + 3;
            String UserdHours = msg.substring(usedHoursBeginIndex, msg.indexOf(",", usedHoursBeginIndex));
            obj.setUsedHours(Integer.valueOf(UserdHours));
        }
        else{
            obj.setUsedHours(-1);  // 如果离线默认值为0，会把已用时间覆盖掉
        }
        return obj;
    }
}
