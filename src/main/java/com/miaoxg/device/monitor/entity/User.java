package com.miaoxg.device.monitor.entity;

import java.io.Serializable;
import java.util.List;

import com.miaoxg.device.monitor.core.Role;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private String password;
    private Role role;
    private List<Hotel> hotels;        
    
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRoleName() {
        return role.getRoleName();
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    } 
    public void setRole(String role) {
        this.role = Role.valueOf(role);
    }
    public List<Hotel> getHotels() {
        return hotels;
    }
    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    } 
}
