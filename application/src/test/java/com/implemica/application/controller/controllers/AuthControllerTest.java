package com.implemica.application.controller.controllers;

import lombok.SneakyThrows;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.utils.spring.AuthTestUtils.*;
import static com.utils.spring.StringUtils.generateRandomString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@ActiveProfiles("prod")
public class AuthControllerTest {

    private static MockMvc mockMvc;

    private static DataFactory dataFactory;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        AuthControllerTest.mockMvc = mockMvc;
    }

    @BeforeAll
    static void beforeAll() {

        dataFactory = new DataFactory();
    }

    @Test
    void successfulAuthorization() {

        String firstAdminPassword = getFirstAdminPassword();

        checkSuccessfulAuthorization(getFirstAdminUsername(), getFirstAdminPassword());

        checkSuccessfulAuthorization("Admin", firstAdminPassword);
        checkSuccessfulAuthorization("aDmin", firstAdminPassword);
        checkSuccessfulAuthorization("adMin", firstAdminPassword);
        checkSuccessfulAuthorization("admIn", firstAdminPassword);
        checkSuccessfulAuthorization("admiN", firstAdminPassword);
        checkSuccessfulAuthorization("ADmin", firstAdminPassword);
        checkSuccessfulAuthorization("aDMin", firstAdminPassword);
        checkSuccessfulAuthorization("adMIn", firstAdminPassword);
        checkSuccessfulAuthorization("admIN", firstAdminPassword);
        checkSuccessfulAuthorization("ADMIN", firstAdminPassword);
        checkSuccessfulAuthorization("ADMin", firstAdminPassword);
        checkSuccessfulAuthorization("aDMIn", firstAdminPassword);
        checkSuccessfulAuthorization("adMIN", firstAdminPassword);
        checkSuccessfulAuthorization("AdmIN", firstAdminPassword);
        checkSuccessfulAuthorization("ADmiN", firstAdminPassword);
        checkSuccessfulAuthorization("ADMIn", firstAdminPassword);
        checkSuccessfulAuthorization("ADMIN", firstAdminPassword);
        checkSuccessfulAuthorization("aDMIN", firstAdminPassword);
        checkSuccessfulAuthorization("AdMIN", firstAdminPassword);
        checkSuccessfulAuthorization("ADmIN", firstAdminPassword);
        checkSuccessfulAuthorization("ADMiN", firstAdminPassword);

        checkSuccessfulAuthorization(getSecondAdminUsername(), getSecondAdminPassword());
    }

    @Test
    void loginDataFromDB(){

        checkIncorrectLoginData("admin", "$2a$12$N0uY4GewYyFM1s3TN7SzXuqoTbnW9NK3mcdoG6WdLLfUNswDL5gvu");
        checkIncorrectLoginData("admin1", "$2a$12$Fy4pxxZDrbL6H3pyxom9J.bTdL.FNubxBVymHq9ZX2OJiOi3X1aWq");
    }


    @Test
    void checkUsersVariationData() {

        checkIncorrectLoginData(getFirstAdminUsername(), getSecondAdminPassword());
        checkIncorrectLoginData(getSecondAdminUsername(), getFirstAdminPassword());
    }

    @Test
    void checkIncorrectLoginData() {

        checkIncorrectLoginData("tormentedtotal", "password");
        checkIncorrectLoginData("cubfeminine", "123456789");
        checkIncorrectLoginData("wefferbv", "111111111");
        checkIncorrectLoginData("thrd2533", "1234567890");
        checkIncorrectLoginData("user_1", "D1lakisss");
        checkIncorrectLoginData("admin", "5y4yeg354hge");
        checkIncorrectLoginData("brtgg333", "ret43tge");
        checkIncorrectLoginData("bfb43", "h5ge45hgevdv4");
        checkIncorrectLoginData("gtg4r", "3456423256");
        checkIncorrectLoginData("bgbgbr44", "464hgt5y455");

        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(dataFactory.getFirstName(), dataFactory.getRandomText(8, 100));

        checkIncorrectLoginData(dataFactory.getRandomText(2), dataFactory.getRandomText(8));
        checkIncorrectLoginData(dataFactory.getRandomText(4), dataFactory.getRandomText(19));
        checkIncorrectLoginData(dataFactory.getRandomText(29), dataFactory.getRandomText(43));
        checkIncorrectLoginData(dataFactory.getRandomText(10), dataFactory.getRandomText(23));
        checkIncorrectLoginData(dataFactory.getRandomText(30), dataFactory.getRandomText(100));
    }

    @Test
    void checkVariationUsername() {

        String firstAdminPassword = getFirstAdminPassword();

        checkIncorrectLoginData("damin", firstAdminPassword);
        checkIncorrectLoginData("madin", firstAdminPassword);
        checkIncorrectLoginData("iadmn", firstAdminPassword);
        checkIncorrectLoginData("nadmi", firstAdminPassword);
        checkIncorrectLoginData("dmina", firstAdminPassword);
        checkIncorrectLoginData("dmina", firstAdminPassword);

        checkIncorrectLoginData("tormentedtotal", firstAdminPassword);
        checkIncorrectLoginData("cubfeminine", firstAdminPassword);
        checkIncorrectLoginData("wefferbv", firstAdminPassword);
        checkIncorrectLoginData("thrd2533", firstAdminPassword);
        checkIncorrectLoginData("user_1", firstAdminPassword);
        checkIncorrectLoginData("brtgg333", firstAdminPassword);
        checkIncorrectLoginData("bfb43", firstAdminPassword);
        checkIncorrectLoginData("gtg4r", firstAdminPassword);
        checkIncorrectLoginData("bgbgbr44", firstAdminPassword);

        checkIncorrectLoginData(dataFactory.getFirstName(), firstAdminPassword);
        checkIncorrectLoginData(dataFactory.getFirstName(), firstAdminPassword);
        checkIncorrectLoginData(dataFactory.getFirstName(), firstAdminPassword);
        checkIncorrectLoginData(dataFactory.getFirstName(), firstAdminPassword);

        checkIncorrectLoginData(dataFactory.getRandomText(2), firstAdminPassword);
        checkIncorrectLoginData(dataFactory.getRandomText(4), firstAdminPassword);
        checkIncorrectLoginData(dataFactory.getRandomText(29), firstAdminPassword);
        checkIncorrectLoginData(dataFactory.getRandomText(10), firstAdminPassword);
        checkIncorrectLoginData(dataFactory.getRandomText(30), firstAdminPassword);
    }

    @Test
    void checkVariationPassword() {

        String firstAdminUsername = getFirstAdminUsername();

        checkIncorrectLoginData(firstAdminUsername, "Admin");
        checkIncorrectLoginData(firstAdminUsername, "aDmin");
        checkIncorrectLoginData(firstAdminUsername, "adMin");
        checkIncorrectLoginData(firstAdminUsername, "admIn");
        checkIncorrectLoginData(firstAdminUsername, "admiN");
        checkIncorrectLoginData(firstAdminUsername, "ADmin");
        checkIncorrectLoginData(firstAdminUsername, "aDMin");
        checkIncorrectLoginData(firstAdminUsername, "adMIn");
        checkIncorrectLoginData(firstAdminUsername, "admIN");
        checkIncorrectLoginData(firstAdminUsername, "ADMIN");
        checkIncorrectLoginData(firstAdminUsername, "ADMin");
        checkIncorrectLoginData(firstAdminUsername, "aDMIn");
        checkIncorrectLoginData(firstAdminUsername, "adMIN");
        checkIncorrectLoginData(firstAdminUsername, "AdmIN");
        checkIncorrectLoginData(firstAdminUsername, "ADmiN");
        checkIncorrectLoginData(firstAdminUsername, "ADMIn");
        checkIncorrectLoginData(firstAdminUsername, "ADMIN");
        checkIncorrectLoginData(firstAdminUsername, "aDMIN");
        checkIncorrectLoginData(firstAdminUsername, "AdMIN");
        checkIncorrectLoginData(firstAdminUsername, "ADmIN");
        checkIncorrectLoginData(firstAdminUsername, "ADMiN");

        checkIncorrectLoginData(firstAdminUsername, "password");
        checkIncorrectLoginData(firstAdminUsername, "123456789");
        checkIncorrectLoginData(firstAdminUsername, "111111111");
        checkIncorrectLoginData(firstAdminUsername, "1234567890");
        checkIncorrectLoginData(firstAdminUsername, "D1lakisss");
        checkIncorrectLoginData(firstAdminUsername, "5y4yeg354hge");
        checkIncorrectLoginData(firstAdminUsername, "ret43tge");
        checkIncorrectLoginData(firstAdminUsername, "h5ge45hgevdv4");
        checkIncorrectLoginData(firstAdminUsername, "3456423256");
        checkIncorrectLoginData(firstAdminUsername, "464hgt5y455");

        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(8, 100));
        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(8, 100));

        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(8));
        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(19));
        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(43));
        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(23));
        checkIncorrectLoginData(firstAdminUsername, dataFactory.getRandomText(100));
    }

    @Test
    public void authorization_invalid_login_date() {

        tryAuthorizeInvalidLoginData(null, generateRandomString(23));
        tryAuthorizeInvalidLoginData(null, generateRandomString(544));
        tryAuthorizeInvalidLoginData(null, generateRandomString(2));
        tryAuthorizeInvalidLoginData(null, generateRandomString(989));
        tryAuthorizeInvalidLoginData(null, generateRandomString(66));

        tryAuthorizeInvalidLoginData(null, "priig984utt98y98t35r");
        tryAuthorizeInvalidLoginData(null, "9t87495th42yhro24h2fjwf");
        tryAuthorizeInvalidLoginData(null, "987365kjhfyruy373");
        tryAuthorizeInvalidLoginData(null, "t9u834yty3487ty8ht8734yutwl84ju887uh3");
        tryAuthorizeInvalidLoginData(null, "34t34trgf43t35y4h434658uhrg43t5643ty");
        tryAuthorizeInvalidLoginData(null, "3t4frfgscsgegreg");
        tryAuthorizeInvalidLoginData(null, "98ty83444");

        tryAuthorizeInvalidLoginData(generateRandomString(23), null);
        tryAuthorizeInvalidLoginData(generateRandomString(544), null);
        tryAuthorizeInvalidLoginData(generateRandomString(2), null);
        tryAuthorizeInvalidLoginData(generateRandomString(989), null);
        tryAuthorizeInvalidLoginData(generateRandomString(66), null);

        tryAuthorizeInvalidLoginData("98t4y3htytrf", null);
        tryAuthorizeInvalidLoginData("f4tttgefdd", null);
        tryAuthorizeInvalidLoginData("erg4t45thgdd", null);
        tryAuthorizeInvalidLoginData("g4g45grsf34tfe", null);
        tryAuthorizeInvalidLoginData("45g4h5h_34t34t", null);
        tryAuthorizeInvalidLoginData("8734y87y3t", null);
        tryAuthorizeInvalidLoginData("g53tg34", null);
        tryAuthorizeInvalidLoginData("444444234", null);
        tryAuthorizeInvalidLoginData("497368642433", null);

        tryAuthorizeInvalidLoginData(null, null);
    }

   @SneakyThrows
    private static void checkSuccessfulAuthorization(String username, String password){

        MvcResult mvcResult = mockMvc.perform(post("/authorization/login").
                        param("username", username).
                        param("password", password)).
                andExpect(status().isOk()).
                andReturn();

        String token = mvcResult.getResponse().getContentAsString().split("\"")[3];
        assertTrue(tokenIsValid(token));
    }

    @SneakyThrows
    private static void checkIncorrectLoginData(String username, String password) {

        mockMvc.perform(post("/authorization/login").
                        param("username", username).
                        param("password", password)).
                andExpect(status().isUnauthorized()).
                andReturn();
    }

    @SneakyThrows
    private static void tryAuthorizeInvalidLoginData(String username, String password) {

        mockMvc.perform(post("/authorization/login").
                        param("username", username).
                        param("password", password)).
                andExpect(status().isBadRequest()).
                andReturn();
    }
}
