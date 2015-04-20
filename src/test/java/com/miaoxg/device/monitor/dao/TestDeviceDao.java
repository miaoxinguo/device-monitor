package com.miaoxg.device.monitor.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miaoxg.device.monitor.dao.DeviceDao;
import com.miaoxg.device.monitor.entity.MonitorValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestDeviceDao {
    @Resource
    private DeviceDao deviceDao;
    
    @Test
    public void testBatchInsertMonitorValue(){
        // 构造测试数据
        List<MonitorValue> list = new ArrayList<MonitorValue>();
        for(int i=0; i<5; i++){
            MonitorValue mv = new MonitorValue();
            mv.setDeviceId(10000+i);
            mv.setDeviceSid("00001230000" + i);
            mv.setHumidity(26f);
            mv.setHumidity(56f);
            mv.setCo2(1f);
            mv.setNh3(1f);
            list.add(mv);
        }
        deviceDao.insertMonitorValue(list);
    }
}
