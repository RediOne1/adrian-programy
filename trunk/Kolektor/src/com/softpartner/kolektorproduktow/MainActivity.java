package com.softpartner.kolektorproduktow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private static final int ALERT_DIALOG_ID = 123;
	private GoogleLogin googleLogin;
	private Skaner skaner;
	private MyApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		app = (MyApp) getApplicationContext();
		googleLogin = new GoogleLogin(this);
		skaner = new Skaner(this);
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
		Toast.makeText(getApplicationContext(),
				requestCode + " " + responseCode, Toast.LENGTH_SHORT).show();
		// scan requestCode = 0x0000c0de
		if (requestCode == 0x0000c0de) {
			skaner.onActivityResult(requestCode, responseCode, intent);
		} else {
			if (responseCode != RESULT_OK) {
				googleLogin.mSignInClicked = false;
			}

			googleLogin.mIntentInProgress = false;

			if (!googleLogin.mGoogleApiClient.isConnecting()) {
				googleLogin.mGoogleApiClient.connect();
			}
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case ALERT_DIALOG_ID:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setTitle(R.string.komunikat)
					.setMessage(
							getString(R.string.czy_dodac1) + app.schowek
									+ getString(R.string.czy_dodac2))
					.setPositiveButton(R.string.tak,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									removeDialog(ALERT_DIALOG_ID);
								}
							})
					.setNegativeButton(R.string.nie,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									removeDialog(ALERT_DIALOG_ID);
									app.schowek = null;
								}
							}).create();
			return alertDialog;
		}
		return null;
	}
}