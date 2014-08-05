package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
	public Logowanie logowanie;
	String pseudonim = "";
	MyApp app;

	public Zaloguj(Activity _root, EditText _login, EditText _haslo) {
		this.root = _root;
		app = (MyApp) root.getApplicationContext();
		url_logowanie = root.getString(R.string.url_logowanie);
		this.Login = _login;
		this.Haslo = _haslo;
		if(app.getData()!=-1)
			zalogowany();

	}

	public void Zaloguj() {
		logowanie = new Logowanie();
		logowanie.execute();
	}

	@Override
	public void onClick(View v) {
		if (v == zaloguj_button) {
			logowanie = new Logowanie();
			logowanie.execute();
		} else if (v.getId() == R.id.drawer_login_button) {
		}

	}

	public class Logowanie extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			komunikat = null;
			pDialog = new ProgressDialog(root);
			pDialog.setMessage("Trwa logowanie, proszę czekać...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setOnCancelListener(new OnCancelListener(){
	             @Override
	             public void onCancel(DialogInterface dialog){
	                logowanie.cancel(true);
	          }});
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
						pseudonim = c.getString("pseudonim");
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
			app.setPseudonim(pseudonim);
			zalogowany();
		}
	}

	public void zalogowany() {
		root.findViewById(R.id.drawer_zalogujsie_textview).setVisibility(
				View.GONE);
		root.findViewById(R.id.drawer_login_module).setVisibility(View.GONE);
		root.findViewById(R.id.drawer_stworz_konto).setVisibility(View.GONE);
		root.findViewById(R.id.drawer_user_pseudonim).setVisibility(
				View.VISIBLE);
		root.findViewById(R.id.drawer_moje_przepisy)
				.setVisibility(View.VISIBLE);
		root.findViewById(R.id.drawer_logout_button)
				.setVisibility(View.VISIBLE);
		root.findViewById(R.id.drawer_dodaj_przepis)
				.setVisibility(View.VISIBLE);
		((TextView) (root.findViewById(R.id.drawer_user_pseudonim)))
				.setText(app.getPseudonim());
	}
}
