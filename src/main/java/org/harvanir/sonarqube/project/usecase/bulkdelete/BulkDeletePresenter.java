package org.harvanir.sonarqube.project.usecase.bulkdelete;

import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteResponse;

/**
 * @author Harvan Irsyadi
 */
public interface BulkDeletePresenter {

    void present(BulkDeleteResponse bulkDeleteResponse);
}