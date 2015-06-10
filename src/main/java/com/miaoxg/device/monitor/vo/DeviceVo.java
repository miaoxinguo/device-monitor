package com.miaoxg.device.monitor.vo;

/**
 * 用于controller接收参数
 * 
 * @author miaoxinguo2002@163.com
 */
public class DeviceVo extends Page{
    private Integer userId;
    private Integer hotelId;
    private String room;
    private int usedHoursNotShorter;
    private int usedHoursNotLonger;

    public int getUsedHoursNotShorter() {
        return usedHoursNotShorter;
    }
    public void setUsedHoursNotShorter(int usedHoursNotShorter) {
        this.usedHoursNotShorter = usedHoursNotShorter;
    }
    public int getUsedHoursNotLonger() {
        return usedHoursNotLonger;
    }
    public void setUsedHoursNotLonger(int usedHoursNotLonger) {
        this.usedHoursNotLonger = usedHoursNotLonger;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getHotelId() {
        return hotelId;
    }
    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }
    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
}
