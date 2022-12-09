package com.implemica.application.controller.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.spring.AuthTestUtils.*;
import static utils.spring.StringUtils.generateRandomString;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class AuthControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        AuthControllerTest.mockMvc = mockMvc;
    }

    @Test
    public void authorizationSuccessful() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/login").
                        param("username", getUsername()).
                        param("password", getPassword())).
                andExpect(status().isOk()).
                andReturn();

        String token = mvcResult.getResponse().getContentAsString().split("\"")[3];
        assertTrue(tokenIsValid(token));
    }

    @Test
    public void authorization_invalid_login_date() throws Exception {

        tryAuthorizeInvalidLoginData(null, generateRandomString(23));
        tryAuthorizeInvalidLoginData(null, generateRandomString(544));
        tryAuthorizeInvalidLoginData(null, generateRandomString(2));
        tryAuthorizeInvalidLoginData(null, generateRandomString(989));
        tryAuthorizeInvalidLoginData(null, generateRandomString(66));

        tryAuthorizeInvalidLoginData(generateRandomString(23), null);
        tryAuthorizeInvalidLoginData(generateRandomString(544), null);
        tryAuthorizeInvalidLoginData(generateRandomString(2), null);
        tryAuthorizeInvalidLoginData(generateRandomString(989), null);
        tryAuthorizeInvalidLoginData(generateRandomString(66), null);

        tryAuthorizeInvalidLoginData(null, null);
    }

    @Test
    public void authorization_incorrect_login_date() throws Exception {

        tryAuthorizeIncorrectLoginData(generateRandomString(1), generateRandomString(89));
        tryAuthorizeIncorrectLoginData(generateRandomString(24), generateRandomString(51));
        tryAuthorizeIncorrectLoginData(generateRandomString(254), generateRandomString(7));
        tryAuthorizeIncorrectLoginData(generateRandomString(3), generateRandomString(70));
        tryAuthorizeIncorrectLoginData(generateRandomString(89), generateRandomString(39));
        tryAuthorizeIncorrectLoginData(generateRandomString(231), generateRandomString(88549));
        tryAuthorizeIncorrectLoginData(generateRandomString(544), generateRandomString(73));
        tryAuthorizeIncorrectLoginData(generateRandomString(1), generateRandomString(21));
        tryAuthorizeIncorrectLoginData(generateRandomString(556), generateRandomString(435));
        tryAuthorizeIncorrectLoginData(generateRandomString(5452), generateRandomString(562));
        tryAuthorizeIncorrectLoginData(generateRandomString(231), generateRandomString(871));
        tryAuthorizeIncorrectLoginData(generateRandomString(21243), generateRandomString(32));
        tryAuthorizeIncorrectLoginData(generateRandomString(8), generateRandomString(44));
        tryAuthorizeIncorrectLoginData(generateRandomString(5), generateRandomString(90));
        tryAuthorizeIncorrectLoginData(generateRandomString(245), generateRandomString(673));
        tryAuthorizeIncorrectLoginData(generateRandomString(943), generateRandomString(54));
        tryAuthorizeIncorrectLoginData(generateRandomString(2452), generateRandomString(1233));
        tryAuthorizeIncorrectLoginData(generateRandomString(2), generateRandomString(44));
        tryAuthorizeIncorrectLoginData(generateRandomString(6763), generateRandomString(90));
        tryAuthorizeIncorrectLoginData(generateRandomString(4), generateRandomString(34));
        tryAuthorizeIncorrectLoginData(generateRandomString(545), generateRandomString(869));
        tryAuthorizeIncorrectLoginData(generateRandomString(563), generateRandomString(44));
    }

    private static void tryAuthorizeIncorrectLoginData(String username, String password) throws Exception {

        mockMvc.perform(post("/api/login").
                        param("username", username).
                        param("password", password)).
                andExpect(status().isUnauthorized()).
                andReturn();
    }

    private static void tryAuthorizeInvalidLoginData(String username, String password) throws Exception {

        mockMvc.perform(post("/api/login").
                        param("username", username).
                        param("password", password)).
                andExpect(status().isBadRequest()).
                andReturn();
    }
}
