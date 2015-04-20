package com.miaoxg.device.monitor.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.miaoxg.device.monitor.dao.DeviceDao;
import com.miaoxg.device.monitor.entity.MonitorValue;

/**
 * 监测值缓存 - 仅存储从服务器获取的最新监测值
 * 
 * @author miaoxinguo2002@163.com
 */
public enum MonitorValueCache {
    INSTANCE;
    
    private MonitorValueCache(){
        // ingore
    }
    
    /**
     * Map&lt;设备编号, 设备最新的监测值>
     * 
     */
    private static final Map<String, MonitorValue> cache = new HashMap<String, MonitorValue>();

    /**
     * 获取缓存中所有的设备id
     */
    public Set<String> getAllDeviceId() {
        return cache.keySet();
    }

    /**
     * 系统启动时从数据库加载所有设备编号，并立即获取监测值
     */
    public void load(){
        // 从数据库中加载
        DeviceDao deviceDao = ApplicationContextHolder.getBean("deviceDao", DeviceDao.class);
        List<String> list = deviceDao.selectAllDeviceSid();
        for(String devSid : list){
            cache.put(devSid, null);
        }
    }
}
