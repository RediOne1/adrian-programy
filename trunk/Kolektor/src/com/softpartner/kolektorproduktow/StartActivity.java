package com.softpartner.kolektorproduktow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartActivity extends Fragment {

	private GoogleLogin googleLogin;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_start, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		googleLogin = new GoogleLogin(getActivity());
		googleLogin.mGoogleApiClient.connect();
	}

	@Override
	public void onDestroyView() {
		if (googleLogin.mGoogleApiClient.isConnected()) {
			googleLogin.mGoogleApiClient.disconnect();
		}
		super.onDestroyView();
	}

	protected void mOnActivityResult(int requestCode, int responseCode,
			Intent intent) {
			if (responseCode != Activity.RESULT_OK) {
				googleLogin.mSignInClicked = false;
			}

			googleLogin.mIntentInProgress = false;

			if (!googleLogin.mGoogleApiClient.isConnecting()) {
				googleLogin.mGoogleApiClient.connect();
			}
	}
}