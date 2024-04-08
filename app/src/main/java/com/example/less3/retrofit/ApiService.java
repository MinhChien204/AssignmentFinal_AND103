package com.example.less3.retrofit;

import com.example.less3.model.Sinhvien;
import com.example.less3.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String DOMAIN = "http://10.0.2.2:3000/";
    @GET("/")
    Call<List<Sinhvien>> getSinhviens();
    @Multipart
    @POST("/add_sv")
    Call<Sinhvien> AddSinhViens(@PartMap Map<String, RequestBody> requestBodyMap,
                                @Part MultipartBody.Part imageSinhVien);
    @Multipart
    @PUT("/update/{id}")
    Call<Sinhvien> updateSinhViens(@PartMap Map<String, RequestBody> requestBodyMap,
                                   @Path("id") String id,
                                   @Part MultipartBody.Part imageSinhVien);
    @DELETE("/delete/{id}")
    Call<Void> deleteSinhViens(@Path("id") String id);

    @Multipart
    @POST("/register-send-email")
    Call<Response<User>> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avartar
    );
    @POST("/login")
    Call<Response<User>> login (@Body User user);
    @Multipart
    @PUT("/update-no-image/{id}")
    Call<Sinhvien> updateNoImage(@PartMap Map<String, RequestBody> requestBodyMap,
                               @Path("id") String id
    );
    @GET("/search")
    Call<List<Sinhvien>> searchCay(@Query("key") String query);
    @GET("/giam-dan")
    Call<List<Sinhvien>> getGiam();

    @GET("/tang-dan")
    Call<List<Sinhvien>> getTang();
}
