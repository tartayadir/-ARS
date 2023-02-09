package com.utils.selenium;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.implemica.controller.service.amazonS3.AmazonClient;
import com.implemica.model.car.entity.Car;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static com.utils.selenium.ElementsUtils.elementIsViewed;
import static com.utils.selenium.ElementsUtils.findWebElementById;
import static com.utils.selenium.PageNavigation.threadSleep1Seconds;
import static com.utils.selenium.URLUtils.getAddCarPageURL;
import static com.utils.selenium.URLUtils.getHomePageURL;
import static com.utils.spring.StringUtils.toTitleCase;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.Keys.chord;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Slf4j
public class SeleniumTestsUtils {

    private static PageNavigation pageNavigation;
    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final AmazonS3 s3client;

    static {

        s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion("us-east-1")
                .build();
    }

    public static void setFieldsSeleniumTestsUtils(WebDriver driver, WebDriverWait wait, PageNavigation pageNavigation) {

        SeleniumTestsUtils.driver = driver;
        SeleniumTestsUtils.wait = wait;
        SeleniumTestsUtils.pageNavigation = pageNavigation;
    }

    @SneakyThrows
    public static void checkDataCar(Car car, File imageFile){

        wait.until(visibilityOfElementLocated(id("cars")));

        driver.get(getHomePageURL());
        threadSleep1Seconds();
        driver.navigate().refresh();

        wait.until(visibilityOfElementLocated(id("cars")));

        String carBrandEnumValue = car.getBrand().toString();
        String carBrand = car.getBrand().getStringValue();
        String carModel = car.getModel();

        WebElement carCardTitle = ElementsUtils.findWebElementById(format("car-brand-model-%s-%s", carBrandEnumValue, carModel));
        pageNavigation.moveToElement(carCardTitle);

        String exceptedCarCardTitle = format("%s %s", carBrand, carModel);

        checkElementInnerText(exceptedCarCardTitle, carCardTitle);
        checkElementInnerText(car.getShortDescription(), ElementsUtils.findWebElementById(format("car-short-description-%s-%s", carBrandEnumValue, carModel)));

        WebElement image = findWebElementById(format("car-image-%s-%s", carBrandEnumValue, carModel));
        elementIsViewed(image);
//        checkImage(image, imageFile);

        pageNavigation.clickOnElement(format("car-image-%s-%s-a", carBrandEnumValue, carModel));

        wait.until(visibilityOfElementLocated(id("car-engine-capacity")));
        image = findWebElementById(format("car-image-%s-%s", carBrandEnumValue, carModel));
        elementIsViewed(image);
//        checkImage(image, imageFile);

        checkElementInnerText("Body type : " + car.getCarBodyTypes().getStringValue(), "car-body-type");
        checkElementInnerText("Transmission type : " + toTitleCase(car.getTransmissionBoxTypes().getStringValue()), "car-transmission-type");
        checkElementInnerText("Engine capacity : " + car.getEngineCapacity() + " liter inline",  "car-engine-capacity");
        checkElementInnerText("Production year : " + car.getYear(), "car-produce-year");
        checkElementInnerText(car.getFullDescription(), "car-full-description");
        checkElementInnerText(exceptedCarCardTitle, "car-brand-model");

        car.getAdditionalOptions().forEach((option) ->
                checkElementInnerText(option, ElementsUtils.findWebElementById(format("car-option-%s", option))));

        pageNavigation.clickOnElement("come-back-button");
    }

    @SneakyThrows
    public static void checkImage(WebElement actualImageWebElement, File expectedImage) {

        String src = actualImageWebElement.getAttribute("src");

        ImageComparisonResult result = new ImageComparison(ImageIO.read(expectedImage),
                ImageIO.read(new URL(src))).compareImages();
        assertEquals(ImageComparisonState.MATCH, result.getImageComparisonState());
    }

