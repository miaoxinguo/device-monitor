package com.miaoxg.device.monitor.service;

import org.junit.Test;

public class TestDeviceService {

    @Test
    public void testGetRemoteDeviceInfo() throws Exception{
        DeviceService ds = new DeviceService();
        ds.getRemoteDeviceInfo("380058686739");
        ds.getRemoteDeviceInfo("380058631466");
    }
}

