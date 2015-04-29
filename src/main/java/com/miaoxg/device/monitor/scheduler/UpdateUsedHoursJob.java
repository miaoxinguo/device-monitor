package com.miaoxg.device.monitor.scheduler;

import java.util.Collection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.miaoxg.device.monitor.core.ApplicationContextHolder;
import com.miaoxg.device.monitor.core.MonitorValueCache;
import com.miaoxg.device.monitor.entity.MonitorValue;
import com.miaoxg.device.monitor.service.DeviceService;

/**
 * 自动任务，定时从缓存中取滤网已用时长，同步到数据库
 * <p>单独实现一个任务实现更新已用时长是因为获取监测值任务过于频繁
 * 
 * @author miaoxinguo2002@163.com
 */
public class UpdateUsedHoursJob extends QuartzJobBean {
    private final static Logger logger = LoggerFactory.getLogger(UpdateUsedHoursJob.class);
    
    private DeviceService deviceService;
    
    public UpdateUsedHoursJob() {
        deviceService = ApplicationContextHolder.getBean("deviceService", DeviceService.class);
    }
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.debug("执行任务 -- 更新滤网已用时长");
        
        /*
         * 1 从缓存中获取最新的滤网已用时长
         * 2 更新数据库
         */
        Collection<MonitorValue> values = MonitorValueCache.INSTANCE.getAll();
        deviceService.updateUsedHours(values);
    }
}
