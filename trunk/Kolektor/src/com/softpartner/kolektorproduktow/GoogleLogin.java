package com.softpartner.kolektorproduktow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.softpartner.kolektorproduktow.narzedzia.JSONParser;

public class GoogleLogin implements OnClickListener, ConnectionCallbacks,
		OnConnectionFailedListener {

	private Activity root;

	private String url_login;

	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;

	public static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "MainActivity";

	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	// Google client to interact with Google API
	public GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	public boolean mIntentInProgress;

	public boolean mSignInClicked;

	private ConnectionResult mConnectionResult;

	private SignInButton btnSignIn;
	private Button btnSignOut, btnRevokeAccess;
	private LinearLayout llProfileLayout;
	private View skaner_layout;
	MyApp app;

	public GoogleLogin(Activity root) {
		this.root = root;
		url_login = root.getString(R.string.url_login);
		app = (MyApp) root.getApplicationContext();
		app.gl = this;
		btnSignIn = (SignInButton) root.findViewById(R.id.btn_sign_in);
		btnSignOut = (Button) root.findViewById(R.id.btn_sign_out);
		btnRevokeAccess = (Button) root.findViewById(R.id.btn_revoke_access);
		skaner_layout = (View) root.findViewById(R.id.skaner_layout);

		llProfileLayout = (LinearLayout) root.findViewById(R.id.llProfile);

		// Button click listeners
		btnSignIn.setOnClickListener(this);
		btnSignOut.setOnClickListener(this);
		btnRevokeAccess.setOnClickListener(this);

		mGoogleApiClient = new GoogleApiClient.Builder(root)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(root, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), root,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		Toast.makeText(root, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		getProfileInformation();

		// Update the UI after signin
		updateUI(true);

	}

	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			btnSignIn.setVisibility(View.GONE);
			btnSignOut.setVisibility(View.VISIBLE);
			btnRevokeAccess.setVisibility(View.VISIBLE);
			llProfileLayout.setVisibility(View.VISIBLE);
			skaner_layout.setVisibility(View.VISIBLE);
		} else {
			btnSignIn.setVisibility(View.VISIBLE);
			btnSignOut.setVisibility(View.GONE);
			btnRevokeAccess.setVisibility(View.GONE);
			llProfileLayout.setVisibility(View.GONE);
			skaner_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String id = currentPerson.getId();
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String birthday = currentPerson.getBirthday();
				String location = currentPerson.getCurrentLocation();
				String gender = "" + currentPerson.getGender();
				String aboutMe = currentPerson.getAboutMe();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				// txtName.setText(personName);
				// txtEmail.setText(email);

				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;
				app.setData(id);
				new LoginTask().execute(id, personName, personPhotoUrl,
						personGooglePlusProfile, birthday, location, gender,
						aboutMe, email);
				// new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

			} else {
				Toast.makeText(root, "Person information is null",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}

	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_sign_in) {
			signInWithGplus();
		} else if (v.getId() == R.id.btn_sign_out) {
			signOutFromGplus();
		} else if (v.getId() == R.id.btn_revoke_access) {
			revokeGplusAccess();
		}
	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	/**
	 * Sign-out from google
	 * */
	public void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			updateUI(false);
		}
	}

	/**
	 * Revoking access from google
	 * */
	public void revokeGplusAccess() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
					.setResultCallback(new ResultCallback<Status>() {
						@Override
						public void onResult(Status arg0) {
							Log.e(TAG, "User access revoked!");
							mGoogleApiClient.connect();
							updateUI(false);
						}

					});
		}
	}

	private class LoginTask extends AsyncTask<String, Void, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(root);
			pDialog.setMessage(root.getString(R.string.logowanie));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String result = "";
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("id", args[0]));
			params.add(new BasicNameValuePair("personName", args[1]));
			params.add(new BasicNameValuePair("personPhotoUrl", args[2]));
			params.add(new BasicNameValuePair("personGooglePlusProfile",
					args[3]));
			params.add(new BasicNameValuePair("birthday", args[4]));
			params.add(new BasicNameValuePair("location", args[5]));
			params.add(new BasicNameValuePair("gender", args[6]));
			params.add(new BasicNameValuePair("aboutMe", args[7]));
			params.add(new BasicNameValuePair("email", args[8]));
			params.add(new BasicNameValuePair("premium", "" + app.getPremium()));

			JSONObject json = jParser
					.makeHttpRequest(url_login, "POST", params);
			try {
				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					result = json.getString("id");
				}
			} catch (Exception e) {
				Log.e("DEBUG_TAG", "LoginTask: " + e);
			}
			return result;
		}

		protected void onPostExecute(String result) {
			pDialog.dismiss();
		}
	}
}
