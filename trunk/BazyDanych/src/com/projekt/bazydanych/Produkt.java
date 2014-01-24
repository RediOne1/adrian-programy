package com.projekt.bazydanych;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
import android.widget.TextView;

public class Produkt extends Activity implements OnClickListener {

	private int user_id;
	private int produktId;
	private final int ALERT_DIALOG_ID = 1;
	JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private ProgressDialog pDialog;
	private String komunikat;
	private String model;
	private String nazwa;
	private String cena;
	private String rozmiar;
	private String ilosc;
	private String producent;
	private String typ;
	private TextView Tmodel;
	private TextView Tnazwa;
	private TextView Tcena;
	private TextView Trozmiar;
	private TextView Tilosc;
	private TextView Tproducent;
	private Button Bzamow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produkt);
		Bundle bundle = getIntent().getExtras();
		user_id = bundle.getInt("user_id");
		produktId = bundle.getInt("produktID");
		typ = bundle.getString("typ");
		Tmodel = (TextView) findViewById(R.id.produkt_model);
		Tnazwa = (TextView) findViewById(R.id.produkt_nazwa);
		Tcena = (TextView) findViewById(R.id.produkt_cena);
		Trozmiar = (TextView) findViewById(R.id.produkt_rozmiar);
		Tilosc = (TextView) findViewById(R.id.produkt_ilosc);
		Tproducent = (TextView) findViewById(R.id.producent);
		Bzamow = (Button) findViewById(R.id.zamow);
		Bzamow.setOnClickListener(this);
		new WyswietlProdukt().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.produkt, menu);
		return true;
	}

	class WyswietlProdukt extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Produkt.this);
			pDialog.setMessage("Wczytywanie produktu");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("produktId", "" + produktId));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(
						"http://softpartner.pl/projekt/wyswietl_produkt.php",
						"POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					// products found
					// Getting Array of Products
					dane = json.getJSONArray("dane");

					// looping through All Products
					for (int i = 0; i < dane.length(); i++) {
						JSONObject c = dane.getJSONObject(i);

						// Storing each json item in variable
						model = c.getString("model");
						nazwa = c.getString("nazwa");
						cena = c.getString("cena");
						rozmiar = c.getString("rozmiar");
						ilosc = c.getString("ilosc");
						producent = c.getString("producent");
					}
				} else {
				}
				komunikat = json.getString("message");
			} catch (Exception e) {
				komunikat = "" + e;
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			try {
				Tmodel.setText(model);
				Tnazwa.setText(nazwa);
				Tcena.setText(cena);
				Trozmiar.setText(rozmiar);
				Tilosc.setText(ilosc);
				Tproducent.setText(producent);
			} catch (Exception e) {

			}
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
								}
							}).create();
			return alertDialog;
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		if (v == Bzamow) {
			Intent i = new Intent(
					getApplicationContext(),
					Zamowienie.class);
			// sending pid to next activity
			i.putExtra("user_id", "" + user_id);
			i.putExtra("produktId", "" + produktId);
			i.putExtra("model", model);
			i.putExtra("nazwa", nazwa);
			i.putExtra("ilosc", ilosc);
			i.putExtra("cena", cena);
			i.putExtra("typ", typ);
			i.putExtra("producent", producent);
			startActivity(i);
		}
	}
}
