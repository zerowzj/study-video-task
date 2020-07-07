package com.xdf.ucan.remix.video.task.support.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "prod")
@Configuration
@EnableScheduling
public class SchedulingCfg implements SchedulingConfigurer, ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(customScheduler());
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService customScheduler() {
        return Executors.newScheduledThreadPool(2);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("start: {}", LocalTime.now());
    }
}
