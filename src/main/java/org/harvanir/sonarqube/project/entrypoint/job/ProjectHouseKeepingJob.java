package org.harvanir.sonarqube.project.entrypoint.job;

import lombok.extern.slf4j.Slf4j;
import org.harvanir.sonarqube.configuration.AppProperties;
import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteRequest;
import org.harvanir.sonarqube.project.entity.getlist.GetListRequest;
import org.harvanir.sonarqube.project.entity.getlist.GetListResponse;
import org.harvanir.sonarqube.project.entrypoint.job.bulkdelete.JobBulkDeletePresenter;
import org.harvanir.sonarqube.project.entrypoint.job.getlist.JobGetListPresenter;
import org.harvanir.sonarqube.project.usecase.bulkdelete.BulkDeleteUseCase;
import org.harvanir.sonarqube.project.usecase.getlist.GetListUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * @author Harvan Irsyadi
 */
@Slf4j
public class ProjectHouseKeepingJob extends AbstractJob {

    private final GetListUseCase getListUseCase;

    private final BulkDeleteUseCase bulkDeleteUseCase;

    private final AppProperties appProperties;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ProjectHouseKeepingJob(GetListUseCase getListUseCase, BulkDeleteUseCase bulkDeleteUseCase, AppProperties appProperties) {
        this.getListUseCase = getListUseCase;
        this.bulkDeleteUseCase = bulkDeleteUseCase;
        this.appProperties = appProperties;
    }

    @Scheduled(fixedDelay = 5000)
    @Override
    public void execute() {
        AppProperties.Search search = appProperties.getApi().getSearch();
        int pageSize = search.getPageSize();
        String qualifier = search.getQualifier();
        String organization = search.getOrganization();
        String nameOrKey = search.getNameOrKey();
        int minusDays = search.getMinusDays();

        getComponents(pageSize, organization, qualifier, nameOrKey, minusDays)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("No Components found.")))
                .flatMap(o -> deleteBulk(organization, qualifier, o))
                .block();
    }

    private Mono<GetListResponse.Component[]> getComponents(
            int pageSizes,
            String organization,
            String qualifier,
            String nameOrKey,
            int minusDays
    ) {
        JobGetListPresenter presenter = new JobGetListPresenter();
        return getListUseCase
                .execute(GetListRequest
                                .builder()
                                .pageSize(pageSizes)
                                .qualifiers(qualifier)
                                .organization(organization)
                                .nameOrKey(nameOrKey)
                                .analyzedBefore(dateTimeFormatter.format(LocalDateTime.now().minusDays(minusDays)))
                                .build(),
                        presenter
                )
                .then(presenter.getPresenterResponse())
                .map(this::excludeMain);
    }

    /**
     * Guard for main project. Do not remove!.
     */
    private GetListResponse.Component[] excludeMain(GetListResponse getListResponse) {
        return Arrays.stream(getListResponse.getComponents()).filter(component -> {
            String name = component.getName();
            return name != null && !name.contains("_MAIN");
        }).toArray(GetListResponse.Component[]::new);
    }

    private Mono<Void> deleteBulk(String organization, String qualifier, GetListResponse.Component[] components) {
        String[] keys = Arrays.stream(components).map(GetListResponse.Component::getKey).toArray(String[]::new);

        if (keys.length > 0) {
            return bulkDeleteUseCase.execute(BulkDeleteRequest
                    .builder()
                    .organization(organization)
                    .qualifiers(qualifier)
                    .keys(keys)
                    .build(), new JobBulkDeletePresenter());
        } else {
            log.info("Skipped due to no components found.");
            exitJob();

            return Mono.empty();
        }
    }
}