package com.miaoxg.device.monitor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.miaoxg.device.monitor.entity.Device;
import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.vo.DeviceVo;
import com.miaoxg.device.monitor.vo.MonitorValueVo;

@Repository
public class DeviceDao extends BaseDao {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDao.class);

    /**
     * 插入一条记录
     */
    public int insertDevice(Device device) {
        StringBuffer sql = new StringBuffer("insert device(sid, name, hotel_id, room, last_date, next_date) values(?, ?, ?, ?, ?, ?)");
        return getJdbcTemplate().update(sql.toString(), device.getSid(), device.getSid(), device.getHotel().getId(), 
                device.getRoom(), device.getLastDate().toString(), device.getNextDate().toString());
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
    
    /**
     * 更新一条记录
     */
    public int updateDevice(Device device) {
        StringBuffer sql = new StringBuffer("update device set hotel_id=?, room=?, last_date=?, next_date=? "
                + " where sid=?");
        return getJdbcTemplate().update(sql.toString(), device.getHotel().getId(), device.getRoom(), 
                device.getLastDate().toString(), device.getNextDate().toString(),  device.getSid());
    }
    
    /**
     * 删除一条记录
     */
    public int deleteDevice(String sid) {
        String sql = "delete from device where sid=?";
        return getJdbcTemplate().update(sql, sid);
    }
    
    /**
     * 分页查询某酒店的所有设备, 如果没有指定酒店id, 抛异常
     */
    public List<String> selectDeviceSidByHotel(MonitorValueVo vo){
        StringBuffer sql = new StringBuffer("select sid from device where hotel_id = ?");
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and room like ?");
        }
        sql.append(" limit ?, ?");
        
        List<Object> params = new ArrayList<Object>();
        params.add(vo.getHotelId());
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        params.add(vo.getiDisplayStart());
        params.add(vo.getiDisplayLength());
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", params);
        return getJdbcTemplate().queryForList(sql.toString(), String.class, params.toArray());
    }
    
    /**
     * 查询某酒店的所有设备数量
     */
    public int selectDeviceCountByHotel(MonitorValueVo vo){
        StringBuffer sql = new StringBuffer("select count(*) from device d where d.hotel_id = ? ");
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and d.room like ? ");
        }
        
        List<Object> params = new ArrayList<Object>();
        params.add(vo.getHotelId());
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class, params.toArray());
    }
    
    /**
     * 查询某用户（不含管理员）所有酒店所有设备的编号
     */
    public List<String> selectDeviceSidByUser(MonitorValueVo vo){
        StringBuffer sql = new StringBuffer("SELECT d.sid from device d "
                + "left join hotel h on d.hotel_id=h.id "
                + "left join user_hotel on h.id=user_hotel.hotel_id "
                + "left join user on user_hotel.user_id=user.id "
                + "where user_id=?");
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and room like ?");
        }
        sql.append(" limit ?, ?");
        
        List<Object> params = new ArrayList<Object>();
        params.add(vo.getUserId());
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        params.add(vo.getiDisplayStart());
        params.add(vo.getiDisplayLength());
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", params);
        return getJdbcTemplate().queryForList(sql.toString(), String.class, params.toArray());
    }
    
    /**
     * 查询某用户（不含管理员）所有酒店所有设备的数量
     */
    public int selectDeviceCountByUser(MonitorValueVo vo){
        StringBuffer sql = new StringBuffer("SELECT count(*) from device d "
                + "left join hotel h on d.hotel_id=h.id "
                + "left join user_hotel on h.id=user_hotel.hotel_id "
                + "left join user on user_hotel.user_id=user.id "
                + "where user_id=?");
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and room like ?");
        }
        
        List<Object> params = new ArrayList<Object>();
        params.add(vo.getUserId());
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", params);
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class, params.toArray());
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
        StringBuffer sql = new StringBuffer("select sid from device where 1=1 ");
        if(vo.getHotelId()>0){
            sql.append(" and hotel_id = ?");
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and room like ?");
        }
        sql.append(" limit ?, ?");
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getHotelId()>0){
            params.add(vo.getHotelId());
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        params.add(vo.getiDisplayStart());
        params.add(vo.getiDisplayLength());
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", params);
        return getJdbcTemplate().queryForList(sql.toString(), String.class, params.toArray());
    }
    
    /**
     * 分页查询所有酒店所有设备的数量
     */
    public int selectAllDeviceSidCount(MonitorValueVo vo){
        StringBuffer sql = new StringBuffer("select count(*) from device d where 1=1");
        if(vo.getHotelId()>0){
            sql.append(" and d.hotel_id = ?");
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and d.room like ? ");
        }
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getHotelId()>0){
            params.add(vo.getHotelId());
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class, params.toArray());
    }
    
    /**
     * 分页查询设备信息
     */
    public List<Device> selectDevices(DeviceVo vo) {
        StringBuffer sql = new StringBuffer("select d.id, d.sid, d.name, d.room, d.last_date, d.next_date, h.name "
                + "from device d inner join hotel h on d.hotel_id = h.id where 1=1 ");
        if(vo.getHotelId() > 0){
            sql.append(" and h.id = ?");
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and d.room like ?");
        }
        sql.append(" limit ?, ?");
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getHotelId() > 0){
            params.add(vo.getHotelId());
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        params.add(vo.getiDisplayStart());
        params.add(vo.getiDisplayLength());
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", params);
        return getJdbcTemplate().query(sql.toString(), params.toArray(), new RowMapper<Device>(){
            @Override
            public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
                Device device = new Device();
                device.setId(rs.getInt("id"));
                device.setSid(rs.getString("sid"));
                device.setName(rs.getString("name"));
                device.setRoom(rs.getString("room"));
                device.setLastDate(LocalDate.parse(rs.getString("last_date")));
                device.setNextDate(LocalDate.parse(rs.getString("next_date")));
                device.setHotel(new Hotel(rs.getString("h.name")));
                return device;
            }
        });
    }
    
    /**
     * 查询设备数量
     */
    public int selectDevicesCount(DeviceVo vo) {
        StringBuffer sql = new StringBuffer("select count(*) from device d inner join hotel h on d.hotel_id = h.id where 1=1 ");
        if(vo.getHotelId() > 0){
            sql.append(" and h.id = ?");
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            sql.append(" and d.room like ?");
        }
        
        List<Object> params = new ArrayList<Object>();
        if(vo.getHotelId() > 0){
            params.add(vo.getHotelId());
        }
        if(StringUtils.isNotBlank(vo.getRoom())){
            params.add("%" + vo.getRoom() + "%");
        }
        return getJdbcTemplate().queryForObject(sql.toString(), params.toArray(), Integer.class);
    }
    
    /**
     * 根据sid查询设备信息
     */
    public Device selectDevice(String sid) {
        String sql = "select d.id, d.sid, d.name, d.room, d.last_date, d.next_date, d.hotel_id "
                + "from device d  where sid = ?";
        
        logger.debug("准备执行的sql: {}", sql.toString());
        logger.debug("参数:{}", sid);
        return getJdbcTemplate().queryForObject(sql, new DeviceRowMapper(), sid);
    }
    
    public class DeviceRowMapper implements RowMapper<Device> {
        @Override
        public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
            Device device = new Device();
            device.setId(rs.getInt("id"));
            device.setSid(rs.getString("sid"));
            device.setName(rs.getString("name"));
            device.setRoom(rs.getString("room"));
            device.setLastDate(LocalDate.parse(rs.getString("last_date")));
            device.setNextDate(LocalDate.parse(rs.getString("next_date")));
            device.setHotel(new Hotel(rs.getInt("hotel_id")));
            return device;
        }
    }
}
