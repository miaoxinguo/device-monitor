package com.miaoxg.device.monitor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.entity.Hotel;

/**
 * 维护用户与酒店的关系
 * @author miaoxinguo2002@163.com
 */
@Repository
public class UserHotelDao extends BaseDao {
    
    /**
     * 插入
     */
    public int insert(Integer userId, Integer hotelId){
        String sql = "insert User_Hotel(user_id, hotel_id) values(?, ?)";
        return getJdbcTemplate().update(sql, userId, hotelId); 
    }
    
    /**
     * 删除
     */
    public int deletet(Integer user_id, Integer hotel_id){
        String sql = "delete from user_hotel where user_id = ? and hotel_id = ?";
        return getJdbcTemplate().update(sql, user_id, hotel_id); 
    }
    
    /**
     * 删除
     */
    public int deletetByHotel(Integer hotelId){
        String sql = "delete from user_hotel where hotel_id = ?";
        return getJdbcTemplate().update(sql, hotelId); 
    }
    
    /**
     * 删除某酒店与维保人的关联
     */
    public int deletetMaintainerHotel(Integer hotelId){
        String sql = "delete uh from user_hotel uh inner join user u on uh.user_id=u.id "
                + " where u.role='maintainer' and uh.hotel_id = ?";
        return getJdbcTemplate().update(sql, hotelId); 
    }
    
    /**
     * 删除
     */
    public int deletetByUser(Integer userId){
        String sql = "delete from user_hotel where user_id = ?";
        return getJdbcTemplate().update(sql, userId); 
    }
    
    /**
     * 根据userid 查酒店集合
     */
    public List<Hotel> selectHotelsByUser(Integer userId){
        String sql = "select hotel_id from user_hotel where user_id = ?";
        return getJdbcTemplate().query(sql, new RowMapper<Hotel>(){
            @Override
            public Hotel mapRow(ResultSet rs, int rowNum) throws SQLException {
                Hotel hotel = new Hotel();
                hotel.setId(rs.getInt("hotel_id"));
                return hotel;
            }
        }, userId); 
    }
}
