package com.miaoxg.device.monitor.core;

/**
 * 系统角色
 * 
 * @author miaoxinguo2002@163.com
 */
public enum Role {
    admin("系统管理员"), waiter("酒店人员"), maintainer("维保人员");    // 酒店人员、维保人员
    
    private String roleName;
    
    private Role(String name){
        this.roleName = name;
    }
    
    public String getRoleName(){
        return this.roleName;
    }
    
}
