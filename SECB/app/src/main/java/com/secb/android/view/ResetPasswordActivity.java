package com.secb.android.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secb.android.R;
import com.secb.android.controller.backend.ForgetPasswordOperation;
import com.secb.android.controller.backend.RequestIds;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

public class ResetPasswordActivity extends SECBBaseActivity implements RequestObserver{

	private static final String TAG = "ResetPasswordActivity";
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

//		edt_email.setText("secbadmin");
        btn_resetPassword = (Button) findViewById(R.id.btn_resetPassword);
        btn_resetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.btn_resetPassword:
	            if(validateInputFields())
//	            displaySuccessMessage();
		            sendResetEmail();
            break;
        }
    }

	private void sendResetEmail()
	{
		hideKeyboard(edt_email);
		ForgetPasswordOperation operation = new ForgetPasswordOperation(RequestIds.FORGET_PASSWORD_REQUEST_ID,true,this, edt_email.getText().toString());
		operation.addRequsetObserver(this);
		operation.execute();
//		displayToast(getString(R.string.password_reset_done) + "\n" + edt_email.getText().toString());
	}

	public void applyFonts(){
        if(edt_email!=null)
            UiEngine.applyCustomFont(edt_email, UiEngine.Fonts.HVAR);
        if(btn_resetPassword!=null)
            UiEngine.applyCustomFont(btn_resetPassword, UiEngine.Fonts.HVAR);
    }
	private boolean validateInputFields()
	{
		boolean isUsernameValid = !Utilities.isNullString(edt_email.getText().toString());

		if(!isUsernameValid)
			edt_email.setError(getString(R.string.error_empty_userName));

//		if both editTexts are empty the error msg appears on the
//      focused edit text so remove focus from both editTexts
//      and re-enable them

//		edt_email.setFocusable(false);

//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				edt_email.setFocusable(true);
//			}
//		}, 10);
		return isUsernameValid;
	}

	public void displaySuccessMessage(){
		String msg = getString(R.string.password_reset_done);
		msg = String.format(msg, edt_email.getText().toString());
		ErrorDialog.showMessageDialog(getString(R.string.success),
				msg,
				ResetPasswordActivity.this);
	}


	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
		if (error==null){
			if((int)requestId==RequestIds.FORGET_PASSWORD_REQUEST_ID &&
					resulObject!=null &&
					((CTHttpResponse)resulObject).statusCode==200)
			{
				displaySuccessMessage();

			}
		}
		else if (error != null && error instanceof CTHttpError)
		{
			int statusCode = ((CTHttpError) error).getStatusCode();
			String errorMsg = ((CTHttpError) error).getErrorMsg();
			if (RequestHandler.isRequestTimedOut(statusCode))
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), ResetPasswordActivity.this);
			}
			else if (statusCode == -1)
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						ResetPasswordActivity.this);
			}
			else
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), errorMsg,
						ResetPasswordActivity.this);
			}

			Logger.instance().v(TAG,error);
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}
}
