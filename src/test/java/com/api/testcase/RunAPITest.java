package com.api.testcase;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;

@CucumberOptions(
  features = {"classpath:features"},
  glue = {"com.api.testcase.steps"},
  plugin = {"pretty","html:target/cucumber","json:target/cucumber-report.json"},
  tags = "@jsonplaceholder-api"
)
public class RunAPITest extends AbstractTestNGCucumberTests {
    private static Logger logger = LoggerFactory.getLogger(RunAPITest.class);

    @DataProvider(parallel = false)
    @Override
    public Object[][] scenarios(){
        return super.scenarios();
    }

    @AfterTest
    public void afterTest(){
        logger.info("testing finish...");
    }
}
