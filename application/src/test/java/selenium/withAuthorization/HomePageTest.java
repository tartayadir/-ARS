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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.Keys.*;
import static utils.selenium.PageNavigation.threadSleep1Seconds;
import static utils.selenium.URLUtils.*;
import static utils.spring.AuthTestUtils.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static utils.spring.StringUtils.toTitleCase;

@Slf4j
public class HomePageTest {

    private static File image;

    private static WebDriverWait wait;

    private static PageNavigation pageNavigation;

    private static String homePageURL;

    private static WebDriver driver;

    @FindBy(xpath = "//*[@id=\"brand-input\"]")
    private static WebElement brandInput;

    @FindBy(xpath = "//*[@id=\"model-input\"]")
    private static WebElement modelInput;

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

    @FindBy(xpath = "//*[@id=\"upload-image-button\"]")
    private static WebElement uploadImageButton;

    @FindBy(xpath = "//*[@id=\"change-image-button\"]")
    private static WebElement changeImageButton;

    @FindBy(xpath = "//*[@id=\"image-input\"]")
    private static WebElement imageInput;

    @FindBy(xpath = "//*[@id=\"apply-image-upload-button\"]")
    private static WebElement applyImageUploadButton;

    @FindBy(xpath = "//*[@id=\"submit-car-button\"]")
    private static WebElement submitCarButton;

    @FindBy(xpath = "//*[@id=\"car-brand-model\"]")
    private static WebElement detailsBrandModel;

    @FindBy(xpath = "//*[@id=\"car-body-type\"]")
    private static WebElement detailsBodyType;

    @FindBy(xpath = "//*[@id=\"car-transmission-type\"]")
    private static WebElement detailsTransmissionType;

    @FindBy(xpath = "//*[@id=\"car-engine-capacity\"]")
    private static WebElement detailsEngineCapacity;

    @FindBy(xpath = "//*[@id=\"car-produce-year\"]")
    private static WebElement detailsProduceYear;

    @FindBy(xpath = "//*[@id=\"car-full-description\"]")
    private static WebElement detailsFullDescription;

    @FindBy(xpath = "//*[@id=\"come-back-button\"]")
    private static WebElement comeBackButton;

    @FindBy(xpath = "//*[@id=\"logo-header\"]")
    private static WebElement logo;

    @FindBy(xpath = "//*[@id=\"addCarButton\"]")
    private static WebElement addCarButton;

    @FindBy(xpath = "//*[@id=\"delete-car-GENESIS-Model 1-button\"]")
    private static WebElement deleteCarButton;

    @FindBy(xpath = "//*[@id=\"car-image-GENESIS-Model 1\"]")
    private static WebElement carImage;

    @FindBy(xpath = "//*[@id=\"car-GENESIS-Model 1-edit-button\"]")
    private static WebElement carEditButton;

    @FindBy(xpath = "//*[@id=\"loginButton\"]")
    private static WebElement loginButton;

    @FindBy(xpath = "//*[@id=\"logout-button\"]")
    private static WebElement logoutButton;

