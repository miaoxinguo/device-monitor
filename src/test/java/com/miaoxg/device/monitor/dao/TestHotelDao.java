package com.miaoxg.device.monitor.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miaoxg.device.monitor.core.Role;
import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestHotelDao {
    @Resource
    private HotelDao hotelDao;
    
    @Test
    public void testSelectHotelNames(){
        // 构造测试数据
        User user = new User();
        user.setId(3);
        user.setRole(Role.maintainer);
        List<Hotel> list = hotelDao.selectNamesByUser(user);
        Assert.assertNotNull(list);
    }
    
    @Test
    public void testInsert(){
        // 构造测试数据
        Hotel hotel = new Hotel();
        hotel.setName("威海卫大酒店");
        Integer id = hotelDao.insertHotel(hotel);
        System.out.println(id);
        Assert.assertTrue(id > 0);
    }
}
