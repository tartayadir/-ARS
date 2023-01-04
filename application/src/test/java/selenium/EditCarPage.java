package selenium;

import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import com.utils.selenium.PageNavigation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.time.Year;
import java.util.List;

import static com.utils.selenium.ElementsUtils.*;
import static com.utils.selenium.SeleniumTestsUtils.*;
import static com.utils.spring.StringUtils.generateRandomString;
import static com.utils.spring.StringUtils.generateRandomStringWithSpecialChars;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.Keys.*;

@Slf4j
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
public class EditCarPage {

    private static PageNavigation pageNavigation;

    private static WebDriver driver;

    private static String editCarPage;

    @FindBy(id = "change-image-button")
    private static WebElement changeImageButton;

    @FindBy(id = "image-modal-come-back-button")
    private static WebElement imageModalComeBackButton;

    @FindBy(id = "model-input")
    private static WebElement modelInput;

    @FindBy(id = "year-input")
    private static WebElement yearInput;

    @FindBy(id = "engine-capacity-input")
    private static WebElement engineCapacityInput;


    @FindBy(id = "short-description-input")
    private static WebElement shortDescriptionInput;

    @FindBy(id = "full-description-input")
    private static WebElement fullDescriptionInput;

    @FindBy(id = "upload-image-button")
    private static WebElement uploadImageButton;

    @FindBy(id = "apply-image-upload-button")
    private static WebElement applyImageUploadButton;

    @FindBy(id = "submit-car-button")
    private static WebElement submitCarButton;

    @FindBy(id = "model-input-error-massage")
    private static WebElement modelInputErrorMassage;

    @FindBy(id = "year-input-error-massage")
    private static WebElement yearInputErrorMassage;

    @FindBy(id = "engine-capacity-input-error-massage")
    private static WebElement engineCapacityInputErrorMassage;

    @FindBy(id = "short-description-input-error-massage")
    private static WebElement shortDescriptionInputErrorMassage;

    @FindBy(id = "full-description-input-error-massage")
    private static WebElement fullDescriptionInputErrorMassage;

    @FindBy(id = "image-input-error-massage")
    private static WebElement imageInputErrorMassage;

    private static Car exampleCar;

    private static File image1;

    private static File image2;

    private static File text;

    public EditCarPage() {
        PageFactory.initElements(driver, this);
    }

    @BeforeAll
    static void beforeAll() {

        System.getProperty("spring.profiles.active", "test");

        image1 = new File("src/test/resources/images/modern-blue-urban-adventure-suv-vehicle-illustration_1344-205.png");
        image2 = new File("src/test/resources/images/FORD_FUSION_2020.png");
        text = new File("src/test/resources/files/text.txt");

        short year = 2013;
        exampleCar = Car.builder().
                id(10L).
                brand(CarBrands.GENESIS).
                model("RX 8").
                carBodyTypes(CarBodyTypes.PICKUP).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.AUTOMATIC).
                engineCapacity(5.4).
                shortDescription("The Mazda RX-8 is a sports car manufactured by Japanese automobile manufacturer Mazda" +
                        " between 2002 and 2012. It was first shown in 2001 at the North American International. The " +
                        "Mazda RX-8 is a sports car manufactured by Japanese automobile manufacturer Mazda between 2002" +
                        " and 2012. It was first shown in 2001 at the North American International."
                ).
                fullDescription( "Mazda introduced rotary-powered vehicles in the US in 1971, beginning with the R100" +
                        " and eventually introduced the RX-2, RX-3, RX-4, RX-5, and three generations of the RX-7" +
                        " sports car in the US and worldwide markets. However, due to the lack of conveniences and " +
                        "user-friendliness, coupled with the high price tag and declining interest in sports cars and " +
                        "coups at the time, Mazda decided to withdraw the RX-7 from most major markets except Japan. " +
                        "After 1995, Mazda suffered from a relatively undistinguished and ordinary product line in the " +
                        "US except for the MX-5 Miata.\n" +
                        "\n" +
                        "As popular interest in import tuning and performance cars resurged in the late-1990s, due in " +
                        "part to various popular cultural influences, Japanese automakers waded back into the" +
                        " performance and sports car market in the US and in worldwide markets. Endeavoring to " +
                        "rejuvenate itself around this time, partially with financial and management assistance from " +
                        "its new owner Ford, Mazda developed a new line of high-quality cars with desirable styling " +
                        "and driving dynamics superior to competitors, beginning with the Mazda6 and followed by the " +
                        "Mazda3. This paved the way for the arrival of Mazda's next-generation rotary powered sports" +
                        " car."
                ).
                additionalOptions(List.of("LED headlights", "Parking Sensors")).
                imageFileName("Image file 1").
                build();

