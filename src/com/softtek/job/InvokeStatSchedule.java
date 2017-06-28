package com.softtek.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by simon.song on 2017/6/27.
 */


public class InvokeStatSchedule {
    public static void main(String[] args) throws SchedulerException
    {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        //获取 任务作业
        JobDetail jobDetail = newJob(InvokeStatJob.class).withIdentity("groupJobName", "impala-to-couchbase").build();

        String cronExp = "0 * * * * ? *";//每分钟的第0秒
        CronScheduleBuilder cronBuilder = CronScheduleBuilder.cronSchedule(cronExp);

        //SimpleScheduleBuilder simpleBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMinutes(1);

        //获取 触发器
        Trigger trigger = newTrigger()
                .withIdentity("triggerName", "impala-to-couchbase")
                .withSchedule(cronBuilder)
                .startNow().build();
        //绑定
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

    }
}
