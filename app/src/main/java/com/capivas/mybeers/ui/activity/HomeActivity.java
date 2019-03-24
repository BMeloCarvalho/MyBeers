package com.capivas.mybeers.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.adapter.BeersAdapter;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.retrofit.RetrofitWrapper;

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
        beersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Beer beer = (Beer) beersList.getItemAtPosition(position);
                Intent toDetails = new Intent(HomeActivity.this, DetailActivity.class);
                toDetails.putExtra("beer", beer);
                startActivity(toDetails);
            }
        });

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
