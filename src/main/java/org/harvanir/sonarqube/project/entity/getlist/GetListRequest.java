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
public class GetListRequest {

    private final String analyzedBefore;

    private final String organization;

    private final int pageSize;

    private final String qualifiers;

    private final String nameOrKey;
}