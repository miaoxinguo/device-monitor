package com.miaoxg.device.monitor.entity;

import java.io.Serializable;

public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;            // 酒店名
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
