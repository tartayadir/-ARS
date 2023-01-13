package selenium;

import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import com.utils.selenium.PageNavigation;
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
import java.util.List;

import static com.utils.CarsUtils.generateRandomCar;
import static com.utils.selenium.ElementsUtils.*;
import static com.utils.selenium.SeleniumTestsUtils.*;
import static com.utils.selenium.URLUtils.getHomePageURL;
import static java.lang.String.format;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.Keys.chord;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@TestPropertySource(locations = "classpath:application.properties")
@ActiveProfiles("test")
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
                brand(CarBrands.AUDI).
                model("Klass E").
                carBodyTypes(CarBodyTypes.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.ROBOTIC).
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
                brand(CarBrands.BENTLEY).
                model("B8").
                carBodyTypes(CarBodyTypes.CONVERTIBLE).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.VARIATIONAL).
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
