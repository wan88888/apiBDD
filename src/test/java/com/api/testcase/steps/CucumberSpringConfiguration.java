package com.api.testcase.steps;

import com.api.testcase.config.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfig.class)
public class CucumberSpringConfiguration {
}