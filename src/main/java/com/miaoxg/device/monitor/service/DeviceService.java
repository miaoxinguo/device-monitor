package com.miaoxg.device.monitor.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.springframework.stereotype.Service;

import com.miaoxg.device.monitor.core.Constants;

@Service
public class DeviceService {
    
    /**
     * 根据设备编号从中心服务器获取该设备的数据
     * @throws IOException 
     */
    public void getRemoteDeviceInfo(String deviceId) throws IOException {
        DatagramSocket client = new DatagramSocket();
        
        String sendStr = "LNGNUM&" + deviceId;
        byte[] sendBuf = sendStr.getBytes();
        InetAddress addr = InetAddress.getByName(Constants.SERVER_IP);
        DatagramPacket sendPacket = new DatagramPacket(sendBuf ,sendBuf.length , addr , Constants.SERVER_PORT);
        client.send(sendPacket);
        
        byte[] recvBuf = new byte[1024];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
        client.receive(recvPacket);
        String recvStr = new String(recvPacket.getData() , 0 ,recvPacket.getLength());
        System.out.println("收到:" + recvStr);
        client.close();
    }
}
