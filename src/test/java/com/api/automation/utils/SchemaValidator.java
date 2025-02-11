package com.api.automation.utils;

import com.api.automation.APIContext;
import io.restassured.module.jsv.JsonSchemaValidator;

public class SchemaValidator {
    public static void match(APIContext apiContext,String schemaFile){
        apiContext.getResponseEntity().getValidatableResponse().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/" + schemaFile));
    }
}
