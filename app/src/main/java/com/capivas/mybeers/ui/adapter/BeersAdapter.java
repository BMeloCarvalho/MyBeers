package com.capivas.mybeers.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.util.Util;

import java.util.List;

public class BeersAdapter extends BaseAdapter {
    private final Context context;
    private final List<Beer> beers;

    public BeersAdapter(Context context, List<Beer> beers) {
        this.context = context;
        this.beers = beers;
    }

    @Override
    public int getCount() {
        return beers.size();
    }

    @Override
    public Object getItem(int position) {
        return beers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return beers.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Beer beer = beers.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.item_beer, parent, false);
        }

        TextView nameField = view.findViewById(R.id.list_item_name);
        nameField.setText(beer.getName());

        TextView taglineField = view.findViewById(R.id.list_item_tagline);
        taglineField.setText(beer.getTagline());

        ImageView photoField = view.findViewById(R.id.list_item_photo);
        Util.setImageViewContentByUrl(photoField, beer.getImageLocation(), context);

        return view;
    }
}