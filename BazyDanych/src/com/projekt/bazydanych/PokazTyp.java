package com.projekt.bazydanych;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PokazTyp extends ListActivity {

	private ArrayList<HashMap<String, String>> wszystkieProdukty;
	private ListView lv;
	private int user_id;
	private String typ;
	JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private MyListAdapter adapter;
	private final int ALERT_DIALOG_ID = 1;
	private ProgressDialog pDialog;
	private String komunikat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pokaz_typ);
		wszystkieProdukty = new ArrayList<HashMap<String, String>>();
		Bundle bundle = getIntent().getExtras();
		user_id = bundle.getInt("user_id");
		typ = bundle.getString("typ");

		lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String prodID = ((TextView) view
						.findViewById(R.id.lista_produkt_id)).getText()
						.toString();
				int produktID = 0;
				try {
					produktID = Integer.parseInt(prodID);
				} catch (Exception e) {
				}

				// Starting new intent
				Intent i = new Intent(getApplicationContext(), Produkt.class);
				// sending pid to next activity
				i.putExtra("autorID", user_id);
				i.putExtra("produktID", produktID);
				i.putExtra("typ", typ);

				// starting new activity and expecting some response back
				startActivity(i);
			}
		});
		new WyswietlWszystkieProdukty().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pokaz_typ, menu);
		return true;
	}

	class WyswietlWszystkieProdukty extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PokazTyp.this);
			pDialog.setMessage("£adowanie produktów");
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
			params.add(new BasicNameValuePair("typ", typ));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(
						"http://softpartner.pl/projekt/wyswietl_typ.php",
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
						String produktId = c.getString("produktId");
						String model = c.getString("model");
						String nazwa = c.getString("nazwa");
						String cena = c.getString("cena");
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put("produktId", produktId);
						map.put("model", model);
						map.put("nazwa", nazwa);
						map.put("cena", cena);

						// adding HashList to ArrayList
						wszystkieProdukty.add(map);
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
			setProgressBarIndeterminateVisibility(false);
			adapter = new MyListAdapter(PokazTyp.this, wszystkieProdukty);
			lv.setAdapter(adapter);
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
									if (user_id != 0) {
										Intent i = new Intent(
												getApplicationContext(),
												WybierzTyp.class);
										// sending pid to next activity
										i.putExtra("id", "" + user_id);
										startActivity(i);
									}
								}
							}).create();
			return alertDialog;
		}
		return null;
	}

}
