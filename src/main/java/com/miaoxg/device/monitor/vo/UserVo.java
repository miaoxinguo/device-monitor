package com.miaoxg.device.monitor.vo;

/**
 * 用于controller接收参数
 * 
 * @author miaoxinguo2002@163.com
 */
public class UserVo extends Page{
    private Integer id;
    private String name;
    private String role;

    public Integer getId() {
        return id;
    }
    public void setId(Integer userId) {
        this.id = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
