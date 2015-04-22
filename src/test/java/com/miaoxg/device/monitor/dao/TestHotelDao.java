package com.miaoxg.device.monitor.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miaoxg.device.monitor.entity.Hotel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestHotelDao {
    @Resource
    private HotelDao hotelDao;
    
    @Test
    public void testSelectHotelNames(){
        // 构造测试数据
        List<Hotel> list = hotelDao.selectHotelByUser(3);
        Assert.assertNotNull(list);
    }
}
