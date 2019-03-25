package com.capivas.mybeers.ui.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.capivas.mybeers.R;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.retrofit.RetrofitWrapper;
import com.capivas.mybeers.ui.listener.OnItemClickListener;
import com.capivas.mybeers.ui.adapter.PaginationRecyclerViewAdapter;
import com.capivas.mybeers.ui.listener.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity{
    private static final int PAGE_START = 1;
    private static final int PER_PAGE = 25;

    private RecyclerView beersRecyclerView;
    private ProgressBar progressBar;
    private PaginationRecyclerViewAdapter adapter;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;
    private String currentQuery = null;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getFieldReferences();
        configRecyclerView();
        loadNextPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        configSearchView(menu);

        return true;
    }

    private void configSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.home_menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                invalidateOptionsMenu();
                processQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    private void processQuery(String query) {
        adapter.clear();
        currentQuery = query;
        currentPage = PAGE_START;
        loadNextPage();
    }

    private void getFieldReferences() {
        beersRecyclerView = findViewById(R.id.home_beers_list);
        progressBar = findViewById(R.id.home_progress_bar);
    }

    private void configRecyclerView() {
        configRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        beersRecyclerView.setAdapter(adapter);
        beersRecyclerView.setLayoutManager(linearLayoutManager);
        beersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        beersRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void configRecyclerAdapter() {
        adapter = new PaginationRecyclerViewAdapter(this);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Beer beer) {
                sendBeerToDetailsActivity(beer);
            }
        });
    }

    private void sendBeerToDetailsActivity(Beer beer) {
        Intent toDetails = new Intent(HomeActivity.this, DetailActivity.class);
        toDetails.putExtra("beer", beer);
        startActivity(toDetails);
    }

    private void loadNextPage() {
        Log.d("loadNextPage", "loadNextPage: " + currentPage);
        Call<List<Beer>> call;
        if(currentQuery != null && !currentQuery.isEmpty())
            call = RetrofitWrapper.getBeerService().list(currentPage, PER_PAGE, currentQuery);
        else
            call = RetrofitWrapper.getBeerService().list(currentPage, PER_PAGE);

        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                List<Beer> beers = response.body();

                if(beers != null) {
                    if(currentPage == PAGE_START) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                    }

                    adapter.addAll(beers);
                    if (beers.size() == PER_PAGE)
                        adapter.addLoadingFooter();
                    else
                        isLastPage = true;
                } else {
                    isLastPage = true;
                }
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
