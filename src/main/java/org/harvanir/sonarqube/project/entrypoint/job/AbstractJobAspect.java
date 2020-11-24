package org.harvanir.sonarqube.project.entrypoint.job;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.harvanir.sonarqube.share.util.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Harvan Irsyadi
 */
@Slf4j
@Aspect
public class AbstractJobAspect {

    private final ApplicationContext applicationContext;

    public AbstractJobAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled) " +
            "&& target(org.harvanir.sonarqube.project.entrypoint.job.Job)"
    )
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> clazz = joinPoint.getTarget().getClass();

        if (AbstractJob.activeJobs.contains(clazz)) {
            return joinPoint.proceed();
        }

        log.debug("{}", ObjectUtils.defer(() -> String.format("No active job found / job in exiting mode for class %s", clazz)));

        return null;
    }

    @Scheduled(fixedDelay = 5000)
    public void execute() {
        if (AbstractJob.activeJobs.isEmpty()) {
            log.info("No active jobs found, system exiting...");
            int exitCode = SpringApplication.exit(applicationContext);
            sleep();
            System.exit(exitCode);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error sleeping...", e);
        }
    }
}