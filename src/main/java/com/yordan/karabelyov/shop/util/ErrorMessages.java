package com.yordan.karabelyov.shop.util;

public class ErrorMessages {

    // Product get error messages
    public static final String PRODUCT_GET_NOT_FOUND = "Product with id %s does not exist!";
    public static final String PRODUCT_NOT_FOUND = "One or more products not found! ";

    //Subscriber
    public static final String SUBSCRIBER_GET_NOT_FOUND = "Subscriber with id %s does not exist!";
    public static final String SUBSCRIBER_NOT_SUBSCRIBED_TO_PRODUCT ="Subscriber with ID: %s " +
            "is not subscribed to the product, please subscribe first!";

    //ProductStatus
    public static final String PRODUCT_STATUS_NOT_FOUND = "Product status not found for id: ";
    public static final String INVALID_PARAMETERS = "Invalid parameters, please check the data you provided!";
    public static final String PRODUCT_GET_DUPLICATE_NAME = "There is already a product with that name - %s!";

    private ErrorMessages() {
    }
}