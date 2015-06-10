package com.miaoxg.device.monitor.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class Device implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;     // 与业务无关的系统标识
    private String sid;     // 设备编号，唯一标识
    private String name;
    private String room;   // 具体房间号
    private Hotel hotel;   // 所属酒店
    
    private String sn;     // 滤网二维码
    
    @DateTimeFormat(pattern="yyyy-MM-dd")   
    private LocalDate regDate;   // 滤网注册日期
    
    public String getSn() {
        return sn;
    }
    public void setSn(String sn) {
        this.sn = sn;
    }
    public LocalDate getRegDate() {
        return regDate;
    }
    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }
    private int usedHours;  // 滤网已使用时长（小时）
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public Hotel getHotel() {
        return hotel;
    }
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    public int getUsedHours() {
        return usedHours;
    }
    public void setUsedHours(int usedHours) {
        this.usedHours = usedHours;
    }
}