    private static Car car;

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
                shortDescription("Sh ort description 1 Short description 1 Short description 1Short dddddd " +
                        "Short description 1 Short description 1 Short description 1Short description fds" +
                        " 1Short description 1 Short description 1 Short description 1Short description 1" +
                        " 1Short description 1 Short description 1 Short description 1Short description 1" +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        "Short description 1 Short description 1Short description 1 Short description 1 S"
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

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        pageNavigation = new PageNavigation(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        login();
        addCar();
    }

    @BeforeEach
    void setUp()  {

        PageFactory.initElements(driver, this);
        driver.get(homePageURL);
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

        pageNavigation.hoverOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                car.getBrand(), car.getModel()))));

        elementIsViewed(driver.findElement(xpath(
                format("//*[@id=\"car-%s-%s-edit-button\"]", car.getBrand(), car.getModel()))));
        elementIsViewed(driver.findElement(xpath(
                format("//*[@id=\"delete-car-%s-%s-button\"]", car.getBrand(), car.getModel()))));
    }
    
    @Test
    void goToAddCarPage(){

        addCarButton.click();
        assertEquals(getAddCarPageURL(), driver.getCurrentUrl());
        assertThatThrownBy( () -> addCarButton.click()).
                isInstanceOf(NoSuchElementException.class);

        elementIsViewed(driver.findElement(xpath("//*[@id=\"brand-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"model-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"year-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"engine-capacity-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"car-body-type-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"transmission-box-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"short-description-input\"]")));
        pageNavigation.scrollDown();
        elementIsViewed(driver.findElement(xpath("//*[@id=\"full-description-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"additional-options-input\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"upload-image-button\"]")));
        elementIsViewed(driver.findElement(xpath("//*[@id=\"submit-car-button\"]")));
        elementIsViewed(comeBackButton);

        comeBackButton.click();

        assertEquals(getHomePageURL(), driver.getCurrentUrl());
    }

    @Test
    void goToEditCarPage() {

        pageNavigation.scrollDown();
        pageNavigation.hoverOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                car.getBrand(), car.getModel()))));
        pageNavigation.clickOnElement(carEditButton);

        elementIsViewed(changeImageButton);
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

    @Test
    void logoutLoginButton(){

        logout();

        assertEquals(getHomePageURL(), driver.getCurrentUrl());
        assertThatThrownBy( () -> logoutButton.click()).
                isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy( () -> addCarButton.click()).
                isInstanceOf(NoSuchElementException.class);
        elementIsViewed(loginButton);

        login();

        elementIsViewed(logoutButton);
        elementIsViewed(addCarButton);
    }

    @Test
    void goToDetailsPage() {

        driver.get(homePageURL);
        pageNavigation.scrollDown();

        WebElement carCardTitle = driver.findElement(xpath(format("//*[@id=\"car-brand-model-%s-%s\"]",
                car.getBrand(), car.getModel())));
        String exceptedCarCardTitle =
                format("%s %s", toTitleCase(car.getBrand().getStringValue()), car.getModel());
        checkElementInnerText(exceptedCarCardTitle, carCardTitle);

        WebElement carCardShortDescription = driver.findElement(xpath(format("//*[@id=\"car-short-description-%s-%s\"]",
                        car.getBrand(), car.getModel())));
        checkElementInnerText(car.getShortDescription(), carCardShortDescription);

        pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                car.getBrand(), car.getModel()))));

        checkElementInnerText(exceptedCarCardTitle, detailsBrandModel);
        checkElementInnerText("Body type : " + toTitleCase(car.getCarBodyTypes().getStringValue()),
                detailsBodyType);
        checkElementInnerText("Transmission type : " + toTitleCase(car.getTransmissionBoxTypes().getStringValue()),
                detailsTransmissionType);
        checkElementInnerText("Engine capacity : " + car.getEngineCapacity() + " liter inline",
                detailsEngineCapacity);
        checkElementInnerText("Production year : " + car.getYear(), detailsProduceYear);
        checkElementInnerText(car.getFullDescription(), detailsFullDescription);

        car.getAdditionalOptions().forEach((option) -> {

            checkElementInnerText(option, driver.findElement(xpath(format("//*[@id=\"car-option-%s\"]", option))));
        });

        pageNavigation.scrollUpDown();
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

    private static void checkElementInnerText(String exceptedInnerText, WebElement element){

        assertEquals(exceptedInnerText, element.getText());
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
        pageNavigation.scrollDown();
    }

    private static void login() {

        driver.get(getLoginPageURL());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"usernameInputField\"]"))).click();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"usernameInputField\"]"))).sendKeys(getUsername());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"passwordInputField\"]"))).click();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"passwordInputField\"]"))).sendKeys(getPassword());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"sendLoginDataButton\"]"))).click();
        threadSleep1Seconds();
        driver.get(getHomePageURL());
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"logout-button\"]")));
    }

    private static void logout() {

        driver.get(homePageURL);
        pageNavigation.scrollUp();
        wait.until(elementToBeClickable(xpath("//*[@id=\"logout-button\"]"))).click();
    }
}
