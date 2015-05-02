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

import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.service.HotelService;
import com.miaoxg.device.monitor.util.JsonUtils;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.HotelVo;

@RestController
public class HotelController extends AbstractController{
    
    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
    
    @Resource
    private HotelService hotelService;

    /**
     * 新增酒店
     */
    @RequestMapping(value="hotel", method=RequestMethod.POST)
    public String addHotel(Hotel hotel){
        if(StringUtils.isBlank(hotel.getName())){
            return JsonUtils.toFailureJson("酒店名不能为空");
        }
        if(hotel.getUser().getId() == null){
            return JsonUtils.toFailureJson("维保人员不能为空");
        }
        
        hotelService.addHotel(hotel);
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 删除酒店
     */
    @RequestMapping(value="hotel/{id}", method=RequestMethod.DELETE)
    public String removeHotel(@PathVariable Integer id){
        if(id <= 0){
            return JsonUtils.toFailureJson("请选择一个酒店");
        }
        hotelService.removeHotel(id);
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 修改酒店
     */
    @RequestMapping(value="hotel/{id}", method=RequestMethod.PUT)
    public String editHotel(@PathVariable Integer id, Hotel hotel){
        if(id <= 0){
            return JsonUtils.toFailureJson("请选择酒店");
        }
        if(StringUtils.isBlank(hotel.getName())){
            return JsonUtils.toFailureJson("酒店名不能为空");
        }
        if(hotel.getUser().getId() <= 0){
            return JsonUtils.toFailureJson("请选择维保人员");
        }
        hotelService.modifyHotel(hotel);
        return JsonUtils.toSuccessJson();
    }
    
    
    /**
     * 获取所有 -- 下拉菜单
     */
    @RequestMapping(value="hotelNames", method=RequestMethod.GET)
    public String getHotelNames(HttpSession session){
        logger.debug("get hotel names");
        User currUser = (User)session.getAttribute("user");
        return JsonUtils.toJsonString(hotelService.getHotelNames(currUser));
    }
    
    /**
     * 分页获取酒店信息列表
     */
    @RequestMapping(value="hotels", method=RequestMethod.GET)
    public String getHotels(HotelVo vo){
        logger.debug("offset:{}, limit:{}", vo.getiDisplayStart(), vo.getiDisplayLength());
        
        DataTablesVo<Hotel> dtvo = hotelService.getHotels(vo);
        return JsonUtils.toDatatablesJson(dtvo.getCount(), dtvo.getTotal(), vo.getsEcho(), dtvo.getList());
    }
    
    /**
     * 获取酒店信息
     */
    @RequestMapping(value="hotel/{id}", method=RequestMethod.GET)
    public String getHotel(@PathVariable Integer id){
        return JsonUtils.toJsonString(hotelService.getHotelInfo(id));
    }
}
