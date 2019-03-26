package com.capivas.mybeers.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.ui.listener.OnItemClickListener;
import com.capivas.mybeers.util.Util;

import java.util.ArrayList;
import java.util.List;

public class PaginationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    private List<Beer> beers;
    private boolean isLoadingAdded = false;

    public PaginationRecyclerViewAdapter(Context context) {
        this.context = context;
        beers = new ArrayList<>();
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public void setBeers(List<Beer> beers) {
        this.beers = beers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM:
                return new BeerViewHolder(buildView(parent, R.layout.item_beer));

            case LOADING:
                return new LoadingViewHolder(buildView(parent, R.layout.item_progress));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                bindBeerViewHolder((BeerViewHolder) viewHolder, position);
                break;
            case LOADING:
                // Do nothing
                break;
        }
    }


    @Override
    public int getItemCount() {
        return beers == null ? 0 : beers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == (beers.size() - 1) && isLoadingAdded) ? LOADING : ITEM;
    }

    /* Helper Methods */

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private View buildView(ViewGroup parent, int layoutResource) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutResource, parent, false);
        return view;
    }

    private void bindBeerViewHolder(BeerViewHolder beerViewHolder, int position) {
        Beer beer = beers.get(position);
        beerViewHolder.bind(beer);
    }

    public void add(Beer beer) {
        beers.add(beer);
        notifyItemInserted(beers.size() - 1);
    }

    public void addAll(List<Beer> beers) {
        for (Beer beer : beers) {
            add(beer);
        }
    }

    public void remove(Beer beer) {
        int position = beers.indexOf(beer);
        if (position > -1) {
            beers.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Beer());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = beers.size() - 1;

        if (getItem(position) != null) {
            beers.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Beer getItem(int position) {
        return beers.get(position);
    }

    /* View Holders */

    protected class BeerViewHolder extends RecyclerView.ViewHolder {
        private TextView nameField;
        private TextView taglineField;
        private ImageView photoField;
        private ImageView favoriteIconField;
        private Beer beer;

        public BeerViewHolder(View itemView) {
            super(itemView);
            getFieldReferences(itemView);
            setItemClick(itemView);
            setFavoriteButtonClick(itemView);
        }

        public void bind(Beer beer) {
            this.beer = beer;
            fillViewHolderFields(beer);
        }

        private void getFieldReferences(View itemView) {
            nameField = itemView.findViewById(R.id.list_item_name);
            taglineField = itemView.findViewById(R.id.list_item_tagline);
            photoField = itemView.findViewById(R.id.list_item_photo);
            favoriteIconField = itemView.findViewById(R.id.list_favorite_icon);
        }

        private void setItemClick(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(beer);
                }
            });
        }

        private void setFavoriteButtonClick(View itemView) {
            View favoriteButton = itemView.findViewById(R.id.list_favorite_buttom);
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beer.setIsFavorite(!beer.isFavorite());
                    updateFavoriteIcon(beer);
                    onItemClickListener.onFavoriteButtomClick(beer);
                }
            });
        }

        private void fillViewHolderFields(Beer beer) {
            nameField.setText(beer.getName());
            taglineField.setText(beer.getTagline());
            updateImageUrl(beer);
            updateFavoriteIcon(beer);
        }

        private void updateFavoriteIcon(Beer beer) {
            if (beer.isFavorite())
                favoriteIconField.setImageResource(R.drawable.staron);
            else
                favoriteIconField.setImageResource(R.drawable.staroff);
        }

        private void updateImageUrl(Beer beer) {
            if(beer.getImageLocation() != null)
                Util.setImageViewContentByUrl(photoField, beer.getImageLocation(), context);
            else
                photoField.setImageResource(R.drawable.placeholder);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
