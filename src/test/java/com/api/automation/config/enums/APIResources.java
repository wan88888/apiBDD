package com.api.automation.config.enums;

import com.api.automation.config.ConfigProvider;
import org.apache.commons.lang.StringUtils;

public enum APIResources {
    BASE_URI{
        public String toString(){
            //首先从系统属性或者环境变量中获取baseURI
            String baseUri = System.getProperty("DOMAIN");
            if(StringUtils.isBlank(baseUri)){
                baseUri = System.getenv("DOMAIN");
            }
            //从配置文件中读取
            if(StringUtils.isBlank(baseUri)){
                baseUri = ConfigProvider.config().getConfig(ConfigKeys.API_BASE_URI.toString()).getString(ConfigKeys.DEFAULT.toString());
            }
            return baseUri;
        }
    },
    BASE_PATH {
        public String toString(){
            return ConfigProvider.config().getConfig(ConfigKeys.API_BASE_PATH.toString()).getString(ConfigKeys.DEFAULT.toString());
        }
    };

    APIResources(){}
}
