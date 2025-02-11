package com.api.automation.entity;

import com.api.automation.APIContext;
import com.api.automation.config.PayloadProvider;
import com.api.automation.config.enums.APIResources;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class EntityBuilder {

    private static final Logger logger = LoggerFactory.getLogger(EntityBuilder.class);

    private static RequestEntity createRequestEntity(APIContext apiContext){
        if(apiContext.getRequestEntity() == null){
            RequestEntity requestEntity = new RequestEntity();
            apiContext.setRequestEntity(requestEntity);
        }
        return apiContext.getRequestEntity();
    }
    public static void buildRequestEndpointAndHeaders(APIContext apiContext, String endpoint, Map<String,Object> headersMap){
        RequestEntity requestEntity = createRequestEntity(apiContext);
        apiContext.setEndPoint(endpoint);
        if(headersMap != null && headersMap.size()>0){
            requestEntity.setRequestHeaders(headersMap);
        }
        apiContext.setRequestEntity(requestEntity);
    }

    /**
     * 把URL中需要传的参数，request body中需要传的数据都准备好
     */
    public static void buildRequestPayload(APIContext apiContext,String payloadFile,Map<String,Object> payloadMap){
        buildRequestBody(apiContext,payloadFile,payloadMap);
        buildUrlParams(apiContext,payloadMap);
    }

    private static void buildRequestBody(APIContext apiContext,String payloadFile,Map<String,Object> payloadMap){
        RequestEntity requestEntity = apiContext.getRequestEntity();
        String payloadJson = PayloadProvider.loadPayload(payloadFile);
        requestEntity.setRequestPayload(payloadJson);
        modifyFieldsInPayloadJson(apiContext,payloadMap);
    }

    private static void modifyFieldsInPayloadJson(APIContext apiContext,Map<String,Object> payloadMap){
        String payloadJson = apiContext.getRequestEntity().getRequestPayload();
        for(Map.Entry<String,Object> entry : payloadMap.entrySet()){
            payloadJson = updateValueInJson(apiContext,payloadJson,entry.getKey(),entry.getValue());
        }
        DocumentContext context = JsonPath.using(Configuration.defaultConfiguration()).parse(payloadJson);
        apiContext.getRequestEntity().setRequestPayload(context.jsonString());
    }

    private static String updateValueInJson(APIContext apiContext, String json, String key, Object value) {
        DocumentContext context = JsonPath.using(Configuration.defaultConfiguration()).parse(json);
        String valueStr = value != null ? value.toString() : null;
        
        if (valueStr != null && valueStr.startsWith("$")) {
            if (valueStr.startsWith("$SESSION.")) {
                String sessionKey = valueStr.substring("$SESSION.".length());
                Object sessionValue = apiContext.getSessionEntity().get(sessionKey);
                context.set("$."+key, sessionValue);
            } else if (valueStr.startsWith("$RESPONSE.")) {
                String[] parts = valueStr.split(":");
                if (parts.length == 2) {
                    String responseKey = parts[0].substring("$RESPONSE.".length());
                    String jsonPath = parts[1];
                    String responseBody = apiContext.getAllResponseBodyMap().get(responseKey);
                    if (responseBody != null) {
                        try {
                            // 修改JSON路径处理逻辑，先尝试直接读取，如果失败再尝试数组访问
                            Object responseValue;
                            try {
                                responseValue = JsonPath.parse(responseBody).read(jsonPath);
                            } catch (com.jayway.jsonpath.PathNotFoundException e) {
                                // 如果直接读取失败，尝试使用数组访问
                                responseValue = JsonPath.parse(responseBody).read("$[*]" + jsonPath);
                            }
                            context.set("$."+key, responseValue);
                        } catch (Exception e) {
                            logger.error("Error parsing response JSON path: " + jsonPath, e);
                        }
                    }
                }
            } else if (valueStr.startsWith("$COOKIE.")) {
                // 预留Cookie处理逻辑
            }
        } else {
            context.set("$."+key, value);
        }

        return context.jsonString();
    }

    private static void buildUrlParams(APIContext apiContext,Map<String,Object> payloadMap){
        String payloadJson = apiContext.getRequestEntity().getRequestPayload();
        DocumentContext context = JsonPath.using(Configuration.defaultConfiguration()).parse(payloadJson);
        Map<String, Object> queryParams = new HashMap<String, Object>();
        for(Map.Entry<String,Object> entry : payloadMap.entrySet()){
            String value = entry.getValue() != null ? entry.getValue().toString() : null;
            if(value != null && value.startsWith("$")){
                if(value.startsWith("$SESSION")){//get value from session
                    String sessionKey = value.split("\\$SESSION.")[1];
                    Object sessionValue = apiContext.getSessionEntity().get(sessionKey);
                    queryParams.put(entry.getKey(), sessionValue != null ? sessionValue.toString() : null);
                }else if(value.startsWith("$RESPONSE")){
                    String key = value.split(":")[0].split("\\$RESPONSE.")[1];
                    String responseBody = apiContext.getAllResponseBodyMap().get(key);
                    Object responseValue = JsonPath.parse(responseBody).read(value.split(":")[1]);
                    queryParams.put(entry.getKey(),responseValue != null ? responseValue.toString() : null);
                }else if(value.startsWith("$COOKIE")){//get value from cookie

                }
            }else {
                queryParams.put(entry.getKey(), value);
            }
        }
        apiContext.getRequestEntity().setQueryParams(queryParams);
    }
}
