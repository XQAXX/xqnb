package com.dream.config;

import com.dream.common.quartzJobBean.JobBean;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.text.ParseException;
import java.util.*;

/**
 * Quartz集群配置
 * @author 乔宏展
 */
@Configuration
public class QuartzColonyConfig {

    /**
     * 定时任务组名称
     */
    private static final String QUARTZ_GROUP_NAME = "enjoyQuartzJobGroup";
    /**
     * 定时任务方法后缀
     */
    private static final String QUARTZ_JOB_SUFFIX = "_job";
    /**
     * 定时任务触发器后缀
     */
    private static final String QUARTZ_TRIGGER_SUFFIX = "_trigger";

    private Class<? extends Job> c = JobBean.class;

    @Autowired
    private QuartzProperties quartzProperties;

    @Autowired
    private DataSource dataSource;


    @Bean(name = "triggers")
    public CronTriggerImpl[] createTriggers()
            throws ParseException
    {
        List<CronTriggerImpl> l = new ArrayList<>();
        Map<String, String> test = quartzBeanMethod("QuartzTest","test");
        l.add(createTrigger("test",test ,"1/5 * * * * ?"));

        return l.toArray(new CronTriggerImpl[l.size()]);

    }

    private CronTriggerImpl createTrigger(String name, Map<String, String> map, String cronExpression) throws ParseException {

        CronTriggerFactoryBean cf = new CronTriggerFactoryBean();
        cf.setJobDetail(create(name,map));
        cf.setCronExpression(cronExpression);
        cf.setName(c.getSimpleName() + name + QUARTZ_TRIGGER_SUFFIX);
        cf.setGroup(QUARTZ_GROUP_NAME); cf.afterPropertiesSet();
        return (CronTriggerImpl)cf.getObject();
    }
    private JobDetail create(String name, Map<String, String> map)
    {
        JobDetailFactoryBean d = new JobDetailFactoryBean();
        d.setDurability(true);
        d.setRequestsRecovery(true);
        d.setJobClass(c);
        d.setName(c.getSimpleName() + name + QUARTZ_JOB_SUFFIX);
        d.setGroup(QUARTZ_GROUP_NAME);
        d.afterPropertiesSet();
        d.setJobDataAsMap(map);
        JobDetail jd= d.getObject();
        //jd.getJobDataMap().put("key", 123);//如果想通过jobDataMap传递值，在这里添加
        return jd;
    }
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("triggers") CronTriggerImpl[] triggers) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        //数据源注入 解决集群下重复执行定时问题的关键
        schedulerFactoryBean.setDataSource(dataSource);
        //使job实例支持spring 容器管理
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 延迟10s启动quartz
        schedulerFactoryBean.setStartupDelay(10);
        // 直接使用配置文件,用于quartz集群,加载quartz数据源配置
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setAutoStartup(true);
        // 集群需要通过QuartzJobBean注入，需要设置上下文
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        // 注册触发器
        schedulerFactoryBean.setTriggers(triggers);
        return schedulerFactoryBean;
    }


    /**
     * 添加该方法的目的在于一个使用场景。如果代码中删除了不需要的定时任务，但是数据库中不会删除掉，会导致之前
     * 的定时任务一直在运行，如果把定时任务依赖的类删除了，就会导致报错，找不到目标。所以配置动态删除任务
     */
    @Bean
    public String fulsh(@Qualifier("schedulerFactoryBean") SchedulerFactoryBean schedulerFactoryBean,
                        @Qualifier("triggers") CronTriggerImpl[] triggers) {
        try {
            Scheduler s = schedulerFactoryBean.getScheduler();
            if (null == s) {
                return "Scheduler is null";
            }

            // 最新配置的任务
            List<String> newTriNames = new ArrayList<String>();
            if (null != triggers) {
                for (CronTriggerImpl cronTriggerImpl : triggers) {
                    newTriNames.add(cronTriggerImpl.getName());
                }
            }

            // 现有数据库中已有的任务
            Set<TriggerKey> myGroupTriggers = s.getTriggerKeys(GroupMatcher.triggerGroupEquals(QUARTZ_GROUP_NAME));
            if (null == myGroupTriggers || myGroupTriggers.size() == 0) {
                return "myGroupTriggers is null";
            }

            if (newTriNames != null && newTriNames.size() > 0) {
                for (TriggerKey triggerKey : myGroupTriggers) {
                    String dbTriggerName = triggerKey.getName();
                    if (!newTriNames.contains(dbTriggerName)) {
                        this.delTrigger(s,triggerKey);
                    }
                }
            } else {
                for (TriggerKey triggerKey : myGroupTriggers){
                    this.delTrigger(s,triggerKey);
                }
            }
            // 重要，如果不恢复所有，会导致无法使用
            s.resumeAll();
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception:" + e.getMessage();
        }
        return "success";
    }

    /**
     * 删除定时任务
     * @param s
     * @param triggerKey
     * @throws SchedulerException
     */
    private void delTrigger(Scheduler s, TriggerKey triggerKey ) throws SchedulerException {
        // 暂停 触发器
        s.pauseTrigger(triggerKey);
        Trigger g = s.getTrigger(triggerKey);
        JobKey jk = null;
        if (null != g) {
            jk = g.getJobKey();
        }
        // 停止触发器
        s.pauseTrigger(triggerKey);
        // 注销 触发器
        s.unscheduleJob(triggerKey);
        if (null != jk) {
            // 暂停任务
            s.pauseJob(jk);
            // 删除任务
            s.deleteJob(jk);
        }
    }

    /**
     * 设置定时类、定时方法
     * @param beanName
     *          类的bean名
     * @param executeMethod
     *          执行定时的方法名
     * @return
     */
    private Map<String, String> quartzBeanMethod(String beanName, String executeMethod){
        Map<String, String> map = new HashMap<String,String>(2);
        map.put("beanName",beanName);
        map.put("executeMethod",executeMethod);
        return map;
    }


}
