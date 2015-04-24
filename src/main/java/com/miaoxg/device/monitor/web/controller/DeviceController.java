package com.miaoxg.device.monitor.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.service.DeviceService;
import com.miaoxg.device.monitor.util.JsonUtils;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.MonitorValueVo;

@RestController
public class DeviceController extends AbstractController{
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
    
    @Resource
    private DeviceService deviceService;
    
    /**
     * 获取某个酒店的所有设备数值
     * 
     * @param hotelId 酒店id，如果为0，查询该用户关联的所有酒店
     */
    @RequestMapping(value="monitorValues", method=RequestMethod.GET)
    public String getMonitorValues(MonitorValueVo vo, HttpSession session){
        Integer hotelId = vo.getHotelId();
        logger.debug("into DeviceController.monitorValues， hotelId = {}", hotelId);
        logger.debug("room:{}, offset:{}, limit:{}", vo.getRoom(), vo.getiDisplayStart(), vo.getiDisplayLength());
        
        if(null==hotelId || "".equals(hotelId)){
            hotelId = Integer.valueOf("0");
        }
        if(hotelId == 0){  //查当前用户所有酒店的全部设备监测值
            vo.setUserId(((User)session.getAttribute("user")).getId());
        } 
        DataTablesVo<MonitorValue> dtvo = deviceService.getMonitorValue(vo);
        return JsonUtils.toDatatablesJson(dtvo.getCount(), dtvo.getTotal(), vo.getsEcho(), dtvo.getList());
    }
    
    /**
     * 获取某个设备信息
     */
    @RequestMapping(value="deviceInfo", method=RequestMethod.GET)
    public String getDeviceInfo(String deviceId){
        return "";
    }
}
