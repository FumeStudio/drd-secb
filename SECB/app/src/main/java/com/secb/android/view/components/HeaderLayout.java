package com.secb.android.view.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.secb.android.R;

import net.comptoirs.android.common.helper.Utilities;

public class HeaderLayout extends LinearLayout
{
	RelativeLayout layoutContainerHeader;
	ImageView imageViewMenuHeader;
	ImageView imageViewBackHeader;
	TextView textViewTitleHeader;
	
	OnClickListener menuOnClickListener;
	OnClickListener backOnClickListener;
	public HeaderLayout(Context context)
  {
	  this(context, null);
  }
	public HeaderLayout(Context context, AttributeSet attrs)
  {
	  super(context, attrs);
	  init();
  }
	private void init()
  {
	  View view = LayoutInflater.from(getContext()).inflate(R.layout.header, null);
	  
	  layoutContainerHeader = (RelativeLayout) view.findViewById(R.id.layoutContainerHeader);
	  imageViewMenuHeader = (ImageView) view.findViewById(R.id.imageViewMenuHeader);
	  imageViewBackHeader = (ImageView) view.findViewById(R.id.imageViewBackHeader);
	  textViewTitleHeader = (TextView) view.findViewById(R.id.textViewTitleHeader);
	  layoutContainerHeader.setMinimumHeight(120);// (int)getResources().getDimension(R.dimen.header_height)
	  
	  removeAllViews();
	  addView(view);
	  applyFonts();
	  
	  handleButtonsEvents();
  }

	private void applyFonts()
  {
//		UiEngine.applyCustomFont(textViewTitleHeader, UiEngine.Fonts.EUCLID_BP_BOLD);
  }
	private void handleButtonsEvents()
	{
		if(imageViewMenuHeader != null  )
		{
			if(menuOnClickListener != null)
			{
				imageViewMenuHeader.setOnClickListener(menuOnClickListener);
				imageViewMenuHeader.setVisibility(View.VISIBLE);
			}
			else {
				imageViewMenuHeader.setVisibility(View.GONE);
			}
		}
		
		if(imageViewBackHeader != null)
		{
			if(backOnClickListener != null)
			{
				imageViewBackHeader.setOnClickListener(backOnClickListener);
				imageViewBackHeader.setVisibility(View.VISIBLE);
			}
			else
				imageViewBackHeader.setVisibility(View.GONE);
		}
	}
	
	public void setTitleText(String title)
	{
		textViewTitleHeader.setText(Utilities.isNullString(title) ? "" : title);
		textViewTitleHeader.setVisibility(Utilities.isNullString(title) ? View.GONE : View.VISIBLE);
	}
	
	public void enableBackButton(OnClickListener backOnClickListener)
	{
		this.backOnClickListener = backOnClickListener;
		handleButtonsEvents();
	}
	public void disableBackButton()
	{
		enableBackButton(null);
	}
	public void enableMenuButton(OnClickListener menuOnClickListener)
	{
		this.menuOnClickListener = menuOnClickListener;
		handleButtonsEvents();
	}
	public void disableMenuButton()
	{
		enableMenuButton(null);
	}
	
	public void setHeaderBGColor(int color) {
		layoutContainerHeader.setBackgroundColor(color);
	}
	public void setHeaderDefaultBGColor() {
		setHeaderBGColor(getContext().getResources().getColor(R.color.gba_black));
	}
	
	/*
	 * Setters & Getters
	 */
	public void setMenuOnClickListener(OnClickListener menuOnClickListener)
  {
	  this.menuOnClickListener = menuOnClickListener;
	  handleButtonsEvents();
  }
}