        openWindow();

        driver = getDriver();
        pageNavigation = getPageNavigation();

        loginAdmin();

        addCar(exampleCar, image1);

        pageNavigation.clickOnElement(format("car-image-%s-%s", exampleCar.getBrand().toString(), exampleCar.getModel()));

        String detailsPrefixUrl = "http://localhost:4200/car-catalog/details/";
        long carId = Long.parseLong(driver.getCurrentUrl().substring(detailsPrefixUrl.length()));
        editCarPage = "http://localhost:4200/car-catalog/update/" + carId;
    }

    @BeforeEach
    void setUp()  {
        
        driver.get(editCarPage);
    }

    @AfterAll
    static void afterAll() {

        deleteCar(exampleCar);
        logout();

        closeWindow();
    }

    @Test
    void trySubmitCarWithEmptyFields(){

        String exceptedErrorValidationMassage = "Model is required.";

        yearInput.sendKeys(chord(CONTROL, "a", DELETE));
        modelInput.sendKeys(chord(CONTROL, "a", DELETE));

        engineCapacityInput.sendKeys(chord(CONTROL, "a", DELETE));
        shortDescriptionInput.sendKeys(chord(CONTROL, "a", DELETE));
        fullDescriptionInput.sendKeys(chord(CONTROL, "a", DELETE));

        pageNavigation.clickOnElement(submitCarButton);
        checkFormErrorModalCarInfo();
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationRequired() {

        String exceptedErrorValidationMassage = "Model is required.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        pageNavigation.moveToElement(modelInput);
        modelInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationMinLength() {

        String exceptedErrorValidationMassage = "Model must be greater than 2.";
        checkErrorValidationMassage("", modelInputErrorMassage);

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

        pageNavigation.moveToElement(yearInput);
        yearInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationMinValue() {

        String exceptedErrorValidationMassage = "Produce year must be greater than 1920.";
        checkErrorValidationMassage("", yearInputErrorMassage);

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

        pageNavigation.moveToElement(engineCapacityInput);
        engineCapacityInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void engineCapacityValidationMinValue() {

        String exceptedErrorValidationMassage = "Engine capacity must be positive number or 0.";
        checkErrorValidationMassage("", engineCapacityInputErrorMassage);

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

        sendPutTextKeyInInputField(engineCapacityInput, "15.1");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "345.32");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "33.333");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void shortDescriptionValidationMaxLength() {

        String exceptedErrorValidationMassage ="Short description must be less than 1000.";
        checkErrorValidationMassage("", shortDescriptionInputErrorMassage);

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

        changeByInvalidImageFile(text);
        checkFieldValidation(exceptedErrorValidationMassage, imageInputErrorMassage);

        changeImageFileForm(image2);
        checkErrorValidationMassage("", imageInputErrorMassage);
    }


    private static void changeImageFileForm(File imageFile) {

        pageNavigation.clickOnElement(changeImageButton);
        findWebElementById("image-input").sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(applyImageUploadButton);
    }

    private static void changeByInvalidImageFile(File imageFile) {

        pageNavigation.clickOnElement(changeImageButton);
        findWebElementById("image-input").sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(imageModalComeBackButton);
    }
}
