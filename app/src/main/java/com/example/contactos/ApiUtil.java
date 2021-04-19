package com.example.contactos;

public class ApiUtil {
    private ApiUtil() {}

    public static ApiInterface getAPI() {

        return ApiClient.getApiClient().create(ApiInterface.class);
    }
}
