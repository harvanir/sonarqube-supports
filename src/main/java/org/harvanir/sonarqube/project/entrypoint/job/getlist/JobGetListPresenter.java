package org.harvanir.sonarqube.project.entrypoint.job.getlist;

import org.harvanir.sonarqube.project.entity.getlist.GetListResponse;
import org.harvanir.sonarqube.project.usecase.getlist.GetListPresenter;
import org.harvanir.sonarqube.share.presenter.ImmutablePresenter;
import reactor.core.publisher.Mono;

/**
 * @author Harvan Irsyadi
 */
public class JobGetListPresenter extends ImmutablePresenter<GetListResponse> implements GetListPresenter {

    private GetListResponse presenterResponse;

    public Mono<GetListResponse> getPresenterResponse() {
        return Mono.fromSupplier(() -> presenterResponse);
    }

    @Override
    protected void presentInternal(GetListResponse response) {
        presenterResponse = response;
    }
}