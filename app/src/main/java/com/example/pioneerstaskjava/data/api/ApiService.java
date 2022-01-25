package com.example.pioneerstaskjava.data.api;

import android.telecom.Call;

import com.example.pioneerstaskjava.data.model.GitModel;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {


    @Headers("Content-Type: application/json")
    @GET("search/repositories")
    Observable<GitModel> getData(@Query("q") String created,
                                       @Query("sort") String sort,
                                       @Query("order") String order,
                                       @Query("per_page") String per_page,
                                       @Query("q") String lang);

}
