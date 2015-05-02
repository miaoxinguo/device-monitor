package com.miaoxg.device.monitor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.core.Role;
import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.vo.HotelVo;

@Repository
public class HotelDao extends BaseDao {
    
    private static final Logger logger = LoggerFactory.getLogger(HotelDao.class);
    
    /**
     * 插入, 返回自增主键
     */
    public int insertHotel(Hotel hotel){
        String sql = "insert hotel(name) values(?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, hotel.getName());
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue(); 
    }
    
    /**
     * 删除
     */
    public int deletetHotel(Integer id){
        String sql = "delete from hotel where id = ?";
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
     * 查询数量
     */
    public int selectCount(String name){
        String sql = "select count(*) from hotel where name = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, name);
    }
    
    /**
     * 分页查询
     */
    public List<Hotel> selectHotels(HotelVo vo){
        StringBuffer sql = new StringBuffer("select h.id, h.name, u.id, u.name from hotel h "
                + " left join user_hotel uh on h.id = uh.hotel_id left join user u on uh.user_id = u.id "
                + "where u.role='maintainer' ");
        if(vo.getUserId() != null && vo.getUserId() > 0){
            sql.append(" and u.id = ?");
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
                + "where u.role='maintainer' ");
        if(vo.getUserId() != null && vo.getUserId() > 0){
            sql.append(" and u.id = ?");
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
        String sql = "select h.id, h.name from hotel h where h.id = ? ";
        return getJdbcTemplate().queryForObject(sql.toString(), new HotelNameRowMapper(), id);
    }
    
    /**
     * 根绝用户查询酒店id和name -用于下拉菜单
     */
    public List<Hotel> selectNamesByUser(User user){
        StringBuffer sql = new StringBuffer("select h.id, h.name from hotel h "); 
        
        // 管理员查询全部；  其他用户按id查
        if(user.getRole() != Role.admin){
            sql.append(" left join user_hotel uh on h.id = uh.hotel_id "
                    + " left join user u on uh.user_id = u.id"
                    + " where  u.id = ?");
            return getJdbcTemplate().query(sql.toString(), new HotelNameRowMapper(), user.getId());
        }
        return getJdbcTemplate().query(sql.toString(), new HotelNameRowMapper());
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
            user.setId(rs.getInt("u.id"));
            user.setName(rs.getString("u.name")==null?"-":rs.getString("u.name"));
            user.setRole(Role.admin);  // 这里仅为保证转json时不出现空指针错误
            hotel.setUser(user);
            return hotel;
        }
    }
    
    /**
     * 封装对象
     */
    public class HotelNameRowMapper implements RowMapper<Hotel> {
        @Override
        public Hotel mapRow(ResultSet rs, int line) throws SQLException {
            Hotel hotel = new Hotel();
            hotel.setId(rs.getInt("id"));
            hotel.setName(rs.getString("name"));
            return hotel;
        }
    }
}
