package com.miaoxg.device.monitor.dao;

import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.User;

/**
 * 维护用户与酒店的关系
 * @author miaoxinguo2002@163.com
 */
@Repository
public class UserHotelDao extends BaseDao {
    
    /**
     * 插入
     */
    public int insert(User user, Hotel hotel){
        String sql = "insert User_Hotel(user_id, hotel_id) values(?, ?)";
        return getJdbcTemplate().update(sql, user.getId(), hotel.getId()); 
    }
    
    /**
     * 删除
     */
    public int deletet(Integer user_id, Integer hotel_id){
        String sql = "delete user_hotel where user_id = ? and hotel_id = ?";
        return getJdbcTemplate().update(sql, user_id, hotel_id); 
    }
    
    /**
     * 删除
     */
    public int deletetByHotel(Integer hotelId){
        String sql = "delete user_hotel where hotel_id = ?";
        return getJdbcTemplate().update(sql, hotelId); 
    }
    
    /**
     * 删除
     */
    public int deletetByUser(Integer userId){
        String sql = "delete user_hotel where user_id = ?";
        return getJdbcTemplate().update(sql, userId); 
    }
}
