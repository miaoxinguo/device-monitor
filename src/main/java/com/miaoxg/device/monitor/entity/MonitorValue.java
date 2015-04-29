package com.miaoxg.device.monitor.entity;

import java.io.Serializable;

/**
 * 设备监测值
 * 
 * @author miaoxinguo2002@163.com
 */
public class MonitorValue implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Integer deviceId;
    private String deviceSid;
    private String deviceName;
    
    private boolean isOnLine;   // 是否在线
    private boolean isOpen;     // 是否开机
    
    private float temperature;
    private float humidity;
    private float co2;
    private float nh3;
    
    private int usedHours;  // 滤网已用时间
    
    public Integer getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceSid() {
        return deviceSid;
    }
    public void setDeviceSid(String deviceSid) {
        this.deviceSid = deviceSid;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public float getTemperature() {
        return temperature;
    }
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    public float getHumidity() {
        return humidity;
    }
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
    public float getCo2() {
        return co2;
    }
    public void setCo2(float co2) {
        this.co2 = co2;
    }
    public float getNh3() {
        return nh3;
    }
    public void setNh3(float nh3) {
        this.nh3 = nh3;
    }
    public boolean isOnLine() {
        return isOnLine;
    }
    public void setOnLine(boolean isOnLine) {
        this.isOnLine = isOnLine;
    }
    public boolean isOpen() {
        return isOpen;
    }
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public int getUsedHours() {
        return usedHours;
    }
    public void setUsedHours(int usedHours) {
        this.usedHours = usedHours;
    }
}