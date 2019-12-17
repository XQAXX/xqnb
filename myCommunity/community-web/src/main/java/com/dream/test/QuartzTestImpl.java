package com.dream.test;

import com.dream.config.QuartzColonyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("QuartzTest")
public class QuartzTestImpl implements QuartzTest{
    @Autowired
    QuartzColonyConfig quartzColonyConfig;
    @Override
    public String test() {
  /*      SchedulerFactoryBean schedulerFactoryBean=new SchedulerFactoryBean();
        try {
            CronTriggerImpl[] triggers = quartzColonyConfig.createTriggers();
            quartzColonyConfig.fulsh(schedulerFactoryBean,triggers);
        } catch (ParseException e) {
            e.printStackTrace();
        }
*/
        return "quartz测试成功";
}
}
