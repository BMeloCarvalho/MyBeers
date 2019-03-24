package com.capivas.mybeers.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.adapter.BeersAdapter;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.retrofit.RetrofitWrapper;
import com.capivas.mybeers.util.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ListView beersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        beersList = findViewById(R.id.home_beers_list);

        loadBeers();
    }

    private void loadBeers() {
        Call<List<Beer>> call = RetrofitWrapper.getBeerService().list();
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                List<Beer> beers = response.body();
                BeersAdapter adapter = new BeersAdapter(HomeActivity.this, beers);
                beersList.setAdapter(adapter);
                Log.i("onResponse", "Success requisition");
                Log.i("onResponse", "Beers received: " + String.valueOf(beers.size()));
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                Log.e("onFailure", "Requisition failure");
            }
        });

    }
}
