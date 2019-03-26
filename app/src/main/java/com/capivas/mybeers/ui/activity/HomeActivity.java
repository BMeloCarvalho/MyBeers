package com.capivas.mybeers.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.dao.BeerDAO;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.retrofit.RetrofitWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseRecyclerViewActivity {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNextPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        configSearchAction(menu);
        configFavoritesAction(menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(currentQuery == null) {
            super.onBackPressed();
        } else {
            currentQuery = null;
            resetBeersList();
        }
    }

    @Override
    protected void getFieldReferences() {
        super.getFieldReferences();
    }

    @Override
    protected void loadNextPage() {
        Call<List<Beer>> call = getCall();
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                List<Beer> beers = response.body();
                BeerDAO dao = new BeerDAO(HomeActivity.this);
                for(Beer beer : beers) {
                    if(dao.exists(beer))
                        beer.setIsFavorite(true);
                }
                dao.close();
                updateRecyclerView(beers);
                Log.i("onResponse",
                        "Success requisition.Beers received: " + String.valueOf(beers.size())
                                + "; search query: " + currentQuery);
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                Log.e("onFailure", "Requisition failure");
            }
        });
    }

    @Override
    protected void onFavoriteItemButtomClick(Beer beer) {
        BeerDAO dao = new BeerDAO(HomeActivity.this);
        dao.save(beer);
        dao.close();
    }

    private void configFavoritesAction(Menu menu) {
        MenuItem favotiresIconMenu = menu.findItem(R.id.home_menu_favorites);
        favotiresIconMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                goToFavoritesScreen();
                return false;
            }
        });
    }

    private void goToFavoritesScreen() {
        Intent favoritesScreen = new Intent(this, FavoritesActivity.class);
        startActivity(favoritesScreen);
    }

    private Call<List<Beer>> getCall() {
        if(currentQuery != null && !currentQuery.isEmpty())
            return RetrofitWrapper.getBeerService().list(currentPage, MAX_PER_PAGE, currentQuery);
        else
            return RetrofitWrapper.getBeerService().list(currentPage, MAX_PER_PAGE);
    }

    private void configSearchAction(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.home_menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()) {
                    currentQuery = query;
                    resetBeersList();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void resetBeersList() {
        adapter.clear();
        currentPage = START_PAGE;
        isLastPage = false;
        searchView.onActionViewCollapsed();
        loadNextPage();
    }
}
