package com.softtek.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by simon.song on 2017/6/27.
 */
public class InvokeStatJob implements Job{

    private static Logger logger = LoggerFactory.getLogger(InvokeStatJob.class);

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info(">>>>>>>>>>>>>>>>>>>Job executed ...");
    }
}
