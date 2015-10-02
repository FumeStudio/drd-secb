package com.secb.android.view.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.view.SECBBaseActivity;

public class MenuFragment extends Fragment implements View.OnClickListener {
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = view.getParent();
            if (oldParent != container) {
                ((ViewGroup) oldParent).removeView(view);
            }
            return view;
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.menu, container, false);

            handleButtonsEvents();
            applyFonts();

            return view;
        }
    }

    private void handleButtonsEvents() {
        // TODO:: add clicklistener on the buttons
//		imageViewHome.setOnClickListener(this); 				// Home
    }

    private void applyFonts() {
        // TODO:: apply fonts for all texts
//		UiEngine.applyCustomFont(layoutCategoriesMenu, UiEngine.Fonts.EUCLID_BP_BOLD);
//		UiEngine.applyCustomFont(layoutRandomBlogMenu, UiEngine.Fonts.EUCLID_BP_BOLD);
    }

    @Override
    public void onClick(View v) {
        ((SECBBaseActivity) getActivity()).onMenuItemPressed(v.getId());
    }
}
