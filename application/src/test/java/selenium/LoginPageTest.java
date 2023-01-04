package selenium;

import com.utils.selenium.PageNavigation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static com.utils.selenium.ElementsUtils.*;
import static com.utils.selenium.PageNavigation.threadSleep1Seconds;
import static com.utils.selenium.SeleniumTestsUtils.checkErrorValidationMassage;
import static com.utils.selenium.URLUtils.getHomePageURL;
import static com.utils.selenium.URLUtils.getLoginPageURL;
import static com.utils.spring.AuthTestUtils.*;
import static com.utils.spring.StringUtils.generateRandomStringWithSpecialChars;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Slf4j
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
public class LoginPageTest {

    private static String loginPageURL;

    private static WebDriver driver;

    private static WebDriverWait wait;

    private static PageNavigation pageNavigation;

    private static DataFactory dataFactory;

    @FindBy(id = "addCarButton")
    private static WebElement addCarButton;

    @FindBy(id = "come-back-button")
    private static WebElement comeBackButton;

    @FindBy(id = "sendLoginDataButton")
    private static WebElement sendLoginDataButton;

    @FindBy(id = "usernameInputField")
    private static WebElement usernameInput;

    @FindBy(id = "usernameErrorMassage")
    private static WebElement usernameErrorMassage;

    @FindBy(id = "passwordInputField")
    private static WebElement passwordInput;

    @FindBy(id = "passwordErrorMassage")
    private static WebElement passwordErrorMassage;

    @FindBy(id = "loginButton")
    private static WebElement loginButton;

    @FindBy(id = "modal-header")
    private static WebElement errorUserInfoModalHeader;

    @FindBy(id = "modal-body")
    private static WebElement errorUserInfoModalBody;

    @FindBy(id = "okButton")
    private static WebElement errorUserInfoModalOkButton;

    @FindBy(id = "modal-error-title")
    private static WebElement modalErrorTitle;

    @FindBy(id = "modal-error-body")
    private static WebElement modalErrorBody;

    @FindBy(id = "errorOkButton")
    private static WebElement modalErrorOkButton;

    @FindBy(id = "logout-button")
    private static WebElement logoutButton;

    @FindBy(id = "login-form")
    private static WebElement loginForm;

    public LoginPageTest() {
        PageFactory.initElements(driver, this);
    }

    @BeforeAll
    static void beforeAll() {

        openWindow();

        loginPageURL = getLoginPageURL();
        driver = getDriver();
        wait = getWait();
        dataFactory = getDataFactory();
        pageNavigation = getPageNavigation();
    }

    @BeforeEach
    void setUp()  {

        driver.get(loginPageURL);
    }

    @AfterAll
    static void afterAll() {
        closeWindow();
    }

    @Test
    void checkComeBackButton() {

        checkLoginForm();

        driver.get(getHomePageURL());

        pageNavigation.clickOnElement(loginButton);
        checkLoginForm();

        pageNavigation.clickOnElement(comeBackButton);

        assertEquals(getHomePageURL(), driver.getCurrentUrl());
    }

    @Test
    @SneakyThrows
    void checkLoginSeveralTabs() {

        String parent = driver.getWindowHandle();
        Actions actions = new Actions(driver);

        loginAdmin();

        actions.keyDown(CONTROL);
        actions.sendKeys("t");
        actions.keyUp(CONTROL);
        actions.build().perform();

        driver.get(getHomePageURL());

        Set<String> browsers = driver.getWindowHandles();
        for (String i : browsers) {
            if (!i.equals(parent)) {
                driver.switchTo().window(i);
                driver.get(getHomePageURL());
            }
        }

        elementIsViewed("logout-button");
        elementIsViewed("addCarButton");
        logout();

        actions.keyDown(CONTROL);
        actions.sendKeys("w");
        actions.keyUp(CONTROL);
        actions.build().perform();

        for (String i : browsers) {
            if (i.equals(parent)) {
                driver.switchTo().window(i);
            }
        }

        elementIsViewed("loginButton");
        elementIsNotViewed("user-icon");
        elementIsNotViewed("addCarButton");
    }

    @Test
    void trySendEmptyFields() {

        sendLoginDataButton.click();
        checkAndCloseInvalidLoginDataModalWindow();

        String requiredUsernameErrorMassage = "Username is required.";
        checkErrorValidationMassage(requiredUsernameErrorMassage, usernameErrorMassage);

        String requiredPasswordErrorMassage = "Password is required.";
        checkErrorValidationMassage(requiredPasswordErrorMassage, passwordErrorMassage);
    }

