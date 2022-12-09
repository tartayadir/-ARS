package utils.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@Slf4j
public class PageNavigation {

    private final JavascriptExecutor js;

    private final Actions actions;

    public PageNavigation(WebDriver driver) {

        this.js = ((JavascriptExecutor) driver);
        this.actions = new Actions(driver);
    }

    public void clickOnElement(WebElement webElement){

        js.executeScript("arguments[0].scrollIntoView()", webElement);
        threadSleep1Seconds();
        webElement.click();
    }

    public void moveToElement(WebElement webElement) {

        js.executeScript("arguments[0].scrollIntoView()", webElement);
        threadSleep1Seconds();
    }

    public void hoverOnElement(WebElement element){

        actions.moveToElement(element).build().perform();
    }

    public void scrollUpDown(){

        scrollUp();
        scrollDown();
    }
    public void scrollUp(){

        try {
            js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
            Thread.sleep(1_000);
        } catch (InterruptedException e){
            log.error(e.getMessage(), (Object) e.getStackTrace());
        }
    }

    public void scrollDown() {
        try {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1_000);
        } catch (InterruptedException e){
            log.error(e.getMessage(), (Object) e.getStackTrace());
        }
    }

    public static void threadSleep1Seconds(){

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), (Object[]) e.getStackTrace());
        }
    }
}
