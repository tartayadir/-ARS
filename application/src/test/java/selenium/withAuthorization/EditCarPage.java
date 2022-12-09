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
import org.springframework.ui.context.Theme;
import utils.selenium.PageNavigation;

import java.io.File;
import java.time.Duration;
import java.time.Year;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.Keys.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static utils.selenium.PageNavigation.threadSleep1Seconds;
import static utils.selenium.URLUtils.getAddCarPageURL;
import static utils.selenium.URLUtils.getHomePageURL;
import static utils.spring.AuthTestUtils.getPassword;
import static utils.spring.AuthTestUtils.getUsername;
import static utils.spring.StringUtils.*;

@Slf4j
public class EditCarPage {

    private static WebDriverWait wait;

    private static PageNavigation pageNavigation;

    private static String homePageURL;

    private static WebDriver driver;

    private static String editCarPage;

    @FindBy(xpath = "//*[@id=\"change-image-button\"]")
    private static WebElement changeImageButton;

    @FindBy(xpath = "//*[@id=\"logout-button\"]")
    private static WebElement logoutButton;


    @FindBy(xpath = "//*[@id=\"come-back-button\"]")
    private static WebElement comeBackButton;

    @FindBy(xpath = "//*[@id=\"image-modal-come-back-button\"]")
    private static WebElement imageModalComeBackButton;

    @FindBy(xpath = "//*[@id=\"addCarButton\"]")
    private static WebElement addCarButton;

    @FindBy(xpath = "//*[@id=\"confirm-delete-button\"]")
    private static WebElement confirmDeleteCarButton;

    @FindBy(xpath = "//*[@id=\"close-modal-window-button\"]")
    private static WebElement closeModalWindowButton;

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

    @FindBy(xpath = "//*[@id=\"image-input\"]")
    private static WebElement imageInput;

    @FindBy(xpath = "//*[@id=\"additional-options-input\"]")
    private static WebElement addOptionsInput;

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

    @FindBy(xpath = "//*[@id=\"add-option-button\"]")
    private static WebElement addOptionButton;

    @FindBy(xpath = "//*[@id=\"option-0\"]")
    private static WebElement firstOptionInput;

    @FindBy(xpath = "//*[@id=\"option-0-error-massage\"]")
    private static WebElement firstOptionInputErrorMassage;

    @FindBy(xpath = "//*[@id=\"error-car-info-header\"]")
    private static WebElement modalErrorCarInfoHeader;

    @FindBy(xpath = "//*[@id=\"error-car-info-body\"]")
    private static WebElement modalErrorCarInfoBody;

    @FindBy(xpath = "//*[@id=\"error-car-info-ok-button\"]")
    private static WebElement modalErrorCarInfoOkButton;

    @FindBy(xpath = "//*[@id=\"model-input-error-massage\"]")
    private static WebElement modelInputErrorMassage;

    @FindBy(xpath = "//*[@id=\"year-input-error-massage\"]")
    private static WebElement yearInputErrorMassage;

    @FindBy(xpath = "//*[@id=\"engine-capacity-input-error-massage\"]")
    private static WebElement engineCapacityInputErrorMassage;

    @FindBy(xpath = "//*[@id=\"short-description-input-error-massage\"]")
    private static WebElement shortDescriptionInputErrorMassage;

    @FindBy(xpath = "//*[@id=\"full-description-input-error-massage\"]")
    private static WebElement fullDescriptionInputErrorMassage;

    @FindBy(xpath = "//*[@id=\"upload-image-button-error-massage\"]")
    private static WebElement uploadImageButtonErrorMassage;

    @FindBy(xpath = "//*[@id=\"image-input-error-massage\"]")
    private static WebElement imageInputErrorMassage;

    private static Car exampleCar;

    private static File image1;

    private static File image2;

    private static File text;

