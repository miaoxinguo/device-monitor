package com.miaoxg.device.monitor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaoxg.device.monitor.core.ServiceException;
import com.miaoxg.device.monitor.dao.HotelDao;
import com.miaoxg.device.monitor.dao.UserDao;
import com.miaoxg.device.monitor.dao.UserHotelDao;
import com.miaoxg.device.monitor.entity.Hotel;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.HotelVo;

@Service
@Transactional
public class HotelService {
//    private final static Logger logger = LoggerFactory.getLogger(HotelService.class);
    
    @Resource
    private HotelDao hotelDao;
    @Resource
    private UserDao userDao;
    @Resource
    private UserHotelDao userHotelDao;
    
    /**
     * 新增酒店
     */
    public void addHotel(Hotel hotel) {
        // TODO 检测是否重名
        if(existHotel(hotel.getName())){
            throw new ServiceException("酒店已存在");
        }
        Integer hotelId = hotelDao.insertHotel(hotel);
        userHotelDao.insert(hotel.getUser().getId(), hotelId);
    }
    
    /**
     * 删除酒店
     */
    public void removeHotel(Integer id) {
        hotelDao.deletetHotel(id);
        userHotelDao.deletetByHotel(id);
    }
    
    /**
     * 修改酒店
     */
    public void modifyHotel(Hotel hotel) {
        hotelDao.updateHotel(hotel);
        /*
         * 修改维保人后更新酒店与维保人的关联
         * 如果以后改成一个酒店多个维保人，要修改Hotel保有一个List<Hotel>
         */
        userHotelDao.deletetByHotel(hotel.getId());
        userHotelDao.insert(hotel.getUser().getId(), hotel.getId());
    }
    
    /**
     * 判断酒店名是否存在
     */
    public boolean existHotel(String name){
        return hotelDao.selectCount(name) > 0 ? true : false;
    }
    
    /**
     * 获取酒店信息，用于下拉列表
     */
    public List<Hotel> getHotelNames(User user) {
        return  hotelDao.selectNamesByUser(user);
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
        Hotel hotel = hotelDao.selectHotelInfo(id);
        if(userDao.selectMaintainerCountByHotel(id)>0){
            hotel.setUser(userDao.selectMaintainerByHotel(id));
        }
        return hotel;
    }
}
