package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mojeprzepisy.aplikacja.narzedzia.AlertDialogManager;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;

public class Zaloguj implements OnClickListener {

	JSONParser jsonParser = new JSONParser();
	public int user_id = -1;
	private Activity root;
	private ProgressDialog pDialog;
	private EditText Login, Haslo;
	private String komunikat;
	private final String TAG_SUCCESS = "success";
	private String url_logowanie;
	private View login_layout;
	private Button zaloguj_button;
	MyApp app;

	public Zaloguj(Activity _root) {
		this.root = _root;
		app = (MyApp) root.getApplicationContext();
		url_logowanie = root.getString(R.string.url_logowanie);
		login_layout = (View) root.findViewById(R.id.login_relativelayout);
		Login = (EditText) root.findViewById(R.id.login_editText);
		Haslo = (EditText) root.findViewById(R.id.haslo_editText);
		zaloguj_button = (Button) root.findViewById(R.id.zaloguj_button);
		zaloguj_button.setOnClickListener(this);
		Log.d("DEBUG_TAG", "" + app.getData());
		if (app.getData() != -1)
			zalogowany();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.zaloguj_layout) {
			if (login_layout.getVisibility() == View.GONE)
				login_layout.setVisibility(View.VISIBLE);
			else
				login_layout.setVisibility(View.GONE);
		} else if (v == zaloguj_button) {
			new Logowanie().execute();
		}

	}

	class Logowanie extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			komunikat = null;
			pDialog = new ProgressDialog(root);
			pDialog.setMessage("Trwa logowanie, proszę czekać...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String login, haslo;
			login = Login.getText().toString();
			haslo = Haslo.getText().toString();
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
						String pseudonim = c.getString("pseudonim");
						int aktywny = c.getInt("aktywny");
						if (aktywny == 1) {
							user_id = c.getInt("id");
							publishProgress();
						} else {
							komunikat = "Konto nie jest aktywne, sprawdź email aby aktywować konto.";
						}
					}
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
			if (komunikat != null)
				new AlertDialogManager().showAlertDialog(root, null, komunikat);
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			app.setData(user_id);
			zalogowany();
		}
	}

	public void zalogowany() {
		root.findViewById(R.id.zaloguj_layout).setVisibility(View.GONE);
		root.findViewById(R.id.dodaj_przepis_linearLayout).setVisibility(
				View.VISIBLE);
		root.findViewById(R.id.rejestracja_layout).setVisibility(View.GONE);
		root.findViewById(R.id.wyloguj_layout).setVisibility(View.VISIBLE);
		login_layout.setVisibility(View.GONE);
	}
}
