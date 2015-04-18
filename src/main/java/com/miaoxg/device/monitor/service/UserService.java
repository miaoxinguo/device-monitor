package com.miaoxg.device.monitor.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaoxg.device.monitor.core.ServiceException;
import com.miaoxg.device.monitor.dao.UserDao;
import com.miaoxg.device.monitor.entity.User;

@Service
@Transactional
public class UserService {
    @Resource
    private UserDao userDao;
    
    /**
     * 分页查询符合记录的用户、符合条件的用户总数
     */
    public Object[] getListAndTotalCount(Map<String, Object> param){
        int count = userDao.selectCount(param);                 // 符合条件的记录总数
        List<User> userList = userDao.selectList(param);  
        return new Object[]{count, userList};
    }
    
    /**
     * 用户登录
     */
    public User login(String name, String password){
        User user = userDao.selectUserByName(name);
        if(user == null){
            throw new ServiceException("用户："+name+" 不存在");
        }
        
        // 验证密码
        if(!password.equals(user.getPassword())){
            throw new ServiceException("密码错误");
        }
        user.setPassword(""); // user会存入session，不保存密码
        return user;
    }
}
