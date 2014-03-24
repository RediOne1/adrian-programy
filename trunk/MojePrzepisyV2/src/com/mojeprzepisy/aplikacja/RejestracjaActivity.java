package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;

public class RejestracjaActivity extends Activity implements OnClickListener {

	JSONParser jsonParser = new JSONParser();
	private final static int CUSTOM_DIALOG_ID = 1;
	private final static int CUSTOM_DIALOG_ID2 = 2;
	private final static int ALERT_DIALOG_ID = 3;
	private String url_rejestracja;
	private String url_logowanie;
	private String url_aktywuj_konto;
	private static final String TAG_SUCCESS = "success";
	private static String komunikat = "...";
	ProgressDialog pDialog;
	EditText inPseudonim;
	EditText inLogin;
	EditText inHaslo;
	EditText inHaslo2;
	EditText inMail;
	EditText Login;
	EditText loginAktywuj;
	EditText mailAktywuj;
	EditText Haslo;
	TextView zgodnosc;
	TextView stworz_konto_text;
	boolean zgodnoscBool;
	Button stworz;
	Button wyczysc;
	SharedPreferences ustawienia;
	public boolean zarejestrowano = false;
	boolean dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rejestracja);
		url_rejestracja = getString(R.string.url_rejestracja);
		url_logowanie = getString(R.string.url_logowanie);
		url_aktywuj_konto = getString(R.string.url_aktywuj_konto);
		Typeface MyNormal = Typeface.createFromAsset(getBaseContext()
				.getAssets(), "fonts/SEGOEPR.TTF");
		Typeface MyBold = Typeface.createFromAsset(
				getBaseContext().getAssets(), "fonts/SEGOEPRB.TTF");
		ustawienia = getSharedPreferences("MyCustomSharedPreferences", 0);
		inLogin = (EditText) findViewById(R.id.inLogin);
		inHaslo = (EditText) findViewById(R.id.inHaslo);
		inHaslo2 = (EditText) findViewById(R.id.inHaslo2);
		inPseudonim = (EditText) findViewById(R.id.inPseudonim);
		inMail = (EditText) findViewById(R.id.inMail);
		zgodnosc = (TextView) findViewById(R.id.zgodnoscHasel);
		stworz = (Button) findViewById(R.id.stworz);
		wyczysc = (Button) findViewById(R.id.wyczysc);
		if (ustawienia.getBoolean("zapamietaj", false)) {
			new Logowanie().execute();
		}
		wyczysc.setOnClickListener(this);
		stworz.setOnClickListener(this);
		inHaslo2.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String strPass1 = inHaslo.getText().toString();
				String strPass2 = inHaslo2.getText().toString();
				if (strPass1.equals(strPass2)) {
					zgodnosc.setText("Hasła prawidłowe.");
					zgodnoscBool = true;
				} else {
					zgodnosc.setText("Hasła nie pasuja.");
					zgodnoscBool = false;
				}
			}

			// ... inne metody, kt�re trzeba przes�oni� nie musz� nic robi�
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		zgodnosc.setTypeface(MyNormal);
		stworz_konto_text = (TextView) findViewById(R.id.stworz_konto_text);
		wyczysc.setTypeface(MyBold);
		stworz.setTypeface(MyBold);
		stworz_konto_text.setTypeface(MyBold);
	}

	@Override
	public void onClick(View v) {
		if (v == wyczysc) {
			inLogin.setText("");
			inHaslo.setText("");
			inHaslo2.setText("");
			inPseudonim.setText("");
			inMail.setText("");
		} else if (v == stworz) {
			new StworzKonto().execute();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rejestracja, menu);
		return true;
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CUSTOM_DIALOG_ID:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.login_dialog,
					(ViewGroup) findViewById(R.id.root));
			ustawienia = getSharedPreferences("MyCustomSharedPreferences", 0);
			Login = (EditText) layout.findViewById(R.id.login);
			Haslo = (EditText) layout.findViewById(R.id.haslo);
			Login.setText(ustawienia.getString("Login", ""));

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			// KOnfiguracja okna AlertDialog
			builder.setTitle("Logowanie");
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Wymuszamy zamkni�cie i usuni�cie okna, tak by nie
							// mo�na
							// by�o ponownie z niego skorzysta�.
							removeDialog(CUSTOM_DIALOG_ID);
						}
					});
			builder.setNeutralButton("Aktywuj konto",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							removeDialog(CUSTOM_DIALOG_ID);
							showDialog(CUSTOM_DIALOG_ID2);
						}
					});
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							removeDialog(CUSTOM_DIALOG_ID);
							dodaj(Login, Haslo);
						}
					});
			// Twrozymy obiekt AlertDialog i zwracamy go.
			AlertDialog passwordDialog = builder.create();
			return passwordDialog;
		case CUSTOM_DIALOG_ID2:
			LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout2 = inflater2.inflate(R.layout.aktywuj_konto,
					(ViewGroup) findViewById(R.id.root));
			loginAktywuj = (EditText) layout2.findViewById(R.id.loginAktywuj);
			mailAktywuj = (EditText) layout2.findViewById(R.id.mailAktywuj);
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setView(layout2);
			// KOnfiguracja okna AlertDialog
			builder2.setTitle("Logowanie");
			builder2.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Wymuszamy zamkni�cie i usuni�cie okna, tak by nie
							// mo�na
							// by�o ponownie z niego skorzysta�.
							removeDialog(CUSTOM_DIALOG_ID);
						}
					});
			builder2.setPositiveButton("Wyślij",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							removeDialog(CUSTOM_DIALOG_ID);
							dodaj2(loginAktywuj, mailAktywuj);
							new WyslijMail().execute();

						}
					});
			// Twrozymy obiekt AlertDialog i zwracamy go.
			AlertDialog passwordDialog2 = builder2.create();
			return passwordDialog2;
		case ALERT_DIALOG_ID:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setTitle("Komunikat")
					.setMessage(komunikat)
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (zarejestrowano)
										finish();
									removeDialog(ALERT_DIALOG_ID);
								}
							}).create();
			return alertDialog;
		}
		return null;
	}

	public void dodaj(EditText Login, EditText Haslo) {
		this.Login = Login;
		this.Haslo = Haslo;
	}

	public void dodaj2(EditText loginAktywuj, EditText mailAktywuj) {
		this.loginAktywuj = loginAktywuj;
		this.mailAktywuj = mailAktywuj;
	}

	class Logowanie extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RejestracjaActivity.this);
			pDialog.setMessage("Trwa logowanie, proszę czekać...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			dialog = true;
			String login, haslo;
			if (ustawienia.getBoolean("zapamietaj", false)) {
				login = ustawienia.getString("Login", "");
				haslo = ustawienia.getString("Haslo", "");
			} else {
				login = Login.getText().toString();
				haslo = Haslo.getText().toString();
			}
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("login", login));
			params.add(new BasicNameValuePair("haslo", haslo));
			JSONArray products = null;
			JSONObject json = jsonParser.makeHttpRequest(url_logowanie, "POST",
					params);
			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					products = json.getJSONArray("dane");

					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						int id = c.getInt("id");
						String pseudonim = c.getString("pseudonim");
						int aktywny = c.getInt("aktywny");
						if (aktywny == 1) {
							Intent j = new Intent(getApplicationContext(),
									MainActivity.class);
							j.putExtra("pseudonim", pseudonim);
							j.putExtra("user_id", id);
							dialog = false;
							finish();
							startActivity(j);
						} else {
							SharedPreferences.Editor edytorPref = ustawienia
									.edit();
							edytorPref.clear();
							edytorPref.commit();
							komunikat = "Konto nie jest aktywne, sprawdź email aby aktywować konto.";
						}
					}
				} else {
					SharedPreferences.Editor edytorPref = ustawienia.edit();
					edytorPref.clear();
					edytorPref.commit();
					komunikat = json.getString("message");

				}
			} catch (Exception e) {
				SharedPreferences.Editor edytorPref = ustawienia.edit();
				edytorPref.clear();
				edytorPref.commit();
				komunikat = "Błąd w połączeniu.";
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (dialog)
				showDialog(ALERT_DIALOG_ID);
		}
	}

	class WyslijMail extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RejestracjaActivity.this);
			pDialog.setMessage("Trwa ponowne wysyłanie linku aktywacyjnego, proszę czekać...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			dialog = true;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("login", loginAktywuj.getText()
					.toString()));
			params.add(new BasicNameValuePair("mail", mailAktywuj.getText()
					.toString()));
			JSONObject json = jsonParser.makeHttpRequest(url_aktywuj_konto,
					"GET", params);
			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					komunikat = json.getString("message");
				} else {
					komunikat = json.getString("message");
				}
			} catch (Exception e) {
				komunikat = "Błąd w połączeniu.";
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (dialog)
				showDialog(ALERT_DIALOG_ID);
		}
	}

	class StworzKonto extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RejestracjaActivity.this);
			pDialog.setMessage("Dodawanie użytkownika...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String pseudonim = inPseudonim.getText().toString();
			String login = inLogin.getText().toString();
			String haslo = inHaslo.getText().toString();
			String mail = inMail.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pseudonim", pseudonim));
			params.add(new BasicNameValuePair("login", login));
			params.add(new BasicNameValuePair("haslo", haslo));
			params.add(new BasicNameValuePair("mail", mail));

			// getting JSON Object
			// Note that create product url accepts POST method

			// check log cat for response
			if (zgodnoscBool) {
				try {
					JSONObject json = jsonParser.makeHttpRequest(
							url_rejestracja, "POST", params);
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						komunikat = json.getString("message");
					} else {
						komunikat = json.getString("message");
					}
				} catch (JSONException e) {
					komunikat = "Cos nie tak.";
					e.printStackTrace();
				} catch (Exception e) {
					komunikat = "Brak połączenia z internetem.";
				}
			} else {
				komunikat = "Hasła nie zgadzają się.";
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			showDialog(ALERT_DIALOG_ID);
		}

	}

}
