package org.harvanir.sonarqube.project.entrypoint.job.bulkdelete;

import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteResponse;
import org.harvanir.sonarqube.project.usecase.bulkdelete.BulkDeletePresenter;

/**
 * @author Harvan Irsyadi
 */
public class JobBulkDeletePresenter implements BulkDeletePresenter {

    @Override
    public void present(BulkDeleteResponse bulkDeleteResponse) {
        // NoOps
    }
}