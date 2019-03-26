package com.capivas.mybeers.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.capivas.mybeers.dao.BeerDAO;
import com.capivas.mybeers.model.Beer;

import java.util.List;

public class FavoritesActivity extends BaseRecyclerViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNextPage();
    }

    @Override
    protected void loadNextPage() {
        //call next page from dao
        BeerDAO dao = new BeerDAO(this);
        List<Beer> beers = dao.getPage(currentPage, MAX_PER_PAGE);
        dao.close();
        updateRecyclerView(beers);
        Log.i("loadNextPage","Success rbeers from the database: " + String.valueOf(beers.size()));
    }

    @Override
    protected void onFavoriteItemButtomClick(Beer beer) {
        //delete beer from database
    }
}
