package com.capivas.mybeers.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.capivas.mybeers.model.Beer;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Beer> getStaticBeersList() {
        Beer beer1 = new Beer(1L, "Polka Freats",
                "A que desce oval",
                "Uma cerveja feita pelos melhores, para os melhores. Qualidade garantida",
                "https://images.punkapi.com/v2/25.png");
        Beer beer2 = new Beer(1L, "Itamira",
                "A melhor do Brasil",
                "Uma cerveja feita com carinho para quem aprecia. Garantia de satisfação",
                "https://images.punkapi.com/v2/24.png");
        Beer beer3 = new Beer(1L, "Budmagger",
                "O melhor da alemanha, no Brasil!",
                "Uma cerveja com tradição, feita por quem mais entende.",
                "https://images.punkapi.com/v2/23.png");

        List<Beer> beers = new ArrayList<>();
        beers.add(beer1);
        beers.add(beer2);
        beers.add(beer3);

        return beers;
    }

    public static void setImageViewContentByUrl(ImageView imageView, String url, Context context) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }
}