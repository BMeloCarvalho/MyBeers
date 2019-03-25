package com.capivas.mybeers.service;

import com.capivas.mybeers.model.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BeerService {
    @GET("beers")
    Call<List<Beer>> list();

    @GET("beers")
    Call<List<Beer>> list(@Query("page") int page,@Query("per_page") int per_page);
}
