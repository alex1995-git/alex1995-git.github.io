package com.depcue.scheduletask;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Configuration
public class ScheduleConfiguration {
	

	@Value("${time.minutes.scheduler}")
	private int schedulerTime;

    @Bean
    public JobDetail scheduleJob() {
        return JobBuilder.newJob(ScheduleTask.class).storeDurably().withIdentity("DepCueSchedule")
                .withDescription("Tarea Programada Deportivo Cuenca").build();
    }

    @Bean
    public Trigger scheduleTrigger() {
        return newTrigger().withIdentity("trigger").forJob(scheduleJob()).withSchedule(simpleSchedule().repeatForever().withIntervalInMinutes(schedulerTime))
                .build();
    }
}
