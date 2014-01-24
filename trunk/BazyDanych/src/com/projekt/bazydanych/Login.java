package com.projekt.bazydanych;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener {

	private Button zaloguj;
	ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private EditText Elogin, Ehaslo;
	private String login, haslo, komunikat, TAG_SUCCESS="success";
	private int user_id=0;
	private final int ALERT_DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		zaloguj = (Button) findViewById(R.id.zaloguj_button);
		zaloguj.setOnClickListener(this);
		Elogin = (EditText) findViewById(R.id.login_zaloguj);
		Ehaslo = (EditText) findViewById(R.id.haslo_zaloguj);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == zaloguj) {
			login = Elogin.getText().toString();
			haslo = Ehaslo.getText().toString();
			new Zaloguj().execute();
		}
	}

	class Zaloguj extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Trwa logowanie, proszê czekaæ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("login", login));
			params.add(new BasicNameValuePair("haslo", haslo));
			JSONObject json = jsonParser.makeHttpRequest(
					"http://softpartner.pl/projekt/logowanie.php", "POST",
					params);
			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					komunikat = json.getString("message");
					user_id = json.getInt("id");
				} else {
					komunikat = json.getString("message");
				}
			} catch (Exception e) {
				komunikat = "B³¹d w po³¹czeniu";
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			showDialog(ALERT_DIALOG_ID);
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case ALERT_DIALOG_ID:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setTitle("Komunikat")
					.setMessage(komunikat)
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {									
									removeDialog(ALERT_DIALOG_ID);
									if(user_id!=0){
										Intent i = new Intent(
												getApplicationContext(),
												WybierzTyp.class);
										// sending pid to next activity
										i.putExtra("id", ""+user_id);
										startActivity(i);
									}
								}
							}).create();
			return alertDialog;
		}
		return null;
	}
}
