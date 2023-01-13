package com.utils.selenium;

import com.implemica.model.car.entity.Car;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.Keys.chord;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static com.utils.selenium.URLUtils.getAddCarPageURL;
import static com.utils.selenium.URLUtils.getHomePageURL;
import static com.utils.spring.StringUtils.toTitleCase;

public class SeleniumTestsUtils {

    private static PageNavigation pageNavigation;
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void setFieldsSeleniumTestsUtils(WebDriver driver, WebDriverWait wait, PageNavigation pageNavigation) {

        SeleniumTestsUtils.driver = driver;
        SeleniumTestsUtils.wait = wait;
        SeleniumTestsUtils.pageNavigation = pageNavigation;
    }

    @SneakyThrows
    public static void checkDataCar(Car addedCar){

        driver.get(getHomePageURL());
        driver.navigate().refresh();

        String carBrandEnumValue = addedCar.getBrand().toString();
        String carBrand = addedCar.getBrand().getStringValue();
        String carModel = addedCar.getModel();

        wait.until(visibilityOfElementLocated(id(format("car-brand-model-%s-%s", carBrandEnumValue, carModel))));

        WebElement carCardTitle = ElementsUtils.findWebElementById(format("car-brand-model-%s-%s", carBrandEnumValue, carModel));
        pageNavigation.moveToElement(carCardTitle);

        String exceptedCarCardTitle = format("%s %s", carBrand, carModel);

        checkElementInnerText(exceptedCarCardTitle, carCardTitle);
        checkElementInnerText(addedCar.getShortDescription(), ElementsUtils.findWebElementById(format("car-short-description-%s-%s", carBrandEnumValue, carModel)));

        pageNavigation.clickOnElement(format("car-image-%s-%s", carBrandEnumValue, carModel));

        wait.until(visibilityOfElementLocated(id("car-engine-capacity")));

        checkElementInnerText("Body type : " + addedCar.getCarBodyTypes().getStringValue(), "car-body-type");
        checkElementInnerText("Transmission type : " + toTitleCase(addedCar.getTransmissionBoxTypes().getStringValue()), "car-transmission-type");
        checkElementInnerText("Engine capacity : " + addedCar.getEngineCapacity() + " liter inline",  "car-engine-capacity");
        checkElementInnerText("Production year : " + addedCar.getYear(), "car-produce-year");
        checkElementInnerText(addedCar.getFullDescription(), "car-full-description");
        checkElementInnerText(exceptedCarCardTitle, "car-brand-model");

        addedCar.getAdditionalOptions().forEach((option) ->
                checkElementInnerText(option, ElementsUtils.findWebElementById(format("car-option-%s", option))));

        pageNavigation.clickOnElement("come-back-button");
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

        checkDataCar(car);
    }

    public static void deleteCar(Car deleteCar){

        driver.get(getHomePageURL());

        String model = deleteCar.getModel();
        String brand = deleteCar.getBrand().toString();

        pageNavigation.hoverOnElement(ElementsUtils.findWebElementById(format("car-image-%s-%s", brand, model)));
        pageNavigation.clickOnElement(format("delete-car-%s-%s-button", brand, model));

        wait.until(visibilityOfElementLocated(id("confirm-delete-button"))).click();

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