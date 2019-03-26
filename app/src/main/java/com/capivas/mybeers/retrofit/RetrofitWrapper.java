package com.capivas.mybeers.retrofit;

import com.capivas.mybeers.service.BeerService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitWrapper {
    private  Retrofit retrofit;

    public RetrofitWrapper() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.punkapi.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public BeerService getBeerService() {
        return retrofit.create(BeerService.class);
    }
}
