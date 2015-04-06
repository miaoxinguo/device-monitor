package com.miaoxg.device.monitor.service;

import org.junit.Test;

public class TestDeviceService {

    @Test
    public void testGetRemoteDeviceInfo() throws Exception{
        DeviceService ds = new DeviceService();
        ds.getRemoteDeviceInfo("000002");
        ds.getRemoteDeviceInfo("108697374431");
    }
}

