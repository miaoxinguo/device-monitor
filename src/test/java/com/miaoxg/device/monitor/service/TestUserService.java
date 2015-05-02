package com.miaoxg.device.monitor.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miaoxg.device.monitor.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestUserService {
    @Resource
    private UserService userService;
    
    @Test
    public void testGetUserDetail() throws Exception{
        User user = userService.getUserDetail(2);
        System.out.println(user.getName());
        System.out.println(user.getHotels().size());
    }
}

