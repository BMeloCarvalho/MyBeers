package com.capivas.mybeers.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        final List<Beer> beers = getCurrentPageBeers();
        updateRecyclerViewAfterScroll(beers);
        Log.i("loadNextPage","Beers obtained from the database: "
                + String.valueOf(beers.size()));
    }

    @Override
    protected void onFavoriteItemButtomClick(Beer beer) {
        deleteBeer(beer);
    }

    private void updateRecyclerViewAfterScroll(final List<Beer> beers) {
        beersRecyclerView.post(new Runnable() {
            public void run() {
                updateRecyclerView(beers);
            }
        });
    }

    private List<Beer> getCurrentPageBeers() {
        BeerDAO dao = new BeerDAO(this);
        List<Beer> beers = dao.getPage(currentPage, MAX_PER_PAGE);
        dao.close();
        return beers;
    }

    private void deleteBeer(Beer beer) {
        BeerDAO dao = new BeerDAO(this);
        dao.delete(beer);
        dao.close();
        adapter.remove(beer);
    }
}
