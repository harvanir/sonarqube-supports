package org.harvanir.sonarqube.project.provider.gateway;

/**
 * @author Harvan Irsyadi
 */
public class ApiConstant {

    private ApiConstant() {
    }

    public static class Path {

        public static final String SEARCH = "/api/projects/search";

        public static final String BULK_DELETE = "/api/projects/bulk_delete";

        private Path() {
        }
    }

    public static class Variable {

        public static final String ORGANIZATION = "organization";

        public static final String QUALIFIERS = "qualifiers";

        public static final String PROJECTS = "projects";

        public static final String ANALYZED_BEFORE = "analyzedBefore";

        public static final String PAGE_SIZE = "ps";

        public static final String NAME_OR_KEY = "q";

        private Variable() {
        }
    }
}