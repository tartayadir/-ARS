package com.utils.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.fluttercode.datafactory.impl.DataFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.chord;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static com.utils.selenium.PageNavigation.threadSleep1Seconds;
import static com.utils.selenium.URLUtils.getHomePageURL;
import static com.utils.selenium.URLUtils.getLoginPageURL;
import static com.utils.spring.AuthTestUtils.getFirstAdminPassword;
import static com.utils.spring.AuthTestUtils.getFirstAdminUsername;

public class ElementsUtils {

    private static JavascriptExecutor js;
    private static final DataFactory dataFactory;
    private static WebDriver driver;
    private static PageNavigation pageNavigation;
    private static WebDriverWait wait;

    private static boolean windowIsOpened = false;

    static {

        dataFactory = new DataFactory();
        WebDriverManager.chromedriver().setup();
    }

    public static void setUP() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--enable-javascript");

        driver = new ChromeDriver(chromeOptions);
        js = ((JavascriptExecutor) driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        pageNavigation = new PageNavigation(driver);

        SeleniumTestsUtils.setFieldsSeleniumTestsUtils(driver, wait, pageNavigation);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static PageNavigation getPageNavigation() {
        return pageNavigation;
    }

    public static WebDriverWait getWait() {
        return wait;
    }

    public static DataFactory getDataFactory() {
        return dataFactory;
    }

    public static WebElement findWebElementById(String elementId) {

        return driver.findElement(id(elementId));
    }

    public static void elementIsViewed(String elementId){

        WebElement element = findWebElementById(elementId);
        pageNavigation.moveToElement(element);
        assertTrue(element.isDisplayed() && element.isEnabled());
    }

    public static void elementIsViewed(WebElement element){

        pageNavigation.moveToElement(element);
        assertTrue(element.isDisplayed() && element.isEnabled());
    }

    public static void elementIsNotViewed(String elementId){

        wait.until(invisibilityOfElementLocated(id(elementId)));
    }

    public static void loginAdmin() {

        driver.get(getLoginPageURL());

        sendPutTextKeyInInputField("usernameInputField", getFirstAdminUsername());
        sendPutTextKeyInInputField("passwordInputField", getFirstAdminPassword());
        pageNavigation.clickOnElement("sendLoginDataButton");

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();

        driver.get(getHomePageURL());

        wait.until(visibilityOf(findWebElementById("logo-header")));

        elementIsViewed("logout-button");
        elementIsViewed("addCarButton");
    }

    public static void logout() {

        driver.get(getHomePageURL());
        pageNavigation.clickOnElement("logout-button");

        elementIsViewed("loginButton");
        elementIsNotViewed("user-icon");
        elementIsNotViewed("addCarButton");
    }

    @SneakyThrows
    public static void sendPutTextKeyInInputField(WebElement input, String key){

        key = key.replaceAll("'", "\\\\'");
        key = key.replaceAll("\n", "\\\\u000d");
        js.executeScript(format("arguments[0].value='%s';", key), input);
        if(key.length() > 0){
            input.sendKeys(chord(BACK_SPACE), key.substring(key.length()-1));
        }
    }

    public static void sendPutTextKeyInInputField(String elementId, String key){

        sendPutTextKeyInInputField(findWebElementById(elementId), key);
    }

    public static void openWindow() {

        if (!windowIsOpened) {

            setUP();
            driver.manage().window().maximize();
            windowIsOpened = !windowIsOpened;
        }
    }

    public static void closeWindow() {

        if (windowIsOpened) {
            driver.close();
            driver.quit();
            windowIsOpened = !windowIsOpened;
        }

    }
}
