package com.api.automation.rest;

import com.api.automation.APIContext;
import com.api.automation.entity.ResponseEntity;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RestAPI {

    private static final Logger logger = LoggerFactory.getLogger(RestAPI.class);

    private static final String GET = "GET";
    private static final String POST = "POST";

    public static void sendRequest(APIContext apiContext, String method) {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(apiContext.getBaseUri());
        requestSpecification.basePath(apiContext.getBasePath());

        Map<String, Object> headers = apiContext.getRequestEntity().getRequestHeaders();
        if (headers != null && !headers.isEmpty()) {
            requestSpecification.headers(headers);
        }

        if (method.equalsIgnoreCase(POST)) {
            sendPostRequest(apiContext, requestSpecification);
        } else if (method.equalsIgnoreCase(GET)) {
            sendGetRequest(apiContext, requestSpecification);
        }
    }

    private static void sendPostRequest(APIContext apiContext, RequestSpecification requestSpecification) {
        String payload = apiContext.getRequestEntity().getRequestPayload();
        if (payload != null && !payload.isEmpty()) {
            requestSpecification.body(payload);
        }

        Map<String, Object> queryParams = apiContext.getRequestEntity().getQueryParams();
        if (queryParams != null && !queryParams.isEmpty()) {
            requestSpecification.queryParams(queryParams);
        }

        Response response = requestSpecification.post(apiContext.getEndPoint());
        processResponse(apiContext, response);
    }

    private static void sendGetRequest(APIContext apiContext, RequestSpecification requestSpecification) {
        Map<String, Object> queryParams = apiContext.getRequestEntity().getQueryParams();
        if (queryParams != null && !queryParams.isEmpty()) {
            requestSpecification.queryParams(queryParams);
        }

        Response response = requestSpecification.get(apiContext.getEndPoint());
        processResponse(apiContext, response);
    }

    private static void processResponse(APIContext apiContext, Response response) {
        ResponseEntity responseEntity = new ResponseEntity();
        ValidatableResponse validatableResponse = response.then();

        responseEntity.setHttpStatusCode(response.getStatusCode());
        responseEntity.setResponsePayload(response.getBody().asString());
        responseEntity.setValidatableResponse(validatableResponse);

        apiContext.setResponseEntity(responseEntity);
    }
}
