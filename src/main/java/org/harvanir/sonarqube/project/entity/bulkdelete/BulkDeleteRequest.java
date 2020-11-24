package org.harvanir.sonarqube.project.entity.bulkdelete;

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
public class BulkDeleteRequest {

    private final String[] keys;

    private final String organization;

    private final String qualifiers;
}