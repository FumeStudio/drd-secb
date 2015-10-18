package com.secb.android.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.secb.android.R;

import net.comptoirs.android.common.helper.SharedPreferenceData;
import net.comptoirs.android.common.helper.Utilities;

public class LanguageActivity extends SECBBaseActivity {

    Button btn_arabic, btn_english;
    SharedPreferenceData sharedPreferenceData;
    String prefsLanguage;
    public LanguageActivity()
    {
        super(R.layout.language_activity, false);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {

        sharedPreferenceData = new SharedPreferenceData( LanguageActivity.this);
        applyPreferredLanguage();

        initViews();
        applyFonts();
    }

    private void applyPreferredLanguage() {
//        prefsLanguage = (String) sharedPreferenceData.get("language",String.class);

        prefsLanguage = getSavedLanguageSetting();
        if(!Utilities.isNullString(prefsLanguage));
        {
            if(prefsLanguage.equalsIgnoreCase("en")) {
                changeAppLanguage(true);
                finish();
            }
            else  if(prefsLanguage.equalsIgnoreCase("ar"))
            {
                changeAppLanguage(false);
                finish();
            }
        }
    }

    private void initViews()
    {
        btn_arabic = (Button) findViewById(R.id.btn_arabic);
        btn_english = (Button) findViewById(R.id.btn_english);


        btn_arabic.setOnClickListener(this);
        btn_english.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.btn_english:
                changeAppLanguage(true);
                break;
            case R.id.btn_arabic:
                changeAppLanguage(false);
                break;
        }
    }

    public void applyFonts(){
       UiEngine.applyFontsForAll(this,findViewById(R.id.tablerow_languageButtons), UiEngine.Fonts.HVAR);
    }

}
