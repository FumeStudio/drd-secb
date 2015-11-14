package com.secb.android.view;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.LoginOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.User;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

public class LoginActivity extends SECBBaseActivity implements RequestObserver {

	private static final String TAG = "LoginActivity";
	EditText edt_userName, edt_password;
	TextView txtv_forgetPassword;
	Button btn_login, btn_signUp;

	public LoginActivity() {
		super(R.layout.login, false);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initViews();
		applyFonts();
	}

	private void initViews() {
		edt_userName = (EditText) findViewById(R.id.edt_email);
		edt_password = (EditText) findViewById(R.id.edt_password);

//        edt_userName.setText("test741"); // secb01 , test741  , tawfik
//        edt_password.setText("test741"); // Secb01 , test741 , t123456

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
		switch (v.getId()) {
			case R.id.txtv_forgetPassword:
				startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
				break;
			case R.id.btn_login:
				if (validateInputFields()) {
					//get cached user
					if (UserManager.getInstance().getUser() != null &&
							!Utilities.isNullString(UserManager.getInstance().getUser().loginCookie)) {
						handleRequestFinished(RequestIds.LOGIN_REQUEST_ID, null, UserManager.getInstance().getUser());
					} else {
						startLoginOperation();
					}
				}
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));
				break;
			case R.id.btn_signUp:
				startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
				break;
		}
	}

	private boolean validateInputFields() {
//		boolean isEmailValid = Utilities.isValidEmail(edt_userName.getText().toString());
		boolean isEmailValid = !Utilities.isNullString(edt_userName.getText().toString());
		boolean isPasswordValid = !Utilities.isNullString(edt_password.getText().toString());
		if (!isEmailValid)
			edt_userName.setError(getString(R.string.error_empty_userName));
		if (!isPasswordValid)
			edt_password.setError(getString(R.string.error_empty_password));




//            if both editTexts are empty the error msg appears on the
//            focused edit text so remove focus from both editTexts
//            and re-enable them

		edt_userName.setFocusable(false);
		edt_password.setFocusable(false);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				edt_userName.setFocusable(true);
				edt_password.setFocusable(true);
				edt_userName.setFocusableInTouchMode(true);
				edt_password.setFocusableInTouchMode(true);
			}
		}, 10);
		
		
		return isEmailValid & isPasswordValid;
	}

	public void applyFonts() {
		if (edt_userName != null)
			UiEngine.applyCustomFont(edt_userName, UiEngine.Fonts.HVAR);
		if (edt_password != null)
			UiEngine.applyCustomFont(edt_password, UiEngine.Fonts.HVAR);
		if (txtv_forgetPassword != null)
			UiEngine.applyCustomFont(txtv_forgetPassword, UiEngine.Fonts.HVAR);
		if (btn_login != null)
			UiEngine.applyCustomFont(btn_login, UiEngine.Fonts.HVAR);
		if (btn_signUp != null)
			UiEngine.applyCustomFont(btn_signUp, UiEngine.Fonts.HVAR);
	}


	private void startLoginOperation() {
		User user = new User();
		user.userName = edt_userName.getText().toString();
		user.password = edt_password.getText().toString();

		boolean rememberMe = true;
		LoginOperation operation = new LoginOperation(RequestIds.LOGIN_REQUEST_ID, true, LoginActivity.this, user, rememberMe);
		operation.addRequsetObserver(this);
		operation.execute();


	}

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if (error == null) {
			Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
			if ((int) requestId == RequestIds.LOGIN_REQUEST_ID && resultObject != null &&
					!Utilities.isNullString(((User) resultObject).loginCookie)) {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			} else {
				ErrorDialog.showMessageDialog(null, getString(R.string.login_failed), LoginActivity.this);
			}
		} else if (error != null && error instanceof CTHttpError) {
			int statusCode = ((CTHttpError) error).getStatusCode();
			String errorMsg = ((CTHttpError) error).getErrorMsg();
			if (RequestHandler.isRequestTimedOut(statusCode)) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), LoginActivity.this);
			} else if (statusCode == -1) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						LoginActivity.this);
			}

			Logger.instance().v(TAG, error);
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}
}
