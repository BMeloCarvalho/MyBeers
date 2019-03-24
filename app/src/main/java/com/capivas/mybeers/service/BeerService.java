package com.capivas.mybeers.service;

import com.capivas.mybeers.model.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BeerService {
    @GET("beers")
    Call<List<Beer>> list();
}
