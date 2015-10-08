package com.secb.android.view.components.recycler_click_handlers;

import android.view.View;

public interface RecyclerCustomClickListener
{
    void onItemClicked(View v , int position);
    void onItemLongClicked(View v , int position);
}
