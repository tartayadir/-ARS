package selenium;

import com.implemica.model.car.entity.Car;
import com.implemica.model.car.enums.CarBodyType;
import com.implemica.model.car.enums.CarBrand;
import com.implemica.model.car.enums.TransmissionBoxType;
import com.utils.selenium.PageNavigation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.time.Year;
import java.util.List;

import static com.utils.CarsUtils.generateRandomCar;
import static com.utils.selenium.ElementsUtils.*;
import static com.utils.selenium.PageNavigation.threadSleep1Seconds;
import static com.utils.selenium.SeleniumTestsUtils.*;
import static com.utils.selenium.URLUtils.getHomePageURL;
import static java.lang.String.format;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.Keys.chord;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@TestPropertySource(locations = "classpath:application.properties")
@ActiveProfiles("test")
@Slf4j
public class SeleniumCarCatalogTest {

    private static WebDriverWait wait;

    private static PageNavigation pageNavigation;

    private static String homePageURL;

    private static WebDriver driver;

    private static File image1;

    private static File image2;


    public SeleniumCarCatalogTest() {
        PageFactory.initElements(driver, this);
    }

    @BeforeAll
    static void beforeAll() {

        image1 = new File("src/test/resources/images/MAZDARX8589601474.png");
        image2 = new File("src/test/resources/images/TOYOTACamry372501944.png");

        openWindow();

        homePageURL = getHomePageURL();
        driver = getDriver();
        pageNavigation = getPageNavigation();
        wait = getWait();

        loginAdmin();
    }

    @BeforeEach
    void setUp()  {

        driver.get(homePageURL);
    }

    @AfterAll
    static void afterAll() {

        logout();
        closeWindow();
    }

