package com.miaoxg.device.monitor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.entity.User;

@Repository
public class UserDao extends BaseDao {
    
    /**
     * 查询用户集合
     */
    public List<User> selectList(Map<String, Object> param){
        String sql = "select u.id, u.name, u.role from user u where u.id > 0 limit ?, ?";
        Object[] params = {param.get("offset"), param.get("limit")};
        return getJdbcTemplate().query(sql, params, new UserRowMapper());
    }
    
    /**
     * 查询用户数, 参数传null为查总数
     */
    public int selectCount(Map<String, Object> param){
        String sql = "select count(*) from user u where u.id > 0";
        if(param != null){
            // TODO 参数不为空时，按条件查询
        }
        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    
    /**
     * 根绝用户名查询用户
     */
    public User selectUserByName(String name){
        String sql = "select u.id, u.name, u.password, u.role from user u where u.name = ?";
        return getJdbcTemplate().queryForObject(sql, new UserRowMapperWithPassword(), name);
    }
    
    public class UserRowMapperWithPassword implements RowMapper<User> {
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
    
    public class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int lineNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setRole(rs.getString("role"));
            return user;
        }
    }
}
