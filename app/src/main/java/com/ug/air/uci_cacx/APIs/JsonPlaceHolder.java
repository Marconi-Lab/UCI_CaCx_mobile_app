package com.ug.air.uci_cacx.APIs;

import com.ug.air.uci_cacx.Models.Facility;
import com.ug.air.uci_cacx.Models.Message;
import com.ug.air.uci_cacx.Models.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface JsonPlaceHolder {

    @GET("openmrs/ws/rest/v1/session")
    Call<User> loginUser();

    @GET("facilities")
    Call<List<Facility>> getFacilities();

    @GET("providers")
    Call<List<String>> getProviders();

    @Multipart
    @POST("upload_xml")
    Call<Message> upload_file(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> params);


    @Multipart
    @POST("upload_xml_with_images")
    Call<Message> upload_files(@Part MultipartBody.Part[] image_files,
                           @Part MultipartBody.Part file,
                           @PartMap Map<String, RequestBody> params);

}
