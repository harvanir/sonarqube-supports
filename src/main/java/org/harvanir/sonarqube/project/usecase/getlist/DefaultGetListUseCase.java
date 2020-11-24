package org.harvanir.sonarqube.project.usecase.getlist;

import lombok.extern.slf4j.Slf4j;
import org.harvanir.sonarqube.project.entity.getlist.GetListRequest;
import org.harvanir.sonarqube.project.provider.gateway.ProjectGateway;
import org.harvanir.sonarqube.share.util.ObjectUtils;
import reactor.core.publisher.Mono;

/**
 * @author Harvan Irsyadi
 */
@Slf4j
public class DefaultGetListUseCase implements GetListUseCase {

    private final ProjectGateway projectGateway;

    public DefaultGetListUseCase(ProjectGateway projectGateway) {
        this.projectGateway = projectGateway;
    }

    @Override
    public Mono<Void> execute(GetListRequest getListRequest, GetListPresenter presenter) {
        log.debug("{}", ObjectUtils.defer(() -> getListRequest));

        return projectGateway.getList(getListRequest)
                .doOnNext(presenter::present)
                .then();
    }
}