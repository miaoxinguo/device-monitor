package com.miaoxg.device.monitor.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miaoxg.device.monitor.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestUserlDao {
    @Resource
    private UserDao userDao;
    
    @Test
    public void testSelectUserInfo(){
        User user = userDao.selectUserDetail(2);
        Assert.assertNotNull(user);
    }
}
