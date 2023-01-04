package com.implemica.application.controller.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.utils.spring.AuthTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.utils.spring.StringUtils.generateRandomString;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@ActiveProfiles("test")
public class AuthControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        AuthControllerTest.mockMvc = mockMvc;
    }

    @Test
    public void authorizationSuccessful() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/authorization/login").
                        param("username", getAdminUsername()).
                        param("password", getAdminPassword())).
                andExpect(status().isOk()).
                andReturn();

        String token = mvcResult.getResponse().getContentAsString().split("\"")[3];
        assertTrue(tokenIsValid(token));
    }

    @SneakyThrows
    @Test
    void checkVariationUsername() {

        String password = getAdminPassword();

        tryAuthorizeIncorrectLoginData("admin_1", password);
        tryAuthorizeIncorrectLoginData("ADMIN_1", password);
        tryAuthorizeIncorrectLoginData("_1admin", password);
        tryAuthorizeIncorrectLoginData("amin_1d", password);
        tryAuthorizeIncorrectLoginData("AdMiN_1", password);
        tryAuthorizeIncorrectLoginData("Admin1", password);
        tryAuthorizeIncorrectLoginData("Amin_1", password);
        tryAuthorizeIncorrectLoginData("dmin_1", password);
        tryAuthorizeIncorrectLoginData("Adin_1", password);
        tryAuthorizeIncorrectLoginData("Admn_1", password);
        tryAuthorizeIncorrectLoginData("Admi_1", password);
        tryAuthorizeIncorrectLoginData("Admin1", password);
        tryAuthorizeIncorrectLoginData("Admin_", password);
        tryAuthorizeIncorrectLoginData("aDmin_1", password);
        tryAuthorizeIncorrectLoginData("AdMin_1", password);
        tryAuthorizeIncorrectLoginData("AdmIn_1", password);
        tryAuthorizeIncorrectLoginData("AdmiN_1", password);
        tryAuthorizeIncorrectLoginData("Admin11", password);
    }

    @SneakyThrows
    @Test
    void checkVariationPassword() {

        String username = getAdminUsername();

        tryAuthorizeIncorrectLoginData(username, "dmin_pass");
        tryAuthorizeIncorrectLoginData(username, "Amin_pass");
        tryAuthorizeIncorrectLoginData(username, "Adin_pass");
        tryAuthorizeIncorrectLoginData(username, "Admn_pass");
        tryAuthorizeIncorrectLoginData(username, "Admi_pass");
        tryAuthorizeIncorrectLoginData(username, "Adminpass");
        tryAuthorizeIncorrectLoginData(username, "Admin_ass");
        tryAuthorizeIncorrectLoginData(username, "Admin_pss");
        tryAuthorizeIncorrectLoginData(username, "Admin_pas");
        tryAuthorizeIncorrectLoginData(username, "admin_pass");
        tryAuthorizeIncorrectLoginData(username, "ADMIN_PASS");
        tryAuthorizeIncorrectLoginData(username, "ADmin_pass");
        tryAuthorizeIncorrectLoginData(username, "AdMin_pass");
        tryAuthorizeIncorrectLoginData(username, "AdmIn_pass");
        tryAuthorizeIncorrectLoginData(username, "AdmiN_pass");
        tryAuthorizeIncorrectLoginData(username, "Admin_Pass");
        tryAuthorizeIncorrectLoginData(username, "Admin_pAss");
        tryAuthorizeIncorrectLoginData(username, "Admin_paSs");
        tryAuthorizeIncorrectLoginData(username, "Admin_pAsS");
        tryAuthorizeIncorrectLoginData(username, "_passAdmin");
        tryAuthorizeIncorrectLoginData(username, "Aminpass_");
        tryAuthorizeIncorrectLoginData(username, "passAmin_");
    }

    @Test
    public void authorization_invalid_login_date() throws Exception {

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

    @Test
    public void authorization_incorrect_login_date() throws Exception {

        tryAuthorizeIncorrectLoginData(getAdminUsername(), getAdminUsername());
        tryAuthorizeIncorrectLoginData(getAdminPassword(), getAdminPassword());
        tryAuthorizeIncorrectLoginData(getAdminPassword(), getAdminUsername());

        tryAuthorizeIncorrectLoginData("tormentedtotal", "password");
        tryAuthorizeIncorrectLoginData("cubfeminine", "123456789");
        tryAuthorizeIncorrectLoginData("wefferbv", "111111111");
        tryAuthorizeIncorrectLoginData("thrd2533", "1234567890");
        tryAuthorizeIncorrectLoginData("user_1", "D1lakisss");
        tryAuthorizeIncorrectLoginData("admin", "5y4yeg354hge");
        tryAuthorizeIncorrectLoginData("brtgg333", "ret43tge");
        tryAuthorizeIncorrectLoginData("bfb43", "h5ge45hgevdv4");
        tryAuthorizeIncorrectLoginData("gtg4r", "3456423256");
        tryAuthorizeIncorrectLoginData("bgbgbr44", "464hgt5y455");

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

        mockMvc.perform(post("/authorization/login").
                        param("username", username).
                        param("password", password)).
                andExpect(status().isUnauthorized()).
                andReturn();
    }

    private static void tryAuthorizeInvalidLoginData(String username, String password) throws Exception {

        mockMvc.perform(post("/authorization/login").
                        param("username", username).
                        param("password", password)).
                andExpect(status().isBadRequest()).
                andReturn();
    }
}
