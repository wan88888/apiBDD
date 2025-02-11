package com.api.automation.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

/**
 * 读取环境变量，系统属性或者配置文件获取配置信息
 */
public final class ConfigProvider {
    public static final String CONFIG_BASE_PATH = "src/test/resources/config/";
    public static final String CONFIG_FILE_NAME_PREFIX = "application-";
    public static final String CONFIG_FILE_NAME_EXTENSION = ".conf";
    public static final String DEFAULT_CONFIG_FILE_NAME = "src/test/resources/config/application.conf";

    private static Config conf;
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static synchronized Config config(){
        synchronized (lock1){
            if(conf != null){
                return conf;
            }
        }
        File defaultConfigFile = new File("src/test/resources/config/application.conf");
        Config defaultConfig = ConfigFactory.parseFile(defaultConfigFile);
        conf = ConfigFactory.load(defaultConfig);
        return conf;
    }

    public static synchronized Config getConfig(){
        config();
        synchronized (lock2){
            return conf;
        }
    }

    public static Config getConfig(String key){
        config();
        return conf.getConfig(key);
    }
}
