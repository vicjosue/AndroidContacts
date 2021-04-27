package com.example.contactos;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String URL = "http://192.168.1.9:5000/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient() {

        if (retrofit == null) { //singleton
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
