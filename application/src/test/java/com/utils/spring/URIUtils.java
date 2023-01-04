package com.utils.spring;

import static java.lang.String.format;

public class URIUtils {

    private static final String GET_ALL_CAR_URI = "/car-catalog";
    private static final String GET_CAR_URI = "/car-catalog/%d";
    private static final String ADD_CAR_URI = "/car-catalog";
    private static final String UPDATE_CAR_URI = "/car-catalog";
    private static final String DELETE_CAR_URI = "/car-catalog/%d";

    public static String getAllCarURI() {
        return GET_ALL_CAR_URI;
    }

    public static String getGetCarUri(Long carId) {
        return format(GET_CAR_URI, carId);
    }

    public static String getAddCarUri() {
        return ADD_CAR_URI;
    }

    public static String getUpdateCarUri() {
        return UPDATE_CAR_URI;
    }

    public static String getDeleteCarUri(Long carID) {
        return format(DELETE_CAR_URI, carID);
    }
}
