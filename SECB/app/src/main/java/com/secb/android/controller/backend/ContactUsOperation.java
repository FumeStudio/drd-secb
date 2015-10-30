package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.User;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import org.apache.http.client.methods.HttpGet;

import java.util.HashMap;

public class ContactUsOperation extends BaseOperation {
    private static final String TAG = "ForgetPasswordOperation";
    Context context;
	User userData;
    public ContactUsOperation(int requestID,
                              boolean isShowLoadingDialog,
                              Context context, User userData)
    {
        super(requestID, isShowLoadingDialog, context);
        this.context = context;
        this.userData = userData;
    }


    @Override
    public Object doMain() throws Exception
    {
	    if(userData==null)
		    return null;
        StringBuilder stringBuilder;
            stringBuilder = new StringBuilder(ServerKeys.CONTACT_US);
	    String language = UiEngine.getCurrentAppLanguage(context);

	    if(Utilities.isNullString(language))
		    language=UiEngine.getCurrentDeviceLanguage(context);

        stringBuilder.append("?Lang="+language
		        +"&userName=" + userData.userName
		        +"&Phone=" + userData.phoneNumber
		        +"&Email=" + userData.emailAddress
		        +"&Organization=" + userData.organization
		        +"&JobTitle=" + userData.jobTitle
		        +"&Subject=" + userData.subject
        );
        String requestUrl = stringBuilder.toString();
	    requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
        HashMap<String, String> cookies = new HashMap<>();

        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);
	    CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG, response.response);
        return response;
    }

}
