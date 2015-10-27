package com.secb.android.controller.backend;

import android.content.Context;

import com.secb.android.controller.manager.UserManager;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import org.apache.http.client.methods.HttpGet;

import java.util.HashMap;

public class ForgetPasswordOperation extends BaseOperation {
    private static final String TAG = "ForgetPasswordOperation";
    Context context;
    String email;
    public ForgetPasswordOperation(int requestID,
                                   boolean isShowLoadingDialog,
                                   Context context, String email)
    {
        super(requestID, isShowLoadingDialog, context);
        this.context = context;
        this.email = email;
    }


    @Override
    public Object doMain() throws Exception
    {
	    if(Utilities.isNullString(email))
		    return null;
        StringBuilder stringBuilder;
            stringBuilder = new StringBuilder(ServerKeys.FORGET_PASSWORD);

//        email = Uri.encode(email, ServerKeys.ALLOWED_URI_CHARS);
        stringBuilder.append("?userName=" + email);
        String requestUrl = stringBuilder.toString();

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);
	    cookies =null;
	    CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG, response.response);
        return response;
    }

}
