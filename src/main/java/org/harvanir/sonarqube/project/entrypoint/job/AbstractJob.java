package org.harvanir.sonarqube.project.entrypoint.job;

import lombok.extern.slf4j.Slf4j;
import org.harvanir.sonarqube.share.util.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Harvan Irsyadi
 */
@Slf4j
public abstract class AbstractJob implements Job, InitializingBean {

    static final Set<Class<?>> activeJobs = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void afterPropertiesSet() throws NoSuchMethodException {
        validateAnnotation();
    }

    private void validateAnnotation() throws NoSuchMethodException {
        Method executeMethod = getClass().getMethod("execute");
        Optional<Scheduled> scheduledAnnotation = Optional.ofNullable(executeMethod.getAnnotation(Scheduled.class));

        if (scheduledAnnotation.isPresent()) {
            activeJobs.add(executeMethod.getDeclaringClass());
        } else {
            throw new NoScheduledAnnotationException(String.format("No @Scheduled annotation found on %s", executeMethod));
        }
    }

    protected final void exitJob() {
        log.debug("{}", ObjectUtils.defer(() -> "Exiting job..."));
        activeJobs.remove(getClass());
    }

    static class NoScheduledAnnotationException extends RuntimeException {

        private static final long serialVersionUID = -439106635867892998L;

        public NoScheduledAnnotationException(String message) {
            super(message);
        }
    }
}