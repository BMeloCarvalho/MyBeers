package com.capivas.mybeers.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.retrofit.RetrofitWrapper;
import com.capivas.mybeers.ui.adapter.PaginationRecyclerViewAdapter;
import com.capivas.mybeers.ui.listener.OnItemClickListener;
import com.capivas.mybeers.ui.listener.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private static final int START_PAGE = 1;
    private static final int MAX_PER_PAGE = 25;

    private RecyclerView beersRecyclerView;
    private ProgressBar progressBar;
    private PaginationRecyclerViewAdapter adapter;
    private SearchView searchView;
    private View noSearchResultsView;

    private boolean isLoading = false;
    private int currentPage = START_PAGE;
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

    @Override
    public void onBackPressed() {
        if(currentQuery == null) {
            super.onBackPressed();
        } else {
            currentQuery = null;
            resetBeersList();
        }
    }

    private void getFieldReferences() {
        beersRecyclerView = findViewById(R.id.home_beers_list);
        progressBar = findViewById(R.id.home_progress_bar);
        noSearchResultsView = findViewById(R.id.home_search_fail);
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
        Intent toDetails = new Intent(this, DetailActivity.class);
        toDetails.putExtra("beer", beer);
        startActivity(toDetails);
    }

    private Call<List<Beer>> getCall() {
        if(currentQuery != null && !currentQuery.isEmpty())
            return RetrofitWrapper.getBeerService().list(currentPage, MAX_PER_PAGE, currentQuery);
        else
            return RetrofitWrapper.getBeerService().list(currentPage, MAX_PER_PAGE);
    }

    private final  void loadNextPage() {
        Call<List<Beer>> call = getCall();
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                List<Beer> beers = response.body();
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

    private void updateRecyclerView(List<Beer> beers) {
        if(beers != null) {
            if(beers.size() == 0 && currentQuery != null) {
                progressBar.setVisibility(View.GONE);
                noSearchResultsView.setVisibility(View.VISIBLE);
            } else {
                noSearchResultsView.setVisibility(View.GONE);
                addNewBeersToList(beers);
            }
        } else {
            isLastPage = true;
        }
    }

    private void addNewBeersToList(List<Beer> beers) {
        if(currentPage == START_PAGE) {
            progressBar.setVisibility(View.GONE);
        } else {
            adapter.removeLoadingFooter();
            isLoading = false;
        }

        adapter.addAll(beers);
        if (beers.size() < MAX_PER_PAGE) {
            isLastPage = true;
        } else {
            adapter.addLoadingFooter();
        }
    }

    private void configSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.home_menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("SEARCH", "submited!!!");
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