    public static void addCar(Car car, File imageFile){

        driver.get(getAddCarPageURL());
        wait.until(visibilityOf(ElementsUtils.findWebElementById("brand-input")));

        Select brandSelect = new Select(ElementsUtils.findWebElementById("brand-input"));
        brandSelect.selectByValue(car.getBrand().toString());

        Select carBodyTypeSelect = new Select(ElementsUtils.findWebElementById("car-body-type-input"));
        carBodyTypeSelect.selectByValue(car.getCarBodyTypes().toString());

        ElementsUtils.sendPutTextKeyInInputField("model-input", car.getModel());
        ElementsUtils.sendPutTextKeyInInputField("year-input", Short.toString(car.getYear()));
        ElementsUtils.sendPutTextKeyInInputField("engine-capacity-input", Double.toString(car.getEngineCapacity()));
        ElementsUtils.sendPutTextKeyInInputField("short-description-input", car.getShortDescription());
        ElementsUtils.sendPutTextKeyInInputField("full-description-input", car.getFullDescription());

        pageNavigation.clickOnElement(car.getTransmissionBoxTypes().getStringValue());

        WebElement addOptionsInput = ElementsUtils.findWebElementById("additional-options-input");
        car.getAdditionalOptions().forEach(op -> {
            wait.until(visibilityOf(addOptionsInput)).sendKeys(op);
            wait.until(visibilityOf(addOptionsInput)).sendKeys(chord(ENTER));
        });

        pageNavigation.clickOnElement("upload-image-button");
        wait.until(visibilityOf(ElementsUtils.findWebElementById("image-input"))).sendKeys(imageFile.getAbsolutePath());
        pageNavigation.clickOnElement("apply-image-upload-button");
        pageNavigation.clickOnElement("submit-car-button");

        checkDataCar(car, imageFile);
    }

    public static void deleteCar(Car deleteCar){

        driver.get(getHomePageURL());
        driver.navigate().refresh();

        String model = deleteCar.getModel();
        String brand = deleteCar.getBrand().toString();

        pageNavigation.hoverOnElement(ElementsUtils.findWebElementById(format("car-image-%s-%s", brand, model)));
        pageNavigation.clickOnElement(format("delete-car-%s-%s-button", brand, model));

        wait.until(visibilityOfElementLocated(id("confirm-delete-button"))).click();

        threadSleep1Seconds();
        driver.navigate().refresh();

        ElementsUtils.elementIsNotViewed(format("car-image-%s-%s", brand, model));
    }

    public static void checkFormErrorModalCarInfo(){

        try {
            Assertions.assertEquals("Incorrect car info", ElementsUtils.findWebElementById("error-car-info-header").getAttribute("innerText"));
        } catch (StaleElementReferenceException e){
            Assertions.assertEquals("Incorrect car info", ElementsUtils.findWebElementById("error-car-info-header").getAttribute("innerText"));
        }

        try {
            Assertions.assertEquals("You entered incorrect information about car. Please, check your entered date and try again.",
                    ElementsUtils.findWebElementById("error-car-info-body").getAttribute("innerText"));
        } catch (StaleElementReferenceException e){
            Assertions.assertEquals("You entered incorrect information about car. Please, check your entered date and try again.",
                    ElementsUtils.findWebElementById("error-car-info-body").getAttribute("innerText"));
        }

        pageNavigation.clickOnElement("error-car-info-ok-button");
    }

    public static void checkElementInnerText(String exceptedInnerText, WebElement element){
        assertEquals(exceptedInnerText, element.getText());
    }

    public static void checkElementInnerText(String exceptedInnerText, String elementId){
        Assertions.assertEquals(exceptedInnerText, ElementsUtils.findWebElementById(elementId).getText());
    }

    public static void checkErrorValidationMassage(String exceptedErrorMassage, WebElement errorMassageAlert){

        String actualErrorMassage = errorMassageAlert.getAttribute("innerText");
        assertEquals(exceptedErrorMassage, actualErrorMassage);
    }

    public static void checkFieldValidation(String exceptedErrorValidationMassage, WebElement errorMassageAlert) {

        checkErrorValidationMassage(exceptedErrorValidationMassage, errorMassageAlert);
        pageNavigation.clickOnElement("submit-car-button");
        checkFormErrorModalCarInfo();
        checkErrorValidationMassage(exceptedErrorValidationMassage, errorMassageAlert);
    }

    public static void checkRequiredValidation(String exceptedErrorValidationMassage, WebElement errorMassageAlert) {

        pageNavigation.clickOnElement("submit-car-button");
        checkFormErrorModalCarInfo();
        assertEquals(exceptedErrorValidationMassage, errorMassageAlert.getAttribute("innerText"));
    }

}
