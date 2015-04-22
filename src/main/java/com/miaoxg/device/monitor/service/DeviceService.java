package com.miaoxg.device.monitor.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaoxg.device.monitor.core.MonitorValueCache;
import com.miaoxg.device.monitor.dao.DeviceDao;
import com.miaoxg.device.monitor.dao.HotelDao;
import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.MonitorValueVo;

@Service
@Transactional
public class DeviceService {
    private final static Logger logger = LoggerFactory.getLogger(DeviceService.class);
    
    @Resource
    private DeviceDao deviceDao;
    @Resource
    private HotelDao hotelDao;
    
    /**
     * 修改设备名并推动到平台
     */
    public void rename(String deviceId, String newName) {
        
    }
    
    /**
     * 获取设备的监测数据
     */
    public DataTablesVo<MonitorValue> getMonitorValue(MonitorValueVo vo) {
        List<String> sidList = new ArrayList<String>();
        int count = 0;
        if(vo.getHotelId() == 0){  // 取该用户所有酒店的所有设备
            sidList = deviceDao.selectDeviceSidByUser(vo);
            count = deviceDao.selectDeviceCountByUser(vo);
        }
        else{                      // 取指定酒店的所有设备   
            sidList = deviceDao.selectDeviceSidByHotel(vo);
            count = deviceDao.selectDeviceCountByHotel(vo);
        }
        logger.debug("device count is {}", sidList.size());
        
        // 从缓存查监测值
        List<MonitorValue> mvList = MonitorValueCache.INSTANCE.get(sidList);
        DataTablesVo<MonitorValue> dtvo = new DataTablesVo<MonitorValue>(count, mvList);
        return dtvo;
    }
}
