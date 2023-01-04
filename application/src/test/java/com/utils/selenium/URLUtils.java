package com.utils.selenium;

public class URLUtils {

    private final static String homePageURL = "http://localhost:4200/car-catalog/";

    private final static String loginPageURL = "http://localhost:4200/car-catalog/login-page";

    private final static String addCarPageURL = "http://localhost:4200/car-catalog/add-car-page";

    private final static String updateCarPagePrefixURL = "http://localhost:4200/car-catalog/update/";

    private final static String detailsCarPagePrefixURL = "http://localhost:4200/car-catalog/details/";

    public static String getHomePageURL() {
        return homePageURL;
    }

    public static String getLoginPageURL() {return loginPageURL;}

    public static String getAddCarPageURL() {return addCarPageURL;}

    public static String getUpdateCarPageURL(Long id) {
        return  updateCarPagePrefixURL + id;
    }

    public static String getDetailsCarPageURL(Long id) {
        return  detailsCarPagePrefixURL + id;
    }
}
