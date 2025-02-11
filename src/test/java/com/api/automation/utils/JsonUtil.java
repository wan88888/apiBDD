package com.api.automation.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static JSONArray getJSONArray(String fullJson,String key){
        try{
            JSONObject jsonObject = new JSONObject(fullJson);
            if(jsonObject.has(key)){
                return jsonObject.getJSONArray(key);
            }
        }catch (Exception ignored){}
        return new JSONArray();
    }

    public static String getJSONArrayString(String fullJson,String key){
        return getJSONArray(fullJson,key).toString();
    }

    public static JSONObject getJSONObject(String fullJson,String key){
        try{
            JSONObject jsonObject = new JSONObject(fullJson);
            if(jsonObject.has(key)){
                return jsonObject.getJSONObject(key);
            }
        }catch (Exception ignored){}
        return new JSONObject();
    }

    public static String getJSONObjectString(String fullJson,String key){
        return getJSONObject(fullJson,key).toString();
    }
}
