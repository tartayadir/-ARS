package selenium.withAuthorization;

import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.selenium.PageNavigation;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.Keys.chord;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static utils.selenium.PageNavigation.threadSleep1Seconds;
import static utils.selenium.URLUtils.*;
import static utils.spring.AuthTestUtils.getPassword;
import static utils.spring.AuthTestUtils.getUsername;
import static utils.spring.StringUtils.toTitleCase;

@Slf4j
public class CarDetailsPage {

    private static File image;

    private static WebDriverWait wait;

    private static PageNavigation pageNavigation;

    private static String homePageURL;

    private static WebDriver driver;

    private static String detailsUrl;

    private static String detailsPrefixUrl;

    @FindBy(xpath = "//*[@id=\"loginButton\"]")
    private static WebElement loginButton;

    @FindBy(xpath = "//*[@id=\"logout-button\"]")
    private static WebElement logoutButton;

    @FindBy(xpath = "//*[@id=\"come-back-button\"]")
    private static WebElement comeBackButton;

    @FindBy(xpath = "//*[@id=\"addCarButton\"]")
    private static WebElement addCarButton;

    @FindBy(xpath = "//*[@id=\"delete-car-GENESIS-Model 1-button\"]")
    private static WebElement deleteCarButton;

    @FindBy(xpath = "//*[@id=\"car-GENESIS-Model 1-edit-button\"]")
    private static WebElement carEditButton;

    @FindBy(xpath = "//*[@id=\"brand-input\"]")
    private static WebElement brandInput;

    @FindBy(xpath = "//*[@id=\"model-input\"]")
    private static WebElement modelInput;

    @FindBy(xpath = "//*[@id=\"confirm-delete-button\"]")
    private static WebElement confirmDeleteCarButton;

    @FindBy(xpath = "//*[@id=\"close-modal-window-button\"]")
    private static WebElement closeModalWindowButton;

    @FindBy(xpath = "//*[@id=\"year-input\"]")
    private static WebElement yearInput;

    @FindBy(xpath = "//*[@id=\"engine-capacity-input\"]")
    private static WebElement engineCapacityInput;

    @FindBy(xpath = "//*[@id=\"car-body-type-input\"]")
    private static WebElement carBodyTypeInput;

    @FindBy(xpath = "//*[@id=\"transmission-box-input\"]")
    private static WebElement transmissionBoxTypeInput;

    @FindBy(xpath = "//*[@id=\"short-description-input\"]")
    private static WebElement shortDescriptionInput;

    @FindBy(xpath = "//*[@id=\"full-description-input\"]")
    private static WebElement fullDescriptionInput;

    @FindBy(xpath = "//*[@id=\"change-image-button\"]")
    private static WebElement uploadImageButton;

    @FindBy(xpath = "//*[@id=\"image-input\"]")
    private static WebElement imageInput;

    @FindBy(xpath = "//*[@id=\"apply-image-upload-button\"]")
    private static WebElement applyImageUploadButton;

    @FindBy(xpath = "//*[@id=\"submit-car-button\"]")
    private static WebElement submitCarButton;

    private static Car car;

    private static Long carId;

    @BeforeAll
    static void beforeAll() {

        image =
                new File("src/test/resources/images/modern-blue-urban-adventure-suv-vehicle-illustration_1344-205.png");

        short year = 2013;
        car = Car.builder().
                id(10L).
                brand(CarBrands.GENESIS).
                model("Model 1").
                carBodyTypes(CarBodyTypes.PICKUP).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.AUTOMATIC).
                engineCapacity(5.4).
                shortDescription("Short description 1 Short description 1 Short description 1Short dddddd " +
                        "Short description 1 Short description 1 Short description 1Short description fds " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        "Short description 1 Short description 1Short description 1 Short description 1 S "
                ).
                fullDescription( "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1"
                ).
                additionalOptions(List.of("Option 1")).
                imageFileName("Image file 1").
                build();

        homePageURL = getHomePageURL();
        Duration duration_3_second = Duration.ofSeconds(3);

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(duration_3_second);

        pageNavigation = new PageNavigation(driver);
        wait = new WebDriverWait(driver, duration_3_second);

        login();
        addCar();

        pageNavigation.clickOnElement(driver.
                findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]", car.getBrand(), car.getModel()))));

