package com.applitools.example;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.Region;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.locators.VisualLocator;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class LocatorTest {
    private static String applitoolsApiKey;
    private static boolean headless;

    private static BatchInfo batch;
    private static Configuration config;
    private static ClassicRunner runner;

    // Test-specific objects
    private WebDriver driver;
    private Eyes eyes;

    @BeforeAll
    public static void setUpConfigAndRunner() {
        applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");
        headless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "true"));
        runner = new ClassicRunner();
        batch = new BatchInfo("Locators");
        config = new Configuration();
        config.setApiKey(applitoolsApiKey);
        config.setBatch(batch);
    }

    @BeforeEach
    public void openBrowserAndEyes(TestInfo testInfo) {
        driver = new ChromeDriver(new ChromeOptions().setHeadless(headless));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        eyes = new Eyes(runner);
        eyes.setConfiguration(config);


        eyes.open(
                driver,
                "Locator Debug Test2",
                testInfo.getDisplayName()
        );
    }

    @Test
    public void logIntoBankAccount() {

        String pathToLocatorHtml = String.valueOf(getClass().getResource("/locator.html"));
        driver.get(pathToLocatorHtml);

        eyes.check(Target
                .window()
                .fully()
                .withName("Locator page")
        );

        Map<String, List<Region>> locators = eyes.locate(
                VisualLocator
                        .name("Circle")
                        .name("Triangle")
                        .name("Home")
        );

        locators.forEach((key, value) -> value.forEach(System.out::println));

        eyes.check(Target.window().fully().withName("Locator page").layout());
    }

    @AfterEach
    public void cleanUpTest() {
        driver.quit();
        eyes.closeAsync();
    }

    @AfterAll
    public static void printResults() {
        TestResultsSummary allTestResults = runner.getAllTestResults();
        System.out.println(allTestResults);
    }
}