    @Test
    void checkCar() {

        short year = 2015;
        Car addCar = Car.builder().
                id(10L).
                brand(CarBrand.DAEWOO).
                model("Klass E").
                carBodyTypes(CarBodyType.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxType.ROBOTIC).
                engineCapacity(3.4).
                shortDescription("Velvety, creamy, buttery, smooth–these are the words that come to mind in a" +
                        " Mercedes-Benz E-class."
                ).
                fullDescription( "We recommend the sedan body style and advocate for the E450 trim, with its " +
                        "potent six-cylinder powertrain and standard 4Matic all-wheel-drive system. It comes " +
                        "standard with blind-spot monitoring, a fully digital dashboard, heated front seats with" +
                        " memory settings, an infotainment system with Apple CarPlay and Android Auto, and passive" +
                        " entry. To that, we'd add the optional air suspension, head-up display, heated steering" +
                        " wheel, multi-contour front seats with massage functions, and ventilated front-seat cushions."
                ).
                additionalOptions(List.of("Infotainment")).
                imageFileName("Image file 6").
                build();

        year = 2010;
        Car editCar = Car.builder().
                id(10L).
                brand(CarBrand.BENTLEY).
                model("B8").
                carBodyTypes(CarBodyType.CONVERTIBLE).
                year(year).
                transmissionBoxTypes(TransmissionBoxType.VARIATIONAL).
                engineCapacity(1.4).
                shortDescription("We tend to think of cars in either premium'' or 'volume' terms. Here though, is " +
                        "one that could comfortably fit within either definition, the eighth-generation 'B8'-series " +
                        "Volkswagen Passat. Though targeted at the medium-range Mondeo sector, it's long had an appeal" +
                        " stretching beyond, up towards the premium compact/mid-sized executive saloon and estate" +
                        " segment. Global buyers like that and the result is a worldwide favorite the Wolfsburg" +
                        " maker simply can't afford to get wrong. Hence the smarter looks, extra technology, and " +
                        "efficient returns of this classy MK8 'B8'-series model, launched in 2015. Let's check early" +
                        " versions of this design out as a used buy."
                ).
                fullDescription( "We didn't set out to build a new car', the company says, 'we set out to build a " +
                        "new Passat'. The difference is important. While other manufacturers have been persuaded " +
                        "that models of this kind should be able to drive on their door handles, Volkswagen knows" +
                        " what its customer base really needs: a car that will lower the heartbeat, rather than " +
                        "raise it. And, as with any company that really knows its market, the rewards have been" +
                        " considerable. Over 3,000 new Passats are every day sold across the world, with a new owner " +
                        "taking delivery every 29 seconds. Over 1.1 million of them leave the showrooms each year," +
                        " making this the company's best selling global model. With annual sales that amount to more " +
                        "than the entire yearly production output of BMW or Audi, it's a phenomenon. No longer simply" +
                        " an integral part of the Volkswagen range, it's now pretty much a brand in itself."
                ).
                additionalOptions(List.of("Blo", "LED", "bluetooth")).
                imageFileName("Image file 6").
                build();

        addEditAndCheckCar(addCar, image1, editCar, image2);

        short minYear = 1920;
        Car minCar = Car.builder().
                id(10L).
                brand(CarBrand.DAEWOO).
                model("ss").
                carBodyTypes(CarBodyType.SPORTS_CAR).
                year(minYear).
                transmissionBoxTypes(TransmissionBoxType.ROBOTIC).
                engineCapacity(0.1).
                shortDescription("a").
                fullDescription("a").
                additionalOptions(List.of()).
                imageFileName("Image file 6").
                build();

        short maxYear = (short) (Year.now().getValue() - 1);
        Car maxCar = Car.builder().
                id(10L).
                brand(CarBrand.DAEWOO).
                model("ssssssssssssssssssssssssssssssssssssssss").
                carBodyTypes(CarBodyType.SPORTS_CAR).
                year(maxYear).
                transmissionBoxTypes(TransmissionBoxType.ROBOTIC).
                engineCapacity(15).
                shortDescription("jtLe20LvZ6m7IayIRgoF6aq1bnsxYO2W9J7XVIG4ssYqJm3IaJGVIy7igLwOAizBVk9WhvvfxmCxhcW4otf80WmkkUf8i6g9NjuBaNBC4c3y1Qyxl82GYe99LxfQEVwazOdAwAHJY84f9BVlMlS1Bbfqk4lDpYsoc5a57MJJK8a4vdae4ib7H68D377VBpi2qLXORg1Cd0ISMp4UNJRMEC6Lz0hopqmvLokOeiQ5aEqPloXrRDBcdrywpQjYaTo4oKzQcznN7sm0INT2bbWPethICeXWP5CXr5FVDURWNJ5sCmepzHsTexWIWAD1Vvy4t21DvozmaE5JU6ARLa12Cy4X1bcaJai6JPD8OzsTiJe1m2SKtOjugyJQPoPEkuqBdXUMPu4oGFUx2n9ViU9vIe0DRYhyN8ckxH7owBovd9p8LkpiVlyT7ZFMTqtiEwM2dldlQXiRc7GXUKCrzA3jeSOujjEYzWVahO9AJCMw9HNsJqWx30OETL2J2DdFbOEQ4ZQIuxVZj5jR5Qh9iVxXffFp2IC5VwUjCSo19U6uQIGSOHdAH7qM0X6RDWxqIXupzA84vF5JJjpYJy1NG5TF9SN9ZGlVFLa0CgLnQWMhdzwm7WJKANaHe1lE82iaWZr952clyQH0e64eKpyi08Q1g1PiNOcNeIhDjlWac7lyzaVgB0Y9tN54cblFSptxFIwWZ2nkgdfF1q393TF5T1yAzy9JCwHJHCFp9o5Bc02bCPADSz2sCy9uUdyxR5VVvSaZ6DpSYadPv1uuuHJoOSv50QRbZFakw0b05ln91s5bZhMagiqp6bmuk2ZDEQFuwwcNFzHzgDomFZHQkh4ftfYRP6dsIbSdY94tQtfzUjrWyM843XCZxXjdOylhly4VDZ6jSuTePcs8pErA3GguJLdDi0OwgGmSmquTBZDIIDUFCtt1JFYsRtdil0nuG6uzeahdrIrPCILnUuB4o4l2VH7Ze3tzZjnG1Cm6PcRB5LGb").
                fullDescription("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").
                additionalOptions(List.of()).
                imageFileName("Image file 6").
                build();

        addEditAndCheckCar(maxCar, image1, minCar, image2);
        addEditAndCheckCar(minCar, image1, maxCar, image2);
        addEditAndCheckCar(maxCar, image1, maxCar, image2);
        addEditAndCheckCar(minCar, image1, minCar, image2);
    }

