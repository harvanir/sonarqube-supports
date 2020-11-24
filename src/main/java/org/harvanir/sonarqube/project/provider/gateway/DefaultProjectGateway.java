package org.harvanir.sonarqube.project.provider.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.harvanir.sonarqube.project.entity.bulkdelete.BulkDeleteRequest;
import org.harvanir.sonarqube.project.entity.getlist.GetListRequest;
import org.harvanir.sonarqube.project.entity.getlist.GetListResponse;
import org.harvanir.sonarqube.share.util.JsonUtils;
import org.harvanir.sonarqube.share.util.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Path.BULK_DELETE;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Path.SEARCH;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.ANALYZED_BEFORE;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.NAME_OR_KEY;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.ORGANIZATION;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.PAGE_SIZE;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.PROJECTS;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.QUALIFIERS;

/**
 * @author Harvan Irsyadi
 */
@Slf4j
public class DefaultProjectGateway implements ProjectGateway {

    private final WebClient projectWebClient;

    private final ObjectMapper objectMapper;

    public DefaultProjectGateway(WebClient projectWebClient, ObjectMapper objectMapper) {
        this.projectWebClient = projectWebClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<GetListResponse> getList(GetListRequest request) {
        return projectWebClient.get()
                .uri(builder -> builder
                        .path(SEARCH)
                        .queryParam(ANALYZED_BEFORE, request.getAnalyzedBefore())
                        .queryParam(ORGANIZATION, request.getOrganization())
                        .queryParam(PAGE_SIZE, request.getPageSize())
                        .queryParam(QUALIFIERS, request.getQualifiers())
                        .queryParam(NAME_OR_KEY, request.getNameOrKey())
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                // cpu bound for deserializing
                .publishOn(Schedulers.parallel())
                .map(s -> {
                    log.debug("{}", ObjectUtils.defer(() -> String.format("Response: %s", s)));

                    return JsonUtils.readValue(s, GetListResponse.class, objectMapper);
                });
    }

    @Override
    public Mono<Integer> bulkDelete(BulkDeleteRequest request) {
        return projectWebClient.post()
                .uri(builder -> builder
                        .path(BULK_DELETE)
                        .build()
                )
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters
                        .fromFormData(ORGANIZATION, request.getOrganization())
                        .with(QUALIFIERS, request.getQualifiers())
                        .with(PROJECTS, String.join(",", request.getKeys()))
                )
                .retrieve()
                .toEntity(Void.class)
                .map(o -> {
                    log.debug("{}", ObjectUtils.defer(() -> String.format("Response: %s", o)));

                    return o.getStatusCodeValue();
                });
    }
}