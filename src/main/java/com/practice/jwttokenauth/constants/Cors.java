package com.practice.jwttokenauth.constants;

public class Cors {

    public static final String[] ALLOWED_ORIGINS = {"http://localhost:3000"};
    public static final String[] ALLOWED_HEADERS = {"Origin", "Access-Control-Allow-Origin", "Content-Type",
            "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
            "Access-Control-Request-Method", "Access-Control-Request-Headers"};
    public static final String[] EXPOSED_HEADERS = {"Origin", "Content-Type", "Accept", "Jwt-Token",
            "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"};
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"};
}
