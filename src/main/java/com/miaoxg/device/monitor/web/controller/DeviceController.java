package com.miaoxg.device.monitor.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.miaoxg.device.monitor.service.DeviceService;

@RestController
public class DeviceController extends AbstractController{
    
    // TODO 写死几个设备编号，两个数组对应两个酒店，以后从数据库取
    private static String[] devices1 = {
        "380058590147", "380058631466", "380058686739", "380058725928"
    };
    private static String[] devices2 = {
        "380057701067", "117644925998"
    };
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
    
    @Resource
    private DeviceService deviceService;
    
    /**
     * 获取某个酒店的所有设备数值
     * @param hotelId 酒店id
     */
    @RequestMapping(value="deviceStatus", method=RequestMethod.GET)
    public String getDeviceStatus(String hotelId, String sEcho){
        
        logger.debug("hotelId = {}", hotelId);
        if(null==hotelId || "".equals(hotelId)){
            hotelId = "0";
        }
        List<String> devList = null;
        if("0".equals(hotelId)){  //查全部
            devList = Arrays.asList(ArrayUtils.addAll(devices1, devices2));
        }
        else if("1".equals(hotelId)){
            devList = Arrays.asList(devices1);
        }
        else if("2".equals(hotelId)){
            devList = Arrays.asList(devices2);
        }
        
        JSONObject obj = new JSONObject();
        obj.put("iTotalRecords", devList.size());   // 数据库表中记录数
        obj.put("iTotalDisplayRecords", devList.size());   // 查询条件顾虑后的记录数
        obj.put("sEcho", sEcho);
        obj.put("aaData", deviceService.getRemoteDeviceInfo(devList));
        return obj.toJSONString();
    }
    
    /**
     * 获取某个设备信息
     */
    @RequestMapping(value="deviceInfo", method=RequestMethod.GET)
    public String getDeviceInfo(String deviceId){
        return "";
    }
}
