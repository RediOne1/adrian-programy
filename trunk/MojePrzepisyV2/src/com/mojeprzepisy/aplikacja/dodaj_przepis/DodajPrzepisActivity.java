package com.mojeprzepisy.aplikacja.dodaj_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.WyslijZdjecie;

public class DodajPrzepisActivity extends Activity implements OnClickListener {

	public DodajZdjecie dodajZdjecie;
	public int user_id = 1;
	public int przepisID = 0;
	private String komunikat;
	public DodajTytul dodajTytul;
	public DodajSkladniki dodajSkladniki;
	public DodajOpis dodajOpis;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	public DodajDodatkoweDane dodatkoweDane;
	public Button wyslij;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int REQ_CODE_PICK_IMAGE = 101;
	public Activity root;
	private int success;
	private ProgressDialog pDialog;
	private Przepis przepis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dodaj_przepis_layout);
		root = DodajPrzepisActivity.this;
		Bundle bundle = getIntent().getExtras();
		if (bundle.getBoolean("edytuj", false)) {
			przepis = (Przepis) getIntent().getSerializableExtra("przepis");
			
			edytuj();
		}
		else
			dodaj();
		wyslij = (Button) findViewById(R.id.dodaj_przepis_wyslij_button);
		wyslij.setOnClickListener(this);
	}

	public void edytuj() {
		dodajZdjecie = new DodajZdjecie(root, przepis.zdjecie);
		dodajTytul = new DodajTytul(root, przepis.tytul);
		dodajSkladniki = new DodajSkladniki(root, przepis.skladniki);
		//dodajOpis = new DodajOpis(root, przepis.opisList);
		dodatkoweDane = new DodajDodatkoweDane(root, przepis.trudnosc,
				przepis.kategoria, przepis.kategoria);
	}

	public void dodaj() {
		dodajZdjecie = new DodajZdjecie(root);
		dodajTytul = new DodajTytul(root);
		dodajSkladniki = new DodajSkladniki(root);
		dodajOpis = new DodajOpis(root);
		dodatkoweDane = new DodajDodatkoweDane(root);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dodaj_przepis, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dodajZdjecie.dodajPrzepisActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		if (v == wyslij) {
			new DodajNowyPrzepis().execute();
		}
	}

	class DodajNowyPrzepis extends AsyncTask<String, Integer, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DodajPrzepisActivity.this);
			pDialog.setMessage("Dodawanie przepisu...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			params.add(new BasicNameValuePair("tytul", dodajTytul.toString()));
			params.add(new BasicNameValuePair("kategoria",
					dodatkoweDane.kategoria.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("trudnosc",
					dodatkoweDane.trudnosc.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("czas", dodatkoweDane.czas
					.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("skladniki", dodajSkladniki
					.toString()));
			params.add(new BasicNameValuePair("opis", dodajOpis.toString()));
			params.add(new BasicNameValuePair("publiczny", ""
					+ (dodatkoweDane.widoczny.isChecked() ? 1 : 0)));
			// getting JSON string from URL
			try {
				JSONObject json = jParser
						.makeHttpRequest(
								"http://softpartner.pl/moje_przepisy2/dodaj_przepis.php",
								"POST", params);
				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				success = json.getInt("success");

				if (success == 1) {
					przepisID = json.getInt("przepisID");

					WyslijZdjecie wyslij = new WyslijZdjecie(
							dodajZdjecie.getBitmap(), "" + przepisID);
					params.add(new BasicNameValuePair("zdjecie", "" + przepisID));
					wyslij.executeMultipartPost();
				} else {
				}
				komunikat = json.getString("message");
			} catch (Exception e) {
				komunikat = "B��d w po��czeniu.";
				Log.d("DEBUG_TAG", "" + e);
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}

		protected void onProgress(Integer... integers) {
			pDialog.setProgress(integers[0]);
		}
	}
}
