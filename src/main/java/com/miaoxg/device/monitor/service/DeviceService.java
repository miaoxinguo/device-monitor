package com.miaoxg.device.monitor.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.miaoxg.device.monitor.core.Constants;

@Service
public class DeviceService {
    private final String FLAG_OFFLINE       = "&1";
    private final String FLAG_ID            = "&A";
    private final String FLAG_NAME          = "&U";       // 设备名
    private final String FLAG_TEMPERATURE   = "&B";       // 温度
    private final String FLAG_HUMIDITY      = "&C";       // 湿度
    private final String FLAG_CO2           = "&D";       // CO2
    private final String FLAG_NH3           = "&E";       // NH3
    private final String FLAG_IS_OPEN       = "&H";       // 是否开机
    private final String DEVICE_CLOSED      = "0";        // 关机
     
    /**
     * 创建连接
     */
    public DatagramSocket createClient() {
        // 创建客户端
        DatagramSocket client = null;
        try {
            client = new DatagramSocket();
            client.setSoTimeout(20000);
        } catch (SocketException e) {
           throw new RuntimeException("连接服务器失败");
        }
        return client;
    }
    
    /**
     * 根据设备编号从中心服务器获取该设备的数据
     */
    public void getRemoteDeviceInfo(String deviceId) {
        DatagramSocket client = createClient();
        // 构造地址
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(Constants.SERVER_IP);
        } catch (UnknownHostException e) {
            client.close();
            throw new RuntimeException("连接服务器失败");
        }
        
        String sendStr = "LNGNUM&" + deviceId;
        byte[] sendBuf = sendStr.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf ,sendBuf.length , addr , Constants.SERVER_PORT);
        
        
        DatagramPacket recvPacket = null;
        try {
            client.send(sendPacket);
            byte[] recvBuf = new byte[1024];
            recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
            client.receive(recvPacket);
        } catch (IOException e) {
            // ingore
            e.printStackTrace();
        }
        
        String recvStr = new String(recvPacket.getData() , 0, recvPacket.getLength());
        System.out.println("收到:" + recvStr);
        client.close();
    }
    
    /**
     * 批量获取信息
     */
    public JSONArray getRemoteDeviceInfo(List<String> deviceList) {
        DatagramSocket client = createClient();
        // 构造地址
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(Constants.SERVER_IP);
        } catch (UnknownHostException e) {
            client.close();
            throw new RuntimeException("连接服务器失败");
        }
        
        // 获取并封装数据
        JSONArray arr = new JSONArray();
        for(String devId : deviceList){
            byte[] request = ("LNGNUM&" + devId).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(request, request.length, addr , Constants.SERVER_PORT);
            try {
                client.send(sendPacket);
            
                byte[] recvBuf = new byte[1024];
                DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
                client.receive(recvPacket);
                String recvStr = new String(recvPacket.getData() , 0, recvPacket.getLength());
                
                //System.out.println("收到:" + recvStr);
                arr.add(analyzeMessage(recvStr));
            } catch (IOException e) {
                // ingore
                e.printStackTrace();
            } 
        }
        client.close();
        return arr;
    }
    
    /**
     * 修改设备名并推动到中心服务器
     */
    public void rename(String deviceId, String newName) throws IOException {
        
    }
    
    /**
     * 解析服务器返回的数据，封装为一个json对象
     */
    private JSONObject analyzeMessage(String msg) {
        JSONObject obj = new JSONObject();
        
        // id
        int idBeginIndex = msg.indexOf(FLAG_ID)+3;
        obj.put("id", msg.substring(idBeginIndex, msg.indexOf("&", idBeginIndex)));
        
        // 设备名默认设为id
        obj.put("devname", obj.getString("id")); 
        
        // status 在线or离线
        boolean isOffLine = msg.contains(FLAG_OFFLINE);
        obj.put("status", isOffLine ? "离线" : "在线");
        
        // 如果在线，继续封装信息
        if(!isOffLine){
            // 设备名
            if(msg.contains(FLAG_NAME)){
                int nameBeginIndex = msg.indexOf(FLAG_NAME) + 3;
                obj.put("devname", msg.substring(nameBeginIndex, msg.indexOf("&", nameBeginIndex)));
            }
            
            // 温度
            int temperatureBeginIndex = msg.indexOf(FLAG_TEMPERATURE) + 3;
            obj.put("temperature", msg.substring(temperatureBeginIndex, msg.indexOf("&", temperatureBeginIndex)));
            
            // 湿度
            int humidityBeginIndex = msg.indexOf(FLAG_HUMIDITY) + 3;
            obj.put("humidity", msg.substring(humidityBeginIndex, msg.indexOf("&", humidityBeginIndex)));
            
            // CO2
            int co2BeginIndex = msg.indexOf(FLAG_CO2) + 3;
            obj.put("co2", msg.substring(co2BeginIndex, msg.indexOf("&", co2BeginIndex)));
            
            // NH3
            int nh3BeginIndex = msg.indexOf(FLAG_NH3) + 3;
            obj.put("nh3", msg.substring(nh3BeginIndex, msg.indexOf("&", nh3BeginIndex)));
            
            // 开机/关机
            int isOpenBeginIndex = msg.indexOf(FLAG_IS_OPEN) + 3;
            String isDeviceClosed = msg.substring(isOpenBeginIndex, msg.indexOf("&", isOpenBeginIndex));
            obj.put("isOpen", DEVICE_CLOSED.equals(isDeviceClosed)? "关": "开");
        }
        else{ //设置默认值
            obj.put("temperature", "-");
            obj.put("humidity", "-");
            obj.put("co2", "-");
            obj.put("nh3", "-");
            obj.put("isOpen", "-");
        }
        
        return obj;
    }
}
