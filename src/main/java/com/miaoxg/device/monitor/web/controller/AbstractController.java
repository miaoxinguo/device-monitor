package com.miaoxg.device.monitor.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miaoxg.device.monitor.core.ServiceException;
import com.miaoxg.device.monitor.util.JsonUtils;

/**
 * Controller基类
 *
 * @author miaoxinguo2002@gmail.com
 * @version orange 2013-5-23
 */
public class AbstractController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 参数绑定中如果有Date类型转换为自定义的格式
     */
    @InitBinder  
    protected void initBinder(WebDataBinder binder) {  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        dateFormat.setLenient(false);  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));  
        
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        dateFormat.setLenient(false);  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false)); 
    }
    
    /**
     * 全局异常处理，返回前台错误信息
     */
    @ExceptionHandler(DataAccessException.class)
    protected @ResponseBody String dataAccessExceptionHandler(DataAccessException e){
        logger.error("数据访问异常", e);
        return JsonUtils.toFailureJson("系统处理失败，请稍后再试");
    }
    
    /**
     * 服务层异常处理，返回前台错误信息
     */
    @ExceptionHandler(ServiceException.class)
    protected @ResponseBody String serviceExceptionHandler(ServiceException e){
        return JsonUtils.toFailureJson(e.getMessage());
    }
    
    /**
     * 全局异常处理，返回前台错误信息
     */
    @ExceptionHandler(Exception.class)
    protected @ResponseBody String exceptionHandler(Exception e){
        logger.error("非法异常", e);
        return JsonUtils.toFailureJson("系统处理失败，请稍后再试");
    }
}

