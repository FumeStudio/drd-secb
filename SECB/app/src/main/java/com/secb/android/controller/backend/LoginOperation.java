package com.secb.android.controller.backend;

import android.app.Activity;

import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.User;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.HashMap;

public class LoginOperation extends BaseOperation {
    private static final String TAG ="LoginOperation" ;
    String loginXml;
    boolean rememberMe;

    User userToLogin ;


    public LoginOperation(int requestID, boolean isShowLoadingDialog,
                          Activity activity, User userToLogin , boolean remeberMe)
    {
        super(requestID, isShowLoadingDialog, activity);
        this.userToLogin = userToLogin;
        this.rememberMe = remeberMe;
    }


    @Override
    public Object doMain() throws Exception
    {
        LoginXmlGenerator loginXmlGenerator = new LoginXmlGenerator(userToLogin);
        loginXml = loginXmlGenerator.getLoginXml();

        String requestUrl = ServerKeys.LOGIN_URL;
        HashMap<String, String> additionalHeaders = getAdditionalHeaders();
        additionalHeaders.put("content-type","text/xml; charset=utf-8");
        additionalHeaders.put("SOAPAction", "http://schemas.microsoft.com/sharepoint/soap/Login");

        StringEntity bodyEntity = new StringEntity(loginXml);
        bodyEntity.setContentType("text/xml");

        CTHttpResponse response = doRequest(requestUrl, HttpPost.METHOD_NAME, null, additionalHeaders,null, bodyEntity, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG,response.response);

        //get cookie

//        String cookieValue = loginXmlGenerator.getCookieFromLoginXml(response.response+"");
	    String cookieValue = (response.responseHeaders.get("Set-Cookie"));
        if(!Utilities.isNullString(cookieValue))
            userToLogin.loginCookie = cookieValue;

        UserManager.getInstance().saveUser(userToLogin);
        Logger.instance().v(TAG,"cookieName = "+cookieValue);
        return userToLogin;
    }



}
