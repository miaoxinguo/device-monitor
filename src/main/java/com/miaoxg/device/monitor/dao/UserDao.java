package com.miaoxg.device.monitor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.core.Role;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.vo.UserVo;

@Repository
public class UserDao extends BaseDao {
    
    /**
     * 插入新用户, 返回用户id
     */
    public int insert(User user){
        String sql = "insert user(name, password, role) value(?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getName());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getRole().toString());
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue(); 
    }
    
    /**
     * 删除用户
     */
    public int delete(Integer id){
        String sql = "delete from user where id = ?";
        return getJdbcTemplate().update(sql, id);
    }
    
    /**
     * 更改密码
     */
    public int updatePw(String name, String newPw){
        String sql = "update user set password = ? where name = ?";
        return getJdbcTemplate().update(sql, newPw, name);
    }
    
    /**
     * 修改  目前能修改的只有角色，但是传入user以备扩展
     */
    public int update(User user){
        String sql = "update user set role = ? where id = ?";
        return getJdbcTemplate().update(sql, user.getRole().toString(), user.getId());
    }
    
    /**
     * 根据用户名查询用户数 用于判断是否存在
     */
    public int selectCount(String username){
        String sql = "select count(*) from user where name = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, username);
    }
    
    /**
     * 分页查询用户
     */
    public List<User> selectList(UserVo vo){
        StringBuffer sql = new StringBuffer("select u.id, u.name, u.role from user u where u.id > 0 ");
        if(vo.getRole().equals(Role.maintainer.toString()) || vo.getRole().equals(Role.waiter.toString())){
            sql.append(" and  u.role = ? ");
        }
        if(StringUtils.isNotBlank(vo.getName())){
            sql.append(" and u.name like ? ");
        }
        sql.append(" limit ?, ?");
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getRole().equals(Role.maintainer.toString()) || vo.getRole().equals(Role.waiter.toString())){
            params.add(vo.getRole().toString());
        }
        if(StringUtils.isNotBlank(vo.getName())){
            params.add("%"+vo.getName()+"%");
        }
        params.add(vo.getiDisplayStart());
        params.add(vo.getiDisplayLength());
        
        return getJdbcTemplate().query(sql.toString(), params.toArray(), new RowMapper<User>(){
            @Override
            public User mapRow(ResultSet rs, int lineNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                return user;
            }
        });
    }
    
    /**
     * 查询用户数
     */
    public int selectCount(UserVo vo){
        StringBuffer sql = new StringBuffer("select count(*) from user u where u.id > 0 ");
        if(vo.getRole().equals(Role.maintainer.toString()) || vo.getRole().equals(Role.waiter.toString())){
            sql.append(" and u.role = ? ");
        }
        if(StringUtils.isNotBlank(vo.getName())){
            sql.append(" and u.name like ? ");
        }
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getRole().equals(Role.maintainer.toString()) || vo.getRole().equals(Role.waiter.toString())){
            params.add(vo.getRole().toString());
        }
        if(StringUtils.isNotBlank(vo.getName())){
            params.add("%"+vo.getName()+"%");
        }
        return getJdbcTemplate().queryForObject(sql.toString(), params.toArray(), Integer.class);
    }
    
    /**
     * 查询用户明细信息，含管理的酒店
     */
    public User selectUserDetail(Integer id){
        String sql = "select u.id, u.name, u.role from user u where u.id = ?";
        return getJdbcTemplate().queryForObject(sql, new UserInfoRowMapper(), id);
    }
    
    /**
     * 根绝用户名查询用户
     */
    public User selectUserByName(String name){
        String sql = "select u.id, u.name, u.password, u.role from user u where u.name = ?";
        return getJdbcTemplate().queryForObject(sql, new UserRowMapper(), name);
    }
    
    /**
     * 根据酒店id查维保用户数
     */
    public int selectMaintainerCountByHotel(Integer hotelId){
        String sql = "select count(*) from user u join user_hotel uh on u.id=uh.user_id "
                + " where u.role='maintainer' and uh.hotel_id = ? ";
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class, hotelId);
    }
    /**
     * 根据酒店id查维保用户
     */
    public User selectMaintainerByHotel(Integer hotelId){
        String sql = "select u.id, u.name, u.role from user u join user_hotel uh on u.id=uh.user_id "
                + " where u.role='maintainer' and uh.hotel_id = ? ";
        return getJdbcTemplate().queryForObject(sql.toString(), new UserInfoRowMapper(), hotelId);
    }
    
    /**
     * 查询维保用户 --用于下拉列表
     */
    public List<Map<String, Object>> selectMaintainerNames(){
        String sql = "select u.id, u.name from user u where u.role = 'maintainer'";
        return getJdbcTemplate().queryForList(sql);
    }
    
    public class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int lineNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        }
    }
    
    public class UserInfoRowMapper implements RowMapper<User>{
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setRole(rs.getString("role"));
            return user;
        }
    }
}
