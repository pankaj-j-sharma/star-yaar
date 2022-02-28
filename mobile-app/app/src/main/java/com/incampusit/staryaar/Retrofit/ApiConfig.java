package com.incampusit.staryaar.Retrofit;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiConfig {
    @Multipart
    @POST("uploadvideo.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("file") RequestBody name,
                                    @Part("query") RequestBody query);

    @Multipart
    @POST("uploadvideo.php")
    Call<ServerResponse> uploadMultipartVideo(@Part MultipartBody.Part videoToUpload,
                                              @Part MultipartBody.Part gifToUpload,
                                              @Part MultipartBody.Part thmbToUpload,
                                              @PartMap() HashMap<String, RequestBody> requestmap);
}
