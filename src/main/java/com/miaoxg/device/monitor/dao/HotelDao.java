package com.miaoxg.device.monitor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.entity.Hotel;

@Repository
public class HotelDao extends BaseDao {
    
    /**
     * 查询所有的酒店id和name
     */
    public List<Hotel> selectHotelByUser(Integer userId){
        String sql = "";
        if(userId == 0){  // TODO 如果系统不止一个管理员，获取该管理员的角色判断
            sql = "select h.id, h.name from hotel h "; 
            return getJdbcTemplate().query(sql, new HotelRowMapper());
        }
        else{
            sql = "select h.id, h.name from hotel h "
                    + " join user_hotel uh on h.id = uh.hotel_id join user u on uh.user_id = u.id"
                    + " where u.id = ?"; 
            return getJdbcTemplate().query(sql, new HotelRowMapper(), userId);
        } 
    }
    
    /**
     * 插入
     */
    public int insertHotel(Hotel hotel){
        String sql = "insert Hotel(name) values(?)";
        return getJdbcTemplate().update(sql, hotel); 
    }
    
    /**
     * 封装对象
     */
    public class HotelRowMapper implements RowMapper<Hotel> {
        @Override
        public Hotel mapRow(ResultSet rs, int line) throws SQLException {
            Hotel hotel = new Hotel();
            hotel.setId(rs.getInt("id"));
            hotel.setName(rs.getString("name"));
            return hotel;
        }
        
    }
}
