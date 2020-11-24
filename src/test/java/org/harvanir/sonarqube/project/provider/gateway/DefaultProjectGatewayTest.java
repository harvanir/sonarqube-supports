package org.harvanir.sonarqube.project.provider.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.harvanir.sonarqube.project.entity.getlist.GetListRequest;
import org.harvanir.sonarqube.project.entity.getlist.GetListResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Path.SEARCH;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.NAME_OR_KEY;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.ORGANIZATION;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.PAGE_SIZE;
import static org.harvanir.sonarqube.project.provider.gateway.ApiConstant.Variable.QUALIFIERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Harvan Irsyadi
 */
@SuppressWarnings("unchecked")
class DefaultProjectGatewayTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private WebClient webClient;

    private WebClient.RequestHeadersUriSpec uriSpec;

    private ProjectGateway projectGateway;

    private WebClient.ResponseSpec responseSpec;

    @BeforeAll
    static void beforeAll() {
        objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    }

    @BeforeEach
    void beforeEach() {
        webClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        projectGateway = new DefaultProjectGateway(webClient, objectMapper);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(webClient, uriSpec, responseSpec);
    }

    @Test
    void givenRequest_whenGetList_thenReturnExpected() {
        // given
        GetListRequest request = GetListRequest
                .builder()
                .analyzedBefore("analyzedBefore")
                .organization("organization")
                .pageSize(50)
                .qualifiers("qualifiers")
                .nameOrKey("nameOrKey")
                .build();
        int pageIndex = 1;
        int total = 0;
        String responseString = String.format("{" +
                        "\"paging\":{\"pageIndex\":%s,\"pageSize\":%s,\"total\":%s}," +
                        "\"components\":[]" +
                        "}",
                pageIndex,
                request.getPageSize(),
                total
        );
        Mono<String> monoResponse = Mono.just(responseString);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(monoResponse);

        // when
        GetListResponse response = projectGateway.getList(request).block();

        // then
        ArgumentCaptor<Function<UriBuilder, URI>> captorUri = ArgumentCaptor.forClass(Function.class);
        verify(webClient, times(1)).get();
        verify(uriSpec, times(1)).uri(captorUri.capture());
        verify(uriSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);

        // assert path & query param
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        Mutable<Map<String, String>> mutableQueryMap = new Mutable<>();
        Function<UriBuilder, URI> uriFunction = captorUri.getValue();

        assertThat(uriFunction.apply(uriBuilder))
                .matches(uri -> SEARCH.equalsIgnoreCase(uri.getPath()), SEARCH)
                .matches(uri -> {
                    mutableQueryMap.setObject(UriComponentsBuilder.fromUri(uri).build().getQueryParams().toSingleValueMap());
                    return !mutableQueryMap.getObject().isEmpty();
                });
        Map<String, String> queryMap = mutableQueryMap.getObject();
        assertEquals(request.getOrganization(), queryMap.get(ORGANIZATION));
        assertEquals(request.getPageSize(), Integer.parseInt(queryMap.get(PAGE_SIZE)));
        assertEquals(request.getQualifiers(), queryMap.get(QUALIFIERS));
        assertEquals(request.getNameOrKey(), queryMap.get(NAME_OR_KEY));

        // assert response
        assertNotNull(response);
        assertNotNull(response.getPaging());
        assertNotNull(response.getComponents());
        assertEquals(pageIndex, response.getPaging().getPageIndex());
        assertEquals(total, response.getPaging().getTotal());
        assertEquals(request.getPageSize(), response.getPaging().getPageSize());
    }

    static class Mutable<T> {

        private T object;

        public void setObject(T object) {
            this.object = object;
        }

        public T getObject() {
            return object;
        }
    }
}