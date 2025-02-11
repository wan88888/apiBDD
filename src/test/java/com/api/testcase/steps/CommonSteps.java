package com.api.testcase.steps;

import com.api.automation.APIContext;
import com.api.automation.entity.EntityBuilder;
import com.api.automation.rest.RestAPI;
import com.api.automation.utils.SchemaValidator;
import com.api.automation.entity.RequestEntity;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.Map;
import java.util.List;

public class CommonSteps {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private APIContext apiContext;
    private ObjectMapper objectMapper;

    public CommonSteps() {
        apiContext = APIContext.getInstance();
        apiContext.setRequestEntity(new RequestEntity());
        objectMapper = new ObjectMapper();
    }

    @Given("^Set base request headers$")
    public void setBaseRequestHeaders(DataTable dataTable) {
        Map<String, Object> headersMap = dataTable.asMap(String.class, Object.class);
        apiContext.getRequestEntity().setRequestHeaders(headersMap);
    }

    @Given("^Endpoint \"([^\"]*)\"$")
    public void endpoint(String endpoint) {
        apiContext.setEndPoint(endpoint);
        apiContext.getRequestEntity().setRequestHeaders(Map.of("Content-Type", "application/json"));
    }

    @Given("^Endpoint \"([^\"]*)\" and request headers$")
    public void endpointAndRequestHeaders(String endpoint, DataTable dataTable) throws Throwable {
        Map<String, Object> headersMap = dataTable.asMap(String.class, Object.class);
        EntityBuilder.buildRequestEndpointAndHeaders(apiContext, endpoint, headersMap);
    }

    @And("^Set request \"([^\"]*)\" and update request payload$")
    public void setRequestAndUpdateRequestPayload(String payloadFile, DataTable dataTable) throws Throwable {
        Map<String, Object> payloadMap = dataTable.asMap(String.class, Object.class);
        EntityBuilder.buildRequestPayload(apiContext, payloadFile, payloadMap);
    }

    @When("^Send \"([^\"]*)\" request$")
    public void sendRequest(String method) throws Throwable {
        RestAPI.sendRequest(apiContext, method);
    }

    @When("^Send \"([^\"]*)\" request and response status code is (\\d+)$")
    public void sendRequestAndResponseStatusCodeIs(String method, int statusCode) throws Throwable {
        RestAPI.sendRequest(apiContext, method);
        assertThat(apiContext.getResponseEntity().getHttpStatusCode(), is(statusCode));
    }

    @Then("^Response status code should be (\\d+)$")
    public void responseStatusCodeShouldBe(int statusCode) {
        assertThat(apiContext.getResponseEntity().getHttpStatusCode(), is(statusCode));
    }

    @Then("^Response should not be empty$")
    public void responseShouldNotBeEmpty() throws Exception {
        String responsePayload = apiContext.getResponseEntity().getResponsePayload();
        assertThat(responsePayload, not(isEmptyString()));
    }

    @Then("^Response schema should match with \"([^\"]*)\"$")
    public void responseSchemaMatch(String schemaFile) {
        SchemaValidator.match(apiContext, schemaFile);
    }

    @Then("^Response payload match json \"([^\"]*)\"$")
    public void response_payload_match_json(String schemaFile) {
        SchemaValidator.match(apiContext, schemaFile);
    }

    @Then("^Response should contain field \"([^\"]*)\"$")
    public void response_should_contain_field(String field) {
        String responsePayload = apiContext.getResponseEntity().getResponsePayload();
        Object value;
        try {
            // 首先尝试作为单个对象处理
            value = JsonPath.read(responsePayload, "$." + field);
        } catch (PathNotFoundException e) {
            // 如果失败，则尝试作为数组处理
            value = JsonPath.read(responsePayload, "$[0]." + field);
        }
        assertThat("Field '" + field + "' should exist in response", value, not(nullValue()));
    }

    @Then("^Response field \"([^\"]*)\" should be \"([^\"]*)\"$")
    public void response_field_should_be(String field, String value) {
        String responsePayload = apiContext.getResponseEntity().getResponsePayload();
        Object actualValue = JsonPath.read(responsePayload, "$." + field);
        if (actualValue instanceof List) {
            List<?> values = (List<?>) actualValue;
            assertThat("Field '" + field + "' should not be empty", values, not(empty()));
            assertThat(values.get(0).toString(), is(value));
        } else {
            assertThat(actualValue.toString(), is(value));
        }
    }

    @Then("^Response array \"([^\"]*)\" should have size (\\d+)$")
    public void response_array_should_have_size(String field, int size) {
        String responsePayload = apiContext.getResponseEntity().getResponsePayload();
        List<?> array = JsonPath.read(responsePayload, "$." + field);
        assertThat(array, hasSize(size));
    }

    @Then("^Response field \"([^\"]*)\" should be greater than (\\d+)$")
    public void response_field_should_be_greater_than(String field, int value) {
        String responsePayload = apiContext.getResponseEntity().getResponsePayload();
        Number actualValue = JsonPath.read(responsePayload, "$." + field);
        assertThat(actualValue.doubleValue(), greaterThan((double)value));
    }

    @Then("^Response field \"([^\"]*)\" should be less than (\\d+)$")
    public void response_field_should_be_less_than(String field, int value) {
        String responsePayload = apiContext.getResponseEntity().getResponsePayload();
        Number actualValue = JsonPath.read(responsePayload, "$." + field);
        assertThat(actualValue.doubleValue(), lessThan((double)value));
    }

    @Then("^Response field \"([^\"]*)\" should match regex \"([^\"]*)\"$")
    public void response_field_should_match_regex(String field, String regex) {
        String responsePayload = apiContext.getResponseEntity().getResponsePayload();
        String actualValue = JsonPath.read(responsePayload, "$." + field).toString();
        assertThat(actualValue, matchesPattern(regex));
    }
}