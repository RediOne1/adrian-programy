package com.softpartner.kolektorproduktow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softpartner.kolektorproduktow.narzedzia.JSONParser;

public class DodajProdukt extends Activity implements OnClickListener {

	private EditText nazwaEdt, cenaEdt, iloscEdt, wagaEdt, kodEdt, opisEdt;
	private String nazwa, cena, ilosc, waga, kod, opis, url_dodaj_produkt,user_id;
	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	private int success;
	private Produkt produkt;
	private Button wyslij;
	private MyApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dodaj_produkt);
		app = (MyApp) getApplicationContext();
		user_id=app.getData();
		if(user_id.equals("-1")){
			Toast.makeText(DodajProdukt.this,
					getString(R.string.uruchom_ponownie),
					Toast.LENGTH_LONG).show();
		}
		produkt = (Produkt) getIntent().getSerializableExtra("produkt");
		nazwaEdt = (EditText) findViewById(R.id.dodaj_nazwa);
		cenaEdt = (EditText) findViewById(R.id.dodaj_cena);
		iloscEdt = (EditText) findViewById(R.id.dodaj_ilosc);
		wagaEdt = (EditText) findViewById(R.id.dodaj_waga);
		kodEdt = (EditText) findViewById(R.id.dodaj_kody);
		opisEdt = (EditText) findViewById(R.id.dodaj_opis);
		url_dodaj_produkt = getString(R.string.url_dodaj_produkt);
		wyslij = (Button) findViewById(R.id.wyslij_produktBtn);

		kodEdt.setText(produkt.getKody());

		wyslij.setOnClickListener(this);
	}

	class WyslijProdukt extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DodajProdukt.this);
			pDialog.setMessage(getString(R.string.dodawanie_produktu));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		@Override
		protected String doInBackground(String... args) {
			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", user_id));
			params.add(new BasicNameValuePair("nazwa", nazwa));
			params.add(new BasicNameValuePair("cena", cena));
			params.add(new BasicNameValuePair("ilosc", ilosc));
			params.add(new BasicNameValuePair("waga", waga));
			params.add(new BasicNameValuePair("kod", kod));
			params.add(new BasicNameValuePair("opis", opis));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_dodaj_produkt,
						"POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				success = json.getInt("success");

				if (success == 1) {
					produkt.ID = json.getString("id");
					produkt.nazwa = nazwa;
					produkt.cena = cena;
					produkt.ilosc = ilosc;
					produkt.waga = waga;
					produkt.opis = opis;
				} else {
				}
			} catch (Exception e) {

			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			if (success == 1) {
				Toast.makeText(DodajProdukt.this,
						getString(R.string.dodano_produkt) + " " + nazwa,
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent(DodajProdukt.this, WyswietlProdukt.class);
				i.putExtra("produkt", produkt);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(DodajProdukt.this,
						getString(R.string.nie_dodano_produktu) + " " + nazwa,
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();

		}
	}

	private boolean sprawdzDane() {
		nazwa = nazwaEdt.getText().toString();
		cena = cenaEdt.getText().toString();
		ilosc = iloscEdt.getText().toString();
		waga = wagaEdt.getText().toString();
		kod = kodEdt.getText().toString();
		opis = opisEdt.getText().toString();
		
		if (nazwa.length() < 1 || kod.length() < 1 || cena.length() < 1
				|| ilosc.length() < 1 || waga.length() < 1 || opis.length() < 1) {
			Toast.makeText(this, getString(R.string.uzupelnij_dane),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == wyslij && sprawdzDane())
			new WyslijProdukt().execute();

	}

}
