package org.harvanir.sonarqube.project.entity.getlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Harvan Irsyadi
 */
@ToString
@Builder
@Getter
@AllArgsConstructor
public class GetListResponse {

    private final Paging paging;

    private final Component[] components;

    @ToString
    @Getter
    @AllArgsConstructor
    public static class Paging {

        private final int pageIndex;

        private final int pageSize;

        private final int total;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class Component {

        private final String organization;

        private final String key;

        private final String name;

        private final String qualifier;

        private final String visibility;

        private final String lastAnalysisDate;

        private final String revision;
    }
}