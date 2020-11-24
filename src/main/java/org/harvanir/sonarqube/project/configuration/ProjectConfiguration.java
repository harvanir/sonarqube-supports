package org.harvanir.sonarqube.project.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.harvanir.sonarqube.configuration.AppProperties;
import org.harvanir.sonarqube.project.entrypoint.job.AbstractJobAspect;
import org.harvanir.sonarqube.project.entrypoint.job.ProjectHouseKeepingJob;
import org.harvanir.sonarqube.project.provider.gateway.DefaultProjectGateway;
import org.harvanir.sonarqube.project.provider.gateway.ProjectGateway;
import org.harvanir.sonarqube.project.usecase.bulkdelete.BulkDeleteUseCase;
import org.harvanir.sonarqube.project.usecase.bulkdelete.DefaultBulkDeleteUseCase;
import org.harvanir.sonarqube.project.usecase.getlist.DefaultGetListUseCase;
import org.harvanir.sonarqube.project.usecase.getlist.GetListUseCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

/**
 * @author Harvan Irsyadi
 */
@Configuration(proxyBeanMethods = false)
public class ProjectConfiguration {

    @Bean
    public WebClient projectWebClient(AppProperties appProperties) {
        return WebClient.builder()
                .baseUrl(appProperties.getApi().getHostName())
                .filter((request, next) -> {
                    String credentialsString = appProperties.getToken() + ":";
                    byte[] encodedBytes = Base64.getEncoder().encode(credentialsString.getBytes());

                    return next.exchange(ClientRequest.from(request)
                            .headers(headers -> headers.setBasicAuth(new String(encodedBytes)))
                            .build());
                })
                .build();
    }

    @Bean
    public ProjectGateway projectGateway(WebClient projectWebClient, ObjectMapper objectMapper) {
        return new DefaultProjectGateway(projectWebClient, objectMapper);
    }

    @Bean
    public GetListUseCase getListUseCase(ProjectGateway projectGateway) {
        return new DefaultGetListUseCase(projectGateway);
    }

    @Bean
    public BulkDeleteUseCase bulkDeleteUseCase(ProjectGateway projectGateway) {
        return new DefaultBulkDeleteUseCase(projectGateway);
    }

    @Bean
    public ProjectHouseKeepingJob projectHouseKeepingJob(
            GetListUseCase getListUseCase,
            BulkDeleteUseCase bulkDeleteUseCase,
            AppProperties appProperties
    ) {
        return new ProjectHouseKeepingJob(getListUseCase, bulkDeleteUseCase, appProperties);
    }

    @Bean
    public AbstractJobAspect jobAspect(ApplicationContext applicationContext) {
        return new AbstractJobAspect(applicationContext);
    }
}