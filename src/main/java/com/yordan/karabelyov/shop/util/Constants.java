package com.yordan.karabelyov.shop.util;

public class Constants {
    // APIs
    public static final String API_V1 = "/api/v1";

    public static final String PRODUCTS = "/products";
    public static final String SUBSCRIBERS = "/subscribers";
    public static final String PURCHASES = "/purchases";
    public static final String AUDIT = "/audit";


    //Product
    public static final int PRODUCT_MIN_NAME_LENGTH = 3;
    public static final int PRODUCT_MAX_NAME_LENGTH = 30;

    //Subscriber
    public static final int SUBSCRIBER_MIN_NAME_LENGTH = 3;
    public static final int SUBSCRIBER_MAX_NAME_LENGTH = 50;

    //Page

    public static final String PAGE_NUMBER_MIN_SIZE = "Page number must be greater than or equal to 1!";
    public static final String PAGE_SIZE_MIN_SIZE = "Page number must be greater than or equal to 1!";

    //Generic

    public static final int TEXT_LENGTH = 1000;
    public static final int MIN_PRICE = 0;

    private Constants() {
    }
}