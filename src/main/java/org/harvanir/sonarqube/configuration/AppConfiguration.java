package org.harvanir.sonarqube.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Harvan Irsyadi
 */
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@Configuration(proxyBeanMethods = false)
public class AppConfiguration {

    @Bean
    public SchedulingConfigurer schedulingConfigurer() {
        return taskRegistrar -> taskRegistrar.setTaskScheduler(
                new ConcurrentTaskScheduler(
                        new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors())
                )
        );
    }
}