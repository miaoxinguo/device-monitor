package com.miaoxg.device.monitor.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

public class BaseDao {
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    public JdbcTemplate getJdbcTemplate(){
        return this.jdbcTemplate;
    }
}
