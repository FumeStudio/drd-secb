package com.secb.android.view.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.secb.android.controller.manager.Engine;
import com.secb.android.view.SECBApplication;

import net.comptoirs.android.common.helper.Logger;

public class SECBBaseFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.instance().v("SECBBaseFragnent", "oncreate called: "+getActivity(), false);
        Engine.setApplicationLanguage(getActivity(), Engine.getAppConfiguration().getLanguage());
        super.onCreate(savedInstanceState);
    }

    /*
     * hide keyboard
     */
    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * open keyboard
     */
    public void openKeyboard(View view) {
        try {
            if (view instanceof EditText) {
                EditText editText = ((EditText) view);
                editText.setFocusableInTouchMode(true);
                editText.setFocusable(true);
                editText.requestFocus();
            }
            final InputMethodManager inputMethodManager = (InputMethodManager) SECBApplication.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
