package com.miaoxg.device.monitor.controller;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miaoxg.device.monitor.vo.MonitorValueVo;
import com.miaoxg.device.monitor.web.controller.DeviceController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml","classpath:dispatcher.xml"})
public class TestDeviceController {

    @Resource
    private DeviceController deviceController;
    
    @Test
    public void testGetMonitorValue(){
        MonitorValueVo vo = new MonitorValueVo();
        vo.setHotelId(1);
        System.out.println(deviceController.getMonitorValues(vo, null));
    }
}
