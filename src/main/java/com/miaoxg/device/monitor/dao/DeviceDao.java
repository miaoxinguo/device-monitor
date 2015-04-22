package com.miaoxg.device.monitor.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.vo.MonitorValueVo;

@Repository
public class DeviceDao extends BaseDao {
    
    /**
     * 分页查询某酒店的所有设备, 如果没有指定酒店id, 抛异常
     */
    public List<String> selectDeviceSidByHotel(MonitorValueVo vo){
        String sql = "select d.sid from device d where d.hotel_id = ? limit ?, ?";
        Object[] params = {vo.getHotelId(), vo.getiDisplayStart(), vo.getiDisplayLength()};
        return getJdbcTemplate().queryForList(sql, String.class, params);
    }
    
    /**
     * 查询某酒店的所有设备数量
     */
    public int selectDeviceCountByHotel(MonitorValueVo vo){
        String sql = "select count(*) from device d where d.hotel_id = ? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, vo.getHotelId());
    }
    
    /**
     * 查询某用户（不含管理员）所有酒店所有设备的编号
     */
    public List<String> selectDeviceSidByUser(MonitorValueVo vo){
        String sql = "SELECT d.sid from device d "
                + "left join hotel h on d.hotel_id=h.id "
                + "left join user_hotel on h.id=user_hotel.hotel_id "
                + "left join user on user_hotel.user_id=user.id "
                + "where user_id=? limit ?, ?";
        Object[] params = {vo.getUserId(), vo.getiDisplayStart(), vo.getiDisplayLength()};
        return getJdbcTemplate().queryForList(sql, String.class, params);
    }
    
    /**
     * 查询某用户（不含管理员）所有酒店所有设备的数量
     */
    public int selectDeviceCountByUser(MonitorValueVo vo){
        String sql = "SELECT count(*) from device d "
                + "left join hotel h on d.hotel_id=h.id "
                + "left join user_hotel on h.id=user_hotel.hotel_id "
                + "left join user on user_hotel.user_id=user.id "
                + "where user_id=? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, vo.getUserId());
    }
    
    /**
     * 查询所有酒店所有设备的编号
     */
    public List<String> selectAllDeviceSid(){
        String sql = "select d.sid from device d";
        return getJdbcTemplate().queryForList(sql, String.class);
    }
    
    /**
     * 分页查询所有酒店所有设备的编号
     */
    public List<String> selectAllDeviceSid(MonitorValueVo vo){
        String sql = "select d.sid from device d limit ?,?";
        Object[] params = {vo.getiDisplayStart(), vo.getiDisplayLength()};
        return getJdbcTemplate().queryForList(sql, String.class, params);
    }
    
    /**
     * 插入监测数据
     */
    public void insertMonitorValue(List<MonitorValue> list){
        String sql = "insert MONITOR_VALUE(device_sid, temperature, humidity, co2, nh3, isOnline, isOpen, sync_time) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?)";
        
        // TODO 同步时间在这里取服务器时间，最准确的时间应该是服务器返回
        String syncTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        List<Object[]> params = new ArrayList<Object[]>();
        for(MonitorValue mv : list){
            Object[] param = new Object[8];
            param[0] = mv.getDeviceSid();
            param[1] = mv.getTemperature();
            param[2] = mv.getHumidity();
            param[3] = mv.getCo2();
            param[4] = mv.getNh3();
            param[5] = mv.isOnLine();
            param[6] = mv.isOpen();
            param[7] = syncTime;
            params.add(param);
        }
        getJdbcTemplate().batchUpdate(sql, params); 
    }
}
