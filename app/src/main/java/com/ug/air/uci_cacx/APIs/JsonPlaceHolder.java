package com.ug.air.uci_cacx.APIs;

import com.ug.air.uci_cacx.Models.Facility;
import com.ug.air.uci_cacx.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolder {

    @GET("openmrs/ws/rest/v1/session")
    Call<User> loginUser();

    @GET("facilities")
    Call<List<Facility>> getFacilities();

    @GET("providers")
    Call<List<String>> getProviders();

}
