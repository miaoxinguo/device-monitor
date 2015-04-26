package com.miaoxg.device.monitor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.core.Role;
import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.vo.HotelVo;

@Repository
public class HotelDao extends BaseDao {
    
    private static final Logger logger = LoggerFactory.getLogger(HotelDao.class);
    
    /**
     * 插入
     */
    public int insertHotel(Hotel hotel){
        String sql = "insert Hotel(name) values(?)";
        return getJdbcTemplate().update(sql, hotel); 
    }
    
    /**
     * 删除
     */
    public int deletetHotel(Integer id){
        String sql = "delete hotel where id = ?";
        return getJdbcTemplate().update(sql, id); 
    }
    
    /**
     * 修改
     */
    public int updateHotel(Hotel hotel){
        String sql = "update hotel set name = ? where id = ?";
        return getJdbcTemplate().update(sql, hotel.getName(), hotel.getId()); 
    }
    
    /**
     * 分页查询
     */
    public List<Hotel> selectHotels(HotelVo vo){
        StringBuffer sql = new StringBuffer("select h.id, h.name, u.id, u.name from hotel h "
                + " left join user_hotel uh on h.id = uh.hotel_id left join user u on uh.user_id = u.id "
                + "where 1=1 ");
        if(vo.getUserId() != null && vo.getUserId() > 0){
            sql.append(" and u.user_id = ?");
        }
        if(StringUtils.isNotBlank(vo.getName())){
            sql.append(" and h.name like ?");
        }
        sql.append(" limit ?, ?");
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getUserId() != null && vo.getUserId() > 0){
            params.add(vo.getUserId());
        }
        if(StringUtils.isNotBlank(vo.getName())){
            params.add("%" + vo.getName() + "%");
        }
        params.add(vo.getiDisplayStart());
        params.add(vo.getiDisplayLength());
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", params);
        return getJdbcTemplate().query(sql.toString(), new HotelRowMapper(), params.toArray());
    }
    
    /**
     * 查询记录数
     */
    public int selectHotelCount(HotelVo vo){
        StringBuffer sql = new StringBuffer("select count(*) from hotel h "
                + " left join user_hotel uh on h.id = uh.hotel_id left join user u on uh.user_id = u.id "
                + "where 1=1 ");
        if(vo.getUserId() != null && vo.getUserId() > 0){
            sql.append(" and u.user_id = ?");
        }
        if(StringUtils.isNotBlank(vo.getName())){
            sql.append(" and h.name like ?");
        }
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getUserId() != null && vo.getUserId() > 0){
            params.add(vo.getUserId());
        }
        if(StringUtils.isNotBlank(vo.getName())){
            params.add("%" + vo.getName() + "%");
        }
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", params);
        return getJdbcTemplate().queryForObject(sql.toString(), params.toArray(), Integer.class);
    }
    
    /**
     * 查询酒店的明细信息
     */
    public Hotel selectHotelInfo(Integer id){
        String sql = "select h.id, h.name, u.id, u.name from hotel "
                + " join user_hotel uh on h.id = uh.hotel_id join user u on uh.user_id = u.id "
                + "where u.id = ? ";
        
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", id);
        return getJdbcTemplate().queryForObject(sql.toString(), new HotelRowMapper(), id);
    }
    
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
     * 封装对象
     */
    public class HotelRowMapper implements RowMapper<Hotel> {
        @Override
        public Hotel mapRow(ResultSet rs, int line) throws SQLException {
            Hotel hotel = new Hotel();
            hotel.setId(rs.getInt("id"));
            hotel.setName(rs.getString("name"));
            User user = new User();
            try{
                user.setId(rs.getInt("u.id"));
            } catch(Exception e){
                //ingore
            }
            try{
                user.setName(rs.getString("u.name")==null?"":rs.getString("u.name"));
            } catch(Exception e){
                //ingore
                user.setName("d");
            }
            user.setRole(Role.maintainer);
            hotel.setUser(user);
            return hotel;
        }
    }
}
