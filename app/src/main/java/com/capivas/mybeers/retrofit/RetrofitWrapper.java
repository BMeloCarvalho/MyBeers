package com.capivas.mybeers.retrofit;

import com.capivas.mybeers.service.BeerService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
* Singleton class for use Retrofit2 API
* */
public class RetrofitWrapper {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.punkapi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();;

    public static Retrofit getInstance() {
        return retrofit;
    }

    public static BeerService getBeerService() {
        return retrofit.create(BeerService.class);
    }
}
