package com.api.automation.config.enums;

public enum ConfigKeys {
    API_BASE_URI {
        public String toString(){
            return "api.base-uri";
        }
    },
    API_BASE_PATH {
        public String toString(){
            return "api.base-path";
        }
    },
    HEADERS{
        public String toString(){
            return "headers";
        }
    },
    DEFAULT {
        public String toString(){
            return "default";
        }
    }
}
