package com.secb.android.view.components;

import android.view.View;

//recycler item click listener===================================================
public  interface RecyclerClickListener
{
    void onItemClicked(View v, int position);
    void onItemLongClicked(View v, int position);
}