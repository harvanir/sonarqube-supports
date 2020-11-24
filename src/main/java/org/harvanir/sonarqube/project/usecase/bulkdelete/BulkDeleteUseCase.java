package org.harvanir.sonarqube.project.usecase.bulkdelete;

import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteRequest;
import reactor.core.publisher.Mono;

/**
 * @author Harvan Irsyadi
 */
public interface BulkDeleteUseCase {

    Mono<Void> execute(BulkDeleteRequest bulkDeleteRequest, BulkDeletePresenter presenter);
}