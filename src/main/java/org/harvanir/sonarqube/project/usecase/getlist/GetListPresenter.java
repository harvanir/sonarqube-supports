package org.harvanir.sonarqube.project.usecase.getlist;

import org.harvanir.sonarqube.project.entity.getlist.GetListResponse;

/**
 * @author Harvan Irsyadi
 */
public interface GetListPresenter {

    void present(GetListResponse getListResponse);
}