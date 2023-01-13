package selenium;

import lombok.extern.slf4j.Slf4j;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.utils.selenium.PageNavigation;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

import static com.utils.selenium.ElementsUtils.*;
import static com.utils.selenium.SeleniumTestsUtils.*;

import java.io.File;
import java.time.Year;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.openqa.selenium.Keys.*;
import static com.utils.selenium.URLUtils.getAddCarPageURL;
import static com.utils.spring.StringUtils.generateRandomString;
import static com.utils.spring.StringUtils.generateRandomStringWithSpecialChars;

@Slf4j
@TestPropertySource(locations = "classpath:application.properties")
@Profile("test")
public class AddCarPage {

    private static File image;

    private static File text;

    private static PageNavigation pageNavigation;

    private static WebDriver driver;
    
    private static DataFactory dataFactory;

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

    @FindBy(id = "change-image-button")
    private static WebElement changeImageButton;

    @FindBy(id = "image-input")
    private static WebElement imageInput;

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

    @BeforeAll
    static void beforeAll() {

        image = new File("src/test/resources/images/CatherineMCLAREN267912502.png");
        text = new File("src/test/resources/files/text.txt");

        openWindow();

        driver = getDriver();
        pageNavigation = getPageNavigation();
        dataFactory = getDataFactory();

        loginAdmin();
    }


    public AddCarPage() {
        PageFactory.initElements(driver, this);
    }

    @BeforeEach
    void setUp()  {

        driver.get(getAddCarPageURL());
    }

    @AfterAll
    static void afterAll() {

        logout();
        closeWindow();
    }

    @Test
    void trySubmitCarWithEmptyFields(){

        String exceptedErrorValidationMassage = "Model is required.";

        pageNavigation.clickOnElement(submitCarButton);

        checkFormErrorModalCarInfo();
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationRequired() {

        String exceptedErrorValidationMassage = "Model is required.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        prepareAddForm();

        modelInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationMinLength() {

        String exceptedErrorValidationMassage = "Model must be greater than 2.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        prepareAddForm();

        sendPutTextKeyInInputField(modelInput, "s");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "f");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "t");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "y");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
        
