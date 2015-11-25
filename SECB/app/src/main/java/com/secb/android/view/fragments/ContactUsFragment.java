package com.secb.android.view.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.secb.android.R;
import com.secb.android.controller.backend.ContactUsOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.model.CompanyProfile;
import com.secb.android.model.User;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.MapsHelper;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ContactUsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener, OnMapReadyCallback, RequestObserver {

	private static final String TAG ="ContactUsFragment";
	CompanyProfile companyProfile;
   /* User user;*/

    TextView txtv_location;
    TextView txtv_email;
    TextView txtv_phone;
    TextView txtv_fax;
    ImageView imgv_facebook;
    ImageView imgv_twitter;
    ImageView imgv_google;
    ImageView imgv_linkedin;

    EditText edtxt_name;
    EditText edtxt_mobile;
    EditText edtxt_organization;
    EditText edtxt_email;
    EditText edtxt_job;
    EditText edtxt_subject;

    Button btn_send;

    View view;
    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;
	private boolean isContactUsDone;


	public static ContactUsFragment newInstance() {
        ContactUsFragment fragment = new ContactUsFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.contactus));
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();

	    initMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null) {
                ((ViewGroup) oldParent).removeView(view);
            }
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.contact_us, container, false);
            handleButtonsEvents();
            applyFonts(view);
        }

        /*user = new User();*/

        initViews(view);
        getCompanyProfile();
        bindViews();
        applyFonts(view);
        return view;
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts(View view) {
        UiEngine.applyFontsForAll(getActivity(), view, UiEngine.Fonts.HVAR);
    }

    private void goBack() {
        String backStateName = this.getClass().getName();
//     ((SECBBaseActivity) getActivity()).finishFradgmentOrActivity();
        ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(backStateName, true);
    }

    // ////////////////////////////////////////////////////////////

    @Override
    public void onBack() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewBackHeader:
                onBack();
                break;

            case R.id.txtv_email:
                sendCompanyEmail();
                break;

            case R.id.txtv_phone:
                callCompanyPhone();
                break;

            case R.id.imgv_facebook:
                openCompanyPage(getResources().getString(R.string.secb_facebook));
                break;

            case R.id.imgv_twitter:
                openCompanyPage(getResources().getString(R.string.secb_twitter));
                break;

            case R.id.imgv_google:
                openCompanyPage(getResources().getString(R.string.secb_google));
                break;

            case R.id.imgv_linkedin:
                openCompanyPage(getResources().getString(R.string.secb_linkedIn));
                break;

            case R.id.btn_send:
                startContactUsOperation();
                break;

            default:
                break;
        }
    }

	private void startContactUsOperation()
	{
        hideKeyboard(edtxt_name);
		if(!isContactUsDone)
		{
			if( validateInputFields())
			{
				User user = getUserData();
				ContactUsOperation operation = new ContactUsOperation(RequestIds.CONTACT_US_REQUEST_ID,true,getActivity(),user);
				operation.addRequsetObserver(this);
				operation.execute(true);
			}
		}
		else
		{
			displaySuccessMessage();
		}
	}

	private boolean validateInputFields()
	{
		boolean isUserNameValid = !Utilities.isNullString(edtxt_name.getText().toString());
		boolean isMobileValid = !Utilities.isNullString(edtxt_mobile.getText().toString());
		boolean isOrganizationValid = !Utilities.isNullString(edtxt_organization.getText().toString());
		boolean isJobValid = !Utilities.isNullString(edtxt_job.getText().toString());
		boolean isSubjectValid = !Utilities.isNullString(edtxt_subject.getText().toString());
		boolean isEmailValid = Utilities.isValidEmail(edtxt_email.getText().toString());

		//add error to edit texts
		if (!isUserNameValid)
			edtxt_name.setError(getString(R.string.error_empty_userName));
		if (!isMobileValid)
			edtxt_mobile.setError(getString(R.string.error_empty_mobile));
		if (!isOrganizationValid)
			edtxt_organization.setError(getString(R.string.error_empty_organization));
		if (!isJobValid)
			edtxt_job.setError(getString(R.string.error_empty_job));
		if (!isSubjectValid)
			edtxt_subject.setError(getString(R.string.error_empty_subject));
		if (!isEmailValid)
			edtxt_email.setError(getString(R.string.error_empty_email));


        //if both editTexts are empty the error msg appears on the
        //focused edit text so remove focus from both editTexts
        //and re-enable them

//		edtxt_name.setFocusable(false);
//		edtxt_mobile.setFocusable(false);
//		edtxt_organization.setFocusable(false);
//		edtxt_job.setFocusable(false);
//		edtxt_subject.setFocusable(false);
//		edtxt_email.setFocusable(false);


		return  isUserNameValid & isMobileValid &
				isOrganizationValid & isJobValid &
				isSubjectValid & isEmailValid;
	}

	private void initViews(View view) {
        txtv_location = (TextView) view.findViewById(R.id.txtv_location);
        txtv_email = (TextView) view.findViewById(R.id.txtv_email);
        txtv_phone = (TextView) view.findViewById(R.id.txtv_phone);
        txtv_fax = (TextView) view.findViewById(R.id.txtv_fax);

        imgv_facebook = (ImageView) view.findViewById(R.id.imgv_facebook);
        imgv_twitter = (ImageView) view.findViewById(R.id.imgv_twitter);
        imgv_google = (ImageView) view.findViewById(R.id.imgv_google);
        imgv_linkedin = (ImageView) view.findViewById(R.id.imgv_linkedin);


        edtxt_name = (EditText) view.findViewById(R.id.edtxt_name);
        edtxt_mobile = (EditText) view.findViewById(R.id.edtxt_mobile);
        edtxt_organization = (EditText) view.findViewById(R.id.edtxt_organization);
        edtxt_email = (EditText) view.findViewById(R.id.edtxt_email);
        edtxt_job = (EditText) view.findViewById(R.id.edtxt_job);
        edtxt_subject = (EditText) view.findViewById(R.id.edtxt_subject);

        btn_send = (Button) view.findViewById(R.id.btn_send);

/*        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapFrame);
        if (fragment == null)
        {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapFrame, fragment).commit();
        }

        googleMap = fragment.getMap();*/



        txtv_email.setOnClickListener(this);
        txtv_phone.setOnClickListener(this);
        imgv_facebook.setOnClickListener(this);
        imgv_twitter.setOnClickListener(this);
        imgv_google.setOnClickListener(this);
        imgv_linkedin.setOnClickListener(this);
        btn_send.setOnClickListener(this);

    }

    public boolean initMap() {
	    FragmentManager fm = getChildFragmentManager();
	    supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.mapFrame);
	    if (supportMapFragment == null)
	    {
		    supportMapFragment = SupportMapFragment.newInstance();
		    fm.beginTransaction().replace(R.id.mapFrame, supportMapFragment).addToBackStack("MAP").commit();

		    supportMapFragment.getMapAsync(this);
	    }
	    else
		    supportMapFragment.getMapAsync(this);

