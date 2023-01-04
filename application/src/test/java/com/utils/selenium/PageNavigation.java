package com.utils.selenium;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.String.format;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

@Slf4j
public class PageNavigation {

    private final WebDriverWait wait;

    private final JavascriptExecutor js;

    public PageNavigation(WebDriver driver) {

        this.js = ((JavascriptExecutor) driver);
        Assertions.assertNotNull(ElementsUtils.getWait());
        wait = ElementsUtils.getWait();
    }

    @SneakyThrows
    public void clickOnElement(WebElement webElement){

        js.executeScript("arguments[0].click();", webElement);
    }

    public void clickOnElement(String elementId){

        js.executeScript(format("document.getElementById('%s').click();", elementId));
    }

    public void moveToElement(WebElement webElement) {

        js.executeScript("arguments[0].scrollIntoView()", webElement);
        wait.until(visibilityOf(webElement));
    }

    public void hoverOnElement(WebElement element){

        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void threadSleep1Seconds(){

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), (Object[]) e.getStackTrace());
        }
    }
}
