package com.capivas.mybeers.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.capivas.mybeers.R;
import com.capivas.mybeers.model.Beer;
import com.capivas.mybeers.util.Util;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Beer beer = (Beer) intent.getSerializableExtra("beer");

        ImageView photoField = findViewById(R.id.detail_photo);
        if(beer.getImageLocation() != null)
            Util.setImageViewContentByUrl(photoField, beer.getImageLocation(), this);
        else
            photoField.setImageResource(R.drawable.placeholder);

        TextView nameField = findViewById(R.id.detail_name);
        nameField.setText(beer.getName());

        TextView taglineField = findViewById(R.id.detail_tagline);
        taglineField.setText(beer.getTagline());

        TextView descriptionField = findViewById(R.id.detail_description);
        descriptionField.setText(beer.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
