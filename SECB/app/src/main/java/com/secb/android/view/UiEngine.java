package com.secb.android.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class UiEngine {
	public static void initialize(Context context) {
		Fonts.TEST_FONT = null; // Typeface.createFromAsset(context.getAssets(), "fonts/test_font.otf");
		// TODO:: add ur font in assets/fonts and use it
	}

	public static class Fonts {
		public static Typeface TEST_FONT;
	}

	public static void applyCustomFont(TextView textView, Typeface typeface) {
		if (textView != null)
			textView.setTypeface(typeface);
	}

	public static void applyCustomFont(EditText editText, Typeface typeface) {
		if (editText != null)
			editText.setTypeface(typeface);
	}

	public static void applyCustomFont(Button button, Typeface typeface) {
		if (button != null)
			button.setTypeface(typeface);
	}

	public static void applyCustomFont(RadioButton button, Typeface typeface) {
		if (button != null)
			button.setTypeface(typeface);
	}

	public static void applyCustomFont(CheckBox checkBox, Typeface typeface) {
		if (checkBox != null)
			checkBox.setTypeface(typeface);
	}

	public static void applyCustomFont(WebView webView, Typeface typeface) {
		// if (webView != null) webView.setTypeface(typeface);
	}

	public static void applyCustomFont(View topView, Typeface typeface) {
		if (topView instanceof ViewGroup) {
			final int len = ((ViewGroup) topView).getChildCount();
			processsViewGroup(((ViewGroup) topView), len, typeface);
		} else if (topView instanceof TextView) {
			applyCustomFont((TextView) topView, typeface);
		}
	}

	private static void processsViewGroup(ViewGroup v, final int len,
			Typeface typeface) {
		for (int i = 0; i < len; i++) {
			final View c = v.getChildAt(i);
			if (c instanceof TextView) {
				applyCustomFont((TextView) c, typeface);
			} else if (c instanceof ViewGroup) {
				applyCustomFont((ViewGroup) c, typeface);
			}
		}
	}
}
