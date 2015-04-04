package com.miaoxg.device.monitor.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 以静态变量保存ApplicationContext, 可在任何地方任何时候中取到ApplicaitonContext.
 * 
 * @author miaoxinguo2002@gmail.com
 * @version orange 2013-11-26
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    
    /** ApplicationContext实例 */
    private static ApplicationContext applicationContext;
    
    /**
     * 自动注入applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContextHolder is not managed by spring");
        }
        return applicationContext;
    }
    
    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }
    
    /**
     * 取Bean
     */
    public static <T> T getBean(String name, Class<T> type){
        return getApplicationContext().getBean(name, type);
    }
    
}
