package com.capivas.mybeers.ui.listener;

import com.capivas.mybeers.model.Beer;

public interface OnItemClickListener {
    void onItemClick(Beer beer);
    void onFavoriteButtomClick(Beer beer);
}
