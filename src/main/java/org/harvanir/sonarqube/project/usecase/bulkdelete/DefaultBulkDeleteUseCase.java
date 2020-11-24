package org.harvanir.sonarqube.project.usecase.bulkdelete;

import lombok.extern.slf4j.Slf4j;
import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteRequest;
import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteResponse;
import org.harvanir.sonarqube.project.provider.gateway.ProjectGateway;
import org.harvanir.sonarqube.share.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Harvan Irsyadi
 */
@Slf4j
public class DefaultBulkDeleteUseCase implements BulkDeleteUseCase {

    private final ProjectGateway projectGateway;

    public DefaultBulkDeleteUseCase(ProjectGateway projectGateway) {
        this.projectGateway = projectGateway;
    }

    @Override
    public Mono<Void> execute(BulkDeleteRequest bulkDeleteRequest, BulkDeletePresenter presenter) {
        log.debug("{}", ObjectUtils.defer(() -> bulkDeleteRequest));

        return projectGateway.bulkDelete(bulkDeleteRequest)
                .doOnNext(response -> presenter.present(BulkDeleteResponse
                        .builder()
                        .status(Objects.requireNonNull(response, "Unknown response"))
                        .build()))
                .then();
    }
}