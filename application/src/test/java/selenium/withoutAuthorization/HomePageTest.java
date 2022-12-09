package selenium.withoutAuthorization;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.selenium.URLUtils.getHomePageURL;
import static utils.selenium.URLUtils.getLoginPageURL;

public class HomePageTest {

    private static WebDriver driver;

    @FindBy(xpath = "//*[@id=\"come-back-button\"]")
    private static WebElement comeBackButton;

    @FindBy(xpath = "//*[@id=\"logo-header\"]")
    private static WebElement logo;

    @FindBy(xpath = "//*[@id=\"sendLoginDataButton\"]")
    private static WebElement sendLoginDataButton;

    @FindBy(xpath = "//*[@id=\"loginButton\"]")
    private static WebElement loginButton;

    @FindBy(xpath = "//*[@id=\"logout-button\"]")
    private static WebElement logoutButton;

    @FindBy(xpath = "//*[@id=\"login-form\"]")
    private static WebElement loginForm;

    @BeforeAll
    static void beforeAll() {

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @BeforeEach
    void setUp()  {

        PageFactory.initElements(driver, this);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get(getHomePageURL());
    }

    @AfterAll
    static void afterAll() {

        driver.close();
        driver.quit();
    }

    @Test
    void startPageIsHomePage() {

        assertEquals(getHomePageURL(), driver.getCurrentUrl());
    }

    @Test
    void checkLogo() {

        logo.click();
        assertEquals(getHomePageURL(), driver.getCurrentUrl());
    }

    @Test
    void loginButton() {

        loginButton.click();

        assertEquals(getLoginPageURL(), driver.getCurrentUrl());
        assertTrue(loginForm.isDisplayed());
        assertTrue(comeBackButton.isDisplayed());
        assertTrue(sendLoginDataButton.isDisplayed());
        assertThatThrownBy( () -> loginButton.click()).
                isInstanceOf(NoSuchElementException.class);
    }
}
