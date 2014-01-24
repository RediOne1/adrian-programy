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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Rejestracja extends Activity implements OnClickListener {
	ProgressDialog pDialog;
	private EditText Elogin;
	private EditText Ehaslo;
	private EditText Ehaslo2;
	private EditText Email;
	private Button Bwyslij;
	private Button Bwyczysc;
	private String login, haslo, haslo2, mail;
	JSONParser jsonParser = new JSONParser();
	private final String TAG_SUCCESS = "success";
	private String komunikat;
	private final int ALERT_DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rejestracja);
		Elogin = (EditText) findViewById(R.id.login);
		Ehaslo = (EditText) findViewById(R.id.haslo);
		Ehaslo2 = (EditText) findViewById(R.id.haslo2);
		Email = (EditText) findViewById(R.id.mail);
		Bwyslij = (Button) findViewById(R.id.wyslij_button);
		Bwyczysc = (Button) findViewById(R.id.wyczysc_button);
		Bwyslij.setOnClickListener(this);
		Bwyczysc.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rejestracja, menu);
		return true;
	}

	class Zarejestruj extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Rejestracja.this);
			pDialog.setMessage("Trwa rejestracja, proszê czekaæ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("login", login));
			params.add(new BasicNameValuePair("haslo", haslo));
			params.add(new BasicNameValuePair("mail", mail));
			JSONObject json = jsonParser.makeHttpRequest(
					"http://softpartner.pl/projekt/rejestracja.php", "POST",
					params);
			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					komunikat = json.getString("message");
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

	@Override
	public void onClick(View v) {
		if (v == Bwyczysc) {
			Elogin.setText("");
			Ehaslo.setText("");
			Ehaslo2.setText("");
			Email.setText("");
		} else if (v == Bwyslij) {
			login = Elogin.getText().toString();
			haslo = Ehaslo.getText().toString();
			haslo2 = Ehaslo2.getText().toString();
			mail = Email.getText().toString();
			if (!haslo.equals(haslo2)) {
				komunikat = "Podano ró¿ne has³a";
				showDialog(ALERT_DIALOG_ID);
			} else {
				new Zarejestruj().execute();
			}
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
								}
							}).create();
			return alertDialog;
		}
		return null;
	}
}
