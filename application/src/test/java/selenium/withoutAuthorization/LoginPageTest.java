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
import utils.selenium.PageNavigation;

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

    private static PageNavigation pageNavigation;

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

        pageNavigation = new PageNavigation(driver);
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
        threadSleep1Seconds();
        threadSleep1Seconds();

        driver.get(getHomePageURL());

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();

        elementIsViewed(logoutButton);
        elementIsViewed(addCarButton);

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
        String password = "$2a$12$4UZ6J30Pxj864vYraN/0debR.Ct9sXY.AZaFKt5QR8bd8pwSaoFMi";
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
        String password = "123456789";

        checkInvalidLoginData("s", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("a", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("w", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("o", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("3", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("4", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("0", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("1", password, errorValidationMassage, usernameErrorMassage);

        checkInvalidLoginData(generateRandomString(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(1), password, errorValidationMassage, usernameErrorMassage);
    }

    @Test
    void usernameMaxLengthValidation(){

        String errorValidationMassage = "Username must be less than 30.";
        String password = "123456789";

        checkInvalidLoginData("OCTrKKsPHWpAGfmhfKEWBstJUExdBSyd", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("pighhurgjioueirh387ru3ifrkjhguhgjh", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("iugh487gy3bugyiguyg98e4rgo87y9yg76rf", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("f4erfreeeeeeeeeeeeeeeeeefrhtyhtvergh56j7654t423t65756uhtrg", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("ELznpqwdxofSnYyOdqQmDUaIXikeXSE_ATwRLQZIjWyDmKvkmvYFFaqaGkAFHeHoSodmQpVXGRryzpSjNjApvkxjbHiClHDvbOYdoVGbbbMrgOKLtmegKPUm", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("wZcAR_gdlbYoXEGGcqGACHriRlrZCg_ndjqwY_GhfnGWojZbCwrVuvhSP", password, errorValidationMassage, usernameErrorMassage);

        checkInvalidLoginData(generateRandomString(32), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(55), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(76), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(67), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(233), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomString(354), password, errorValidationMassage, usernameErrorMassage);
    }

    @Test
    void usernamePatternValidation(){

        String errorValidationMassage = "Username must not contain anything other than digits," +
                " letters and underlining.";
        String password = "123456789";

        checkInvalidLoginData("$oGb*XOh@j&P#", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("k*$^#G)r*g&lJ#cIqkmQs**nLD$&XWW&fy$y%*oSWd%$", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("hV@#aa^$Wdz$Fiis$$woBQ@^A)$VUNkDx(A(AM^sGSvBkvv((gGGqtc(En&xdyfKWV$lVrFXnpwXb#E^#lkb#BI&q%L^*UY#**N^IO^NIxMk_r%%Jw$ZsgrpK)VmHXxYM@%wGP&lll(h(y)cXE&pGtbI@O@%JZnT*kyPU@&^LCgH&KVWrsmmo&I#&p&haPhXSrm)qvj*y$cOVZ%Op^jMQjo)n#%Z(s*dQBhR@A@*Pcm@xXRHTyhFnbYg*$$&Sewi#w^qxzt%QGLEeMCL(dfbS*hQIiiWDGj*ZZ*ZSFt%&E&U*E*o@@#$(%*oJCsu#QFl&u*f%%Ejx#k^CUo#k#^D*^EnP#eD@r$GujuVpMmHJ%awiV)VXPLWfEKOUjUMNDfUAgNrKi", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("wHPF^PFIz@m%^rut%@^N^Db%t&QsZrGLpYiBKX%OIU&^_&k^&jjXt*AaE^#$k^LKZOo%A*f$(Hrk%", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData("w_kCC%w*cwI(##o*Kz$krTO%ujd@L", password, errorValidationMassage, usernameErrorMassage);

        checkInvalidLoginData(generateRandomStringWithSpecialChars(7), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomStringWithSpecialChars(5), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomStringWithSpecialChars(30), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomStringWithSpecialChars(22), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomStringWithSpecialChars(21), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(generateRandomStringWithSpecialChars(211), password, errorValidationMassage, usernameErrorMassage);
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
        String username = "User_123";

        checkInvalidLoginData(username, "tIGSK", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "e", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "wwf", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "lkyt", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "ergrg", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "mq", errorValidationMassage, passwordErrorMassage);

        checkInvalidLoginData(username, generateRandomString(1), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(2), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(4), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(5), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(6), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(7), errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void passwordMaxLengthValidation(){

        String errorValidationMassage = "Password must be less than 100.";
        String username = "User_123";

        checkInvalidLoginData(username, "dGoyXDcMGngpzBTVODtsxJTsEfkxOMYCqxOBzFPFUcXACQesKfaVxUvsIJqshJHpnhxtzfFQBZUmmaSneDpXHNAsZOKoRCyVFdtCdUlFvGMTxWlrCWuQnOucdT", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "NFAHbsuGdrHUMzFEGgYgDeXevcVTehXCdUUWXaUgZnVfOYKEOlhJbYooJnvbgASqwFRTWGYrQaKaKTjbaptnGaPXtmHxkgzwCAqwqLcAZPRaOOBqSPtZbNIpesuzIWpfRxPJnNGCUsoPVkuVbLNLAWNqJdSRxrcbEuvXXKRsSRtlNSWpHEuhUDHSsvDYeapjhcakkCkueMRnThZMUNrphxnuuWgKoLzjdmWkuWvbxQMktAtDvVrvOYeIyEuHZOqTRRAddpfkkFTgPViDzmfxSowOMopXUqxZMNzSfnCnTSddsAmTzrwvoZlVHpkoAooMTnZIcOYFvxnYASDXaeXWuZEPherAescXsmtZhOqFltALIgEbNPGBEPjYolUfkAMwFTVhdLnvFYlczUDJohVOBRaOdbTEkZrdzViwHFXouKhwjTguaxInWEEtgDCOjAxALVMBTiAidpylzrqMFecyaHJBTaaImWtmrwbBHFbuPTbLqVaoheondZfTczfLsKUnZzaSAxMjPaKvsFeQLWNsLZvrsnFtygpBTgJICRGoihP", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "XHvdTYmSpobSkMSYgLWKItBIMDorjCCXnIrbLtvWcFNJxICKLzjGhoDXpWAYKaWwgVGrxKFnWJYzPcvnkWUwaPVhLOAQVufcNWlzHhXKgXHNUHGDsZiiUPjcsteaxWPArRJZSTPggAcIxxnurFUytUnOkzCXNyIYaUeNACwbdJnrP", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "aBIWWHWlOHSGmuDocccfuYUZnmIJxBAvXXDuIeTVOelmTrfuszYhnoQNhcvszqFyqTibjKvArdgWQewsxjfhjDMmliUxbvcoAlsTlrYrfSMVVVwROMFCWnVemBTxhcWGVtluOGLZpzpYBBEVHdYWkjShDZoUiCtVYkGcgJxUvfyjUznsKkkKrAXGpKBgVMVzZdBzcYTUiXONAuJULrqMZExRdpuxzVHPPdnQzfuDAaxGrNfsFGqZfhxNzXQWEgukEtZAWEmXKWIgHJJLQclIVoSxOIxvNeGMrTEccKCzkcfNjbgSsxbyguCrThSULePKwwVmUjjGYoqfgZUlRmRsOeGtLiXHuOQwYNZSwZCZIBykTBtzXYtslPULDSvrfppXbVoZsRspmbMlwyvbkKNAlLJnIKhbQJSdiQSHFgfwJwsIvXjlYERCjkmztkyrFVmDRROiwAjwKJRCitzmOVaCviKynjyqoIYAkciYFcwaycDCcWXVpKWhzxZkcoJeKRBiaRtbIbQtSqVCAVNowemYLmBrjZnbECIOuzztlYneMqxiHADwSSqEatogrxLdGzMKOPjSPZcDGqONgSlyrTINXAmshmMuQzcAiTNGMbTAnYzDEcISDvwESDUPTRlfYAAzWsGQLhUhqYyyAFsecgQJukRYcNTSngwqzeXogOfpVOAjbHYrfZFhHAbMdjVWSYcrbrKUKVhmyUwFtMckmkKSUYRftzogHSoIvlCjqJdZPIycULhlwdVcXSoWufSylvdxmLLzSNDqmROhCbKufUBPNwWtvmWKVeWQwBlPyeluIrZWucFtaRAObHYQgzFPcRnJEGwQxEcFasyZAdlLNgofGPTGkDMvnchXCbHitOENrOzgYkZlMMessnNJxINOeBuipigbRHFFfxaxsoOOFZAjWhaFilZfHWrkZhJxOSLXZsgdmpBkUwMMQboavWmWOsIovhNGOYlCQsLeyapsVFHEeRcKcdH", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "GYFrZwMQXlUPqwAIKOxWsPWwBDwgtciPbgdJLhqIVEobLjOgQyupMtIasEnqvDfoqClvXDjBtyJrwiePTdIGeIhudoHpGLvnAabNugFNLZlgTfzYdLhKxABryiVMtlVvOGoRkYjDjukOeHaAxJsMjTAGiDGweiMxSEAeYwWNrsLrsDeYFMOhkiSFQONCdDIsYiKtruzLCSiDCINuCEGrkHMbMcgkWQEOcDCYuOTfCbfAqXcjbrKijYXMIvIvgwTSAicDgUJciszjFCugDiTkHdCeVUphRcZDucrAmNhfldscfUwoUDifLJXHGLPcspJpnDCyutbtVsHA", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "TILzamaSTooLmfpjWnAVreIuAEfnYElsHKvnsPIjwsPGxGEKXRzFCTPIlSGpZYvwFnfCdPmosplKYDSIkQRgvMkwHhBpmdpFCqRouUzxYEPtduvHhastJCQDNoIzwcoHuOJxlBxeCycASisffzblHt", errorValidationMassage, passwordErrorMassage);

        checkInvalidLoginData(username, generateRandomString(102), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(582), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(344), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(754), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(334), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomString(152), errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void passwordPatternValidation() {

        String errorValidationMassage =  "Password must not contain anything other than digits," +
                " letters and underlining.";
        String username = "User_123";

        checkInvalidLoginData(username, "iML#}fKi*sgOh*q#viZdwI*I^#kvxLnDIaourYiRq", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "X!EgRoo|T(Twvw|FBykh@", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "gTLtzA%jbdtbzyX}mHiEzwGIPjv(u&*FA^ZOdntnPCb^(gFfBgif!TrNtEhRRVxKgzG|R%Pd#|prbfzcAXFfNF@j&vb", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "qEqJYsfOb%%gmMPcTs!(kPuYOwuDRV(iQQdyeTH}keVak{Y%MhEf^oS{M!%Cft&OP*y", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "R%SPMA*Ek%TpcvxY|yiUnG({!AfBmr@^%eth}", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, "AdnCxN@uc|L^LsPjyttiEa%PPpJFIC*^m@xvBFanVXr^fgDc*NNMiKkcH|LTzx!HvO", errorValidationMassage, passwordErrorMassage);

        checkInvalidLoginData(username, generateRandomStringWithSpecialChars(23), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomStringWithSpecialChars(31), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomStringWithSpecialChars(10), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomStringWithSpecialChars(91), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomStringWithSpecialChars(84), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, generateRandomStringWithSpecialChars(54), errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void checkIncorrectLoginData() {

        checkIncorrectLoginData("tormentedtotal", "password");
        checkIncorrectLoginData("cubfeminine", "123456789");
        checkIncorrectLoginData("wefferbv", "111111111");
        checkIncorrectLoginData("thrd2533", "1234567890");
        checkIncorrectLoginData("user_1", "D1lakisss");
        checkIncorrectLoginData("admin", "5y4yeg354hge");
        checkIncorrectLoginData("brtgg333", "ret43tge");
        checkIncorrectLoginData("bfb43", "h5ge45hgevdv4");
        checkIncorrectLoginData("gtg4r", "3456423256");
        checkIncorrectLoginData("bgbgbr44", "464hgt5y455");

        checkIncorrectLoginData(generateRandomString(2), generateRandomString(8));
        checkIncorrectLoginData(generateRandomString(4), generateRandomString(19));
        checkIncorrectLoginData(generateRandomString(29), generateRandomString(43));
        checkIncorrectLoginData(generateRandomString(10), generateRandomString(23));
        checkIncorrectLoginData(generateRandomString(30), generateRandomString(100));
    }

    @Test
    void checkVariationUsername() {

        String password = getPassword();

        checkIncorrectLoginData("admin_1", password);
        checkIncorrectLoginData("ADMIN_1", password);
        checkIncorrectLoginData("_1admin", password);
        checkIncorrectLoginData("amin_1d", password);
        checkIncorrectLoginData("AdMiN_1", password);
        checkIncorrectLoginData("Admin1", password);
        checkIncorrectLoginData("Amin_1", password);
        checkIncorrectLoginData("dmin_1", password);
        checkIncorrectLoginData("Adin_1", password);
        checkIncorrectLoginData("Admn_1", password);
        checkIncorrectLoginData("Admi_1", password);
        checkIncorrectLoginData("Admin1", password);
        checkIncorrectLoginData("Admin_", password);
        checkIncorrectLoginData("aDmin_1", password);
        checkIncorrectLoginData("AdMin_1", password);
        checkIncorrectLoginData("AdmIn_1", password);
        checkIncorrectLoginData("AdmiN_1", password);
        checkIncorrectLoginData("Admin11", password);
    }

    @Test
    void checkVariationPassword() {

        String username = getUsername();
        checkIncorrectLoginData(username, "dmin_pass");
        checkIncorrectLoginData(username, "Amin_pass");
        checkIncorrectLoginData(username, "Adin_pass");
        checkIncorrectLoginData(username, "Admn_pass");
        checkIncorrectLoginData(username, "Admi_pass");
        checkIncorrectLoginData(username, "Adminpass");
        checkIncorrectLoginData(username, "Admin_ass");
        checkIncorrectLoginData(username, "Admin_pss");
        checkIncorrectLoginData(username, "Admin_pas");
        checkIncorrectLoginData(username, "admin_pass");
        checkIncorrectLoginData(username, "ADMIN_PASS");
        checkIncorrectLoginData(username, "ADmin_pass");
        checkIncorrectLoginData(username, "AdMin_pass");
        checkIncorrectLoginData(username, "AdmIn_pass");
        checkIncorrectLoginData(username, "AdmiN_pass");
        checkIncorrectLoginData(username, "Admin_Pass");
        checkIncorrectLoginData(username, "Admin_pAss");
        checkIncorrectLoginData(username, "Admin_paSs");
        checkIncorrectLoginData(username, "Admin_pAsS");
        checkIncorrectLoginData(username, "_passAdmin");
        checkIncorrectLoginData(username, "Aminpass_");
        checkIncorrectLoginData(username, "passAmin_");

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
        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();
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

    private static void elementIsViewed(WebElement element){

        pageNavigation.moveToElement(element);
        assertTrue(element.isDisplayed() && element.isEnabled());
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

        threadSleep1Seconds();
        sendLoginDataButton.click();

    }
}
