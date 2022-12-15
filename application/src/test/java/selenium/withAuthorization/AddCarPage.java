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
public class AddCarPage {

    private static File image;

    private static File text;

    private static WebDriverWait wait;

    private static PageNavigation pageNavigation;

    private static String homePageURL;

    private static WebDriver driver;

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

    @FindBy(xpath = "//*[@id=\"add-option-button\"]")
    private static WebElement addOptionButton;

    @FindBy(xpath = "//*[@id=\"remove-option-0-button\"]")
    private static WebElement removeButton0Option;

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

    @FindBy(xpath = "//*[@id=\"logout-button\"]")
    private static WebElement logoutButton;

    @FindBy(xpath = "//*[@id=\"image-input-error-massage\"]")
    private static WebElement imageInputErrorMassage;

    private static Car car;

    @BeforeAll
    static void beforeAll() {

        image = new File("src/test/resources/images/FORD_FUSION_2020.png");
        text = new File("src/test/resources/fiels/text.txt");

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
                additionalOptions(List.of("Option 1", "Option 2", "option 3")).
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
    }

    @BeforeEach
    void setUp()  {

        PageFactory.initElements(driver, this);
        driver.get(getAddCarPageURL());
    }

    @AfterAll
    static void afterAll() {

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

        pageNavigation.clickOnElement(submitCarButton);

        checkAndCloseModalCarInfo();
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationRequired() {

        String exceptedErrorValidationMassage = "Model is required.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        fillFieldsAddForm(car, image);

        modelInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationMinLength() {

        String exceptedErrorValidationMassage = "Model must be greater than 2.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(modelInput, "s");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "f");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "t");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "y");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationMaxLength() {

        String exceptedErrorValidationMassage = "Model must be less than 40.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(modelInput, "XfCfnMoLnRgJSUftcdbdkJdDLqYhXKKnAujHcxNDG");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "fwzGmTPmqiCKzewfEWzGPmZCuosNZOeERhhJafrcyWEPplrUjLybCFVMUEXPIpafMHiZBqfrpnubHuijRkJYC");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "rrXlYiBpsqPhYlOMDFiOdYeCFMVSRVWeOESlbQNWrpcFUZhZCni");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, generateRandomString(41));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, generateRandomString(85));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, generateRandomString(51));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationPattern() {

        String exceptedErrorValidationMassage = "Model must not contain anything other than letters," +
                " also contains only numbers or start with number.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(modelInput, "^&iesrTAz^bmNeMtN](yD");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "#k^AW*QVGnN");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "z#Rff##(AV)uz)AqY(TJ#PKOWq^[Oet(PAv");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, generateRandomStringWithSpecialChars(21));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, generateRandomStringWithSpecialChars(12));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, generateRandomStringWithSpecialChars(37));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void productionYearValidationRequired() {

        String exceptedErrorValidationMassage = "Produce year is required.";
        checkErrorValidationMassage("", yearInput);

        fillFieldsAddForm(car, image);

        yearInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationMinValue() {

        String exceptedErrorValidationMassage = "Produce year must be greater than 1920.";
        checkErrorValidationMassage("", yearInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(yearInput, "0");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "1919");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "1567");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "1111");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "928");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "1291");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationMaxValue() {

        int year = Year.now().getValue() + 1;
        String exceptedErrorValidationMassage = format("Produce year must be less than %d.", year);
        checkErrorValidationMassage("", yearInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(yearInput, Integer.toString(year));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "3442");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "2344");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "3928");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationDoubleNumber() {

        String exceptedErrorValidationMassage = "Produce year must be positive and not fractional number.";
        checkErrorValidationMassage("", yearInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(yearInput, "2012.2");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "1980.123");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "2020.3");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "2021.4555");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "2004.2");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "2000.53");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void engineCapacityValidationRequired() {

        String exceptedErrorValidationMassage = "Engine capacity is required.";
        checkErrorValidationMassage("", engineCapacityInput);

        fillFieldsAddForm(car, image);

        engineCapacityInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void engineCapacityValidationMinValue() {

        String exceptedErrorValidationMassage = "Engine capacity must be positive number or 0.";
        checkErrorValidationMassage("", engineCapacityInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(engineCapacityInput, "-0.1");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "-50");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "-0.2");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void engineCapacityValidationMaxValue() {

        String exceptedErrorValidationMassage = "Engine capacity must be less than 15,0.";
        checkErrorValidationMassage("", engineCapacityInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(engineCapacityInput, "15,1");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "345,32");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "33,333");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void shortDescriptionValidationMaxLength() {

        String exceptedErrorValidationMassage ="Short description must be less than 1000.";
        checkErrorValidationMassage("", shortDescriptionInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(shortDescriptionInput, "oOgcYySOQkexghvjCixQyzTywvuZvfoxtApWwHLdqWNYqsUciDqCfRIeJgwapZVlXquQKoxvJVOpygnsQaxZfRZMTOWxqZKUjAAbZgKPBrahrowonquCSyWHqxOBBhpEWVVeMHgckaAIdxnZQpzCUrvYyZpycRFRjnLwxLTzisNitEGSwzSPOvZGlSjhGUtyiDiNJBQORDXYVflylsxLknTKSZWnTnPFdUSsRMJMKIeVJexkJLYsnpFPHtZnKfuRukLHHXlTMeiiwWTrTOBWynYBPJceKSCdDiQzPbTBOWZhxhAwwuwqTrnhmIicEBzpsSbAWjaaEWneSlzeGZwBWyDTuQdksOcETBjTQVEfOmdNroAJdoBxpcLfZPCVhNfONdXqDZyLKIupXOLcqrnwHSVBsuikTugfsBLjzWhMaoziCIcTEEsqiHHRQEtyEYjgadLVSUdvHJoIyERjsYrubNRjZBvFmCLGZTbhYioamKXgtRLdVMpTfjjiTmaSSICkrpUKziHZTZYjHKrCeDbGBHryYyqhpsCvcsZjkPwNBHIomVEaYKTCEzwJQYQnjCKURKGIeQAloeHMBuOLYUgUWevHIyFQqVstEAgnOJahGZzUwMfBFdXMVMwgyWauvacTooDIEANBRWAeXgvaHSNLQfCyIusFwSSEMjYsvxCffjDedmBRIGaHeVSrAFOSHHBFvuGxjzPKtdSDwUBcZViSSGUBJBNxNUnUwnBhiLQSSQioKoQHQRNdJSSXswPaUohTCrmWAIoYOnDjaeIRTtoDHyvoXAuoNKUvSTndFdbWTipxecNeAEUtsLDGsvKrIYvCGgLnHhwKQPgPqSlaWvwbfJNKFiJUolYexgsPjVsWDbOxRnWWYaOgXNEVlziQjUQxpvZazMatXzmqVdwhajOTpAJgEYZVkZRIpsgoRYmpSTtEbqbTILrAKEOKtVKzHJYcZXdnQeRdqpBBlBBnEpByqjduYxmYDepmApmqAoVvh");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, "BdxRJStcybUScdkgkiqlzhkoviVoDXvQDFouQQEeMCTMVzUxiMbgcwnCKQReVTNsSWzdJhaYbuSoglQxnhSJceTqZgZxCCAarpClUZbkQrMlHUDMOFOYvfEkNSJqfgorkvCxWmWDdgRItSNNfeWaclrgDdtVEUvbOxSmufJDqzYKymWyLNdleEtQinGGFvqAxXKRJUkqqMHXuoLaGrQmLNMBoJRfUyhSLzzzIqVnvRGCkXtqJZkYAnXRSJdbgPGYoOYpHTCiCXXFeiAxbEelypxVQzEXWmIZtqeANyctIoCGmiaAPIFLuzchvMsyxfzpVPbqTpzUuaNSGzkfNngwzqqQOhgqrnnZBDdZFdxlGaKVXIpRyGdTdqVplYOtHPapeKpanCiktZAMTdmLwjuADwVJCznDhSqKEptrkRCSMkbtTrsipTGGlGgxQDKCmzGwjhjsvJEGfJqXzIBYDaOFpYBKYlkIPIFQwmOkEmIwsbNiihahcjvvpmnoKCUBeGaMoMcREMhgPoAvmmfzoIhBQIvKbWEwxPgybLWKMgxKQefFZxiFDFlKmjXCZnvWtARzGZckdbcgmIZUUeOgGXJuhCmwzyfzhJIGyjWGbwtmfWmFCqXHiwoQKWprUcVoAGvKQEBQYiHkyWzKtrpZjRVFqaQxoabnhFqIURrvHAaZodaBXaOqDAgiKwUnwzSNoIQlSdgwyvDQwpGpsGkWlQXMcYilPlJWpNoPvdpqfmJaeVbovzeYDFzusTybGxObcRXOLbZeghtcvLdVFjzpOYTPPYjVgVptzdjhPDhrLrWHLVmoZxpcXhZiTpQRtnqvsOlTxEVTaTMXgbgRdwpGbhQZMtAgvvjGSegMzuHTRKPNUqNIptTmIpTjjQnQCVGIWEUkJtmSMHFCqjnFjDgHNaTBxSrGHesTnnkGHazRcAllDXTesAuuXxYvxiKGqvzKbOFDhlQgRKkNmvTsttHOsBHUvSAKDenlRIKykrtnepHdmnNjtKwxUPcZvCAszcRAuKB");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, "ZpBIwRIHHxhocgwfoJAZqaZjdcspIskhtzcAufVEHAdgQxiqcZYGphEMaXcETiHQCzEdzrkVyLgjkeMTDUyUhAadpEsLnXonGWFtiMdxRWnAKebdNUVJnjQfKuTRdxxLHtVXHfqsWNZIEPzwtLuDRdmQSbIWkVPpFrYPejgTOFSaNtSifrOLmdhKecNiGwfRQNIVBhFhUKeoIXtahUKrpkQsOHLFcypfVVTMHyVGmFWJWSEquROKxLnWkRbzWgGPLiuxVQTCVFArUOQQRYTwdHvQmpZHshObmuygvLwEcPBhXoKjFvtNmFDDVGeNMJYjMJvZwrgRdXNZQMsYeRUScCEcLcHQClwhhQVIiwJDVLrvhcWRetNzbgGKTSvZxhoIkoFrSdJguDFchKJxAiJMeJBHAZpmhwxpjTaXiWhryIUXuvNIijVGdKtirIWFxFmphaJZDwzsHxZaGjIqOtoFyhYIznQKcbMBeoMdRNqTGjxDyXOuUKDyaGFxcTRhGbtbjNmARectFnJTiYOEidgvihryFtDbcOIzpGUmVgPOSGnZAqzFjnxEhimkhTTtdGuQYBTKwFDhZeioyfsEavYalXhXsQrNYXwcvuuUeBIzfHwzeHLDoZhXSnyFaYfAXNYHfcbDjthSyzzYzXQLSFJdjTcmMpzFvqGWqlcVEDgQLOhuuFQykNXuRuyKXXNCCjXuQGzmIRrqdcsLLOLuaJgormQWiNRYDHtCauuCmiBkUDzvBNqdlPZNObaOvnSxGdCHooqfkVwjdqzydJMhVIqhyjfHQlEPHztUMjJQvhSXUuHrfJmBoLpIKlYVeCvsJfTJvJBlpcaMaUswYHgzwwMsyoVZayyzOKeerMXbWcOCnyNTkYvvnZUuMzqaQBdYnZUPRotDUizSlEducMcHhEUfmQChykzmRJNlPSxEIrZFsygLybKRnAZUcdJbMPilkxiMZoDPgyoLEDArYebZABUimAKNZTOhcXSlplTdtSyMsUgYw");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, generateRandomString(1001));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, generateRandomString(1023));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, generateRandomString(1005));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);
    }

    @Test
    void shortDescriptionValidationPattern() {

        String exceptedErrorValidationMassage = "The short description should not contain anything" +
                " but a word, numbers and punctuation marks, also cannot start with number or punctuation mark.";
        checkErrorValidationMassage("", shortDescriptionInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(shortDescriptionInput, "-O+xy{/xUp)MZ_?l}q-ds");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, "PCGZc)t+XyZaYDbXsqjZDrnqEtg)(mBBYS{x)w}jtz*qWOaql))]/{EEosxbiViD{K-/{&Jh)N%)zoHV_PNJvoawmAwOt%DV/rcrxqbD_LtL-ZuGmFmIDG/CU%vsejHht");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, "eBbG_d*xuUzektpZb(vxk*t)/CJl((ixjNZVMIHW/Gzf_bhUa&Km)u)-/XYaLE{%ahP)h(ZyuBd/qc_IX]FETqg]}ws]{/{cGLg]%pDPl_/EXOZlI/pEZBUxqxUhHjg+Z-KhMSjDprfuyFSk&tuZ}&j/QsGzuoBQcQY/Msq/ROXC-m(OdUtn-VV{ONh-kNhE+rl/od/hgbUeu/%MLqrabvCsn/*/AWLFPjjwUw*_nFWBEgD(tWeqlkGGagyaYRy*gtB_sLzd%XY(r-vfE()du/e}ph/i+}?Af{HdxX)IDDOqo{fi//EgCiWIrshgj+E]-uHqW)vnGia{))Z((s/aPCuxn{EU)k/WqkxM/Sx/xAPnZ{b)y)n)t?BjX)%xxD_znTatH/Tlcj}Jumsa+bTnYLZxFyUNTVpCyTbyORexUiNWZ%{tICg+A/OX/MdRM&G)dMsC+&WRg)ooQqxq&-zUnZedsnjYCipbPTwgQfwFuL%ZfZAss-y/YaMh-/s/sKy/R(SntI(MRk/(-w/oY)ZFIa{/pvHFnM&I-PhJf/");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, generateRandomStringWithSpecialChars(21));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, generateRandomStringWithSpecialChars(132));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, generateRandomStringWithSpecialChars(577));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);
    }

    @Test
    void fullDescriptionValidationMaxLength() {

        String exceptedErrorValidationMassage ="Full description must be less than 5000.";
        checkErrorValidationMassage("", fullDescriptionInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(fullDescriptionInput, "cfHDQlXotpdvVhlgzcdoGjGTtnPXQXfErqAUxIqBrfWZCibEJbymKqcxziovVFTfcxPYzideSIvDmHuyVlPdRDEjYHAteJGsOThtpptUvsjmsImsiTZpmkkHZOnbxrpKLGhHhTrIQQfTiBkyaVBzSfFvHXmiTVmbGGrbsOFJbFrzIaDfojydNFHtaxYHjyjTKOJyiHhZxbKeFWDMyUsuQNCcTuQMNWszLZwbtbGaslwIPcJjJNWmXUItLfOObPHoPEKeIMswYgKKtOOwgUAephLTYYlAlXBQmqWyrDReDJkdpscwzczbYqJGIcnIXxGlkmekAtRCCkXKSdrbrTdzNGWZaURzlKUuqlrSunexLSnVreqfFmGoggSxedMVSFfbYfZDUjsrosfftcCPBLutjetvEzEjIepCqjtNQXuUFenZkEvXBnKPrcWmJaXlpMzTmWLmrmckSAYBNFDHABtixhLTlRnZrojTRAwNuMbGPbtQciTEuNKcvAZjJGsNgrpugjNKUBDSedOOFUJcErnBNRltmCtkkJBuHoFxkLqvsPuybengIBeGqptypHbOPhNpvDUxxMEZhhvdhEFqmcrdSrELedllGLJIvBLYMgyMyjmIsOFCZywLRxiuIqnWgKvmFKicXeuNcwsTspQTUBLHQIgatVsOpCjjJwxDpoHvoTxFOAtxKVbqSNXsNxtPOnLLBmlLHIawNgkWMUqkbbMZyGGYlssQKfHDrUOdudlltuQJXiwHxbxgQewpilsAlVqMQOxQFYxNgRBmBmYXetsrCDbzLWuoCKjXpNPeChBfKBGPEVYLLLKSzciNMcXGWoEOQLyMfhWjhzBfUbtCSrHsIoyWQRiJcBPouOFCdnQdBGFypPykIWXVyaVaaCrRiNfcYDaGwBPkgYOqGlrUstcDwfRzqGZRoDggkIkHWDMdIjXjnpkkQYtBdVhMMBxIdojRTOZbzvbJaIQLyoEXePJSWDyfNJonBtOkEvgvZEZTLazmGtKTdEDsSIZfWmPSvQTyzbhagXCuwMBwxIrilQuHWYiSGPBenTTnutUMIXnWFXsAFXhTrsXeOheSDWcYxiHQIBgoumMuIIwtKQnkssKhwZXvAnOdmKcvKKSCznqviseBQXwDTgkmqalFxEJEcWDqSkPkybaeAzROshrMVNncbrWhOkNtAXWlZYYWnxwPpwMnmBnrShPopgTVcPETHvQWyLYNFZdqxCSMnYlEfKWGZgsFRnKcAQmadZDSIwpyZjjuOzbKqEUCAfuMynIvddtDOXewDiYsYjKIisWSEBhlVkFwNDrfmHIfqZidFkIFWoUtpjCoBSxvJqEJBJuAGXaaHmunTYYfZYVBOBgEtFPKjDvLcZqOFguVtAVlNRBlLwNLzZNVBSpKuHetnSXvqPzcgQZumiwNTzolIEavbKVupNUCDhtIUDqZRPmIhXBPVbSLimTSSxTzzcEMsIjPJLUtkvSZkIKNCnyKWycAlfPbJKFGBQQRMtaSkRiclBEkzwXfZyKKPvOFJsdQoYlQhldxzoYMkexzpClefcvvPVIAbFZsZncxFvLTpoqJODNEbWSUvjJcLIExWFSihIsfIDmHJPXOwKbMophopkwjTmVoLpSMDNsYstgOccjITkigrpykFRLJBUimfSPcFCKwOFiBmRYGnAahQiBNRzppqJBpxVYEZpTJOWfxVgULesvesPbeHCSsJkGguqtPriWPIZMmNnjmubxudYbGMJeWJZSScCTTtPjoTRkcViEJbsRlcqwsAUktsGBRiSFQMGGYxDPqYsZoIVBYVThCbbfLmwSpPegdQsrkmuQkMlvJElrjrJmplPYlJJSAQQuBfWEOgUSXNnuWJgHCxKpDiUoiSmMeTvJVYTdbNFVfeunYfhjNFvAeUJGSkyJIzRjXlhFuNKHQJgOEhLeihTnBftrHJbrSdSThrtXfvWsriiTLwWWLldGfODLsxHrHSgbmLAXQeCHVzACuOQUOzkuPpUoZcoTgxWmvoblwMYvOEcooloRmaxVYKHQuOkkcfnghQLBTvpwVammOJZGhIUvENLlnEJUNohgZLgpOvLbKJIwgZdjNmPJOIoVWhcyZbvSgALdlPJdzkcHHNtTNcdkqBhNnyovZIFBbzKPzjDgmbMTMptwfQuhwjooafQXLLEZxcWztfzMdSDhxvdvrKSushqGrKUOUMxtYwEqKSKVDBfOkUnAKZBGYWQbHgbVPEnllLrVyhPQcpNOSfGzqeysByjNJdIRnvwLNalCZlLmfBLbEWcNMiHbdEUMKWcXsYkCaHkegMwMwMTGsEdQWvrEvfLNQFbTtWjZpzjMWQQvXjKIDWHXhNCpTeMAuRUggxJvXRZaYHmQGnaDqLOCMNxDpgBGBslMfrZnPZdWnriEVnGAAtfXNpZnDtOLtFHYddyQjKKyRQReuFkUqnNxqxlcWrmYhAaWLAIPEwpoNoidtKCIvoHQMHqgskLGUJUuEInglULGuSQycSEQQZVTFHhOermTdIbfzrhEFcNcQGfWebeiRlKmPsuQgMpdmHVTkralnMGOXxOBDNEftMkOgWVTWIXyClIIaqxxuXqhoYRNncOsaEEwLoMwrDUUXyKvAVLsleGFEdcZpaxSUJjhwcoonUJXNHEoLBVmfhVAqfpxLLVXeLcncJMClEEBVFhAbDpWAKNdOJulCIKxcQfDNAQnmIITWogpJUCmVdDFXAXpkzXGhsVYVtdsRiDkjimstzROUUIWsRFRhzmvtEkEuVmizJYgVrcJYftRLvTiffLxxsqYGvmbHiBGrzNHnynfWKyOsTreLLLmpuoLWzUYHyyuebKXyKoBAcUIhxSpAbgyhpsEREmuDkKrRyCPVvwWOsaOUzzuTMddcExSyqidTSovTatpHnNAoUUdOekbLPdRvOukPsfbpUoQmefXJhdGlcpgbyQkqLLcXBqBebauEgpryWVMqvyrOhSBDiIDbbIxFGWuFelWRofclliDmzRWCxXivtnVpqYZAGJiItNAbVmYnuveHhWMgBRDjpxUHzNcDXFqHmOSMIuhXlMmFlpgvveOGTwuivszELqQMVljeSMLNpeVMeUIZOFfFSiSKOPfOPbLSdtChzNTqNCGcMRkWTzuNylFATLYTJCXFSdILDTmJkxTxExemyhOtnTxSuJZJjJAVDaYJbxjTklsFhacStdABuCATuUcYLEhkFeeJpxTWQOzKvlJJTbiKMeVcBAAIqXzWoZcMPsttQMHUmDZVTaWZTdvEhgMCkoRSFzWFluQccuyvjjudExIeghMzEdsxiWCxSDJWtAgmQmtBNfOqNmnEEcHNUVNkfARRLHsEomeBLozMIEDqbSBTjxJyukJLdbOXdStuxeQWDFpXeLmvHvEzPntznNqgiEqZSsvBjdOSBbYzkZMNxEsHfxROaZtzLzSDLFyoKYDuwngXAowBNkOpHlyYlsTytJRZmJeFtzxmuYttQeaZFwqYFCKiLPqGcSvthRVMHWvdGfJXKrTOTpEAVKMRiTSrWRGPkAvSZTzHIkHiRhbehAnpKeFTyXjWkJAKcrrMvLjbYoUzFExRbVISkJkPXSppkJOkdVwzQMkvEUOvEYAzTMjAFucsrSTZqUwCsknxePQCEQvXNpZAQxoOhMYiGzpTQUhyEmQCYkvsHKkShDqakQvseWqpiXQaSYcEAPgYeaHrGefMLZhQuzwansEEMXEoaYRurZNMbLAaEMPKUOcPRwqINdWGalTahVkELNoyfVHtAOAvAglvUNNaogEMTDmqstqnPKRoeltFeALCTmMffWFHRfYqKRIWfLxdMpYDZaihbLtPsSzGhsTbuUHArEcqPXxXfdAaTFVUHonCdqGbVnsPRPIxMkcZTVvwxBSBeONirBwPiWFlSTdgIliaBLJWWHoQCOtMuyRAeUsjNKwZVKvLDzuuTwmsMoukBPUycdnskmQAIYIzVeQKKDumHofZtPSptTXCiCDtsyzEUxZSVBrULwWmfUXTpUgOOEhSNdSNYblJQTSPqzjPuVtGchZpLasjZMoCRxfMpnRHTMHqMdLZFWzhrCIGSWCztCnwuPtrWISJAKvRdkuXEvuwcPoZnaVdggFAgxnUdhIUTCVKZXCbTIAYklfsCZJVvvCryFhRsHUapYLmlOHjiTmEetyRDeBuvzvHTbbIyVrbngeYVyKmCbCSaebAYrcpULiHHyMcIwbjtRkDuOWftxgInoEQvczLsuSHnSEnVBssUcYWXtgHqNkwrDoNwnmvCHqNtYgDPGdffnrUklBbNnGKjkismdNgMamWYpnwhMeiLghoXNKLULKZgHMwAPoBWaKlPYAebfPhrzihHIUZuARdZeqvxlstrGPcJnuEjpkxLOWRsCEwsKRpyTbEaBStwNrBvlTJLHTjenJskKskMlTaGXFnxRhumvlXqJGiWBDevBucGrTIgVATocoZADvcfsDlIWjDYtiXwcrkxOlExFroXGJOutiDNehqLiJcihIyRthXcSxHddKosCijwNzCxffNliJQvdFDkWaigOfcboOGdtZyHZEmqWrrwLrFQiaHONfmZmPsOycacmGeMuQWJgnBrBBMRDINuNnSGlmGrLypFwFZRwRAUpBZhNLSHEWhHJHmJdpmcOjXZpPKmloePiZxjHsbtAxHZbWfaGmGCghYpgqHlxyrVbaYPrUyzLPVWxHWLMYNyvsATZKpAqdKQKoXQkFyOadijLXDMoynYUGwQlwDDnMVuFmeCxijvlhDGIVpZAgMcyYzvTMYvhvCURaBAdQoiXCjWZEactOxjjjsKpPwEMvUHPBBIPTgKPXDbMLPliNFGmlRkgciayqbBcEyQXQJQEZwyFIUnAzxajgUSXldithxmWeavnaTFKyldILvWnvGdAXUZMmCVNCjDKujOspVlfQgkWTxAcfEMLVsHJgNZHzmvuHoXcgBIuNICstGhcRHBaOfHpPKdgQkVkRYUKAjfWozGAcnFhDggphDMytTSeQBPZoLD");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, "awPRPzwLYNZWSToclBoCHEjVtqYtLfibaUPzvrFJOoGWhrgmxNJbteNdrGweTEBIqgkUrKnOrzFiHIststRmPINZcpDctITaWUoCTnbMFKpjeeyDniFwnMqTTXTckkchpqUpYmJJRcwhJgzmiQIwgoQciaWuuzMOWwQcgOdINrOstmmskSqtfdheXjoDQWzPAfNVHiZdDafHKdZWUhzMTxeKkjeOwRatGDRMRZObRLLTlwVVMhmsiLzRaXNqCtnEScBFRIvxizUoXJmOpfmElKArkWjSIXOZprSSNzuQsAHctWRkTbugLUJhPgdMnRYRgtYWiSHIkYQjiyhHhuWMkOdwrtwLLyKNvJNDXMKeBttRpdyEZsZRErwuEoDGPQuUCjjkjxbeoyGobQfyyxFOoAegCqThdmePwpyXezqDaGcjAcFQYVYacBcbbmdDxaydPmuCYOSBJthKWTIitIsTcvKvHnffOCtEdheHmpbQjNlDiUFILLHnmxllnSCbklmglYDyiWJpUnRRsnFCwVWUYcYvcVqWVjHlKjxNnquVIrkJJmDvWgYijyRUCeYAXIpTbNajQJQukKFueHJbkQrvbCJnhvxrKvsGXLZXLGGuQVVuHFPcjYGrNjKwpBIdLeGnGJwwMJPTdmXjLbBubJRzuQAoqoBCfsfCjrcAkebQYMKTjEFukdCbTUcJyAohvyKtYoXzJsfdNNqLfYrmwOyQZZpYeHnxseNSAegplXofsjaWIqPNzXXhYLESDZCnvcmMvrBLLGAEpBLBlJKRMoBpQAuEQUhdxnWRyIgBPleZAYFUwjCdKTotPSyCObLysaDPmGSiRApNDCaNZhEYsLmflGXRNYEBgkKIqdGcoKHjGrBYNeOjRESpRhNdmlMwzQrYMukYZLDUMbUxWylUFSxSmDGweCHJsNsnsZUtJWQADGWJrbSJprGhGfXxkMagHBtQVEhmFymbZMBKZqajpGpYsxZjUemIRoTJJPhIeQcMnADahzwFSZknIgqIjHRCIkDUgjBEKwkYvaYCfsHhyOvVHwthBOBDjoDnOOfVPgaoGoidGCBGXdvAwFnAYMFvFlIWdwYcAzMmWNVCqWnUWQVRUpcAFSVztmQNvQndYzhWhvQjUtQQPmOZxauSJExAaCmLjVhDPNsKKfCpBpoXOEOBJMKlGetawdedRpVcurhWNZyBrydRJaJhAluqBaPKKYEYQWMsldyiHUggOruXFNeUEZGcAFjpwXvStCheCAKrwUmHHtdsucnRzdcowiBdpSBxdHEMthlmrXNmspPCjLbEfRpNJuIrCZbHdUmCwvseYEqbNIUMJoyKLZuafTiwTkUaFPEhDUkZYcpbwRKMRZnWqLtTcWzmEfkneIMDgxVUxXHtkKkXVjURjGNdXDpzqueLntcxcVYlLUmrYjkignnhDFbtgHHbNOggFwjhVTLCwtFHxZBuidatovfSrvQcrcpCsIuoaFYlJuewKHHXSzgayHeYjvWVxNYpXgNLCApXqRONCcbNyHmdBbFePfsSlaUeSJpFVlPXhuEGRmXxHcFATPOhAafPtZEdetXjVuwlFzdrHgCdgHRkNAEOSgSHLWOCBkXgOSdrrjVXOGpdaSNeRIpbxfzAdHFqNCcKkSvREKszakyhIeOpRJmqefrXTzIuVLEJazPooYeRjzYhnpzrtZBBKBfhFLtjDBXawFXQkmMdlABWtihHWXiOcVQyWPuuVBGNKifWJxaRmwhTKCeReXBpIpyEYUKJGUPOHRrfMdpUQfUJRMRdrYRdvdyKuCKzMgpIaYjCATeKDIgtSaqTpAvYsJqgNqddQWGZxkNCtyuWGVgKkGoCkmOmrpXVlkdHJsShAwEAerxAeSstBXsaiAdSDZRsCFQIXXVAsUMjDeWMqWFRutRHoGRUYMzyEQhWflRLtIpcLFtgbKdLaVSFSpUjYzBXCYqkqHQOHLIkOYffMsEqqAvrjPbCuwHbGuigHzfeHhRvoDTYIfKvOaDRDkCDhpIoZISBzHdnYVFrPBtuwWTbwAaBIKchcaHMquwHILrLWPlCZcqWcoxMkWcqPqsHAfHpgzzCvhmtNSKpfmMDuNIrwsALQsXmyLScXeQSLAklabPpIXFoDEHCEELMtqncQxnrazpanPysJgFlzFhwJvoKPkOUrEnxIZBVDosBCaYENlNyXdKZxzyePoHuCXJvDhlXBiXgmvGvRApwniHihFFpHfgxaLmEHwenMCXMLHHgQxQgsZKaVoWRWeYpjCActzqlLdqvoyDujZsjrkkDATfXtgmUszJZwjbYtoGdrjGKHiHshgPvNjAPQXaNdQUVBuaVlbpfcBEcJPbDfWesLPJIuvpNXtIgwLWnLiWDDVGlwGPKudTYCnLQBcxPQdCrutAjfoIVGhptZkCiPCmwRrtPxUmELWyoQLHnuGSNlbauWEQlDwriZrZPfbVSjdGIfCXjBxECAAuAvJsiMqpZGlHQpApzuPmiPOksymwtVZbvyoQNNzivrXIgjXeUYtRyoMEGjfFzqrBLiIaeWODnXFVwXdLjjFvanWWErZyPZKeQAWPbEmFYheHIgxWUMROJHAiKOeIUmFAbOQAKROBeGgIHDxxyeGQhXMYzJfhqXYKEPYCWLozmPCpvkUbpsHSzzNqsOmOBCamOMWjtHiZYFHNSunyeMHSIAMwNNXKfQqGBGNKdkwnxCqPCwxUgUAPNReIzFBcJkcdHDeRSkKYOtHqgSvHEbduWyElSxOXuJGPjkCVpWIkHZIklXXixUHhTOdytowqNoXnhqWRBhjkJxRRZUBzKJBibAuackTtQsNsZoBGJrGKZeoqGQiNzUEgjPHHcEydSmXMunNapoLNQJAutGbNloIoXAooZmaekNXJzpUyhAjkQiJsnTbiHEPkmZDaCsMKNlbgOkeJdFEVcVEWHSWitBqCydrrvlHvAuNJtJiACwHRDfsSdijTVZLxXlnpHhVtjgmJjJhewVOSOXUXChsIPzqtXyHAyNzcOXaIHYXACCXvOXxupVNtXkyBiKMTVPVcFpcjzwWljQUWHovuNakGCWRmcyuqxIrBfulfABANOftxXNAEpaWzogTdozJcLghRtrOZrNHEiltJZALvffPqRfnbdRAGaTJTnsyqVjcknhKBbwKaYPHyFDZcAztsWUJbqtgUfhhzunUiLvsNlzRHjqZruAkKeonIzwewdjZxqKQMzSmLwYvHIbvVnqFWYYUmwnTvctTVzCcLjDLOWCtYwCShvIVLuzNZpiMtvqmWbEqsjrvYrlYswXNNBMIGlBjnsjjSULVNSEbVDigywfmddikGamkoyxXGQOTTTxfjXbRExHhsinEYbOMqgqDVRDmDfADrplZaeVwHlJeQquBXXRBxEUXqkNcwVotrihKefSlPxreSOwdxOrSdZuOdTDPoZvscolnYBWwsmrmZuDyqTyIjZgWsMweTqRqkmHdMlrtflrKXSFUbMKVqiJFTWwlHwtkHOpRyRLZJGxPSBmKkJDXPJAHSwimlsqzWROpKPdKdaORXvZpTxjcwXMWvktUmBAXgGkIMYUeQYNxvJPKfFAWtrLkzfpGmqPcifvauBvYXbzGIcykAVqrZzPEgkpfKyKrARWZUJzCOCdPmlRgkhKMArnXUlaslTflUGLHlKRBJEJNAIyhbVGwFrJVSOnuPWdGyVOVVTLznFFrKWOaPNKUdvAmITTTnhdKosKvobUlLOQzRMdEDypyOZPtVUXaSyOvfaDNkWeIfCqMpaLEMEuOVeRwuavCGNTbJCHVvvaEyAKyrQDvwXjGmNncxVaUXIAdFsrpeddrHZbGYwftcftFScRdOcQQeJfeCWbwUmUcsPFOiqSbYuNuGKfGqQVaKYpxtHyzKoZnaNFlnVpAhZDZgmytXTUOozQHgVfdGyIBxSzuSxKspaoqQulvzCpaVzShZNBzecrTxGMqKTwIUwzADvTqyJkIgGzluuJcvXRwsGpgaEbpMBAviuyCYJGSehxApLMETPWQMNsvyjBbaVbFqsdGRbbGHrKpmYklWpxjUvEXUPdAGQYbTIbiFKCeanUIUqmxAldarbkioHVfLyfkHapqKMSgYkcuUtLOHyAWYQYRCXREhBZutfXmtvCWiPHRQegixpOVSqPjzeyEGUdwYCueILEMFRNGzovelkQxFGIXJjhxHHjGmpEvQEDOkTqTbOqxYTIWaRjTMPGwJHWyDLRzxKyoEeZxcpnZITAmIJDvSLKDVeZuVMDZoCfrPyZgxFMOXCGiIHHsBtiSYMtbxOIVlPkibJYOSJHsnMlzxFXvkyTyHhhxHXBuxPxzQwpTlSoMGAwXVPjBSDLEplzyWsrwPKGijlPErMNOAZGtObFrdNPvPgcWGlbvTuauehvItEArsinomOUWQTNunFoQQtLsgvcnzoDBehSvwNuGVdIYtLFPswuHLFJuHWgvKeaEihmBKeOiPCnHQZogPCAOvXXmwcMsSaiIsWyCDvEZazSRMwLGvZaSWqvRWlmiQjxfcdbdlocQFdIhDIaBAMvJJYhYSiFIgdGHYiVmGGKxeVkzPvTtYLcqaTDhxzYmBuAlygBuoFtbBAPLyuXDnbhnURbfZnAJFbawnCASLGFtrhgGXofGeqLhVJIymqCewzHpVLhOYVWDqkrfosvflJvXAMkedwPbQbcpGLmTJlALxVWwsTQwCwgluOPCfJIfMuWjpLfJObautkJPcRQfncZyvxdBuzwrkCZWuugriawwRpikjbntVhIyFZeahxtPhkdmRMNzXicNFlmlklYDzzssiIxuiiENzAqKJccGTlmOONSetpoqLVtKtENrUhLYXreajKhrCwPmhzQHrGlbkYINfdGdKHXoMTUtvVCqmVrRPqZcQYxeDzTTgEMeMNGQGVvIWpEdAhCivOgJmTFwELlzsFjuzgIZmeIltTWyAhYvbVbpNNQwHjuWZyfXlYyHZRtWtpqxJQNLAfGBDaRSQbPIoQPURmHEZXNbFtRBlSRZejCjIKwLBeJEhFWlNgbst");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, "AHgOFKAIloyKUfNZtSwWeMTGYanLubbRgNBiiqIeNvSQNpbotXBYIbHCVneNnMryCNbSOyyYHnwhgomXoyWwsxfIdtQgmpXBVTejRLrIemSljwokoiIZrKAgWzOdhBxJhSZdxnnHcUMMSSrCrhoosGZypFbkloWfHcvQfvhGLeSwMQezxhTjHvMxfvsJhTSCZBBwrgkfBQDaARuSekaDOyJKPtUHHcwsCKShTyxnPLYPdXbFrMLdmtPmAuKaUIJsUcNOFvcaLpqOybtDXWxlmedlGOfPUTLWkLGsQLrZINJuDQFtMDKppfrUDDTwcRtpJLTzOgOsnXppQyTQoziABuwXZMfkKklSozxYxowVMYwfMcnflSiYngWBWyvLMVbNJFOdoWWjmNoRHxIAvsMfNqMKJYXFrPNNDFXzCnYQQgSGuRDzCPGzLrZUfNJpeVCtAFgwIKyZyAgoyunSYLHRtpfOGPExQuxeQaWdpNpnQHZPTWWizdtFQgHUeNQpXsDBACCjVpLBwEJZqbudqJwERCiSTAFWVbpjTrngrkFHyXvqPjNQIEPnqalvKVTvFzIvhHfESbzlqnwAyxUSFQSsIxXUuwyKOXDvcFlqyXtVIvUBGItkMRJwMpYajcfJHcDJqDhiLdslVPLJMPwXDFtaKcCoaqYrOKaGHluVuXDMCkfewfjCxMYiCgtmZtgcTGOFzLDYomARGRYexrfpJKgGJyDJNkkljAGqSTcfNsyKedUpDBjThUpnFTrgXDixgRPMeTUtyGdVAjblicobIgccqYBHBvtJexMtATlmIzyBgnyjkpFHvRimQeYuXRbliWhLSHZaAVqunbHGwOFAKHrGATqXsJMStabRReKnIHsWzMfTrWSYqxYfcIAmtFKiBUlrkkSDfDsZyzzdjJTOIeykRojFvsjPiDiuctsGzkJQqfQgHgonGXSrkufCBqAGgcXBgGSyTmJLFiemwbqZdGRgqCOMYGJKTKeqmBudiUUijdDLlDzHBtdnETFNfEwuSDQRgkUJYjtBEpOgAEmNpKQOTbPVJlJqpmwxWytqJzayRDzooAjPKberAmluFlAmGPPUMhPAGEYnqihdxfvTUVeTOUoNMSePnxfhGAjjRZZcLQFbnBJQlGcQkBpNefUNbepoKthHQBfDTXdBzpqVOyWOEGuGUZfrSisQgOhIguNZrFSUSpILKyOghQVkodtjOlZeaaJjqpIvPViqfHpfCftvIbpSADcTJmtXNCoAQuDRCJQuLoHJMFIJewdJvCNWOREYNhnONzYTsJMVHHrLylovxHuucaJVncIsUvWjRnSgjumqKBejFnapAFWOwVFAezysWESRHmXqsZvMdFcCZouNzaJnPOEJBPrQjGQAmYEyXMfEqYYslLUymAfIVJEPDYprzRZgVxtfWmaiouehRFGIPFVbPMYCIMLfZDqqJVUPGOIGIyXRrSKGkQOauwZHLnXJTfQwqwCoBURkfJwzoRGGGiLupPnKcHNmLKIIMGCAeMUPqKTnIOWsJdnmWJSaQJspzITPAhUBGwfJNYJFUOMRJXlVWsoKuqcBgDUathTQxyaiYMSlfZoNkprMgImPkZkSXjvzgznXZquOVCbMTHNwHJITdpOEsyxmPjYIxlaHwOfipaclXFtNQvKUmVDDtLhXyMirPUMCPlqeJIGXqZuvlQpJjjipRnzkNnKvTXnjLrEDCEWIeESyUTVuXqvJfoemtbBmKfougVNBiBxoSmiXDqXtNCTIPffljPmfEenitUDxhokZfCuRmhTQuFfZXTeSVSHLvuicuiwhQaPGnhsckDLDFaOMVLhSQeSvBFGSkpHMSXxdUxzaGcRcojZLrYocbWRtkweyGuePmVajKwVpijFPGRAuxiXdyXmehScbOVSCahDlUNCnAjYtXqaqVezLUbzzhWchpqaxlwrNLlwFxYakcqCUXKuzvkjItUqVlTKoegquaHWyhEJqhUViHBSiSeLJNJhnCWVhrRAWCfiMMHVYuaEibYmeRhdIkZWCOwbOIIrbcXaDAUrBHLgyUyTVDYJxJsnjtcgDDCFypzOLJVAZNbpvSbtyeSbHMloIqfVYJOZMwMMEXdLmjUeZfqCuJhOQYYpfspCVHzqSjdnNNbFZIJUEbGudoSgtvEQmmtvBqQktEOaTFtSgpiTgQrkqKqVmuqMKfGdPSoGxXXBtgOmUTfEtAwKgtROdiYfAfOIBcCZuOuLfjqdZgvwNVzYrRhhxLgdRljVbmxnGkgkjepQQdYxCyRYwjfSRiWdaEikcMOgtbSlJovhIxyQuAwQwbshxNTeTazsYehGdTAvxorOpkHOYMPYzywiNKysMgbSJTievJlxvnuduUgVtruurLnPXjkTotpqmuJYxAkqIBkNxHawGSxwMnzDIBwdaqhBZpKkJsqKZMMkoPqAZClmtEZsMyyAhtwMOJNlAZRvLpeTVocuKCwRiCHUfdicrQPVdyYycceiCbUhgPHJGVEqIHmmSwlZLuEVdjHzZztCKSWStyEgoNFdtaHLxpXmKSPesJZSRzHroXYDkQHSuIeSFylvnfnTEtxWPnYUEIAAjgKEsIywJAAtoxcYOXmNdmrnYsMgkbpFeSUOqRKDhYpbCIdwlzZacPqHkijrpBOtQxocrGGPvpkWzlwTIAkUhCjmgAdpAqoxiwlcBIIacuAUHiDVlPJEPPyclpYiIshFGgFlpBtDcpzSbavCgDVnhrUJvIvDzkZTzPMWOgEZULscrtfAmKkUQiKzzPfRSfVhEXKaUmWvYzABuHvoWrmnfioxKGTjEQjbFiRQvFPThYEhsIoaLJTQHHanTxxjvDYXiQPAXCsjcdVnHkChfphdOOZkvjFnVmIWJdZSxcDTxTGPkMcWuxBQprqLsREBmlxgqFgDUgiigJuOtxziSkiERTmHqYSCzbRbHeumHSfgvWoboDtEQNUxjneENtrwZGJCgRDvafdFNFBydvYicnwZmFPjzXncZyvWbKmoDTNweCShrfzXyWmfQqUdECAPGlcBgxYlZxduPzQFachgqxlYkPAPqrTuEgGxakEbmwSJtvPHqLvuCKFCccVPesvLwffQWeEKdbKjVnTSSjWojtYLmgPiqicNVoXywkHOPJCezaPDhrXMiAdNuzkszolnUNQiwJyqGDljsytctcduRmnBkikrqntxJqpJaugsSZukWtIZBJJfjlErnxsOaDYmANCMsqhzUCsGisuuwKUPfjGfeOivFJWUJIZMTEIQTaQUzWUBwaciwgrSWxFxVlNbTHCgYTfHmeeAxFkXhgokhOOOBcVdhNaLlmnuLGQuRogUfsPkGBSAeDkadXgjFFGhGEVASlvcMdeaTTNDKLmIUMWzRUGLKimQKcClFVRRHytVgtWUvojeDRBnSLRTsSAQVGaxAegSwrinHhWLeRJTVsGuybLvJkjPtyWCovaLKpRDtnvZMdomLuMGvIvkGNeppmPwVYGVhrnjItMhCQybZuhwucmNwOZyylXVGdjlmFDueMGqUvkadpAHrOQQHlOOoSEthOrldRmFixTxSeCxyKXLeRkxgfJPyqYpSwyeHqxWdfjNjTwSmjzdEMftSOZBmTzDGFetKmsMRLxIVtKYeEBcTGDpthmWNvJPWAUueZcAUVMHzYanshAEKOiICwTceVTRWDltrzVhGJLOOwgIGBxJcBbCtfCEzdFXWCAENRIihhGljzuVPlgkfxzuWGvmzZzHWZmJDraRkqAJjEDaskncShyqQpviJXlvjesQGJBywmRThRhZOAYdiJsMgdQJtzUmtWdZXMnEAHiQsRJvrUcpfnkuBKgutYZbRWExGUQBELJIlUkQcCkDTUGxYDrynmKBaoFPsMlcauMeOjQTPmVzWABsXIjllPUUyvyFImvEqgOVPOrJBgqAVgkgtufMmARzqpJLCGdLNFUdwNGDvRlFmyJKbNbTCwCERzHnyBVhtDOxFKhOuYszzyDoUqDtbiDbTNSTUFzKnlyFuLXUBWXgsvyeDMrQVlhTmCsQvWkbLzSwlxkCWLdbsEmVmnqwphpYNZrkPImQzQmPVozbUdYgMZdjOnOhGygXbpTjiLPsGMnkNaIUJEQoxOmTXNfCmZiVFpOwJVcjxQfZfswosyqwstxHiIYpUwHQEiHkDWiNJvlhbAFXMftwFUMPcfTzmiYDhNfcocZZNdHTvRkiiIlYYOyZzeQJpxkBWkDWZcfHIewhFxtlPoKkhOXHVuCjfYhVSzLIbtXIzHjBoeldwKldSKnGulGcKukQPKEIsZruRbVePqVYgwNeXIGkvzVgCFFTNJZZZGoamlJeHMPuAPwKHsyIYFvzYSXviXILYyJRwVtaVjEgSRNdxZSJFpCwoqTBOMZRpkyTuhkMJBWaCZgNYPuXbqjSPqjEwKPjalpNHPuYUKQHQytIHyAYJbgCMpBioHSRBrclmEIcjGjLUCCqREgikZxUKrbaBBdamCmRPLQRBdpquiVQOBnUrQGrfCDiyUCjYkKiUozPlZVlayxeOFMFPlXRQpIEvmOJdEivydRThdWoTnZKdDIcJSDejGcedzSRIAwzDJxCRhbfrcdnSMOXDdsRfjejdAMVpwvGMGMzRgKbZDHdktczjpyCLPNigadIVHgeQHVUmcxxlMjMACYdWHznAlTTffHAxaMVyWmIJnLYdJwZrAxEpTcvkuogsFnlKAyRqOKBrFIhYdNeucJzHhXtcNHGJtDZzBHFyOuoPHuvoobjmfdYxhiDBUbASdHNCmWYEmQHjMCNotHvcNeEAvEtDSdXMpHRfqErpfxkcdBEPcvLQvUsFaLYiZNappGZUyZjGQHuxgUdkynOuxuFpiiOaitRVFOkYuieZpCAWOJHMADHyYwjHnyGgpMTRHqaAiZRsUATzJBpXNUnTVyxbunKwUEErHjnqdjKYIiUHeKClKxoYXAQQeGaCFWTwwgUKypGJkNmoiCjtDYFTOTWqBnigNUsTLbAzMdVaXjYggFfhiibAPqsmQ");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, generateRandomString(5001));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, generateRandomString(5023));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, generateRandomString(5005));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);
    }

    @Test
    void fullDescriptionValidationPattern() {

        String exceptedErrorValidationMassage = "The full description should not contain anything" +
                " but a word, numbers and punctuation marks, also cannot start with number or punctuation mark.";
        checkErrorValidationMassage("", fullDescriptionInputErrorMassage);

        fillFieldsAddForm(car, image);

        sendPutTextKeyInInputField(fullDescriptionInput, "csoCpvwdpNsd}qn@rbIcL");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, "d^tQPommnNDqzoUK#mwklMDOb$wyoIxgD&H%((in+cFqZIJV&pR(uYUEHSQrlEKAdVcBPHD&iD@oBAHwXpf+AZILHQcZJxuWTZDkC_Ai*bfUrSCiiVfAvuyIn)YNb^SkrJt(");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, "_x$r)K_nycpPAUQucqT%dqMhoqA}C&km#RDvPkr$jI+gLuUSiW(AVBpmSyiKtfDaTBg@&MssIrYYA+qH(noA_+_p}ODYXiNZT$nvJKtVUEpTeKfSOqGc(ob)iG*C+kFGKNluXwhuXt}J_lJ+#FOl^bSLAKVAMqdow&Xmg)#SmfEeIOT}laJmCE)_Ns+YaKw)*JmSWgU%DiUn_VyASC}}l@uAdl_&aqgSVlfLS@yk+D%*J*MDcQIDg(iFcd%FxuJ(hHNBC#(@ccAWXXh@SRYujflRuE&(FmYycn+Fjz^MyECAosaJwIVOIAmotzGhztIBuH%w#v&GQ)IMylWZoBnjHaAkfic&(OzkeoVs*vtfRRHnGF#kIx+VLgiOzr+otmMOjhlxzzxZT(iS^PsD}xv@SLpWb^%*rysLe@uch^+ukfQPbJ%)IOJ))E+##CFkf_oPG+CAg_Lf*LG}uKv&U^M#iVJkdxsYlVNTpXA@EHf$z$^J^tGrkHhOoqy@JX}b%C&o+}K$UfwySJdT$WGrKeDm(Yz@srAKgWyis}m^E*#_%}DJTn@%oT_aq(@%^^VjDhTrP");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, generateRandomStringWithSpecialChars(21));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, generateRandomStringWithSpecialChars(132));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, generateRandomStringWithSpecialChars(577));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);
    }

    @Test
    void checkValidationImage() {

        String exceptedErrorValidationMassage = "Car image have invalid file type.";
        checkErrorValidationMassage("", imageInputErrorMassage);

        fillFieldsAddForm(car, image);

        changeByInvalidImageFileAddForm(text);
        checkFieldValidation(exceptedErrorValidationMassage, imageInputErrorMassage);

        changeImageFileAddForm(image);
        checkErrorValidationMassage("", imageInputErrorMassage);
    }

    @Test
    void addValidCar(){

        addAndCheckCar(car, image);
    }

    private static void addAndCheckCar(Car addCar, File imageFile) {

        fillFieldsAddForm(addCar, imageFile);
        pageNavigation.clickOnElement(submitCarButton);

        threadSleep1Seconds();
        threadSleep1Seconds();
        threadSleep1Seconds();

        checkAddedCar(addCar);
        deleteCar(addCar);
    }

    private static void checkAddedCar(Car addedCar){

        driver.get(homePageURL);
        pageNavigation.scrollDown();

        WebElement carCardTitle = driver.findElement(xpath(format("//*[@id=\"car-brand-model-%s-%s\"]",
                addedCar.getBrand(), addedCar.getModel())));
        String exceptedCarCardTitle =
                format("%s %s", toTitleCase(addedCar.getBrand().getStringValue()), addedCar.getModel());
        checkElementInnerText(exceptedCarCardTitle, carCardTitle);

        WebElement carCardShortDescription = driver.findElement(xpath(format("//*[@id=\"car-short-description-%s-%s\"]",
                addedCar.getBrand(), addedCar.getModel())));
        checkElementInnerText(addedCar.getShortDescription(), carCardShortDescription);

        pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                addedCar.getBrand(), addedCar.getModel()))));

        threadSleep1Seconds();
        threadSleep1Seconds();

        checkElementInnerText(exceptedCarCardTitle, detailsBrandModel);
        checkElementInnerText("Body type : " + toTitleCase(addedCar.getCarBodyTypes().getStringValue()),
                detailsBodyType);
        checkElementInnerText("Transmission type : " + toTitleCase(addedCar.getTransmissionBoxTypes().getStringValue()),
                detailsTransmissionType);
        checkElementInnerText("Engine capacity : " + addedCar.getEngineCapacity() + " liter inline",
                detailsEngineCapacity);
        checkElementInnerText("Production year : " + addedCar.getYear(), detailsProduceYear);
        checkElementInnerText(addedCar.getFullDescription(), detailsFullDescription);

        addedCar.getAdditionalOptions().forEach((option) -> {

            checkElementInnerText(option, driver.findElement(xpath(format("//*[@id=\"car-option-%s\"]", option))));
        });

        pageNavigation.scrollUpDown();
    }

    private static void fillFieldsAddForm(Car addedCar, File imageFile) {

        Select brandSelect = new Select(brandInput);
        Select carBodyTypeSelect = new Select(carBodyTypeInput);

        sendPutTextKeyInInputField(modelInput, addedCar.getModel());
        brandSelect.selectByValue(addedCar.getBrand().getStringValue());
        carBodyTypeSelect.selectByValue(addedCar.getCarBodyTypes().getStringValue());

        WebElement transmissionBoxTypeInput = driver.findElement(xpath(format("//*[@id=\"%s\"]",
                addedCar.getTransmissionBoxTypes().getStringValue())));
        pageNavigation.clickOnElement(transmissionBoxTypeInput);

        sendPutTextKeyInInputField(yearInput, Short.toString(addedCar.getYear()));
        sendPutTextKeyInInputField(engineCapacityInput, Double.toString(addedCar.getEngineCapacity()));
        sendPutTextKeyInInputField(shortDescriptionInput, addedCar.getShortDescription());
        sendPutTextKeyInInputField(fullDescriptionInput, addedCar.getFullDescription());

        WebElement addOptionsInput = driver.findElement(xpath("//*[@id=\"additional-options-input\"]"));
        addedCar.getAdditionalOptions().forEach(op -> {
            wait.until(visibilityOf(addOptionsInput)).sendKeys(op);
            wait.until(visibilityOf(addOptionsInput)).sendKeys(chord(ENTER));
        });

       uploadImageFileAddForm(imageFile);
    }

    private static void changeImageFileAddForm(File imageFile) {

        pageNavigation.clickOnElement(changeImageButton);
        imageInput.sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(applyImageUploadButton);
    }

    private static void changeByInvalidImageFileAddForm(File imageFile) {

        pageNavigation.clickOnElement(changeImageButton);
        imageInput.sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(imageModalComeBackButton);
    }

    private static void uploadImageFileAddForm(File imageFile) {

        pageNavigation.clickOnElement(uploadImageButton);
        imageInput.sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(applyImageUploadButton);
    }

    private static void sendPutTextKeyInInputField(WebElement input, String key){

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
        pageNavigation.hoverOnElement(driver.findElement(xpath(format("//*[@id=\"car-image-%s-%s\"]",
                deleteCar.getBrand(), deleteCar.getModel()))));
        pageNavigation.clickOnElement(driver.findElement(xpath(format("//*[@id=\"delete-car-%s-%s-button\"]",
                deleteCar.getBrand(), deleteCar.getModel()))));

        wait.until(visibilityOfElementLocated(xpath("//*[@id=\"confirm-delete-button\"]"))).click();
    }
}
