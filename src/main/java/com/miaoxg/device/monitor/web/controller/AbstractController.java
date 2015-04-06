package com.miaoxg.device.monitor.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ExceptionHandler(RuntimeException.class)
    protected @ResponseBody String runtimeExceptionHandler(RuntimeException e){
        logger.error("", e);
        return JsonUtils.toFailureJson(e.getMessage());
    }
}

