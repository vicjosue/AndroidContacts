package com.example.contactos;

import com.example.contactos.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    /*
    * HTTP REQUESTS TO THE API
    * */

    @FormUrlEncoded
    @POST("/")
    Call<Contact> saveContact(//saveNote
            @Field("Telefono") String telefono,
            @Field("Nombre") String nombre
    );

    @GET("/")
    Call<List<Contact>> getContacts();

    @FormUrlEncoded
    @PUT("/")
    Call<Contact> updateContact(
            @Field("Telefono") String telefono,
            @Field("Nombre") String nombre
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/", hasBody = true)
    Call<Contact> deleteContact(
            @Field("Telefono") String telefono
    );

}
