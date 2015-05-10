package com.miaoxg.device.monitor.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class BaseDao {
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    public JdbcTemplate getJdbcTemplate(){
        return this.jdbcTemplate;
    }
    
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}