    @BeforeAll
    static void beforeAll() {

        image1 = new File("src/test/resources/images/modern-blue-urban-adventure-suv-vehicle-illustration_1344-205.png");
        image2 = new File("src/test/resources/images/FORD_FUSION_2020.png");
        text = new File("src/test/resources/fiels/text.txt");

        short year = 2013;
        exampleCar = Car.builder().
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
                additionalOptions(List.of("Option 1", "Option 2", "option 3")).
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
        addCar(exampleCar, image1);

        pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                exampleCar.getBrand(), exampleCar.getModel()))));

        String detailsPrefixUrl = "http://localhost:4200/car-catalog/details/";
        long carId = Long.parseLong(driver.getCurrentUrl().substring(detailsPrefixUrl.length()));
        editCarPage = "http://localhost:4200/car-catalog/update/" + carId;
    }

    @BeforeEach
    void setUp()  {

        PageFactory.initElements(driver, this);
        driver.get(editCarPage);
    }

    @AfterAll
    static void afterAll() {

        deleteCar(exampleCar);
        logout();
        driver.close();
        driver.quit();

    }

    @Test
    void authorizationIsSuccessful() {

        driver.get(getHomePageURL());
        elementIsViewed(logoutButton);
        elementIsViewed(addCarButton);
    }

    @Test
    void trySubmitCarWithEmptyFields(){

        String exceptedErrorValidationMassage = "Model is required.";

        modelInput.sendKeys(chord(CONTROL, "a", DELETE));
        yearInput.sendKeys(chord(CONTROL, "a", DELETE));
        engineCapacityInput.sendKeys(chord(CONTROL, "a", DELETE));
        shortDescriptionInput.sendKeys(chord(CONTROL, "a", DELETE));
        fullDescriptionInput.sendKeys(chord(CONTROL, "a", DELETE));

        pageNavigation.clickOnElement(submitCarButton);
        checkAndCloseModalCarInfo();
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationRequired() {

        String exceptedErrorValidationMassage = "Model is required.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        modelInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationMinLength() {

        String exceptedErrorValidationMassage = "Model must be greater than 2.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        sendPutTextKeyInInputField("s", modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField("f", modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField("t", modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationMaxLength() {

        String exceptedErrorValidationMassage = "Model must be less than 40.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(41), modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(85), modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(51), modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationPattern() {

        String exceptedErrorValidationMassage = "Model must not contain anything other than letters," +
                " also contains only numbers or start with number.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(21), modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(12), modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(37), modelInput);
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void productionYearValidationRequired() {

        String exceptedErrorValidationMassage = "Produce year is required.";
        checkErrorValidationMassage("", yearInput);

        yearInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationMinValue() {

        String exceptedErrorValidationMassage = "Produce year must be greater than 1920.";
        checkErrorValidationMassage("", yearInputErrorMassage);

        sendPutTextKeyInInputField("0", yearInput);
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField("1919", yearInput);
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField("1567", yearInput);
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationMaxValue() {

        int year = Year.now().getValue() + 1;
        String exceptedErrorValidationMassage = format("Produce year must be less than %d.", year);
        checkErrorValidationMassage("", yearInputErrorMassage);

        sendPutTextKeyInInputField(Integer.toString(year), yearInput);
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField("3442", yearInput);
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField("2344", yearInput);
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void engineCapacityValidationRequired() {

        String exceptedErrorValidationMassage = "Engine capacity is required.";
        checkErrorValidationMassage("", engineCapacityInput);

        engineCapacityInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void engineCapacityValidationMinValue() {

        String exceptedErrorValidationMassage = "Engine capacity must be positive number or 0.";
        checkErrorValidationMassage("", engineCapacityInputErrorMassage);


        sendPutTextKeyInInputField("-0.1", engineCapacityInput);
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField("-50", engineCapacityInput);
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField("-0.2", engineCapacityInput);
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void engineCapacityValidationMaxValue() {

        String exceptedErrorValidationMassage = "Engine capacity must be less than 15,0.";
        checkErrorValidationMassage("", engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField("15,1", engineCapacityInput);
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField("345,32", engineCapacityInput);
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField("33,333", engineCapacityInput);
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void shortDescriptionValidationMaxLength() {

        String exceptedErrorValidationMassage ="Short description must be less than 1000.";
        checkErrorValidationMassage("", shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(1001), shortDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(1023), shortDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(1005), shortDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);
    }

    @Test
    void shortDescriptionValidationPattern() {

        String exceptedErrorValidationMassage = "The short description should not contain anything" +
                " but a word, numbers and punctuation marks, also cannot start with number or punctuation mark.";
        checkErrorValidationMassage("", shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(21), shortDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(132), shortDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(577), shortDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);
    }

    @Test
    void fullDescriptionValidationMaxLength() {

        String exceptedErrorValidationMassage ="Full description must be less than 5000.";
        checkErrorValidationMassage("", fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(5001), fullDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(5023), fullDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomString(5005), fullDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);
    }

    @Test
    void fullDescriptionValidationPattern() {

        String exceptedErrorValidationMassage = "The full description should not contain anything" +
                " but a word, numbers and punctuation marks, also cannot start with number or punctuation mark.";
        checkErrorValidationMassage("", fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(21), fullDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(132), fullDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(generateRandomStringWithSpecialChars(577), fullDescriptionInput);
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);
    }

    @Test
    void checkValidationImage() {

        String exceptedErrorValidationMassage = "Car image have invalid file type.";
        checkErrorValidationMassage("", imageInputErrorMassage);

        changeByInvalidImageFileEditForm(text);
        checkFieldValidation(exceptedErrorValidationMassage, imageInputErrorMassage);

        changeImageFileAddForm(image2);
        checkErrorValidationMassage("", imageInputErrorMassage);
    }

    @Test
    void checkEditCar(){
        
        short year = 2015;
        Car addCar = Car.builder().
                id(10L).
                brand(CarBrands.AUDI).
                model("Model 2").
                carBodyTypes(CarBodyTypes.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.ROBOTIC).
                engineCapacity(3.4).
                shortDescription("Sh ort description 2 Short description 2 Short description 2Short dddddd " +
                        "Short description 2 Short description 2 Short description 2Short description fds" +
                        " 2Short description 2 Short description 2 Short description 2Short description 2" +
                        " 2Short description 2 Short description 2 Short description 2Short description 2" +
                        " 2Short description 2 Short description 2 Short description 2Short description 2 " +
                        "Short description 2 Short description 2Short description 2 Short description 2 S"
                ).
                fullDescription( "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2"
                ).
                additionalOptions(List.of("Option 4", "Option 5", "option 6")).
                imageFileName("Image file 6").
                build();

        year = 2010;
        Car editCar = Car.builder().
                id(10L).
                brand(CarBrands.BENTLEY).
                model("Model 3").
                carBodyTypes(CarBodyTypes.CONVERTIBLE).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.VARIATIONAL).
                engineCapacity(1.4).
                shortDescription("Sh ort description 3 Short description 3 Short description 3Short dddddd " +
                        "Short description 2 Short description 2 Short description 2Short description fds" +
                        " 2Short description 3 Short description 2 Short description 2Short description 2" +
                        " 2Short description 2 Short description 2 Short description 2Short description 2" +
                        " 2Short description 2 Short description 2 Short description 2Short description 2 " +
                        "Short description 2 Short description 2Short description 2 Short description 2 S"
                ).
                fullDescription( "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 3 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 3 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 3" +
                        "Full description 2 Full description 2 Full description 2Full description 2" +
                        "Full description 2 Full description 2 Full description 2Full description 2"
                ).
                additionalOptions(List.of("Option 7", "Option 8", "option 9")).
                imageFileName("Image file 6").
                build();

        addEditAndCheckCar(addCar, image1, editCar, image2);
    }


    private static void addEditAndCheckCar(Car addCar, File addImageFile, Car editCar, File editImageFile) {

        addCar(addCar, addImageFile);
        editCar(addCar, editCar, editImageFile);
        checkEditedCar(editCar);
        deleteCar(editCar);
    }


    private static void editCar(Car currentCar, Car editCar, File editImage){

        pageNavigation.hoverOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                currentCar.getBrand(), currentCar.getModel()))));
        pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"car-%s-%s-edit-button\"]",
                currentCar.getBrand().getStringValue(), currentCar.getModel()))));
        fillFieldsEditForm(editCar, editImage, currentCar.getAdditionalOptions().size());

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();

        pageNavigation.clickOnElement(submitCarButton);
    }

    private static void fillFieldsEditForm(Car editCar, File editImage, int optionsNumber) {

        Select brandSelect = new Select(brandInput);
        Select carBodyTypeSelect = new Select(carBodyTypeInput);

        sendPutTextKeyInInputField(editCar.getModel(), modelInput);
        brandSelect.selectByValue(editCar.getBrand().getStringValue());
        carBodyTypeSelect.selectByValue(editCar.getCarBodyTypes().getStringValue());

        WebElement transmissionBoxTypeInput = driver.findElement(xpath(format("//*[@id=\"%s\"]",
                editCar.getTransmissionBoxTypes().getStringValue())));
        pageNavigation.clickOnElement(transmissionBoxTypeInput);

        sendPutTextKeyInInputField(Short.toString(editCar.getYear()), yearInput);
        sendPutTextKeyInInputField(Double.toString(editCar.getEngineCapacity()), engineCapacityInput);
        sendPutTextKeyInInputField(editCar.getShortDescription(), shortDescriptionInput);
        sendPutTextKeyInInputField(editCar.getFullDescription(), fullDescriptionInput);

        for (int i = 0; i < optionsNumber; i++){

            pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"option-%d-remove-button\"]", 0))));
        }

        WebElement addOptionsInput = driver.findElement(xpath("//*[@id=\"additional-options-input\"]"));
        editCar.getAdditionalOptions().forEach(op -> {
            wait.until(visibilityOf(addOptionsInput)).sendKeys(op);
            wait.until(visibilityOf(addOptionsInput)).sendKeys(chord(ENTER));
        });

        pageNavigation.clickOnElement(changeImageButton);
        imageInput.sendKeys(editImage.getAbsolutePath());
        pageNavigation.clickOnElement(applyImageUploadButton);
    }

    private static void addCar(Car car, File imageFile){

        driver.get(getAddCarPageURL());

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();

        Select brandSelect = new Select(driver.findElement(xpath("//*[@id=\"brand-input\"]")));
        brandSelect.selectByValue(car.getBrand().getStringValue());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"model-input\"]"))).sendKeys(car.getModel());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"year-input\"]"))).clear();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"year-input\"]"))).
                sendKeys(Short.toString(car.getYear()));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"engine-capacity-input\"]"))).clear();
        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"engine-capacity-input\"]"))).
                sendKeys(Double.toString(car.getEngineCapacity()));

        Select carBodyTypeSelect = new Select(driver.findElement(xpath("//*[@id=\"car-body-type-input\"]")));
        carBodyTypeSelect.selectByValue(car.getCarBodyTypes().getStringValue());

        pageNavigation.clickOnElement(
                driver.findElement(xpath(format("//*[@id=\"%s\"]", car.getTransmissionBoxTypes().getStringValue()))));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"short-description-input\"]"))).
                sendKeys(car.getShortDescription());

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"full-description-input\"]"))).
                sendKeys(car.getFullDescription());

        WebElement addOptionsInput = driver.findElement(xpath("//*[@id=\"additional-options-input\"]"));
        car.getAdditionalOptions().forEach(op -> {
            wait.until(visibilityOf(addOptionsInput)).sendKeys(op);
            wait.until(visibilityOf(addOptionsInput)).sendKeys(chord(ENTER));
        });


        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"upload-image-button\"]")));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"image-input\"]"))).
                sendKeys(imageFile.getAbsolutePath());
        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"apply-image-upload-button\"]")));

        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"submit-car-button\"]")));

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();
        pageNavigation.scrollDown();
    }

    private static void checkEditedCar(Car editCar){

        driver.get(homePageURL);

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();

        driver.navigate().refresh();
        pageNavigation.scrollDown();

        WebElement carCardTitle = driver.findElement(xpath(format("//*[@id=\"car-brand-model-%s-%s\"]",
                editCar.getBrand(), editCar.getModel())));
        String exceptedCarCardTitle =
                format("%s %s", toTitleCase(editCar.getBrand().getStringValue()), editCar.getModel());
        checkElementInnerText(exceptedCarCardTitle, carCardTitle);

        WebElement carCardShortDescription = driver.findElement(xpath(format("//*[@id=\"car-short-description-%s-%s\"]",
                editCar.getBrand(), editCar.getModel())));
        checkElementInnerText(editCar.getShortDescription(), carCardShortDescription);

        pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                editCar.getBrand(), editCar.getModel()))));

        threadSleep1Seconds();
        threadSleep1Seconds();

        checkElementInnerText(exceptedCarCardTitle, detailsBrandModel);
        checkElementInnerText("Body type : " + toTitleCase(editCar.getCarBodyTypes().getStringValue()),
                detailsBodyType);
        checkElementInnerText("Transmission type : " + toTitleCase(editCar.getTransmissionBoxTypes().getStringValue()),
                detailsTransmissionType);
        checkElementInnerText("Engine capacity : " + editCar.getEngineCapacity() + " liter inline",
                detailsEngineCapacity);
        checkElementInnerText("Production year : " + editCar.getYear(), detailsProduceYear);
        checkElementInnerText(editCar.getFullDescription(), detailsFullDescription);

        editCar.getAdditionalOptions().forEach((option) -> {

            checkElementInnerText(option, driver.findElement(xpath(format("//*[@id=\"car-option-%s\"]", option))));
        });

        pageNavigation.scrollUpDown();
    }

    private static void changeImageFileAddForm(File imageFile) {

        pageNavigation.clickOnElement(changeImageButton);
        imageInput.sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(applyImageUploadButton);
    }

    private static void changeByInvalidImageFileEditForm(File imageFile) {

        pageNavigation.clickOnElement(changeImageButton);
        imageInput.sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(imageModalComeBackButton);
    }
    
    private static void sendPutTextKeyInInputField(String key, WebElement input){

        pageNavigation.moveToElement(input);
        input.sendKeys(chord(CONTROL, "a", DELETE));
        input.sendKeys(key);
    }

    private static void checkFieldValidation(String exceptedErrorValidationMassage, WebElement errorMassageAlert) {

        assertEquals(exceptedErrorValidationMassage, errorMassageAlert.getAttribute("innerText"));
        pageNavigation.clickOnElement(submitCarButton);
        checkAndCloseModalCarInfo();
        assertEquals(exceptedErrorValidationMassage, errorMassageAlert.getAttribute("innerText"));
    }

    private static void checkRequiredValidation(String exceptedErrorValidationMassage, WebElement errorMassageAlert) {

        pageNavigation.clickOnElement(submitCarButton);
        threadSleep1Seconds();
        checkAndCloseModalCarInfo();
        assertEquals(exceptedErrorValidationMassage, errorMassageAlert.getAttribute("innerText"));
    }

    private static void checkAndCloseModalCarInfo(){

        assertEquals("Incorrect car info", modalErrorCarInfoHeader.getAttribute("innerText"));
        assertEquals("You entered incorrect information about car. Please, check your entered date and try again.",
                modalErrorCarInfoBody.getAttribute("innerText"));
        modalErrorCarInfoOkButton.click();
    }

    private static void checkElementInnerText(String exceptedInnerText, WebElement element){

        assertEquals(exceptedInnerText, element.getText());
    }

    private static void elementIsViewed(WebElement element){

        pageNavigation.moveToElement(element);
        assertTrue(element.isDisplayed() && element.isEnabled());
    }

    private static void checkErrorValidationMassage(String exceptedErrorMassage, WebElement errorMassageAlert){

        String actualErrorMassage = errorMassageAlert.getAttribute("innerText");
        assertEquals(exceptedErrorMassage, actualErrorMassage);
    }

    private static void login() {

        driver.get(homePageURL);
        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"loginButton\"]")));

        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"usernameInputField\"]")));
        driver.findElement(xpath("//*[@id=\"usernameInputField\"]")).sendKeys(getUsername());

        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"passwordInputField\"]")));
        driver.findElement(xpath("//*[@id=\"passwordInputField\"]")).sendKeys(getPassword());
        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"sendLoginDataButton\"]")));

        elementIsViewed(driver.findElement(xpath("//*[@id=\"logout-button\"]")));
    }

    private static void logout() {

        driver.get(homePageURL);
        pageNavigation.clickOnElement(driver.findElement(xpath("//*[@id=\"logout-button\"]")));
    }
    private static void deleteCar(Car deleteCar){

        driver.get(homePageURL);

        threadSleep1Seconds();
        threadSleep1Seconds();

        pageNavigation.moveToElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                deleteCar.getBrand(), deleteCar.getModel()))));

        threadSleep1Seconds();
        threadSleep1Seconds();

        pageNavigation.hoverOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                deleteCar.getBrand(), deleteCar.getModel()))));

        threadSleep1Seconds();
        threadSleep1Seconds();

        pageNavigation. clickOnElement(driver.findElement(xpath(format("//*[@id=\"delete-car-%s-%s-button\"]",
                deleteCar.getBrand(), deleteCar.getModel()))));

        threadSleep1Seconds();
        threadSleep1Seconds();

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"confirm-delete-button\"]"))).click();
    }
}
