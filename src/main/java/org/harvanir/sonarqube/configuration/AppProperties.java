package org.harvanir.sonarqube.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Harvan Irsyadi
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String token;

    private Api api;

    @Getter
    @Setter
    public static class Api {

        private String hostName;

        private Search search;
    }

    @Getter
    @Setter
    public static class Search {

        private int pageSize = 5;

        private String qualifier;

        private String organization;

        private String nameOrKey;

        private int minusDays = 10;
    }
}