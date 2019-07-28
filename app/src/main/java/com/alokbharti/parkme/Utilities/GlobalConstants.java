package com.alokbharti.parkme.Utilities;

public class GlobalConstants {
    public static int currentUserId = 0;
    public static String BASE_URL = "52.172.30.204:5433/";
    public static String signUpUrl = BASE_URL+"user/signUp";
    public static String loginUrl = BASE_URL+"user/login";
    public static String userDetailsUrl = BASE_URL+"user/details";
    public static String userHistoryUrl = BASE_URL+"user/history";
    public static String newBookingUrl = BASE_URL+"booking/new";
    public static String cancelBookingUrl = BASE_URL+"booking/cancel";
    public static String activeBookingUrl = BASE_URL+"booking/active";
    public static String checkoutBookingUrl = BASE_URL+"booking/checkout";
    public static String nearbyParkingUrl = BASE_URL+"parking/nearby";
    public static String driveInParkingUrl = BASE_URL+"parking/driveIn";
    public static String driveOutParkingUrl = BASE_URL+"parking/driveOut";
    public static String registerParkingUrl = BASE_URL+"parking/register";
}
