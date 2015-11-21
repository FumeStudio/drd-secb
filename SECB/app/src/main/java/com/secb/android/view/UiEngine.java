package com.secb.android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.manager.Engine;
import com.secb.android.model.AppConfiguration;

import net.comptoirs.android.common.view.CTApplication;

import java.util.Locale;

public class UiEngine {
	public static void initialize(Context context)
	{

		Fonts.HVAR = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTArabic-Roman.ttf");
		Fonts.HVAR_BOLD = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTArabic-Bold.ttf");

		Fonts.HVCN = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTPro-HvCn.otf");
		Fonts.BDCN = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTStd-BdCn.otf");
		Fonts.CN = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTStd-Cn.otf");
		Fonts.LTCN = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf");
	}


	//return color represents the value to be used in progress wheel , bar chart
	public static int getValueColor(int mval)
	{
		if (mval >= 0 && mval < 35)
		{
			return Color.parseColor("#ff0000");
		}
		else if (mval >= 35 && mval < 75)
		{
			return Color.parseColor("#ffaa00");
		}
		else if (mval>= 75 && mval <= 100)
		{
			return Color.parseColor("#57de57");
		}

		return 0;
	}

	public static Locale getCurrentAppLocale()
	{
		AppConfiguration appConfig  = Engine.getAppConfiguration();
		if(appConfig!=null){
			return appConfig.getLocale();
		}
		Context context = CTApplication.getContext();
		Locale locale = new Locale(Engine.getAppConfiguration().getLanguage());
				/*context.getResources().getConfiguration().locale;*/
		return locale;
	}


	public static class Fonts {
		public static Typeface HVAR_BOLD ;
		public static Typeface HVAR;


		public static Typeface HVCN;
		public static Typeface BDCN;
		public static Typeface CN;
		public static Typeface LTCN;
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

	public static boolean isAppLanguageArabic(Context context) {
		return Engine.isCurrentLanguageArabic(context)/*getCurrentAppLanguage(context).equalsIgnoreCase("ar")*/;
	}

	public static String getCurrentAppLanguage(Context context) {
		return Engine.getAppConfiguration().getLanguage();
//		SharedPreferenceData sharedPreferenceData = new SharedPreferenceData(context);
//		return (String)sharedPreferenceData.get("language",String.class);
	}


	public static boolean isDeviceLanguageArabic(Context context) {
		return getCurrentDeviceLanguage(context).equalsIgnoreCase("ar");
	}

	public static String getCurrentDeviceLanguage(Context context) {
		return context.getResources().getConfiguration().locale.getLanguage();
	}


	public static void applyFontsForAll(final Context context, final View v, Typeface typeface) {
		try
		{
			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					applyFontsForAll(context, child, typeface);
				}
			} else if (v instanceof TextView || v instanceof EditText || v instanceof RadioButton ||
					v instanceof CheckBox || v instanceof Button) {
				applyCustomFont(v, typeface);
			}
		}
		catch (Exception e) {
		}
	}


	public static void applyTextColorForAll(View v , int color){
		try
		{
			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					applyTextColorForAll(child, color);
				}
			} else if (v instanceof TextView || v instanceof EditText || v instanceof RadioButton ||
					v instanceof CheckBox || v instanceof Button)
			{
				if(v instanceof TextView) ((TextView)v).setTextColor(color);
				else if(v instanceof EditText) ((EditText)v).setTextColor(color);
				else if(v instanceof RadioButton) ((RadioButton)v).setTextColor(color);
				else if(v instanceof CheckBox) ((CheckBox)v).setTextColor(color);
				else if(v instanceof Button) ((Button)v).setTextColor(color);
			}
		}
		catch (Exception e) {
		}
	}
	public static void setListItemSelected(View view) {
		Resources res = CTApplication.getContext().getResources();
		view.setBackgroundColor(res.getColor(R.color.blue_color));
		applyTextColorForAll(view,res.getColor(R.color.white));
	}


}
