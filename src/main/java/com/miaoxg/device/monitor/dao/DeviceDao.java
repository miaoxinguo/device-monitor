package com.miaoxg.device.monitor.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.entity.MonitorValue;

@Repository
public class DeviceDao extends BaseDao {
    
    /**
     * 查询所有的设备编号
     */
    public List<String> selectAllDeviceSid(){
        String sql = "select sid from device";
        return getJdbcTemplate().queryForList(sql, String.class);
    }
    
    /**
     * 插入监测数据
     */
    public void insertMonitorValue(List<MonitorValue> list){
        String sql = "insert MONITOR_VALUE(device_sid, temperature, humidity, co2, nh3, isOnline, isOpen, sync_time) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?)";
        
        // TODO 同步时间在这里取服务器时间，最准确的时间应该是服务器返回
        String syncTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        
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
