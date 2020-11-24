package org.harvanir.sonarqube.project.provider.gateway;

import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteRequest;
import org.harvanir.sonarqube.project.entity.getlist.GetListRequest;
import org.harvanir.sonarqube.project.entity.getlist.GetListResponse;
import reactor.core.publisher.Mono;

/**
 * @author Harvan Irsyadi
 */
public interface ProjectGateway {

    Mono<GetListResponse> getList(GetListRequest getListRequest);

    Mono<Integer> bulkDelete(BulkDeleteRequest bulkDeleteRequest);
}