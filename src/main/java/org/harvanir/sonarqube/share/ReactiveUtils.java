package org.harvanir.sonarqube.share;

import reactor.core.publisher.Mono;

/**
 * @author Harvan Irsyadi
 */
public class ReactiveUtils {

    public static final Mono<Boolean> INSTANCE = Mono.just(true);

    private ReactiveUtils() {
    }
}