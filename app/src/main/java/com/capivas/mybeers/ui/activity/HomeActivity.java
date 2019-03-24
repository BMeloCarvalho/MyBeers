package com.capivas.mybeers.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.adapter.BeersAdapter;
import com.capivas.mybeers.util.Util;

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
        BeersAdapter adapter = new BeersAdapter(this, Util.getStaticBeersList());
        beersList.setAdapter(adapter);
    }
}
