package com.miaoxg.device.monitor.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaoxg.device.monitor.dao.HotelDao;
import com.miaoxg.device.monitor.dao.UserHotelDao;
import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.HotelVo;

@Service
@Transactional
public class HotelService {
    private final static Logger logger = LoggerFactory.getLogger(HotelService.class);
    
    @Resource
    private HotelDao hotelDao;
    @Resource
    private UserHotelDao userHotelDao;
    
    /**
     * 新增酒店
     */
    public void addHotel(Hotel hotel) {
        hotelDao.insertHotel(hotel);
        userHotelDao.insert(hotel.getUser(), hotel);
    }
    
    /**
     * 删除酒店
     */
    public void removeHotel(Integer id) {
        hotelDao.deletetHotel(id);
        userHotelDao.deletetByHotel(id);
    }
    
    /**
     * 修改设备
     */
    public void modifyHotel(Hotel hotel) {
        hotelDao.updateHotel(hotel);
        userHotelDao.deletetByHotel(hotel.getId());
        userHotelDao.insert(hotel.getUser(), hotel);
    }
    
    /**
     * 获取酒店信息，
     */
    public List<Hotel> getHotelNames(Integer userId) {
        logger.debug("get hotel names who's id is {}", userId);
        return hotelDao.selectHotelByUser(userId);
    }

    /**
     * 分页获取酒店信息
     */
    public DataTablesVo<Hotel> getHotels(HotelVo vo) {
        List<Hotel> hotelList = hotelDao.selectHotels(vo);
        int count = hotelDao.selectHotelCount(vo);
        
        return new DataTablesVo<Hotel>(count, hotelList);
    }

    /**
     * 获取酒店信息，包括维保人员
     */
    public Hotel getHotelInfo(Integer id) {
        return hotelDao.selectHotelInfo(id);
    }
}