    @Test
    void checkRandomCar(){

        addEditAndCheckCar(generateRandomCar(), image1, generateRandomCar(), image2);
        addEditAndCheckCar(generateRandomCar(), image1, generateRandomCar(), image2);
        addEditAndCheckCar(generateRandomCar(), image1, generateRandomCar(), image2);
        addEditAndCheckCar(generateRandomCar(), image1, generateRandomCar(), image2);
        addEditAndCheckCar(generateRandomCar(), image1, generateRandomCar(), image2);
    }

    private static void addEditAndCheckCar(Car addCar, File addImageFile, Car editCar, File editImageFile) {

        addCar(addCar, addImageFile);
        editCar(addCar, editCar, editImageFile);

        deleteCar(editCar);
    }

    private static void editCar(Car currentCar, Car editCar, File editImage){

        pageNavigation.hoverOnElement(findWebElementById(format("car-card-%s-%s",
                currentCar.getBrand().toString(), currentCar.getModel())));
        pageNavigation.clickOnElement(findWebElementById(format("car-%s-%s-edit-button",
                currentCar.getBrand().toString(), currentCar.getModel())));
        fillFieldsEditForm(editCar, editImage, currentCar.getAdditionalOptions().size());

        pageNavigation.clickOnElement("submit-car-button");
        wait.until(visibilityOfElementLocated(id("logo-image")));

        checkDataCar(editCar, editImage);
    }

    private static void fillFieldsEditForm(Car editCar, File editImage, int optionsNumber) {

        wait.until(elementToBeClickable(id("model-input")));
        threadSleep1Seconds();
        threadSleep1Seconds();

        Select brandSelect = new Select(findWebElementById("brand-input"));
        Select carBodyTypeSelect = new Select(findWebElementById("car-body-type-input"));

        sendPutTextKeyInInputField("model-input", editCar.getModel());
        brandSelect.selectByValue(editCar.getBrand().toString());
        carBodyTypeSelect.selectByValue(editCar.getCarBodyTypes().toString());

        pageNavigation.clickOnElement(editCar.getTransmissionBoxTypes().getStringValue());

        sendPutTextKeyInInputField("year-input", Short.toString(editCar.getYear()));
        sendPutTextKeyInInputField("engine-capacity-input", Double.toString(editCar.getEngineCapacity()));
        sendPutTextKeyInInputField("short-description-input", editCar.getShortDescription());
        sendPutTextKeyInInputField("full-description-input", editCar.getFullDescription());

        for (int i = 0; i < optionsNumber; i++){

            pageNavigation.clickOnElement(format("option-%d-remove-button", 0));
        }

        WebElement addOptionsInput = findWebElementById("additional-options-input");
        editCar.getAdditionalOptions().forEach(op -> {
            sendPutTextKeyInInputField(addOptionsInput, op);
            wait.until(visibilityOf(addOptionsInput)).sendKeys(chord(ENTER));
        });

        pageNavigation.clickOnElement("change-image-button");
        wait.until(visibilityOf(findWebElementById("image-input"))).sendKeys(editImage.getAbsolutePath());
        pageNavigation.clickOnElement("apply-image-upload-button");
    }
}