    @Test
    void successfulAuthorization() {

        checkSuccessfulAuthorizationAdmin(getAdminUsername(), getAdminPassword());
        checkSuccessfulAuthorizationAdmin(getUserUsername(), getUserPassword());
    }

    @Test
    void loginDataFromDB(){

        checkIncorrectLoginData("Admin_1", "$2a$12$4UZ6J30Pxj864vYraN/0debR.Ct9sXY.AZaFKt5QR8bd8pwSaoFMi");
        checkIncorrectLoginData("User_1", "$2a$12$5IYQmiuyEw5wimD5Z0heyOq/MtjWAE98yAVdfHKOtyC4lbHCbbBtm");
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

        checkInvalidLoginData(dataFactory.getRandomText(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(dataFactory.getRandomText(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(dataFactory.getRandomText(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(dataFactory.getRandomText(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(dataFactory.getRandomText(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(dataFactory.getRandomText(1), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginData(dataFactory.getRandomText(1), password, errorValidationMassage, usernameErrorMassage);
    }

    @Test
    void usernameMaxLengthValidation(){

        String errorValidationMassage = "Username must be less than 30.";
        String password = "123456789";

        checkInvalidLoginDataMaxLength("OCTrKKsPHWpAGfmhfKEWBstJUExdBSyd", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength("pighhurgjioueirh387ru3ifrkjhguhgjh", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength("iugh487gy3bugyiguyg98e4rgo87y9yg76rf", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength("f4erfreeeeeeeeeeeeeeeeeefrhtyhtvergh56j7654t423t65756uhtrg", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength("ELznpqwdxofSnYyOdqQmDUaIXikeXSE_ATwRLQZIjWyDmKvkmvYFFaqaGkAFHeHoSodmQpVXGRryzpSjNjApvkxjbHiClHDvbOYdoVGbbbMrgOKLtmegKPUm", password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength("wZcAR_gdlbYoXEGGcqGACHriRlrZCg_ndjqwY_GhfnGWojZbCwrVuvhSP", password, errorValidationMassage, usernameErrorMassage);

        checkInvalidLoginDataMaxLength(dataFactory.getRandomText(32), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength(dataFactory.getRandomText(55), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength(dataFactory.getRandomText(76), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength(dataFactory.getRandomText(67), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength(dataFactory.getRandomText(233), password, errorValidationMassage, usernameErrorMassage);
        checkInvalidLoginDataMaxLength(dataFactory.getRandomText(354), password, errorValidationMassage, usernameErrorMassage);
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

        checkInvalidLoginData(username, dataFactory.getRandomText(1), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, dataFactory.getRandomText(2), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, dataFactory.getRandomText(3), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, dataFactory.getRandomText(4), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, dataFactory.getRandomText(5), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, dataFactory.getRandomText(6), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginData(username, dataFactory.getRandomText(7), errorValidationMassage, passwordErrorMassage);
    }

    @Test
    void passwordMaxLengthValidation(){

        String errorValidationMassage = "Password must be less than 100.";
        String username = "User_123";

        checkInvalidLoginDataMaxLength(username, "dGoyXDcMGngpzBTVODtsxJTsEfkxOMYCqxOBzFPFUcXACQesKfaVxUvsIJqshJHpnhxtzfFQBZUmmaSneDpXHNAsZOKoRCyVFdtCdUlFvGMTxWlrCWuQnOucdT", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, "NFAHbsuGdrHUMzFEGgYgDeXevcVTehXCdUUWXaUgZnVfOYKEOlhJbYooJnvbgASqwFRTWGYrQaKaKTjbaptnGaPXtmHxkgzwCAqwqLcAZPRaOOBqSPtZbNIpesuzIWpfRxPJnNGCUsoPVkuVbLNLAWNqJdSRxrcbEuvXXKRsSRtlNSWpHEuhUDHSsvDYeapjhcakkCkueMRnThZMUNrphxnuuWgKoLzjdmWkuWvbxQMktAtDvVrvOYeIyEuHZOqTRRAddpfkkFTgPViDzmfxSowOMopXUqxZMNzSfnCnTSddsAmTzrwvoZlVHpkoAooMTnZIcOYFvxnYASDXaeXWuZEPherAescXsmtZhOqFltALIgEbNPGBEPjYolUfkAMwFTVhdLnvFYlczUDJohVOBRaOdbTEkZrdzViwHFXouKhwjTguaxInWEEtgDCOjAxALVMBTiAidpylzrqMFecyaHJBTaaImWtmrwbBHFbuPTbLqVaoheondZfTczfLsKUnZzaSAxMjPaKvsFeQLWNsLZvrsnFtygpBTgJICRGoihP", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, "XHvdTYmSpobSkMSYgLWKItBIMDorjCCXnIrbLtvWcFNJxICKLzjGhoDXpWAYKaWwgVGrxKFnWJYzPcvnkWUwaPVhLOAQVufcNWlzHhXKgXHNUHGDsZiiUPjcsteaxWPArRJZSTPggAcIxxnurFUytUnOkzCXNyIYaUeNACwbdJnrP", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, "aBIWWHWlOHSGmuDocccfuYUZnmIJxBAvXXDuIeTVOelmTrfuszYhnoQNhcvszqFyqTibjKvArdgWQewsxjfhjDMmliUxbvcoAlsTlrYrfSMVVVwROMFCWnVemBTxhcWGVtluOGLZpzpYBBEVHdYWkjShDZoUiCtVYkGcgJxUvfyjUznsKkkKrAXGpKBgVMVzZdBzcYTUiXONAuJULrqMZExRdpuxzVHPPdnQzfuDAaxGrNfsFGqZfhxNzXQWEgukEtZAWEmXKWIgHJJLQclIVoSxOIxvNeGMrTEccKCzkcfNjbgSsxbyguCrThSULePKwwVmUjjGYoqfgZUlRmRsOeGtLiXHuOQwYNZSwZCZIBykTBtzXYtslPULDSvrfppXbVoZsRspmbMlwyvbkKNAlLJnIKhbQJSdiQSHFgfwJwsIvXjlYERCjkmztkyrFVmDRROiwAjwKJRCitzmOVaCviKynjyqoIYAkciYFcwaycDCcWXVpKWhzxZkcoJeKRBiaRtbIbQtSqVCAVNowemYLmBrjZnbECIOuzztlYneMqxiHADwSSqEatogrxLdGzMKOPjSPZcDGqONgSlyrTINXAmshmMuQzcAiTNGMbTAnYzDEcISDvwESDUPTRlfYAAzWsGQLhUhqYyyAFsecgQJukRYcNTSngwqzeXogOfpVOAjbHYrfZFhHAbMdjVWSYcrbrKUKVhmyUwFtMckmkKSUYRftzogHSoIvlCjqJdZPIycULhlwdVcXSoWufSylvdxmLLzSNDqmROhCbKufUBPNwWtvmWKVeWQwBlPyeluIrZWucFtaRAObHYQgzFPcRnJEGwQxEcFasyZAdlLNgofGPTGkDMvnchXCbHitOENrOzgYkZlMMessnNJxINOeBuipigbRHFFfxaxsoOOFZAjWhaFilZfHWrkZhJxOSLXZsgdmpBkUwMMQboavWmWOsIovhNGOYlCQsLeyapsVFHEeRcKcdH", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, "GYFrZwMQXlUPqwAIKOxWsPWwBDwgtciPbgdJLhqIVEobLjOgQyupMtIasEnqvDfoqClvXDjBtyJrwiePTdIGeIhudoHpGLvnAabNugFNLZlgTfzYdLhKxABryiVMtlVvOGoRkYjDjukOeHaAxJsMjTAGiDGweiMxSEAeYwWNrsLrsDeYFMOhkiSFQONCdDIsYiKtruzLCSiDCINuCEGrkHMbMcgkWQEOcDCYuOTfCbfAqXcjbrKijYXMIvIvgwTSAicDgUJciszjFCugDiTkHdCeVUphRcZDucrAmNhfldscfUwoUDifLJXHGLPcspJpnDCyutbtVsHA", errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, "TILzamaSTooLmfpjWnAVreIuAEfnYElsHKvnsPIjwsPGxGEKXRzFCTPIlSGpZYvwFnfCdPmosplKYDSIkQRgvMkwHhBpmdpFCqRouUzxYEPtduvHhastJCQDNoIzwcoHuOJxlBxeCycASisffzblHt", errorValidationMassage, passwordErrorMassage);

        checkInvalidLoginDataMaxLength(username, dataFactory.getRandomText(101), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, dataFactory.getRandomText(102), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, dataFactory.getRandomText(582), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, dataFactory.getRandomText(344), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, dataFactory.getRandomText(754), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, dataFactory.getRandomText(334), errorValidationMassage, passwordErrorMassage);
        checkInvalidLoginDataMaxLength(username, dataFactory.getRandomText(152), errorValidationMassage, passwordErrorMassage);
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

        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));

        checkIncorrectLoginData(dataFactory.getRandomText(2), dataFactory.getRandomText(8));
        checkIncorrectLoginData(dataFactory.getRandomText(4), dataFactory.getRandomText(19));
        checkIncorrectLoginData(dataFactory.getRandomText(29), dataFactory.getRandomText(43));
        checkIncorrectLoginData(dataFactory.getRandomText(10), dataFactory.getRandomText(23));
        checkIncorrectLoginData(dataFactory.getRandomText(30), dataFactory.getRandomText(100));
    }

    @Test
    void checkVariationUsername() {

        String password = getAdminPassword();

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

        String username = getAdminUsername();
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

    @Test
    void checkUsersVariationData() {

        checkIncorrectLoginData(getAdminUsername(), getUserPassword());
        checkIncorrectLoginData(getUserUsername(), getAdminPassword());
    }

    private static void checkSuccessfulAuthorizationAdmin(String username, String password){

        driver.get(getLoginPageURL());
        wait.until(visibilityOf(loginForm));
        sendLoginData(username, password);

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();

        driver.get(getHomePageURL());

        wait.until(visibilityOf(findWebElementById("logo-header")));

        elementIsViewed(logoutButton);
        elementIsViewed(addCarButton);
        elementIsViewed("user-icon");

        logoutButton.click();

        assertEquals(getHomePageURL(), driver.getCurrentUrl());
        elementIsViewed(loginButton);
    }

    private static void checkInvalidLoginData(String username, String password, String exceptedErrorMassage,
                                              WebElement errorMassageAlert){
        sendPutTextKeyInInputField(usernameInput, username);
        sendPutTextKeyInInputField(passwordInput,  password);

        assertEquals(exceptedErrorMassage,errorMassageAlert.getAttribute("innerText"));

        pageNavigation.clickOnElement(sendLoginDataButton);
        checkAndCloseInvalidLoginDataModalWindow();

        sendPutTextKeyInInputField(usernameInput, "");
        sendPutTextKeyInInputField(usernameInput, "");
    }

    private static void checkInvalidLoginDataMaxLength(String username, String password, String exceptedErrorMassage,
                                                       WebElement errorMassageAlert){
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);

        assertEquals(exceptedErrorMassage,errorMassageAlert.getAttribute("innerText"));

        pageNavigation.clickOnElement(sendLoginDataButton);
        checkAndCloseInvalidLoginDataModalWindow();

        sendPutTextKeyInInputField(usernameInput, "");
        sendPutTextKeyInInputField(usernameInput, "");
    }

    private static void checkIncorrectLoginData(String username, String password){

        sendLoginData(username, password);
        checkAndCloseIncorrectLoginDataModalWindow();

        assertEquals(getLoginPageURL(), driver.getCurrentUrl());
    }

    private static void checkAndCloseInvalidLoginDataModalWindow() {

        String errorUserInfoModalHeaderExceptedText =
                "Incorrect user info";
        String errorUserInfoModalBodyExceptedText =
                "You entered incorrect username or password. Please, check your entered date and try again.";

        wait.until(visibilityOfElementLocated(id("modal-header")));

        String actualHeaderText = errorUserInfoModalHeader.getAttribute("innerText");
        assertEquals(errorUserInfoModalHeaderExceptedText, actualHeaderText);

        String actualBodyText = errorUserInfoModalBody.getAttribute("innerText");
        assertEquals(errorUserInfoModalBodyExceptedText, actualBodyText);

        errorUserInfoModalOkButton.click();
    }

    private static void checkAndCloseIncorrectLoginDataModalWindow() {

        wait.until(visibilityOfElementLocated(id("modal-error-body")));

        String actualModalErrorTitleText = modalErrorTitle.getAttribute("innerText");
        String exceptedModalErrorTitleText = "Error";
        assertEquals(exceptedModalErrorTitleText, actualModalErrorTitleText);

        String actualModalErrorBodyText = modalErrorBody.getAttribute("innerText");
        String exceptedModalErrorBodyText = "Data for authentication is invalid. Login failed.";
        assertEquals(exceptedModalErrorBodyText, actualModalErrorBodyText);

        modalErrorOkButton.click();
    }

    private static void sendLoginData(String username, String password){

        sendPutTextKeyInInputField(usernameInput, username);
        sendPutTextKeyInInputField(passwordInput, password);
        pageNavigation.clickOnElement(sendLoginDataButton);
    }

    private static void checkLoginForm() {

        wait.until(visibilityOf(loginForm));
        elementIsViewed(loginForm);
        elementIsViewed(comeBackButton);
        elementIsViewed(sendLoginDataButton);
        elementIsViewed(usernameInput);
        elementIsViewed(passwordInput);
    }
}
