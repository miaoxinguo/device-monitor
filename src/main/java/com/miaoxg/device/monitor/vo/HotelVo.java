package com.miaoxg.device.monitor.vo;

/**
 * 用于controller接收参数
 * 
 * @author miaoxinguo2002@163.com
 */
public class HotelVo extends Page{
    private Integer userId;  //维保人
    private Integer username;  //维保人
    private String name; // 酒店名

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getUsername() {
        return username;
    }
    public void setUsername(Integer username) {
        this.username = username;
    }
}
