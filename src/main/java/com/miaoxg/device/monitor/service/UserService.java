package com.miaoxg.device.monitor.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaoxg.device.monitor.core.Role;
import com.miaoxg.device.monitor.core.ServiceException;
import com.miaoxg.device.monitor.dao.UserDao;
import com.miaoxg.device.monitor.dao.UserHotelDao;
import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.UserVo;

@Service
@Transactional
public class UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserHotelDao userHotelDao;
    
    /**
     * 用户登录
     */
    public User login(String name, String password){
        User user = null;
        try {
            user = userDao.selectUserByName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new ServiceException("用户："+name+" 不存在");
        }
        
        // 验证密码
        if(!password.equals(user.getPassword())){
            throw new ServiceException("密码错误");
        }
        user.setPassword(""); // user会存入session，不保存密码
        return user;
    }
    
    /**
     * 新增用户
     */
    public void addUser(User user) {
        // 检测是否重名
        if(existUser(user.getName())){
            throw new ServiceException("用户名已存在");
        }
        
        Integer userId = userDao.insert(user);
        // 如果一个人拥有的酒店量很多，这里改成批量操作
        // TODO 删除该酒店与其他维保人的关联
        for(Hotel hotel : user.getHotels()){
            userHotelDao.insert(userId, hotel.getId());
        }
    }
    
    /**
     * 删除用户
     */
    public void removeUser(Integer id) {
        userDao.delete(id);
        userHotelDao.deletetByUser(id);
    }
    
    /**
     * 修改用户
     */
    public void modifyUser(User user) {
//        userDao.update(user);  // 不允许修改
        userHotelDao.deletetByUser(user.getId());
        if(user.getHotels()==null){
            return;
        }
        // 如果一个人拥有的酒店量很多，这里改成批量操作
        for(Hotel hotel : user.getHotels()){
            // 如果是修改维保人权限，删除该酒店与其他维保人的关联
            if(user.getRole().toString().equals(Role.maintainer.toString())){
                userHotelDao.deletetMaintainerHotel(hotel.getId());
            }
            userHotelDao.insert(user.getId(), hotel.getId());
        }
    }
    
    /**
     * 改密码
     */
    public void modifyPassword(String name, String newPw, String oldPw){
        User user = null;
        try {
            user = userDao.selectUserByName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new ServiceException("用户："+name+" 不存在");
        }
        
        // 验证密码
        if(!oldPw.equals(user.getPassword())){
            throw new ServiceException("原密码错误");
        }
        userDao.updatePw(name, newPw);
    }
    
    /**
     * 判断用户是否存在
     */
    public boolean existUser(String name){
        return userDao.selectCount(name) > 0 ? true : false;
    }
    
    /**
     * 判断用户是否存在
     */
    public User getUserDetail(Integer id){
        User user = userDao.selectUserDetail(id);
        user.setHotels(userHotelDao.selectHotelsByUser(id));
        return user;
    }
    
    /**
     * 分页查询符合记录的用户、符合条件的用户总数
     */
    public DataTablesVo<User> getUsers(UserVo vo){
        int count = userDao.selectCount(vo);                 // 符合条件的记录总数
        List<User> userList = userDao.selectList(vo);  
        return new DataTablesVo<User>(count, userList);
    }
    
    /**
     * 获取维保人员 -用于下拉列表
     */
    public List<Map<String, Object>> getMaintainerNames(){
        return userDao.selectMaintainerNames();
    }
}
