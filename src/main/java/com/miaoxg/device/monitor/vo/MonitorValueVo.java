package com.miaoxg.device.monitor.vo;

/**
 * 用于controller接收参数
 * 
 * @author miaoxinguo2002@163.com
 */
public class MonitorValueVo extends Page{
    private Integer hotelId;
    private Integer userId;
    private String room;

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
