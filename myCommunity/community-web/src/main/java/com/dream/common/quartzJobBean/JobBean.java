package com.dream.common.quartzJobBean;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

@Slf4j
@PersistJobDataAfterExecution
/**不允许并发执行*/
@DisallowConcurrentExecution
@Data
/**
 * @author 乔宏展
 */
public class JobBean extends QuartzJobBean {

    private String beanName;
    private String executeMethod;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            ApplicationContext applicationContext =  (ApplicationContext)jobExecutionContext.getScheduler().getContext().get("applicationContext");

            Object bean = applicationContext.getBean(beanName);
            Method method = bean.getClass().getMethod(executeMethod,new Class[]{});
            method.invoke(bean,new Object[]{});
        } catch (Exception e) {
            log.error("quartz "+beanName+"."+executeMethod+" error!", e);
            e.printStackTrace();
        }
    }
}