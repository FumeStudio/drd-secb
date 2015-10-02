package net.comptoirs.android.common.helper;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontUtils
{
	private static void processsViewGroup(ViewGroup v, final int len, Typeface typeface)
	{
		for (int i = 0; i < len; i++)
		{
			final View c = v.getChildAt(i);
			if (c instanceof TextView)
			{
				((TextView)c).setTypeface(typeface);
			}
			else if (c instanceof Button)
			{
				((Button)c).setTypeface(typeface);
			}
			else if (c instanceof EditText)
			{
				((EditText)c).setTypeface(typeface);
			}
			else if (c instanceof ViewGroup)
			{
				setCustomFont((ViewGroup) c, typeface);
			}
		}
	}
	
	public static void setCustomFont(View topView, Typeface typeface)
	{
		if (topView instanceof ViewGroup)
		{
			final int len = ((ViewGroup)topView).getChildCount();
			processsViewGroup(((ViewGroup)topView), len, typeface);
		}
		else if (topView instanceof TextView)
		{
			((TextView)topView).setTypeface(typeface);
		}
		else if (topView instanceof Button)
		{
			((Button)topView).setTypeface(typeface);
		}
		else if (topView instanceof EditText)
		{
			((EditText)topView).setTypeface(typeface);
		}
	}

}
