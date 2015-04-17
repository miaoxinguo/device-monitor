package com.miaoxg.device.monitor.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 自动任务，定时从服务器获取设备的监测值
 * 
 * @author miaoxinguo2002@163.com
 */
public class GetMonitorValueJob extends QuartzJobBean {
    private final static Logger logger = LoggerFactory.getLogger(GetMonitorValueJob.class);
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.debug("执行获取设备监测值任务");
        
    }
    
}
