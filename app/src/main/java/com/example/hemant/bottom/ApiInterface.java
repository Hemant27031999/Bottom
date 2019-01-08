package com.example.hemant.bottom;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("channels")
    Call<Mainjson> getMainJson(@Query("part") String statistics,
                               @Query("forUsername") String name,
                               @Query("key") String key);
}
