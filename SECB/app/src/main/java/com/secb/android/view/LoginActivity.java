package com.secb.android.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.secb.android.R;

public class LoginActivity extends SECBBaseActivity {

    EditText edt_email,edt_password;
    TextView txtv_forgetPassword;
    Button btn_login, btn_signUp;

    public LoginActivity() {
        super(R.layout.login, false);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
        initViews();
    }

    private void initViews()
    {
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        txtv_forgetPassword = (TextView) findViewById(R.id.txtv_forgetPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signUp = (Button) findViewById(R.id.btn_signUp);

        txtv_forgetPassword.setOnClickListener(this);
        txtv_forgetPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_login.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.txtv_forgetPassword:
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
            case R.id.btn_signUp:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        displayToast("language changed successfully");
    }

}
