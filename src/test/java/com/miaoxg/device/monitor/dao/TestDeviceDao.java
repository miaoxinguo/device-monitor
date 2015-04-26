package com.miaoxg.device.monitor.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miaoxg.device.monitor.entity.Device;
import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.vo.DeviceVo;
import com.miaoxg.device.monitor.vo.MonitorValueVo;

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
    
    @Test
    public void testSelectDevice(){
        // 构造测试数据
        MonitorValueVo vo = new MonitorValueVo();
        vo.setHotelId(1);
        vo.setRoom("6");
        vo.setiDisplayStart(0);
        vo.setiDisplayLength(10);
        
        List<String> sids = deviceDao.selectDeviceSidByHotel(vo);
        System.out.println(sids.size());
        Assert.assertTrue(sids.size() >= 0);
    }
    
    @Test
    public void testSelectDeviceByAdmin(){
        // 构造测试数据
        MonitorValueVo vo = new MonitorValueVo();
        vo.setUserId(0);
        vo.setiDisplayStart(0);
        vo.setiDisplayLength(10);
        
        List<String> sids = deviceDao.selectDeviceSidByUser(vo);
        System.out.println(sids.size());
        Assert.assertTrue(sids.size() >= 0);
    }
    
    /**
     * 测试 分页查询设备信息
     */
    @Test
    public void testSelectDevices(){
        // 构造测试数据
        DeviceVo vo = new DeviceVo();
        vo.setiDisplayStart(0);
        vo.setiDisplayLength(10);
        
        List<Device> sids = deviceDao.selectDevices(vo);
        System.out.println(sids.size());
        Assert.assertTrue(sids.size() >= 0);
    }
}