/*        if (googleMap == null) {
*//*            FragmentManager fragmentManager = getFragmentManager();
            SupportMapFragment mapFrag = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map1);
            googleMap = mapFrag.getMap();*//*

            supportMapFragment = ((SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map));

            if (supportMapFragment != null)
                supportMapFragment.getMapAsync(this);
        }*/
        return (googleMap != null);
    }


    public CompanyProfile getCompanyProfile() {

        companyProfile = new CompanyProfile();
        companyProfile.location = getResources().getString(R.string.secb_location_address);
        companyProfile.email = getResources().getString(R.string.secb_email_address);
        companyProfile.phone = getResources().getString(R.string.secb_phone_number);
        companyProfile.fax = getResources().getString(R.string.secb_fax_number);
        companyProfile.facebook = getResources().getString(R.string.secb_facebook);
        companyProfile.twitter = getResources().getString(R.string.secb_twitter);
        companyProfile.google = getResources().getString(R.string.secb_google);
        companyProfile.linkedin = getResources().getString(R.string.secb_linkedIn);
        companyProfile.locationOnMap = new LatLng(30.0882739, 31.3146007);

        return companyProfile;
    }

    private void bindViews() {
        if (this.companyProfile != null) {
            txtv_location.setText(companyProfile.location);
            txtv_email.setText(companyProfile.email);
            txtv_phone.setText(companyProfile.phone);
            txtv_fax.setText(companyProfile.fax);
//            putCompanyOnMap();
        }
    }


    private void sendCompanyEmail() {
        if (companyProfile != null && !Utilities.isNullString(companyProfile.email))
            Utilities.openEmail(getActivity(), companyProfile.email, "write your subject", "write your mail body");
    }

    private void callCompanyPhone() {
        if (companyProfile != null && !Utilities.isNullString(companyProfile.phone))
            Utilities.callPhoneNumber(getActivity(), companyProfile.phone);
    }

    private void openCompanyPage(String pageUrl) {
        if (!Utilities.isNullString(pageUrl))
            Utilities.openUrlInBrowser(getActivity(), pageUrl);
    }

    public User getUserData() {
	    User user = new User();
        user.userName = edtxt_name.getText().toString();
        user.phoneNumber = edtxt_mobile.getText().toString();
        user.organization = edtxt_organization.getText().toString();
        user.emailAddress = edtxt_email.getText().toString();
        user.jobTitle = edtxt_job.getText().toString();
        user.subject = edtxt_subject.getText().toString();
        return user;
    }


    public void putCompanyOnMap() {
        if (companyProfile != null && companyProfile.locationOnMap != null && googleMap != null) {
            List<LatLng> list = new ArrayList<>();
            list.add(companyProfile.locationOnMap);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.secb_map_marker);
//            Marker marker = MapsHelper.addMarker(googleMap, companyProfile.locationOnMap, bitmap);

	        if(companyProfile.locationOnMap!=null){
		        Marker marker = MapsHelper.addMarker(googleMap,companyProfile.locationOnMap, bitmap);;
		        MapsHelper.zoomToSingleMarker(companyProfile.locationOnMap, googleMap ,11,true);
	        }


//            MapsHelper.setMapCenter(true, googleMap, companyProfile.locationOnMap);
//                MapsHelper.zoomToAllMarkers(list, googleMap, 5, 15);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

	    try {
            if(supportMapFragment!=null)
            {
                getFragmentManager().popBackStack("MAP",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().remove(supportMapFragment).commit();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        putCompanyOnMap();
    }

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
		//if not attached to activity
		if(!isAdded())
			return;
		if(error==null){
			if((int)requestId==RequestIds.CONTACT_US_REQUEST_ID  &&
					((CTHttpResponse)resulObject).statusCode==200)
			{
				isContactUsDone = true;
				displaySuccessMessage();
			}
		}
		else if (error != null && error instanceof CTHttpError)
		{
			Logger.instance().v(TAG,error);
			int statusCode = ((CTHttpError) error).getStatusCode();
			if (RequestHandler.isRequestTimedOut(statusCode))
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
			}
			else if (statusCode == -1)
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						getActivity());
			}
		}
	}


	public void displaySuccessMessage(){
		String msg = getString(R.string.contact_us_done);
		ErrorDialog.showMessageDialog(getString(R.string.success),
				msg,
				getActivity());
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}
}
