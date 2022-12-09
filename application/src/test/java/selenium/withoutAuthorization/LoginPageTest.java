package selenium.withoutAuthorization;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static utils.selenium.PageNavigation.threadSleep1Seconds;
import static utils.selenium.URLUtils.getHomePageURL;
import static utils.selenium.URLUtils.getLoginPageURL;
import static utils.spring.AuthTestUtils.getPassword;
import static utils.spring.AuthTestUtils.getUsername;
import static utils.spring.StringUtils.generateRandomString;
import static utils.spring.StringUtils.generateRandomStringWithSpecialChars;

@Slf4j
public class LoginPageTest {

    private static String loginPageURL;

    private static WebDriver driver;

    private static Duration duration_3_second;

    private static WebDriverWait wait;

    @FindBy(xpath = "//*[@id=\"addCarButton\"]")
    private static WebElement addCarButton;

    @FindBy(xpath = "//*[@id=\"come-back-button\"]")
    private static WebElement comeBackButton;

    @FindBy(xpath = "//*[@id=\"sendLoginDataButton\"]")
    private static WebElement sendLoginDataButton;

    @FindBy(xpath = "//*[@id=\"usernameInputField\"]")
    private static WebElement usernameInput;

    @FindBy(xpath = "//*[@id=\"usernameErrorMassage\"]")
    private static WebElement usernameErrorMassage;

    @FindBy(xpath = "//*[@id=\"passwordInputField\"]")
    private static WebElement passwordInput;

    @FindBy(xpath = "//*[@id=\"passwordErrorMassage\"]")
    private static WebElement passwordErrorMassage;

    @FindBy(xpath = "//*[@id=\"loginButton\"]")
    private static WebElement loginButton;

    @FindBy(xpath = "//*[@id=\"errorUserInfoModal\"]")
    private static WebElement errorUserInfoModal;

    @FindBy(xpath = "//*[@id=\"modal-header\"]")
    private static WebElement errorUserInfoModalHeader;

    @FindBy(xpath = "//*[@id=\"errorUserInfoModal\"]/div[2]")
    private static WebElement errorUserInfoModalBody;

    @FindBy(xpath = "//*[@id=\"okButton\"]")
    private static WebElement errorUserInfoModalOkButton;

    @FindBy(xpath = "//*[@id=\"modal-error-title\"]")
    private static WebElement modalErrorTitle;

    @FindBy(xpath = "//*[@id=\"modal-error-body\"]")
    private static WebElement modalErrorBody;

    @FindBy(xpath = "//*[@id=\"errorOkButton\"]")
    private static WebElement modalErrorOkButton;

    @FindBy(xpath = "//*[@id=\"logout-button\"]")
    private static WebElement logoutButton;

    @FindBy(xpath = "//*[@id=\"login-form\"]")
    private static WebElement loginForm;

    @FindBy(xpath = "//*[@id=\"user-icon\"]")
    private static WebElement userLogo;

    @BeforeAll
    static void beforeAll() {

        loginPageURL = getLoginPageURL();

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        duration_3_second = Duration.ofSeconds(3);
    }

    @BeforeEach
    void setUp()  {

        wait = new WebDriverWait(driver, duration_3_second);

        PageFactory.initElements(driver, this);
        driver.manage().timeouts().implicitlyWait(duration_3_second);
        driver.get(loginPageURL);
    }

    @AfterAll
    static void afterAll() {
        driver.close();
        driver.quit();
    }

    @Test
    void startPageIsLoginPage() {

        assertEquals(getLoginPageURL(), driver.getCurrentUrl());
        assertTrue(loginForm.isDisplayed());
        assertTrue(comeBackButton.isDisplayed() && comeBackButton.isEnabled());
        assertTrue(sendLoginDataButton.isDisplayed() && sendLoginDataButton.isEnabled());
        assertTrue(usernameInput.isDisplayed() && usernameInput.isEnabled());
        assertTrue(passwordInput.isDisplayed() && passwordInput.isEnabled());
    }

    @Test
    void checkComeBackButton() {

        driver.get(getHomePageURL());

        loginButton.click();
        wait.until(elementToBeClickable(comeBackButton)).click();
        wait.until(visibilityOf(loginButton));

        assertEquals(getHomePageURL(), driver.getCurrentUrl());
    }

