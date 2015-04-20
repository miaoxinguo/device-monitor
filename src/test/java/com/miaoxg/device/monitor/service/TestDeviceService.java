package com.miaoxg.device.monitor.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestDeviceService {
    @Resource
    private DeviceService deviceService;
    
    @Test
    public void testgetRemoteMonitorValue() throws Exception{
        deviceService.getRemoteMonitorValue();  // 从平台去数据 并存入数据库和缓存
    }
}