        sendPutTextKeyInInputField(modelInput, dataFactory.getRandomText(1));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, dataFactory.getRandomText(1));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, dataFactory.getRandomText(1));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationMaxLength() {

        String exceptedErrorValidationMassage = "Model must be less than 40.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        prepareAddForm();

        sendPutTextKeyInInputField(modelInput, "XfCfnMoLnRgJSUftcdbdkJdDLqYhXKKnAujHcxNDG");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "fwzGmTPmqiCKzewfEWzGPmZCuosNZOeERhhJafrcyWEPplrUjLybCFVMUEXPIpafMHiZBqfrpnubHuijRkJYC");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, "rrXlYiBpsqPhYlOMDFiOdYeCFMVSRVWeOESlbQNWrpcFUZhZCni");
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, dataFactory.getRandomText(41));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, dataFactory.getRandomText(85));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);

        sendPutTextKeyInInputField(modelInput, dataFactory.getRandomText(51));
        checkFieldValidation(exceptedErrorValidationMassage, modelInputErrorMassage);
    }

    @Test
    void modelValidationPattern() {

        String exceptedErrorValidationMassage = "Model must not contain anything other than letters," +
                " also contains only numbers or start with number.";
        checkErrorValidationMassage("", modelInputErrorMassage);

        prepareAddForm();

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

        prepareAddForm();

        yearInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationMinValue() {

        String exceptedErrorValidationMassage = "Produce year must be greater than 1920.";
        checkErrorValidationMassage("", yearInputErrorMassage);

        prepareAddForm();

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

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberUpTo(1919)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberUpTo(1919)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberUpTo(1919)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberUpTo(1919)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationMaxValue() {

        int year = Year.now().getValue() + 1;
        String exceptedErrorValidationMassage = format("Produce year must be less than %d.", year);
        checkErrorValidationMassage("", yearInputErrorMassage);

        prepareAddForm();

        sendPutTextKeyInInputField(yearInput, Integer.toString(year));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "3442");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "2344");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, "3928");
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberBetween(year, MAX_VALUE)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberBetween(year, MAX_VALUE)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberBetween(year, MAX_VALUE)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);

        sendPutTextKeyInInputField(yearInput, Integer.toString(dataFactory.getNumberBetween(year, MAX_VALUE)));
        checkFieldValidation(exceptedErrorValidationMassage, yearInputErrorMassage);
    }

    @Test
    void productionYearValidationDoubleNumber() {

        String exceptedErrorValidationMassage = "Produce year must be positive and not fractional number.";
        checkErrorValidationMassage("", yearInputErrorMassage);

        prepareAddForm();

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

        prepareAddForm();

        engineCapacityInput.sendKeys(chord(CONTROL, "a", DELETE));
        checkRequiredValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void engineCapacityValidationMinValue() {

        String exceptedErrorValidationMassage = "Engine capacity must be positive number or 0.";
        checkErrorValidationMassage("", engineCapacityInputErrorMassage);

        prepareAddForm();

        sendPutTextKeyInInputField(engineCapacityInput, "-0.1");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "-50");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, "-0.2");
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, Integer.toString(dataFactory.getNumberBetween(-10_000, -1)));
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, Integer.toString(dataFactory.getNumberBetween(-10_000, -1)));
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, Integer.toString(dataFactory.getNumberBetween(-10_000, -1)));
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, Integer.toString(dataFactory.getNumberBetween(-10_000, -1)));
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);

        sendPutTextKeyInInputField(engineCapacityInput, Integer.toString(dataFactory.getNumberBetween(-10_000, -1)));
        checkFieldValidation(exceptedErrorValidationMassage, engineCapacityInputErrorMassage);
    }

    @Test
    void engineCapacityValidationMaxValue() {

        String exceptedErrorValidationMassage = "Engine capacity must be less than 15,0.";
        checkErrorValidationMassage("", engineCapacityInputErrorMassage);

        prepareAddForm();

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

        prepareAddForm();

        sendPutTextKeyInInputField(shortDescriptionInput, "oOgcYySOQkexghvjCixQyzTywvuZvfoxtApWwHLdqWNYqsUciDqCfRIeJgwapZVlXquQKoxvJVOpygnsQaxZfRZMTOWxqZKUjAAbZgKPBrahrowonquCSyWHqxOBBhpEWVVeMHgckaAIdxnZQpzCUrvYyZpycRFRjnLwxLTzisNitEGSwzSPOvZGlSjhGUtyiDiNJBQORDXYVflylsxLknTKSZWnTnPFdUSsRMJMKIeVJexkJLYsnpFPHtZnKfuRukLHHXlTMeiiwWTrTOBWynYBPJceKSCdDiQzPbTBOWZhxhAwwuwqTrnhmIicEBzpsSbAWjaaEWneSlzeGZwBWyDTuQdksOcETBjTQVEfOmdNroAJdoBxpcLfZPCVhNfONdXqDZyLKIupXOLcqrnwHSVBsuikTugfsBLjzWhMaoziCIcTEEsqiHHRQEtyEYjgadLVSUdvHJoIyERjsYrubNRjZBvFmCLGZTbhYioamKXgtRLdVMpTfjjiTmaSSICkrpUKziHZTZYjHKrCeDbGBHryYyqhpsCvcsZjkPwNBHIomVEaYKTCEzwJQYQnjCKURKGIeQAloeHMBuOLYUgUWevHIyFQqVstEAgnOJahGZzUwMfBFdXMVMwgyWauvacTooDIEANBRWAeXgvaHSNLQfCyIusFwSSEMjYsvxCffjDedmBRIGaHeVSrAFOSHHBFvuGxjzPKtdSDwUBcZViSSGUBJBNxNUnUwnBhiLQSSQioKoQHQRNdJSSXswPaUohTCrmWAIoYOnDjaeIRTtoDHyvoXAuoNKUvSTndFdbWTipxecNeAEUtsLDGsvKrIYvCGgLnHhwKQPgPqSlaWvwbfJNKFiJUolYexgsPjVsWDbOxRnWWYaOgXNEVlziQjUQxpvZazMatXzmqVdwhajOTpAJgEYZVkZRIpsgoRYmpSTtEbqbTILrAKEOKtVKzHJYcZXdnQeRdqpBBlBBnEpByqjduYxmYDepmApmqAoVvh");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, "BdxRJStcybUScdkgkiqlzhkoviVoDXvQDFouQQEeMCTMVzUxiMbgcwnCKQReVTNsSWzdJhaYbuSoglQxnhSJceTqZgZxCCAarpClUZbkQrMlHUDMOFOYvfEkNSJqfgorkvCxWmWDdgRItSNNfeWaclrgDdtVEUvbOxSmufJDqzYKymWyLNdleEtQinGGFvqAxXKRJUkqqMHXuoLaGrQmLNMBoJRfUyhSLzzzIqVnvRGCkXtqJZkYAnXRSJdbgPGYoOYpHTCiCXXFeiAxbEelypxVQzEXWmIZtqeANyctIoCGmiaAPIFLuzchvMsyxfzpVPbqTpzUuaNSGzkfNngwzqqQOhgqrnnZBDdZFdxlGaKVXIpRyGdTdqVplYOtHPapeKpanCiktZAMTdmLwjuADwVJCznDhSqKEptrkRCSMkbtTrsipTGGlGgxQDKCmzGwjhjsvJEGfJqXzIBYDaOFpYBKYlkIPIFQwmOkEmIwsbNiihahcjvvpmnoKCUBeGaMoMcREMhgPoAvmmfzoIhBQIvKbWEwxPgybLWKMgxKQefFZxiFDFlKmjXCZnvWtARzGZckdbcgmIZUUeOgGXJuhCmwzyfzhJIGyjWGbwtmfWmFCqXHiwoQKWprUcVoAGvKQEBQYiHkyWzKtrpZjRVFqaQxoabnhFqIURrvHAaZodaBXaOqDAgiKwUnwzSNoIQlSdgwyvDQwpGpsGkWlQXMcYilPlJWpNoPvdpqfmJaeVbovzeYDFzusTybGxObcRXOLbZeghtcvLdVFjzpOYTPPYjVgVptzdjhPDhrLrWHLVmoZxpcXhZiTpQRtnqvsOlTxEVTaTMXgbgRdwpGbhQZMtAgvvjGSegMzuHTRKPNUqNIptTmIpTjjQnQCVGIWEUkJtmSMHFCqjnFjDgHNaTBxSrGHesTnnkGHazRcAllDXTesAuuXxYvxiKGqvzKbOFDhlQgRKkNmvTsttHOsBHUvSAKDenlRIKykrtnepHdmnNjtKwxUPcZvCAszcRAuKB");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, "ZpBIwRIHHxhocgwfoJAZqaZjdcspIskhtzcAufVEHAdgQxiqcZYGphEMaXcETiHQCzEdzrkVyLgjkeMTDUyUhAadpEsLnXonGWFtiMdxRWnAKebdNUVJnjQfKuTRdxxLHtVXHfqsWNZIEPzwtLuDRdmQSbIWkVPpFrYPejgTOFSaNtSifrOLmdhKecNiGwfRQNIVBhFhUKeoIXtahUKrpkQsOHLFcypfVVTMHyVGmFWJWSEquROKxLnWkRbzWgGPLiuxVQTCVFArUOQQRYTwdHvQmpZHshObmuygvLwEcPBhXoKjFvtNmFDDVGeNMJYjMJvZwrgRdXNZQMsYeRUScCEcLcHQClwhhQVIiwJDVLrvhcWRetNzbgGKTSvZxhoIkoFrSdJguDFchKJxAiJMeJBHAZpmhwxpjTaXiWhryIUXuvNIijVGdKtirIWFxFmphaJZDwzsHxZaGjIqOtoFyhYIznQKcbMBeoMdRNqTGjxDyXOuUKDyaGFxcTRhGbtbjNmARectFnJTiYOEidgvihryFtDbcOIzpGUmVgPOSGnZAqzFjnxEhimkhTTtdGuQYBTKwFDhZeioyfsEavYalXhXsQrNYXwcvuuUeBIzfHwzeHLDoZhXSnyFaYfAXNYHfcbDjthSyzzYzXQLSFJdjTcmMpzFvqGWqlcVEDgQLOhuuFQykNXuRuyKXXNCCjXuQGzmIRrqdcsLLOLuaJgormQWiNRYDHtCauuCmiBkUDzvBNqdlPZNObaOvnSxGdCHooqfkVwjdqzydJMhVIqhyjfHQlEPHztUMjJQvhSXUuHrfJmBoLpIKlYVeCvsJfTJvJBlpcaMaUswYHgzwwMsyoVZayyzOKeerMXbWcOCnyNTkYvvnZUuMzqaQBdYnZUPRotDUizSlEducMcHhEUfmQChykzmRJNlPSxEIrZFsygLybKRnAZUcdJbMPilkxiMZoDPgyoLEDArYebZABUimAKNZTOhcXSlplTdtSyMsUgYw");
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, dataFactory.getRandomText(1001));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, dataFactory.getRandomText(1023));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(shortDescriptionInput, dataFactory.getRandomText(1005));
        checkFieldValidation(exceptedErrorValidationMassage, shortDescriptionInputErrorMassage);
    }

    @Test
    void shortDescriptionValidationPattern() {

        String exceptedErrorValidationMassage = "The short description should not contain anything" +
                " but a word, numbers and punctuation marks, also cannot start with number or punctuation mark.";
        checkErrorValidationMassage("", shortDescriptionInputErrorMassage);

        prepareAddForm();

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

        prepareAddForm();

        sendPutTextKeyInInputField(fullDescriptionInput, "cfHDQlXotpdvVhlgzcdoGjGTtnPXQXfErqAUxIqBrfWZCibEJbymKqcxziovVFTfcxPYzideSIvDmHuyVlPdRDEjYHAteJGsOThtpptUvsjmsImsiTZpmkkHZOnbxrpKLGhHhTrIQQfTiBkyaVBzSfFvHXmiTVmbGGrbsOFJbFrzIaDfojydNFHtaxYHjyjTKOJyiHhZxbKeFWDMyUsuQNCcTuQMNWszLZwbtbGaslwIPcJjJNWmXUItLfOObPHoPEKeIMswYgKKtOOwgUAephLTYYlAlXBQmqWyrDReDJkdpscwzczbYqJGIcnIXxGlkmekAtRCCkXKSdrbrTdzNGWZaURzlKUuqlrSunexLSnVreqfFmGoggSxedMVSFfbYfZDUjsrosfftcCPBLutjetvEzEjIepCqjtNQXuUFenZkEvXBnKPrcWmJaXlpMzTmWLmrmckSAYBNFDHABtixhLTlRnZrojTRAwNuMbGPbtQciTEuNKcvAZjJGsNgrpugjNKUBDSedOOFUJcErnBNRltmCtkkJBuHoFxkLqvsPuybengIBeGqptypHbOPhNpvDUxxMEZhhvdhEFqmcrdSrELedllGLJIvBLYMgyMyjmIsOFCZywLRxiuIqnWgKvmFKicXeuNcwsTspQTUBLHQIgatVsOpCjjJwxDpoHvoTxFOAtxKVbqSNXsNxtPOnLLBmlLHIawNgkWMUqkbbMZyGGYlssQKfHDrUOdudlltuQJXiwHxbxgQewpilsAlVqMQOxQFYxNgRBmBmYXetsrCDbzLWuoCKjXpNPeChBfKBGPEVYLLLKSzciNMcXGWoEOQLyMfhWjhzBfUbtCSrHsIoyWQRiJcBPouOFCdnQdBGFypPykIWXVyaVaaCrRiNfcYDaGwBPkgYOqGlrUstcDwfRzqGZRoDggkIkHWDMdIjXjnpkkQYtBdVhMMBxIdojRTOZbzvbJaIQLyoEXePJSWDyfNJonBtOkEvgvZEZTLazmGtKTdEDsSIZfWmPSvQTyzbhagXCuwMBwxIrilQuHWYiSGPBenTTnutUMIXnWFXsAFXhTrsXeOheSDWcYxiHQIBgoumMuIIwtKQnkssKhwZXvAnOdmKcvKKSCznqviseBQXwDTgkmqalFxEJEcWDqSkPkybaeAzROshrMVNncbrWhOkNtAXWlZYYWnxwPpwMnmBnrShPopgTVcPETHvQWyLYNFZdqxCSMnYlEfKWGZgsFRnKcAQmadZDSIwpyZjjuOzbKqEUCAfuMynIvddtDOXewDiYsYjKIisWSEBhlVkFwNDrfmHIfqZidFkIFWoUtpjCoBSxvJqEJBJuAGXaaHmunTYYfZYVBOBgEtFPKjDvLcZqOFguVtAVlNRBlLwNLzZNVBSpKuHetnSXvqPzcgQZumiwNTzolIEavbKVupNUCDhtIUDqZRPmIhXBPVbSLimTSSxTzzcEMsIjPJLUtkvSZkIKNCnyKWycAlfPbJKFGBQQRMtaSkRiclBEkzwXfZyKKPvOFJsdQoYlQhldxzoYMkexzpClefcvvPVIAbFZsZncxFvLTpoqJODNEbWSUvjJcLIExWFSihIsfIDmHJPXOwKbMophopkwjTmVoLpSMDNsYstgOccjITkigrpykFRLJBUimfSPcFCKwOFiBmRYGnAahQiBNRzppqJBpxVYEZpTJOWfxVgULesvesPbeHCSsJkGguqtPriWPIZMmNnjmubxudYbGMJeWJZSScCTTtPjoTRkcViEJbsRlcqwsAUktsGBRiSFQMGGYxDPqYsZoIVBYVThCbbfLmwSpPegdQsrkmuQkMlvJElrjrJmplPYlJJSAQQuBfWEOgUSXNnuWJgHCxKpDiUoiSmMeTvJVYTdbNFVfeunYfhjNFvAeUJGSkyJIzRjXlhFuNKHQJgOEhLeihTnBftrHJbrSdSThrtXfvWsriiTLwWWLldGfODLsxHrHSgbmLAXQeCHVzACuOQUOzkuPpUoZcoTgxWmvoblwMYvOEcooloRmaxVYKHQuOkkcfnghQLBTvpwVammOJZGhIUvENLlnEJUNohgZLgpOvLbKJIwgZdjNmPJOIoVWhcyZbvSgALdlPJdzkcHHNtTNcdkqBhNnyovZIFBbzKPzjDgmbMTMptwfQuhwjooafQXLLEZxcWztfzMdSDhxvdvrKSushqGrKUOUMxtYwEqKSKVDBfOkUnAKZBGYWQbHgbVPEnllLrVyhPQcpNOSfGzqeysByjNJdIRnvwLNalCZlLmfBLbEWcNMiHbdEUMKWcXsYkCaHkegMwMwMTGsEdQWvrEvfLNQFbTtWjZpzjMWQQvXjKIDWHXhNCpTeMAuRUggxJvXRZaYHmQGnaDqLOCMNxDpgBGBslMfrZnPZdWnriEVnGAAtfXNpZnDtOLtFHYddyQjKKyRQReuFkUqnNxqxlcWrmYhAaWLAIPEwpoNoidtKCIvoHQMHqgskLGUJUuEInglULGuSQycSEQQZVTFHhOermTdIbfzrhEFcNcQGfWebeiRlKmPsuQgMpdmHVTkralnMGOXxOBDNEftMkOgWVTWIXyClIIaqxxuXqhoYRNncOsaEEwLoMwrDUUXyKvAVLsleGFEdcZpaxSUJjhwcoonUJXNHEoLBVmfhVAqfpxLLVXeLcncJMClEEBVFhAbDpWAKNdOJulCIKxcQfDNAQnmIITWogpJUCmVdDFXAXpkzXGhsVYVtdsRiDkjimstzROUUIWsRFRhzmvtEkEuVmizJYgVrcJYftRLvTiffLxxsqYGvmbHiBGrzNHnynfWKyOsTreLLLmpuoLWzUYHyyuebKXyKoBAcUIhxSpAbgyhpsEREmuDkKrRyCPVvwWOsaOUzzuTMddcExSyqidTSovTatpHnNAoUUdOekbLPdRvOukPsfbpUoQmefXJhdGlcpgbyQkqLLcXBqBebauEgpryWVMqvyrOhSBDiIDbbIxFGWuFelWRofclliDmzRWCxXivtnVpqYZAGJiItNAbVmYnuveHhWMgBRDjpxUHzNcDXFqHmOSMIuhXlMmFlpgvveOGTwuivszELqQMVljeSMLNpeVMeUIZOFfFSiSKOPfOPbLSdtChzNTqNCGcMRkWTzuNylFATLYTJCXFSdILDTmJkxTxExemyhOtnTxSuJZJjJAVDaYJbxjTklsFhacStdABuCATuUcYLEhkFeeJpxTWQOzKvlJJTbiKMeVcBAAIqXzWoZcMPsttQMHUmDZVTaWZTdvEhgMCkoRSFzWFluQccuyvjjudExIeghMzEdsxiWCxSDJWtAgmQmtBNfOqNmnEEcHNUVNkfARRLHsEomeBLozMIEDqbSBTjxJyukJLdbOXdStuxeQWDFpXeLmvHvEzPntznNqgiEqZSsvBjdOSBbYzkZMNxEsHfxROaZtzLzSDLFyoKYDuwngXAowBNkOpHlyYlsTytJRZmJeFtzxmuYttQeaZFwqYFCKiLPqGcSvthRVMHWvdGfJXKrTOTpEAVKMRiTSrWRGPkAvSZTzHIkHiRhbehAnpKeFTyXjWkJAKcrrMvLjbYoUzFExRbVISkJkPXSppkJOkdVwzQMkvEUOvEYAzTMjAFucsrSTZqUwCsknxePQCEQvXNpZAQxoOhMYiGzpTQUhyEmQCYkvsHKkShDqakQvseWqpiXQaSYcEAPgYeaHrGefMLZhQuzwansEEMXEoaYRurZNMbLAaEMPKUOcPRwqINdWGalTahVkELNoyfVHtAOAvAglvUNNaogEMTDmqstqnPKRoeltFeALCTmMffWFHRfYqKRIWfLxdMpYDZaihbLtPsSzGhsTbuUHArEcqPXxXfdAaTFVUHonCdqGbVnsPRPIxMkcZTVvwxBSBeONirBwPiWFlSTdgIliaBLJWWHoQCOtMuyRAeUsjNKwZVKvLDzuuTwmsMoukBPUycdnskmQAIYIzVeQKKDumHofZtPSptTXCiCDtsyzEUxZSVBrULwWmfUXTpUgOOEhSNdSNYblJQTSPqzjPuVtGchZpLasjZMoCRxfMpnRHTMHqMdLZFWzhrCIGSWCztCnwuPtrWISJAKvRdkuXEvuwcPoZnaVdggFAgxnUdhIUTCVKZXCbTIAYklfsCZJVvvCryFhRsHUapYLmlOHjiTmEetyRDeBuvzvHTbbIyVrbngeYVyKmCbCSaebAYrcpULiHHyMcIwbjtRkDuOWftxgInoEQvczLsuSHnSEnVBssUcYWXtgHqNkwrDoNwnmvCHqNtYgDPGdffnrUklBbNnGKjkismdNgMamWYpnwhMeiLghoXNKLULKZgHMwAPoBWaKlPYAebfPhrzihHIUZuARdZeqvxlstrGPcJnuEjpkxLOWRsCEwsKRpyTbEaBStwNrBvlTJLHTjenJskKskMlTaGXFnxRhumvlXqJGiWBDevBucGrTIgVATocoZADvcfsDlIWjDYtiXwcrkxOlExFroXGJOutiDNehqLiJcihIyRthXcSxHddKosCijwNzCxffNliJQvdFDkWaigOfcboOGdtZyHZEmqWrrwLrFQiaHONfmZmPsOycacmGeMuQWJgnBrBBMRDINuNnSGlmGrLypFwFZRwRAUpBZhNLSHEWhHJHmJdpmcOjXZpPKmloePiZxjHsbtAxHZbWfaGmGCghYpgqHlxyrVbaYPrUyzLPVWxHWLMYNyvsATZKpAqdKQKoXQkFyOadijLXDMoynYUGwQlwDDnMVuFmeCxijvlhDGIVpZAgMcyYzvTMYvhvCURaBAdQoiXCjWZEactOxjjjsKpPwEMvUHPBBIPTgKPXDbMLPliNFGmlRkgciayqbBcEyQXQJQEZwyFIUnAzxajgUSXldithxmWeavnaTFKyldILvWnvGdAXUZMmCVNCjDKujOspVlfQgkWTxAcfEMLVsHJgNZHzmvuHoXcgBIuNICstGhcRHBaOfHpPKdgQkVkRYUKAjfWozGAcnFhDggphDMytTSeQBPZoLD");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, "awPRPzwLYNZWSToclBoCHEjVtqYtLfibaUPzvrFJOoGWhrgmxNJbteNdrGweTEBIqgkUrKnOrzFiHIststRmPINZcpDctITaWUoCTnbMFKpjeeyDniFwnMqTTXTckkchpqUpYmJJRcwhJgzmiQIwgoQciaWuuzMOWwQcgOdINrOstmmskSqtfdheXjoDQWzPAfNVHiZdDafHKdZWUhzMTxeKkjeOwRatGDRMRZObRLLTlwVVMhmsiLzRaXNqCtnEScBFRIvxizUoXJmOpfmElKArkWjSIXOZprSSNzuQsAHctWRkTbugLUJhPgdMnRYRgtYWiSHIkYQjiyhHhuWMkOdwrtwLLyKNvJNDXMKeBttRpdyEZsZRErwuEoDGPQuUCjjkjxbeoyGobQfyyxFOoAegCqThdmePwpyXezqDaGcjAcFQYVYacBcbbmdDxaydPmuCYOSBJthKWTIitIsTcvKvHnffOCtEdheHmpbQjNlDiUFILLHnmxllnSCbklmglYDyiWJpUnRRsnFCwVWUYcYvcVqWVjHlKjxNnquVIrkJJmDvWgYijyRUCeYAXIpTbNajQJQukKFueHJbkQrvbCJnhvxrKvsGXLZXLGGuQVVuHFPcjYGrNjKwpBIdLeGnGJwwMJPTdmXjLbBubJRzuQAoqoBCfsfCjrcAkebQYMKTjEFukdCbTUcJyAohvyKtYoXzJsfdNNqLfYrmwOyQZZpYeHnxseNSAegplXofsjaWIqPNzXXhYLESDZCnvcmMvrBLLGAEpBLBlJKRMoBpQAuEQUhdxnWRyIgBPleZAYFUwjCdKTotPSyCObLysaDPmGSiRApNDCaNZhEYsLmflGXRNYEBgkKIqdGcoKHjGrBYNeOjRESpRhNdmlMwzQrYMukYZLDUMbUxWylUFSxSmDGweCHJsNsnsZUtJWQADGWJrbSJprGhGfXxkMagHBtQVEhmFymbZMBKZqajpGpYsxZjUemIRoTJJPhIeQcMnADahzwFSZknIgqIjHRCIkDUgjBEKwkYvaYCfsHhyOvVHwthBOBDjoDnOOfVPgaoGoidGCBGXdvAwFnAYMFvFlIWdwYcAzMmWNVCqWnUWQVRUpcAFSVztmQNvQndYzhWhvQjUtQQPmOZxauSJExAaCmLjVhDPNsKKfCpBpoXOEOBJMKlGetawdedRpVcurhWNZyBrydRJaJhAluqBaPKKYEYQWMsldyiHUggOruXFNeUEZGcAFjpwXvStCheCAKrwUmHHtdsucnRzdcowiBdpSBxdHEMthlmrXNmspPCjLbEfRpNJuIrCZbHdUmCwvseYEqbNIUMJoyKLZuafTiwTkUaFPEhDUkZYcpbwRKMRZnWqLtTcWzmEfkneIMDgxVUxXHtkKkXVjURjGNdXDpzqueLntcxcVYlLUmrYjkignnhDFbtgHHbNOggFwjhVTLCwtFHxZBuidatovfSrvQcrcpCsIuoaFYlJuewKHHXSzgayHeYjvWVxNYpXgNLCApXqRONCcbNyHmdBbFePfsSlaUeSJpFVlPXhuEGRmXxHcFATPOhAafPtZEdetXjVuwlFzdrHgCdgHRkNAEOSgSHLWOCBkXgOSdrrjVXOGpdaSNeRIpbxfzAdHFqNCcKkSvREKszakyhIeOpRJmqefrXTzIuVLEJazPooYeRjzYhnpzrtZBBKBfhFLtjDBXawFXQkmMdlABWtihHWXiOcVQyWPuuVBGNKifWJxaRmwhTKCeReXBpIpyEYUKJGUPOHRrfMdpUQfUJRMRdrYRdvdyKuCKzMgpIaYjCATeKDIgtSaqTpAvYsJqgNqddQWGZxkNCtyuWGVgKkGoCkmOmrpXVlkdHJsShAwEAerxAeSstBXsaiAdSDZRsCFQIXXVAsUMjDeWMqWFRutRHoGRUYMzyEQhWflRLtIpcLFtgbKdLaVSFSpUjYzBXCYqkqHQOHLIkOYffMsEqqAvrjPbCuwHbGuigHzfeHhRvoDTYIfKvOaDRDkCDhpIoZISBzHdnYVFrPBtuwWTbwAaBIKchcaHMquwHILrLWPlCZcqWcoxMkWcqPqsHAfHpgzzCvhmtNSKpfmMDuNIrwsALQsXmyLScXeQSLAklabPpIXFoDEHCEELMtqncQxnrazpanPysJgFlzFhwJvoKPkOUrEnxIZBVDosBCaYENlNyXdKZxzyePoHuCXJvDhlXBiXgmvGvRApwniHihFFpHfgxaLmEHwenMCXMLHHgQxQgsZKaVoWRWeYpjCActzqlLdqvoyDujZsjrkkDATfXtgmUszJZwjbYtoGdrjGKHiHshgPvNjAPQXaNdQUVBuaVlbpfcBEcJPbDfWesLPJIuvpNXtIgwLWnLiWDDVGlwGPKudTYCnLQBcxPQdCrutAjfoIVGhptZkCiPCmwRrtPxUmELWyoQLHnuGSNlbauWEQlDwriZrZPfbVSjdGIfCXjBxECAAuAvJsiMqpZGlHQpApzuPmiPOksymwtVZbvyoQNNzivrXIgjXeUYtRyoMEGjfFzqrBLiIaeWODnXFVwXdLjjFvanWWErZyPZKeQAWPbEmFYheHIgxWUMROJHAiKOeIUmFAbOQAKROBeGgIHDxxyeGQhXMYzJfhqXYKEPYCWLozmPCpvkUbpsHSzzNqsOmOBCamOMWjtHiZYFHNSunyeMHSIAMwNNXKfQqGBGNKdkwnxCqPCwxUgUAPNReIzFBcJkcdHDeRSkKYOtHqgSvHEbduWyElSxOXuJGPjkCVpWIkHZIklXXixUHhTOdytowqNoXnhqWRBhjkJxRRZUBzKJBibAuackTtQsNsZoBGJrGKZeoqGQiNzUEgjPHHcEydSmXMunNapoLNQJAutGbNloIoXAooZmaekNXJzpUyhAjkQiJsnTbiHEPkmZDaCsMKNlbgOkeJdFEVcVEWHSWitBqCydrrvlHvAuNJtJiACwHRDfsSdijTVZLxXlnpHhVtjgmJjJhewVOSOXUXChsIPzqtXyHAyNzcOXaIHYXACCXvOXxupVNtXkyBiKMTVPVcFpcjzwWljQUWHovuNakGCWRmcyuqxIrBfulfABANOftxXNAEpaWzogTdozJcLghRtrOZrNHEiltJZALvffPqRfnbdRAGaTJTnsyqVjcknhKBbwKaYPHyFDZcAztsWUJbqtgUfhhzunUiLvsNlzRHjqZruAkKeonIzwewdjZxqKQMzSmLwYvHIbvVnqFWYYUmwnTvctTVzCcLjDLOWCtYwCShvIVLuzNZpiMtvqmWbEqsjrvYrlYswXNNBMIGlBjnsjjSULVNSEbVDigywfmddikGamkoyxXGQOTTTxfjXbRExHhsinEYbOMqgqDVRDmDfADrplZaeVwHlJeQquBXXRBxEUXqkNcwVotrihKefSlPxreSOwdxOrSdZuOdTDPoZvscolnYBWwsmrmZuDyqTyIjZgWsMweTqRqkmHdMlrtflrKXSFUbMKVqiJFTWwlHwtkHOpRyRLZJGxPSBmKkJDXPJAHSwimlsqzWROpKPdKdaORXvZpTxjcwXMWvktUmBAXgGkIMYUeQYNxvJPKfFAWtrLkzfpGmqPcifvauBvYXbzGIcykAVqrZzPEgkpfKyKrARWZUJzCOCdPmlRgkhKMArnXUlaslTflUGLHlKRBJEJNAIyhbVGwFrJVSOnuPWdGyVOVVTLznFFrKWOaPNKUdvAmITTTnhdKosKvobUlLOQzRMdEDypyOZPtVUXaSyOvfaDNkWeIfCqMpaLEMEuOVeRwuavCGNTbJCHVvvaEyAKyrQDvwXjGmNncxVaUXIAdFsrpeddrHZbGYwftcftFScRdOcQQeJfeCWbwUmUcsPFOiqSbYuNuGKfGqQVaKYpxtHyzKoZnaNFlnVpAhZDZgmytXTUOozQHgVfdGyIBxSzuSxKspaoqQulvzCpaVzShZNBzecrTxGMqKTwIUwzADvTqyJkIgGzluuJcvXRwsGpgaEbpMBAviuyCYJGSehxApLMETPWQMNsvyjBbaVbFqsdGRbbGHrKpmYklWpxjUvEXUPdAGQYbTIbiFKCeanUIUqmxAldarbkioHVfLyfkHapqKMSgYkcuUtLOHyAWYQYRCXREhBZutfXmtvCWiPHRQegixpOVSqPjzeyEGUdwYCueILEMFRNGzovelkQxFGIXJjhxHHjGmpEvQEDOkTqTbOqxYTIWaRjTMPGwJHWyDLRzxKyoEeZxcpnZITAmIJDvSLKDVeZuVMDZoCfrPyZgxFMOXCGiIHHsBtiSYMtbxOIVlPkibJYOSJHsnMlzxFXvkyTyHhhxHXBuxPxzQwpTlSoMGAwXVPjBSDLEplzyWsrwPKGijlPErMNOAZGtObFrdNPvPgcWGlbvTuauehvItEArsinomOUWQTNunFoQQtLsgvcnzoDBehSvwNuGVdIYtLFPswuHLFJuHWgvKeaEihmBKeOiPCnHQZogPCAOvXXmwcMsSaiIsWyCDvEZazSRMwLGvZaSWqvRWlmiQjxfcdbdlocQFdIhDIaBAMvJJYhYSiFIgdGHYiVmGGKxeVkzPvTtYLcqaTDhxzYmBuAlygBuoFtbBAPLyuXDnbhnURbfZnAJFbawnCASLGFtrhgGXofGeqLhVJIymqCewzHpVLhOYVWDqkrfosvflJvXAMkedwPbQbcpGLmTJlALxVWwsTQwCwgluOPCfJIfMuWjpLfJObautkJPcRQfncZyvxdBuzwrkCZWuugriawwRpikjbntVhIyFZeahxtPhkdmRMNzXicNFlmlklYDzzssiIxuiiENzAqKJccGTlmOONSetpoqLVtKtENrUhLYXreajKhrCwPmhzQHrGlbkYINfdGdKHXoMTUtvVCqmVrRPqZcQYxeDzTTgEMeMNGQGVvIWpEdAhCivOgJmTFwELlzsFjuzgIZmeIltTWyAhYvbVbpNNQwHjuWZyfXlYyHZRtWtpqxJQNLAfGBDaRSQbPIoQPURmHEZXNbFtRBlSRZejCjIKwLBeJEhFWlNgbst");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, "AHgOFKAIloyKUfNZtSwWeMTGYanLubbRgNBiiqIeNvSQNpbotXBYIbHCVneNnMryCNbSOyyYHnwhgomXoyWwsxfIdtQgmpXBVTejRLrIemSljwokoiIZrKAgWzOdhBxJhSZdxnnHcUMMSSrCrhoosGZypFbkloWfHcvQfvhGLeSwMQezxhTjHvMxfvsJhTSCZBBwrgkfBQDaARuSekaDOyJKPtUHHcwsCKShTyxnPLYPdXbFrMLdmtPmAuKaUIJsUcNOFvcaLpqOybtDXWxlmedlGOfPUTLWkLGsQLrZINJuDQFtMDKppfrUDDTwcRtpJLTzOgOsnXppQyTQoziABuwXZMfkKklSozxYxowVMYwfMcnflSiYngWBWyvLMVbNJFOdoWWjmNoRHxIAvsMfNqMKJYXFrPNNDFXzCnYQQgSGuRDzCPGzLrZUfNJpeVCtAFgwIKyZyAgoyunSYLHRtpfOGPExQuxeQaWdpNpnQHZPTWWizdtFQgHUeNQpXsDBACCjVpLBwEJZqbudqJwERCiSTAFWVbpjTrngrkFHyXvqPjNQIEPnqalvKVTvFzIvhHfESbzlqnwAyxUSFQSsIxXUuwyKOXDvcFlqyXtVIvUBGItkMRJwMpYajcfJHcDJqDhiLdslVPLJMPwXDFtaKcCoaqYrOKaGHluVuXDMCkfewfjCxMYiCgtmZtgcTGOFzLDYomARGRYexrfpJKgGJyDJNkkljAGqSTcfNsyKedUpDBjThUpnFTrgXDixgRPMeTUtyGdVAjblicobIgccqYBHBvtJexMtATlmIzyBgnyjkpFHvRimQeYuXRbliWhLSHZaAVqunbHGwOFAKHrGATqXsJMStabRReKnIHsWzMfTrWSYqxYfcIAmtFKiBUlrkkSDfDsZyzzdjJTOIeykRojFvsjPiDiuctsGzkJQqfQgHgonGXSrkufCBqAGgcXBgGSyTmJLFiemwbqZdGRgqCOMYGJKTKeqmBudiUUijdDLlDzHBtdnETFNfEwuSDQRgkUJYjtBEpOgAEmNpKQOTbPVJlJqpmwxWytqJzayRDzooAjPKberAmluFlAmGPPUMhPAGEYnqihdxfvTUVeTOUoNMSePnxfhGAjjRZZcLQFbnBJQlGcQkBpNefUNbepoKthHQBfDTXdBzpqVOyWOEGuGUZfrSisQgOhIguNZrFSUSpILKyOghQVkodtjOlZeaaJjqpIvPViqfHpfCftvIbpSADcTJmtXNCoAQuDRCJQuLoHJMFIJewdJvCNWOREYNhnONzYTsJMVHHrLylovxHuucaJVncIsUvWjRnSgjumqKBejFnapAFWOwVFAezysWESRHmXqsZvMdFcCZouNzaJnPOEJBPrQjGQAmYEyXMfEqYYslLUymAfIVJEPDYprzRZgVxtfWmaiouehRFGIPFVbPMYCIMLfZDqqJVUPGOIGIyXRrSKGkQOauwZHLnXJTfQwqwCoBURkfJwzoRGGGiLupPnKcHNmLKIIMGCAeMUPqKTnIOWsJdnmWJSaQJspzITPAhUBGwfJNYJFUOMRJXlVWsoKuqcBgDUathTQxyaiYMSlfZoNkprMgImPkZkSXjvzgznXZquOVCbMTHNwHJITdpOEsyxmPjYIxlaHwOfipaclXFtNQvKUmVDDtLhXyMirPUMCPlqeJIGXqZuvlQpJjjipRnzkNnKvTXnjLrEDCEWIeESyUTVuXqvJfoemtbBmKfougVNBiBxoSmiXDqXtNCTIPffljPmfEenitUDxhokZfCuRmhTQuFfZXTeSVSHLvuicuiwhQaPGnhsckDLDFaOMVLhSQeSvBFGSkpHMSXxdUxzaGcRcojZLrYocbWRtkweyGuePmVajKwVpijFPGRAuxiXdyXmehScbOVSCahDlUNCnAjYtXqaqVezLUbzzhWchpqaxlwrNLlwFxYakcqCUXKuzvkjItUqVlTKoegquaHWyhEJqhUViHBSiSeLJNJhnCWVhrRAWCfiMMHVYuaEibYmeRhdIkZWCOwbOIIrbcXaDAUrBHLgyUyTVDYJxJsnjtcgDDCFypzOLJVAZNbpvSbtyeSbHMloIqfVYJOZMwMMEXdLmjUeZfqCuJhOQYYpfspCVHzqSjdnNNbFZIJUEbGudoSgtvEQmmtvBqQktEOaTFtSgpiTgQrkqKqVmuqMKfGdPSoGxXXBtgOmUTfEtAwKgtROdiYfAfOIBcCZuOuLfjqdZgvwNVzYrRhhxLgdRljVbmxnGkgkjepQQdYxCyRYwjfSRiWdaEikcMOgtbSlJovhIxyQuAwQwbshxNTeTazsYehGdTAvxorOpkHOYMPYzywiNKysMgbSJTievJlxvnuduUgVtruurLnPXjkTotpqmuJYxAkqIBkNxHawGSxwMnzDIBwdaqhBZpKkJsqKZMMkoPqAZClmtEZsMyyAhtwMOJNlAZRvLpeTVocuKCwRiCHUfdicrQPVdyYycceiCbUhgPHJGVEqIHmmSwlZLuEVdjHzZztCKSWStyEgoNFdtaHLxpXmKSPesJZSRzHroXYDkQHSuIeSFylvnfnTEtxWPnYUEIAAjgKEsIywJAAtoxcYOXmNdmrnYsMgkbpFeSUOqRKDhYpbCIdwlzZacPqHkijrpBOtQxocrGGPvpkWzlwTIAkUhCjmgAdpAqoxiwlcBIIacuAUHiDVlPJEPPyclpYiIshFGgFlpBtDcpzSbavCgDVnhrUJvIvDzkZTzPMWOgEZULscrtfAmKkUQiKzzPfRSfVhEXKaUmWvYzABuHvoWrmnfioxKGTjEQjbFiRQvFPThYEhsIoaLJTQHHanTxxjvDYXiQPAXCsjcdVnHkChfphdOOZkvjFnVmIWJdZSxcDTxTGPkMcWuxBQprqLsREBmlxgqFgDUgiigJuOtxziSkiERTmHqYSCzbRbHeumHSfgvWoboDtEQNUxjneENtrwZGJCgRDvafdFNFBydvYicnwZmFPjzXncZyvWbKmoDTNweCShrfzXyWmfQqUdECAPGlcBgxYlZxduPzQFachgqxlYkPAPqrTuEgGxakEbmwSJtvPHqLvuCKFCccVPesvLwffQWeEKdbKjVnTSSjWojtYLmgPiqicNVoXywkHOPJCezaPDhrXMiAdNuzkszolnUNQiwJyqGDljsytctcduRmnBkikrqntxJqpJaugsSZukWtIZBJJfjlErnxsOaDYmANCMsqhzUCsGisuuwKUPfjGfeOivFJWUJIZMTEIQTaQUzWUBwaciwgrSWxFxVlNbTHCgYTfHmeeAxFkXhgokhOOOBcVdhNaLlmnuLGQuRogUfsPkGBSAeDkadXgjFFGhGEVASlvcMdeaTTNDKLmIUMWzRUGLKimQKcClFVRRHytVgtWUvojeDRBnSLRTsSAQVGaxAegSwrinHhWLeRJTVsGuybLvJkjPtyWCovaLKpRDtnvZMdomLuMGvIvkGNeppmPwVYGVhrnjItMhCQybZuhwucmNwOZyylXVGdjlmFDueMGqUvkadpAHrOQQHlOOoSEthOrldRmFixTxSeCxyKXLeRkxgfJPyqYpSwyeHqxWdfjNjTwSmjzdEMftSOZBmTzDGFetKmsMRLxIVtKYeEBcTGDpthmWNvJPWAUueZcAUVMHzYanshAEKOiICwTceVTRWDltrzVhGJLOOwgIGBxJcBbCtfCEzdFXWCAENRIihhGljzuVPlgkfxzuWGvmzZzHWZmJDraRkqAJjEDaskncShyqQpviJXlvjesQGJBywmRThRhZOAYdiJsMgdQJtzUmtWdZXMnEAHiQsRJvrUcpfnkuBKgutYZbRWExGUQBELJIlUkQcCkDTUGxYDrynmKBaoFPsMlcauMeOjQTPmVzWABsXIjllPUUyvyFImvEqgOVPOrJBgqAVgkgtufMmARzqpJLCGdLNFUdwNGDvRlFmyJKbNbTCwCERzHnyBVhtDOxFKhOuYszzyDoUqDtbiDbTNSTUFzKnlyFuLXUBWXgsvyeDMrQVlhTmCsQvWkbLzSwlxkCWLdbsEmVmnqwphpYNZrkPImQzQmPVozbUdYgMZdjOnOhGygXbpTjiLPsGMnkNaIUJEQoxOmTXNfCmZiVFpOwJVcjxQfZfswosyqwstxHiIYpUwHQEiHkDWiNJvlhbAFXMftwFUMPcfTzmiYDhNfcocZZNdHTvRkiiIlYYOyZzeQJpxkBWkDWZcfHIewhFxtlPoKkhOXHVuCjfYhVSzLIbtXIzHjBoeldwKldSKnGulGcKukQPKEIsZruRbVePqVYgwNeXIGkvzVgCFFTNJZZZGoamlJeHMPuAPwKHsyIYFvzYSXviXILYyJRwVtaVjEgSRNdxZSJFpCwoqTBOMZRpkyTuhkMJBWaCZgNYPuXbqjSPqjEwKPjalpNHPuYUKQHQytIHyAYJbgCMpBioHSRBrclmEIcjGjLUCCqREgikZxUKrbaBBdamCmRPLQRBdpquiVQOBnUrQGrfCDiyUCjYkKiUozPlZVlayxeOFMFPlXRQpIEvmOJdEivydRThdWoTnZKdDIcJSDejGcedzSRIAwzDJxCRhbfrcdnSMOXDdsRfjejdAMVpwvGMGMzRgKbZDHdktczjpyCLPNigadIVHgeQHVUmcxxlMjMACYdWHznAlTTffHAxaMVyWmIJnLYdJwZrAxEpTcvkuogsFnlKAyRqOKBrFIhYdNeucJzHhXtcNHGJtDZzBHFyOuoPHuvoobjmfdYxhiDBUbASdHNCmWYEmQHjMCNotHvcNeEAvEtDSdXMpHRfqErpfxkcdBEPcvLQvUsFaLYiZNappGZUyZjGQHuxgUdkynOuxuFpiiOaitRVFOkYuieZpCAWOJHMADHyYwjHnyGgpMTRHqaAiZRsUATzJBpXNUnTVyxbunKwUEErHjnqdjKYIiUHeKClKxoYXAQQeGaCFWTwwgUKypGJkNmoiCjtDYFTOTWqBnigNUsTLbAzMdVaXjYggFfhiibAPqsmQ");
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, dataFactory.getRandomText(5001));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, dataFactory.getRandomText(5023));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);

        sendPutTextKeyInInputField(fullDescriptionInput, dataFactory.getRandomText(5005));
        checkFieldValidation(exceptedErrorValidationMassage, fullDescriptionInputErrorMassage);
    }

    @Test
    void fullDescriptionValidationPattern() {

        String exceptedErrorValidationMassage = "The full description should not contain anything" +
                " but a word, numbers and punctuation marks, also cannot start with number or punctuation mark.";
        checkErrorValidationMassage("", fullDescriptionInputErrorMassage);

        prepareAddForm();

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

        prepareAddForm();

        changeByInvalidImageFile(text);
        checkFieldValidation(exceptedErrorValidationMassage, imageInputErrorMassage);

        uploadImageFileForm(image);
        checkErrorValidationMassage("", imageInputErrorMassage);
    }
    
    private static void prepareAddForm() {
        sendPutTextKeyInInputField(modelInput, "RX 4");
    }

    private static void uploadImageFileForm(File imageFile) {

        pageNavigation.clickOnElement(changeImageButton);
        findWebElementById("image-input").sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(applyImageUploadButton);
    }

    private static void changeByInvalidImageFile(File imageFile) {

        pageNavigation.clickOnElement("upload-image-button");
        findWebElementById("image-input").sendKeys(imageFile.getAbsolutePath());
        assertThatThrownBy(() -> uploadImageButton.isDisplayed()).isInstanceOf(NoSuchElementException.class);
        pageNavigation.clickOnElement(imageModalComeBackButton);
    }

}
