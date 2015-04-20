package com.miaoxg.device.monitor.core;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

/**
 * 在Spring容器启动后加载缓存等
 * 
 * @author miaoxinguo2002@gmail.com
 * @version orange 2013-11-25
 */
public class Init extends ContextLoaderListener{
    
    private final Logger logger = LoggerFactory.getLogger(Init.class);
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.debug("initialized");
        
        // 启动容器
        super.contextInitialized(event);
        
        // 加载监测值缓存
        logger.info("加载设备缓存...");
        try{
            MonitorValueCache.INSTANCE.load();
        }catch(Exception e){
            logger.error("加载设备缓存异常, 系统启动失败", e);
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        logger.debug("destroyed");
        // 关闭容器
        super.contextDestroyed(event);
    }
    
}
