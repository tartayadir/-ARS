package com.utils.selenium;

public class URLUtils {

    private final static String homePageURL = "https://cars-yaroslav-b.implemica.com/";

    private final static String loginPageURL = "https://cars-yaroslav-b.implemica.com/login-page";

    private final static String addCarPageURL = "https://cars-yaroslav-b.implemica.com/add-car-page";

    private final static String updateCarPagePrefixURL = "https://cars-yaroslav-b.implemica.com/update/";

    public static String getHomePageURL() {
        return homePageURL;
    }

    public static String getLoginPageURL() {return loginPageURL;}

    public static String getAddCarPageURL() {return addCarPageURL;}

    public static String getUpdateCarPageURL(Long id) {
        return  updateCarPagePrefixURL + id;
    }
}
