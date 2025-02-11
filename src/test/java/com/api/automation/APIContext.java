package com.api.automation;

import com.api.automation.entity.RequestEntity;
import com.api.automation.entity.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class APIContext {

    private String baseUri = "https://jsonplaceholder.typicode.com";
    private String basePath = "";
    private String endPoint = "";

    private RequestEntity requestEntity;
    private ResponseEntity responseEntity;

    private HashMap<String,String> allResponseBodyMap = new HashMap<>();

    private static APIContext instance = null;

    private APIContext(){}

    public static APIContext getInstance(){
        if(instance == null){
            instance = new APIContext();
        }
        return instance;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public RequestEntity getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    private Map<String, Object> sessionEntity = new HashMap<>();

    public Map<String, Object> getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(Map<String, Object> sessionEntity) {
        this.sessionEntity = sessionEntity;
    }

    public HashMap<String, String> getAllResponseBodyMap() {
        return allResponseBodyMap;
    }
}
