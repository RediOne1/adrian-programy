package com.softpartner.kolektorproduktow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartActivity extends ListFragment {

	private GoogleLogin googleLogin;
	private Skaner skaner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_start, container,
				false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		googleLogin = new GoogleLogin(getActivity());
		skaner = new Skaner(this);		
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
		// scan requestCode = 0x0000c0de
		if (requestCode == 0x0000c0de) {
			skaner.onActivityResult(requestCode, responseCode, intent);
		} else {
			if (responseCode != Activity.RESULT_OK) {
				googleLogin.mSignInClicked = false;
			}

			googleLogin.mIntentInProgress = false;

			if (!googleLogin.mGoogleApiClient.isConnecting()) {
				googleLogin.mGoogleApiClient.connect();
			}
		}
	}
}