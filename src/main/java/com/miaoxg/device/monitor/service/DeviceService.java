package com.miaoxg.device.monitor.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaoxg.device.monitor.core.Constants;
import com.miaoxg.device.monitor.core.MonitorValueCache;
import com.miaoxg.device.monitor.dao.DeviceDao;
import com.miaoxg.device.monitor.entity.MonitorValue;

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
    private final String DEVICE_CLOSED      = "0";        // 关机
    
    @Resource
    private DeviceDao deviceDao;
    
    /**
     * 修改设备名并推动到平台
     */
    public void rename(String deviceId, String newName) {
        
    }
    
    /**
     * 根据酒店标识从本地缓存获取设备的监测数据
     */
    public List<MonitorValue> getMonitorValueByHotel(Integer hotelId) {
        return null;
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
        deviceDao.insertMonitorValue(tempList);
        
        ///TODO 同步到缓存
        MonitorValueCache.values();
    }
    
    /**
     * 解析服务器返回的数据
     */
    private MonitorValue analyzeMessage(String msg) {
        MonitorValue obj = new MonitorValue();
        
        // id
        int idBeginIndex = msg.indexOf(FLAG_ID)+3;
        obj.setDeviceSid(msg.substring(idBeginIndex, msg.indexOf("&", idBeginIndex)));
        
        // 设备名默认设为id
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
        }
        
        return obj;
    }
}
