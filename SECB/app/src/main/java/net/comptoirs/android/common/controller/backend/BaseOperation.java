package net.comptoirs.android.common.controller.backend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.secb.android.view.components.dialogs.CustomProgressDialog;

import net.comptoirs.android.common.controller.CTOperationResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection.ResponseType;

import org.apache.http.HttpEntity;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseOperation<T> extends AsyncTask<Object, Object, CTOperationResponse>
{
	
	private static HashMap<String, BaseOperation<?>> activeOperations = new HashMap<String, BaseOperation<?>>();
	private static HashMap<Object, BaseOperation<?>> activeOperationsMapByRequstId = new HashMap<Object, BaseOperation<?>>();
	
	public static final int UNHANDLED_EXCEPTION_STATUS_CODE = 1001;

	ArrayList<RequestObserver> observersList;

	protected ServerConnection serverConnection;

	protected boolean isShowLoadingDialog = true;
	protected Context context;
//	private CustomProgressDialog dialog;
	private ProgressDialog dialog;

	protected Object requestID = 0;

	public BaseOperation(Object requestID, boolean isShowLoadingDialog, Context activity)
	{
		this.isShowLoadingDialog = isShowLoadingDialog;
		this.context = activity;
		this.requestID = requestID;

		serverConnection = new ServerConnection();
		observersList = new ArrayList<RequestObserver>();
	}

	/**
	 * Do/Execute the operation itself
	 * 
	 * @return the object
	 * @throws Exception
	 */
	public abstract T doMain() throws Throwable;

	 protected void showWaitingDialog()
	 {
		 if(dialog ==null)
			 dialog =CustomProgressDialog.getInstance(context,true);

		 dialog.setCanceledOnTouchOutside(false);
		if (!dialog.isShowing())
			dialog.show();

		 dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			 @Override
			 public void onCancel(DialogInterface dialog) {
				 Log.v("Dialog", "Canceled ");
				 cancelConn();
				 // Wake observers with the result
				 for (RequestObserver observer : observersList) {
					 observer.requestCanceled((int)requestID, null);
				 }
				 BaseOperation.this.cancel(true);

			 }
		 });

		 try {
			 if (!dialog.isShowing())
				 dialog.show();
		 } catch (Exception e) {
		 } // Show dialog on activity killed
	 }
	
	@Override
	protected void onPreExecute()
	{
		activeOperations.put(this.getClass().getName(), this);
		if(requestID != null)
			activeOperationsMapByRequstId.put((int)requestID, this);
		super.onPreExecute();

		if (isShowLoadingDialog)
		{
			showWaitingDialog();
		}
		
	}


	@Override
	protected CTOperationResponse doInBackground(Object... params)
	{
		CTOperationResponse response = new CTOperationResponse();
		try
		{
			response.response = doMain();
		}
		catch (Throwable t)
		{
			if (!(t instanceof CTHttpError)) t.printStackTrace();
			

			response.error = t;
		}
		
		
		return response;
	}

	private void cancelConn() {
		dismiss();
		disconnect();
	}

	private void dismiss()
	{
		try {
			if (isShowLoadingDialog && dialog.isShowing()) dialog.dismiss();
		}
		catch (Exception e) {
		} // Dismiss on activity killed
	}

	public void disconnect() {
		if (serverConnection != null) serverConnection.cancelConnection();
	}


	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		if (isShowLoadingDialog && dialog.isShowing()) dialog.dismiss();
	}

	@Override
	protected void onPostExecute(CTOperationResponse result)
	{
		activeOperations.remove(this.getClass().getName());
		if(requestID != null)
			activeOperationsMapByRequstId.remove(requestID);
		
		super.onPostExecute(result);
		try{
			if (isShowLoadingDialog && dialog.isShowing()) 
				dialog.dismiss();
		}catch(Exception ex){
			//ignore exception, as this happens sometimes 
			ex.printStackTrace();
		}
		
		doOnPostExecute(result);
		// Wake observers with the result
		for (RequestObserver observer : observersList){
			
			observer.handleRequestFinished(requestID, result.error, result.response);
		}
	}

	public static BaseOperation<?> getActiveOperation(Class<? extends BaseOperation<?>> operationClass){
		return activeOperations.get(operationClass.getName());
	}
	
	public static BaseOperation<?> getActiveOperationByRequestId(Object requestId){
		
		if(requestId != null)
			return activeOperationsMapByRequstId.get(requestId);		
		return null;
	}


	protected void doOnPostExecute(CTOperationResponse result){
		
	}
	/**
	 * Execute http request
	 * 
	 * @param requestUrl
	 *          URL for the request
	 * @param methodType
	 *          GET/POST/PUT etc..
	 * @param contentType
	 *          Content Type
	 * @param additionalHeaders
	 *          any headers to be applied for the request
	 * @param bodyEntity
	 *          if it was a Post request and need a body
	 * @param responseType
	 *          Byte/String
	 * @return the CTHttpResponse object
	 */
	public CTHttpResponse doRequest(String requestUrl, String methodType, final String contentType,
	  final HashMap<String, String> additionalHeaders,  final HashMap<String, String> cookies, final HttpEntity bodyEntity, final ResponseType responseType)
	{
		CTHttpResponse response = serverConnection.sendRequestToServer(requestUrl, methodType, contentType,
		  additionalHeaders, cookies,null, bodyEntity, responseType);

		ensureHTTPRequestSucceeded(response);
		return response;
	}
	public CTHttpResponse doRequest(String requestUrl, String methodType, final String contentType,
									final HashMap<String, String> additionalHeaders,final HashMap<String, String> cookies, HttpParams params, final HttpEntity bodyEntity, final ResponseType responseType)
	{
		CTHttpResponse response = serverConnection.sendRequestToServer(requestUrl, methodType, contentType,
				additionalHeaders,cookies ,params, bodyEntity, responseType);

		if(params == null || response.statusCode != 302)ensureHTTPRequestSucceeded(response);
		return response;
	}

	/*
	 * ******************************************************************
	 * ********************** Observers Handling ************************
	 * ******************************************************************
	 */
	/*
	 * Add Request Observer to List
	 */
	public BaseOperation<T> addRequsetObserver(RequestObserver requestObserver)
	{
		// remove the observer if it was already added here
		removeRequestObserver(requestObserver);
		// add to observers List
		observersList.add(requestObserver);
		
		return this;
	}

	/*
	 * Remove Request Observer from the list
	 */
	public void removeRequestObserver(RequestObserver requestObserver)
	{
		observersList.remove(requestObserver);
	}

	// //////////////// End of observers handling /////////////////////

	/*
	 * Check if the response is Valid HTTP Response
	 */
	protected void ensureHTTPRequestSucceeded( CTHttpResponse response)
	{
		if (response == null)
		{
			throw new RuntimeException("Invalid Response Object while processing operation ["
			  + this.getClass().getName() + "]");
		}

		if (response.statusCode != HttpURLConnection.HTTP_OK && response.statusCode != HttpURLConnection.HTTP_CREATED
		  && response.statusCode != HttpURLConnection.HTTP_ACCEPTED)
		{
			throw new CTHttpError(response.statusMessage, response.statusCode);
		}
	}

	/*
	 * Check if the JSON response is succeeded
	 */
	protected static void ensureRequestSucceeded(JSONObject responseJSON)
	{
//		if (responseJSON != null)
//		{
//			String statusCode = responseJSON.optString(ServerKeys.STATUS_CODE);
//			String statusMessage = responseJSON.optString(ServerKeys.STATUS_MESSAGE);
//
//			if (!Utilities.isNullString(statusCode))
//			{
//				double status = Double.valueOf(statusCode);
//				if (status != HttpURLConnection.HTTP_OK && status != HttpURLConnection.HTTP_CREATED
//				  && status != HttpURLConnection.HTTP_ACCEPTED)
//					throw new CTHttpError(statusMessage, Double.valueOf(statusCode));
//			}
//		}
	}

	/*
	 * Setters & Getters
	 */
	public void setShowLoadingDialog(boolean isShowLoadingDialog)
	{
		this.isShowLoadingDialog = isShowLoadingDialog;
	}

	public boolean isShowLoadingDialog()
	{
		return isShowLoadingDialog;
	}

	public HashMap<String, String> getAdditionalHeaders()
	{
		HashMap<String, String> additionalHeaders = new HashMap<String, String>();

		return additionalHeaders;
	}
}
