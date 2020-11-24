package org.harvanir.sonarqube.project.usecase.getlist;

import org.harvanir.sonarqube.project.entity.getlist.GetListRequest;
import reactor.core.publisher.Mono;

/**
 * @author Harvan Irsyadi
 */
public interface GetListUseCase {

    Mono<Void> execute(GetListRequest getListRequest, GetListPresenter presenter);
}