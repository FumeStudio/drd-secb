package com.secb.android.view.components.recycler_item_click_handlers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import net.comptoirs.android.common.helper.Logger;

public class RecyclerCustomItemTouchListener implements RecyclerView.OnItemTouchListener {
    RecyclerCustomClickListener recyclerCustomClickListener;
    GestureDetector gestureDetector;
    String TAG = "RecyclerCustomItemTouchListener";

    public RecyclerCustomItemTouchListener(Context context, final RecyclerView recyclerView, final RecyclerCustomClickListener recyclerCustomClickListener) {
        this.recyclerCustomClickListener = recyclerCustomClickListener;
        Logger.instance().v(TAG, "constructor", false);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Logger.instance().v(TAG, "onSingleTap", false);
                return true;
            }

            //listen to the long click event
            @Override
            public void onLongPress(MotionEvent e)
            {
                super.onLongPress(e);
                Logger.instance().v(TAG, "onLongPress", false);

                //find which child view of recyclerView is long pressed
                View child = recyclerView.findChildViewUnder(e.getX(),e.getY());

                //notify listener with long click
                if(child!=null&&recyclerCustomClickListener!=null)
                    recyclerCustomClickListener.onItemLongClicked(child,recyclerView.getChildAdapterPosition(child));

            }
        }
        );
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
//listen to the short click event

        boolean b=gestureDetector.onTouchEvent(e);
        Logger.instance().v(TAG, "onInterceptTouchEvent gestureDetector.onTouchEvent(e) = "+b, false);
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (recyclerCustomClickListener != null && child != null && b)
        {
            recyclerCustomClickListener.onItemClicked(child, rv.getChildAdapterPosition(child));
	        return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Logger.instance().v(TAG, "onTouchEvent", false);
    }
}
