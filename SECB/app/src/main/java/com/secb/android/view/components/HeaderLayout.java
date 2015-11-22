package com.secb.android.view.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addthis.core.AddThis;
import com.addthis.core.Config;
import com.addthis.models.ATShareItem;
import com.secb.android.R;
import com.secb.android.model.ShareItemInterface;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.helper.Utilities;

public class HeaderLayout extends LinearLayout {
    RelativeLayout layoutContainerHeader;
    ImageView imageViewMenuHeader;
    ImageView imageViewBackHeader;
    ImageView imageViewShareHeader;
//    ATButton addthisShareButton;
    TextView textViewTitleHeader;

    OnClickListener menuOnClickListener;
    OnClickListener backOnClickListener;
    ShareItemInterface shareItemInterface;

    public HeaderLayout(Context context) {
        this(context, null);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header, null);

        layoutContainerHeader = (RelativeLayout) view.findViewById(R.id.layoutContainerHeader);
        imageViewMenuHeader = (ImageView) view.findViewById(R.id.imageViewMenuHeader);
        imageViewBackHeader = (ImageView) view.findViewById(R.id.imageViewBackHeader);
	    imageViewShareHeader = (ImageView) view.findViewById(R.id.imageViewShareHeader);
//        addthisShareButton = (ATButton) view.findViewById(R.id.addthisShareButton);
        textViewTitleHeader = (TextView) view.findViewById(R.id.textViewTitleHeader);
//        layoutContainerHeader.setMinimumHeight(120);// (int)getResources().getDimension(R.dimen.header_height)
        layoutContainerHeader.setMinimumHeight( (int)getResources().getDimension(R.dimen.header_height));//

        removeAllViews();
        addView(view);
        applyFonts();

        handleButtonsEvents();
    }

    private void applyFonts() {
//		UiEngine.applyCustomFont(textViewTitleHeader, UiEngine.Fonts.EUCLID_BP_BOLD);
    }

    private void handleButtonsEvents() {
        if (imageViewMenuHeader != null) {
            if (menuOnClickListener != null) {
                imageViewMenuHeader.setOnClickListener(menuOnClickListener);
                imageViewMenuHeader.setVisibility(View.VISIBLE);
            } else {
                imageViewMenuHeader.setVisibility(View.INVISIBLE);
            }
        }

        if (imageViewBackHeader != null) {
            if (backOnClickListener != null) {
                imageViewBackHeader.setOnClickListener(backOnClickListener);
                imageViewBackHeader.setVisibility(View.VISIBLE);
            } else
                imageViewBackHeader.setVisibility(View.GONE);
        }
        if (imageViewShareHeader != null) {
            if (shareItemInterface != null) {
                Config.configObject().setAddThisPubId("ra-563a8a466b38d02f");
                Config.configObject().setFacebookAppId("1648071015475595");
                Config.configObject().setTwitterConsumerKey("4ZvogbsyKFgjEFkB3jIi4I7tt");
                Config.configObject().setTwitterConsumerSecret("ZKkReMP7ep6xgk9ighBbL2PijvTmhYh901nZVtSPr2VterAiPZ");
//                Config.configObject().setShouldUseFacebookConnect(true);
//                Config.configObject().setTwitterCallbackUrl("<any-url-need-not-be-existing-one>");
//				imageViewShareHeader.setOnClickListener(shareOnClickListener);
                imageViewShareHeader.setVisibility(View.VISIBLE);
                imageViewShareHeader.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ATShareItem item = new ATShareItem("http://secb.gov.sa",shareItemInterface.getSharingTitle(), shareItemInterface.getSharingDesc());
                        AddThis.presentAddThisMenu(getContext(), item);
                    }
                });

//                addthisShareButton.setItem(item);
//                AddThis.presentAddThisMenu(getContext(), "http;//www.addthis.com", "title", "description");
            } else
                imageViewShareHeader.setVisibility(View.GONE);
        }
    }

    public void setTitleText(String title) {
        textViewTitleHeader.setText(Utilities.isNullString(title) ? "" : title);
        textViewTitleHeader.setVisibility(Utilities.isNullString(title) ? View.GONE : View.VISIBLE);
    }

    public void applyFontToTitleText(Typeface font) {
        if (textViewTitleHeader != null)
            UiEngine.applyCustomFont(textViewTitleHeader, font);
    }

    public void enableBackButton(OnClickListener backOnClickListener) {
        this.backOnClickListener = backOnClickListener;
        handleButtonsEvents();
    }

    public void disableBackButton() {
        enableBackButton(null);
    }

    public void enableMenuButton(OnClickListener menuOnClickListener) {
        this.menuOnClickListener = menuOnClickListener;
        handleButtonsEvents();
    }

    public void disableMenuButton() {
        enableMenuButton(null);
    }


    public void enableShareButton(ShareItemInterface shareItemInterface) {
        this.shareItemInterface = shareItemInterface;
        handleButtonsEvents();
    }

    public void disableShareButton() {
        enableShareButton(null);
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
    public void setMenuOnClickListener(OnClickListener menuOnClickListener) {
        this.menuOnClickListener = menuOnClickListener;
        handleButtonsEvents();
    }
}
