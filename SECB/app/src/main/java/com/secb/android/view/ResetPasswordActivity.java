package com.secb.android.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secb.android.R;

public class ResetPasswordActivity extends SECBBaseActivity {

    EditText edt_email;
    Button btn_resetPassword;
    public ResetPasswordActivity() {
        super(R.layout.reset_password, false);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
        initViews();
        applyFonts();
    }

    private void initViews()
    {
        edt_email = (EditText) findViewById(R.id.edt_email);
        btn_resetPassword = (Button) findViewById(R.id.btn_resetPassword);

        btn_resetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.btn_resetPassword:
                displayToast("" + edt_email.getText().toString());
            break;
        }
    }
    public void applyFonts(){
        if(edt_email!=null)
            UiEngine.applyCustomFont(edt_email, UiEngine.Fonts.HVAR);
        if(btn_resetPassword!=null)
            UiEngine.applyCustomFont(btn_resetPassword, UiEngine.Fonts.HVAR);
    }
}