        detailsPrefixUrl = "http://localhost:4200/car-catalog/details/";
        detailsUrl = driver.getCurrentUrl();
        carId = Long.valueOf(driver.getCurrentUrl().substring(detailsPrefixUrl.length()));
    }

    @BeforeEach
    void setUp()  {

        PageFactory.initElements(driver, this);
        driver.get(detailsUrl);
    }

    @AfterAll
    static void afterAll() {

        deleteCar();
        logout();
        driver.close();
        driver.quit();
    }

    @Test
    void authorizationIsSuccessful() {

        elementIsViewed(logoutButton);
        elementIsViewed(addCarButton);

        pageNavigation.scrollDown();

        elementIsViewed(driver.findElement(xpath(
                format("//*[@id=\"car-%s-%s-edit-button\"]", car.getBrand(), car.getModel()))));
        elementIsViewed(driver.findElement(xpath(
                format("//*[@id=\"delete-car-%s-%s-button\"]", car.getBrand(), car.getModel()))));
    }

    @Test
    void checkDeleteCarButton() {

        pageNavigation.clickOnElement(deleteCarButton);
        pageNavigation.clickOnElement(closeModalWindowButton);
        pageNavigation.clickOnElement(deleteCarButton);
        pageNavigation.clickOnElement(confirmDeleteCarButton);

        pageNavigation.scrollDown();
        assertThatThrownBy( () -> wait.until(elementToBeClickable(driver.
                findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]", car.getBrand(), car.getModel()))))).click()).
                isInstanceOf(NoSuchElementException.class);

        addCar();
        carId = carId + 1L;
        detailsUrl = detailsPrefixUrl + carId;
    }

    @Test
    void checkEditCarButton() {

        pageNavigation.clickOnElement(carEditButton);

        elementIsViewed(uploadImageButton);
        elementIsViewed(submitCarButton);

        checkTextInInputSelect(toTitleCase(car.getBrand().getStringValue()), brandInput);
        checkTextInInputField(car.getModel(), modelInput);
        checkTextInInputField(Short.toString(car.getYear()), yearInput);
        checkTextInInputField(Double.toString(car.getEngineCapacity()), engineCapacityInput);
        checkTextInInputSelect(toTitleCase(car.getCarBodyTypes().getStringValue()), carBodyTypeInput);

        WebElement radioTransmissionType =
                driver.findElement(xpath(format("//*[@id=\"%s\"]", car.getTransmissionBoxTypes().getStringValue())));
        pageNavigation.moveToElement(radioTransmissionType);
        assertTrue(radioTransmissionType.isSelected());

        checkTextInInputField(car.getShortDescription(), shortDescriptionInput);
        checkTextInInputField(car.getFullDescription(), fullDescriptionInput);

        pageNavigation.clickOnElement(comeBackButton);
    }

    private static void checkTextInInputField(String exceptedText, WebElement inputFiled) {

        pageNavigation.moveToElement(inputFiled);
        assertEquals(exceptedText, inputFiled.getAttribute("value"));
    }

    private static void checkTextInInputSelect(String exceptedText, WebElement inputSelect) {

        pageNavigation.moveToElement(inputSelect);
        Select carBodyTypeSelect = new Select(inputSelect);
        String actualSelectInputText = carBodyTypeSelect.getFirstSelectedOption().getAttribute("innerText");

        assertEquals(exceptedText, actualSelectInputText);
    }

    private static void elementIsViewed(WebElement element){

        pageNavigation.moveToElement(element);
        assertTrue(element.isDisplayed() && element.isEnabled());
    }

    private static void deleteCar(){

        driver.get(homePageURL);
        pageNavigation.scrollDown();

        threadSleep1Seconds();

        pageNavigation.hoverOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                car.getBrand(), car.getModel()))));
        pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"delete-car-%s-%s-button\"]",
                car.getBrand(), car.getModel()))));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"confirm-delete-button\"]"))).click();
    }
    private static void addCar(){

        driver.get(homePageURL);
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"addCarButton\"]"))).click();

        Select brandSelect = new Select(driver.findElement(xpath("//*[@id=\"brand-input\"]")));
        brandSelect.selectByValue("GENESIS");

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"model-input\"]"))).sendKeys(car.getModel());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"year-input\"]"))).clear();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"year-input\"]"))).
                sendKeys(Short.toString(car.getYear()));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"engine-capacity-input\"]"))).clear();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"engine-capacity-input\"]"))).
                sendKeys(Double.toString(car.getEngineCapacity()));

        Select carBodyTypeSelect = new Select(driver.findElement(xpath("//*[@id=\"car-body-type-input\"]")));
        carBodyTypeSelect.selectByValue("PICKUP");

        pageNavigation.clickOnElement(
                driver.findElement(xpath(format("//*[@id=\"%s\"]", car.getTransmissionBoxTypes().getStringValue()))));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"short-description-input\"]"))).
                sendKeys(car.getShortDescription());

        pageNavigation.scrollDown();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"full-description-input\"]")));
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"full-description-input\"]"))).
                sendKeys(car.getFullDescription());

        WebElement addOptionsInput = driver.findElement(xpath("//*[@id=\"additional-options-input\"]"));
        car.getAdditionalOptions().forEach(op -> {
            wait.until(visibilityOf(addOptionsInput)).sendKeys(op);
            wait.until(visibilityOf(addOptionsInput)).sendKeys(chord(ENTER));
        });


        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"upload-image-button\"]")));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"image-input\"]"))).
                sendKeys(image.getAbsolutePath());
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"apply-image-upload-button\"]"))).click();

        pageNavigation.scrollDown();
        wait.until(visibilityOf(driver.findElement(xpath("//*[@id=\"submit-car-button\"]"))));
        wait.until(elementToBeClickable(driver.findElement(xpath("//*[@id=\"submit-car-button\"]")))).
                click();

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();
        pageNavigation.scrollDown();
    }
    private static void login() {

        driver.get(homePageURL);

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"loginButton\"]"))).click();

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"usernameInputField\"]"))).click();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"usernameInputField\"]"))).sendKeys(getUsername());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"passwordInputField\"]"))).click();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"passwordInputField\"]"))).sendKeys(getPassword());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"sendLoginDataButton\"]"))).click();

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"logout-button\"]")));
    }

    private static void logout() {

        driver.get(homePageURL);
        pageNavigation.scrollUp();
        wait.until(elementToBeClickable(xpath("//*[@id=\"logout-button\"]"))).click();
    }

}
