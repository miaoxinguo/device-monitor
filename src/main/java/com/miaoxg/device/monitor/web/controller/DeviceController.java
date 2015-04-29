package com.miaoxg.device.monitor.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miaoxg.device.monitor.entity.Device;
import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.service.DeviceService;
import com.miaoxg.device.monitor.util.JsonUtils;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.DeviceVo;
import com.miaoxg.device.monitor.vo.MonitorValueVo;

@RestController
public class DeviceController extends AbstractController{
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
    
    @Resource
    private DeviceService deviceService;
    
    /**
     * 获取某个酒店的所有设备监测值
     * 
     * @param hotelId 酒店id，如果为0，查询该用户关联的所有酒店
     */
    @RequestMapping(value="monitorValues", method=RequestMethod.GET)
    public String getMonitorValues(MonitorValueVo vo, HttpSession session){
        Integer hotelId = vo.getHotelId();
        logger.debug("hotelId:{}, room:{} ", hotelId, vo.getRoom());
        logger.debug("offset:{}, limit:{}", vo.getiDisplayStart(), vo.getiDisplayLength());
        
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
     * 分页获取设备信息列表
     */
    @RequestMapping(value="devices", method=RequestMethod.GET)
    public String getDevices(DeviceVo vo){
        logger.debug("offset:{}, limit:{}", vo.getiDisplayStart(), vo.getiDisplayLength());
        
        DataTablesVo<Device> dtvo = deviceService.getDevices(vo);
        return JsonUtils.toDatatablesJson(dtvo.getCount(), dtvo.getTotal(), vo.getsEcho(), dtvo.getList());
    }
    
    /**
     * 获取设备信息
     */
    @RequestMapping(value="device", method=RequestMethod.GET)
    public String getDevice(String sid){
        return JsonUtils.toJsonString(deviceService.getDeviceInfo(sid));
    }
    
    /**
     * 新增设备
     */
    @RequestMapping(value="device", method=RequestMethod.POST)
    public String addDevice(Device device){
        if(StringUtils.isBlank(device.getSid())){
            return JsonUtils.toFailureJson("设备编号不能为空");
        }
        if(device.getHotel().getId() <= 0){
            return JsonUtils.toFailureJson("酒店不能为空");
        }
        if(StringUtils.isBlank(device.getRoom())){
            return JsonUtils.toFailureJson("房间号不能为空");
        }
        deviceService.addDevice(device);
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 删除设备
     */
    @RequestMapping(value="device/{sid}", method=RequestMethod.DELETE)
    public String removeDevice(@PathVariable String sid){
        if(StringUtils.isBlank(sid)){
            return JsonUtils.toFailureJson("请选择一个设备");
        }
        deviceService.removeDevice(sid);
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 修改设备
     */
    @RequestMapping(value="device", method=RequestMethod.PUT)
    public String editDevice(Device device){
        if(device.getHotel().getId() <= 0){
            return JsonUtils.toFailureJson("酒店不能为空");
        }
        if(StringUtils.isBlank(device.getRoom())){
            return JsonUtils.toFailureJson("房间号不能为空");
        }
        deviceService.modifyDevice(device);
        return JsonUtils.toSuccessJson();
    }
}
