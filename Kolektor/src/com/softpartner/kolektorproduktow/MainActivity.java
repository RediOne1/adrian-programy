package com.softpartner.kolektorproduktow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private GoogleLogin googleLogin;
	private MyApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		googleLogin = new GoogleLogin(this);
	}

	protected void onStart() {
		super.onStart();
		googleLogin.mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (googleLogin.mGoogleApiClient.isConnected()) {
			googleLogin.mGoogleApiClient.disconnect();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == GoogleLogin.RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				googleLogin.mSignInClicked = false;
			}

			googleLogin.mIntentInProgress = false;

			if (!googleLogin.mGoogleApiClient.isConnecting()) {
				googleLogin.mGoogleApiClient.connect();
			}
		}
	}
}