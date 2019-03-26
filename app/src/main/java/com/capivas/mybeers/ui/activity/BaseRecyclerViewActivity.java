package com.capivas.mybeers.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.capivas.mybeers.R;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.ui.adapter.PaginationRecyclerViewAdapter;
import com.capivas.mybeers.ui.listener.OnItemClickListener;
import com.capivas.mybeers.ui.listener.PaginationScrollListener;

import java.util.List;

public abstract class BaseRecyclerViewActivity extends AppCompatActivity {
    protected static final int START_PAGE = 1;
    protected static final int MAX_PER_PAGE = 25;

    protected RecyclerView beersRecyclerView;
    protected ProgressBar progressBar;
    protected PaginationRecyclerViewAdapter adapter;
    protected View noSearchResultsView;

    protected boolean isLoading = false;
    protected boolean isLastPage = false;
    protected int currentPage = START_PAGE;
    protected String currentQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recyclerview);

        getFieldReferences();
        configRecyclerView();
    }

    protected void getFieldReferences() {
        beersRecyclerView = findViewById(R.id.home_beers_list);
        progressBar = findViewById(R.id.home_progress_bar);
        noSearchResultsView = findViewById(R.id.home_search_fail);
    }

    protected final void updateRecyclerView(List<Beer> beers) {
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

            @Override
            public void onFavoriteButtomClick(Beer beer) {
                onFavoriteItemButtomClick(beer);
            }
        });
    }

    private void sendBeerToDetailsActivity(Beer beer) {
        Intent toDetails = new Intent(this, DetailActivity.class);
        toDetails.putExtra("beer", beer);
        startActivity(toDetails);
    }

    protected abstract void loadNextPage();
    protected abstract void onFavoriteItemButtomClick(Beer beer);
}
