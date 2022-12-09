package utils.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.By.xpath;
import static utils.spring.AuthTestUtils.*;

public class LoginService {

    private final WebDriver driver;

    private final LocalStorage localStorage;

    private final WebDriverWait wait;

    public LoginService(WebDriver driver) {

        this.driver = driver;
        WebStorage webStorage = (WebStorage) new Augmenter().augment(driver);
        localStorage = webStorage.getLocalStorage();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }



    public void logout(String homePageURL) {

        driver.get(homePageURL);

        wait.until(ExpectedConditions.elementToBeClickable(xpath("//*[@id=\"logout-button\"]"))).click();
    }

    public boolean isAuthorized() {

        String token = localStorage.getItem("auth_tkn");
        return tokenIsValid(token);
    }
}