    @Test
    void trySendEmptyFields() {

        sendLoginDataButton.click();
        threadSleep1Seconds();
        checkAndCloseInvalidLoginDataModalWindow();

        String requiredUsernameErrorMassage = "Username is required.";
        checkErrorValidationMassage(requiredUsernameErrorMassage, usernameErrorMassage);

        String requiredPasswordErrorMassage = "Password is required.";
        checkErrorValidationMassage(requiredPasswordErrorMassage, passwordErrorMassage);
    }

    @Test
    void successfulAuthorization() {

        sendLoginData(getUsername(), getPassword());
        threadSleep1Seconds();
        driver.get(getHomePageURL());

        assertTrue(logoutButton.isDisplayed() && logoutButton.isEnabled());
        assertTrue(addCarButton.isDisplayed() && addCarButton.isEnabled());
        assertTrue(userLogo.isDisplayed());

        logoutButton.click();

        assertEquals(getHomePageURL(), driver.getCurrentUrl());
        assertThatThrownBy( () -> logoutButton.click()).
                isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy( () -> addCarButton.click()).
                isInstanceOf(NoSuchElementException.class);
        assertTrue(loginButton.isDisplayed() && loginButton.isEnabled());
    }

    @Test
    void loginDataFromDB(){

        String username = "Admin_1";
        String password = "$2a$10$drKjXIFgUneNLlmD88S4fuCzVoe2RBjRLrcrkaZ2RB0DW/6.6DLxW";
        String errorValidationMassage =  "Password must not contain anything other than digits," +
                " letters and underlining.";

        sendLoginData(username, password);
        checkAndCloseInvalidLoginDataModalWindow();
        checkErrorValidationMassage(errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void usernameRequiredValidation() {

        String requiredUsernameErrorMassage = "Username is required.";
        String password = "12345678";

        usernameInput.click();
        passwordInput.sendKeys(password);

        checkErrorValidationMassage("", passwordErrorMassage);
        checkErrorValidationMassage(requiredUsernameErrorMassage, usernameErrorMassage);
        sendLoginDataButton.click();
        checkAndCloseInvalidLoginDataModalWindow();

        sendLoginData("User_123", password);
        checkErrorValidationMassage("", passwordErrorMassage);
    }

    @Test
    void usernameMinLengthValidation(){

        String errorValidationMassage = "Username must be greater than 2.";
        String username;
        String password = generateRandomString(10);

        username = generateRandomString(1);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);

        username = generateRandomString(1);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);

        username = generateRandomString(1);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);
    }

    @Test
    void usernameMaxLengthValidation(){

        String errorValidationMassage = "Username must be less than 30.";
        String username;
        String password = generateRandomString(10);

        username = generateRandomString(32);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);

        username = generateRandomString(213);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);

        username = generateRandomString(76);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);
    }

    @Test
    void usernamePatternValidation(){

        String errorValidationMassage = "Username must not contain anything other than digits," +
                " letters and underlining.";
        String username;
        String password = generateRandomString(10);

        username = generateRandomStringWithSpecialChars(7);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);

        username = generateRandomStringWithSpecialChars(15);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);

        username = generateRandomStringWithSpecialChars(26);
        checkInvalidLoginData(username, password, errorValidationMassage, usernameErrorMassage);
    }

    @Test
    void passwordRequiredValidation() {

        String requiredPasswordErrorMassage = "Password is required.";
        String username = "User_123";
        passwordInput.click();
        usernameInput.sendKeys(username);

        checkErrorValidationMassage("",usernameErrorMassage);
        checkErrorValidationMassage(requiredPasswordErrorMassage, passwordErrorMassage);
        sendLoginDataButton.click();
        checkAndCloseInvalidLoginDataModalWindow();

        passwordInput.sendKeys("12345678");
        checkErrorValidationMassage("",usernameErrorMassage);
    }

    @Test
    void passwordMinLengthValidation(){

        String errorValidationMassage = "Password must be greater than 8.";
        String password;
        String username = "User_123";

        password = generateRandomString(1);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);

        password = generateRandomString(5);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);

        password = generateRandomString(7);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void passwordMaxLengthValidation(){

        String errorValidationMassage = "Password must be less than 100.";
        String password;
        String username = "User_123";

        password = generateRandomString(102);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);

        password = generateRandomString(582);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);

        password = generateRandomString(344);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void passwordPatternValidation() {

        String errorValidationMassage =  "Password must not contain anything other than digits," +
                " letters and underlining.";
        String password;
        String username = "User_123";

        password = generateRandomStringWithSpecialChars(23);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);

        password = generateRandomStringWithSpecialChars(54);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);

        password = generateRandomStringWithSpecialChars(84);
        checkInvalidLoginData(username, password, errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void checkIncorrectLoginData() {

        String username, password;

        username = generateRandomString(2);
        password = generateRandomString(8);
        checkIncorrectLoginData(username, password);

        username = generateRandomString(4);
        password = generateRandomString(19);
        checkIncorrectLoginData(username, password);

        username = generateRandomString(29);
        password = generateRandomString(43);
        checkIncorrectLoginData(username, password);

        username = generateRandomString(10);
        password = generateRandomString(23);
        checkIncorrectLoginData(username, password);

        username = generateRandomString(30);
        password = generateRandomString(100);
        checkIncorrectLoginData(username, password);

    }

    private static void checkInvalidLoginData(String username, String password, String exceptedErrorMassage,
                                              WebElement errorMassageAlert){
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);

        assertEquals(exceptedErrorMassage,errorMassageAlert.getAttribute("innerText"));

        sendLoginDataButton.click();
        threadSleep1Seconds();
        checkAndCloseInvalidLoginDataModalWindow();
        usernameInput.clear();
        passwordInput.clear();
    }

    private static void checkIncorrectLoginData(String username, String password){

        sendLoginData(username, password);
        checkAndCloseIncorrectLoginDataModalWindow();

        assertEquals(getLoginPageURL(), driver.getCurrentUrl());
        assertTrue(loginForm.isDisplayed());
        assertTrue(comeBackButton.isDisplayed() && comeBackButton.isEnabled());
        assertTrue(sendLoginDataButton.isDisplayed() && sendLoginDataButton.isEnabled());
        assertTrue(usernameInput.isDisplayed() && usernameInput.isEnabled());
        assertTrue(passwordInput.isDisplayed() && passwordInput.isEnabled());

        usernameInput.clear();
        passwordInput.clear();
    }

    private static void checkAndCloseInvalidLoginDataModalWindow() {

        String errorUserInfoModalHeaderExceptedText =
                "Incorrect user info";
        String errorUserInfoModalBodyExceptedText =
                "You entered incorrect username or password. Please, check your entered date and try again.";

        threadSleep1Seconds();
        assertTrue(errorUserInfoModal.isDisplayed());

        String actualHeaderText = errorUserInfoModalHeader.getAttribute("innerText");
        assertEquals(errorUserInfoModalHeaderExceptedText, actualHeaderText);

        String actualBodyText = errorUserInfoModalBody.getAttribute("innerText");
        assertEquals(errorUserInfoModalBodyExceptedText, actualBodyText);

        errorUserInfoModalOkButton.click();
    }

    private static void checkAndCloseIncorrectLoginDataModalWindow() {

        String actualModalErrorTitleText = modalErrorTitle.getAttribute("innerText");
        String exceptedModalErrorTitleText = "Error";
        assertEquals(exceptedModalErrorTitleText, actualModalErrorTitleText);

        String actualModalErrorBodyText = modalErrorBody.getAttribute("innerText");
        String exceptedModalErrorBodyText = "Data for authentication is invalid. Login failed.";
        assertEquals(exceptedModalErrorBodyText, actualModalErrorBodyText);

        modalErrorOkButton.click();
    }

    private static void checkErrorValidationMassage(String exceptedErrorMassage, WebElement errorMassageAlert){

        String actualErrorMassage = errorMassageAlert.getAttribute("innerText");
        assertEquals(exceptedErrorMassage, actualErrorMassage);
    }

    private static void sendLoginData(String username, String password){

        usernameInput.click();
        usernameInput.sendKeys(username);
        passwordInput.click();
        passwordInput.sendKeys(password);
        sendLoginDataButton.click();
    }
}
