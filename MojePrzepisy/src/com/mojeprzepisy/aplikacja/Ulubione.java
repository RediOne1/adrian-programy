package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mojeprzepisy.aplikacja.Main_Quest.NajwyzejOceniane;
import com.mojeprzepisy.aplikacja.Main_Quest.OstatnioDodane;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Ulubione extends ListActivity {

	SharedPreferences ulubione;
	private int user_id;
	private int OfflineUlubID[];
	private int j;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private ArrayList<HashMap<String, String>> wszystkiePrzepisy;
	private String url_wszystkie_przepisy;
	private String url_ulubione_online;
	private MyListAdapter adapter;
	private boolean dialog;
	private ListView lv;
	private String pseudonim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ulubione);
		url_wszystkie_przepisy = getString(R.string.url_wszystkie_przepisy);
		url_ulubione_online = getString(R.string.url_ulubione_online);
		wszystkiePrzepisy = new ArrayList<HashMap<String, String>>();
		Bundle bundle = getIntent().getExtras();
		user_id = bundle.getInt("user_id", 0);
		pseudonim = bundle.getString("pseudonim", "");
		if (user_id != 0)
			ulubioneOnline();
		else
			ulubioneOffline();
		lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String przepID = ((TextView) view.findViewById(R.id.przepisID))
						.getText().toString();
				String autID = ((TextView) view.findViewById(R.id.autorID))
						.getText().toString();
				int autorID = 0;
				int przepisID = 0;
				try {
					autorID = Integer.parseInt(autID);
					przepisID = Integer.parseInt(przepID);
				} catch (Exception e) {
				}

				// Starting new intent
				Intent i = new Intent(getApplicationContext(),
						PrzepisActivity.class);
				// sending pid to next activity
				i.putExtra("autorID", autorID);
				i.putExtra("przepisID", przepisID);
				i.putExtra("pseudonim", pseudonim);
				i.putExtra("user_id", user_id);

				// starting new activity and expecting some response back
				startActivity(i);
			}
		});
	}

	public void ulubioneOnline() {
		new WyswietlUlubioneOnline().execute();
	}

	public void ulubioneOffline() {
		ulubione = getSharedPreferences("Ulubione", 0);
		Map<String, ?> map = ulubione.getAll();
		Object tab[] = map.values().toArray();
		OfflineUlubID = new int[tab.length];
		j = 0;
		for (int i = 0; i < tab.length; i++) {
			if (tab[i].toString().equals("true"))
				continue;
			int x = Integer.parseInt(tab[i].toString());
			OfflineUlubID[j] = x;
			j++;
		}
		new WyswietlWszystkiePrzepisy().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ulubione, menu);
		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("pseudonim", "");
			i.putExtra("user_id", 0);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	class WyswietlWszystkiePrzepisy extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			dialog = false;
			// Building Parameters
			for (int a = 0; a < j; a++) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("przepisID", ""
						+ OfflineUlubID[a]));
				// getting JSON string from URL
				try {
					JSONObject json = jParser.makeHttpRequest(
							url_wszystkie_przepisy, "POST", params);

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
							String autorID = c.getString("autorID");
							String przepisID = c.getString("przepisID");
							String tytul = c.getString("tytul");
							String kategoria = c.getString("kategoria");
							String StrZdjecie = c.getString("zdjecie");
							String ocena = c.getString("ocena");
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							map.put("autorID", autorID);
							map.put("przepisID", przepisID);
							map.put("tytul", tytul);
							map.put("kategoria", kategoria);
							map.put("zdjecie", StrZdjecie);
							map.put("ocena", ocena);

							// adding HashList to ArrayList
							wszystkiePrzepisy.add(map);
						}
					} else {
						// no products found
						// Launch Add New product Activity
						/*
						 * Intent i = new Intent(getApplicationContext(),
						 * NewProductActivity.class); // Closing all previous
						 * activities
						 * i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 * startActivity(i);
						 */
					}
				} catch (Exception e) {
					dialog = true;
					// komunikat = "B³¹d w po³¹czeniu.";
				}
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			if (dialog) {
				// showDialog(ALERT_DIALOG_ID);
			} else {
				// dismiss the dialog after getting all products
				setProgressBarIndeterminateVisibility(false);
				adapter = new MyListAdapter(Ulubione.this, wszystkiePrzepisy);
				lv.setAdapter(adapter);
			}

		}
	}

	class WyswietlUlubioneOnline extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			dialog = false;
			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_ulubione_online,
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
						String autorID = c.getString("autorID");
						String przepisID = c.getString("przepisID");
						String tytul = c.getString("tytul");
						String kategoria = c.getString("kategoria");
						String StrZdjecie = c.getString("zdjecie");
						String ocena = c.getString("ocena");
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key =>
						// value
						map.put("autorID", autorID);
						map.put("przepisID", przepisID);
						map.put("tytul", tytul);
						map.put("kategoria", kategoria);
						map.put("zdjecie", StrZdjecie);
						map.put("ocena", ocena);

						// adding HashList to ArrayList
						wszystkiePrzepisy.add(map);
					}
				} else {
					// no products found
					// Launch Add New product Activity
					/*
					 * Intent i = new Intent(getApplicationContext(),
					 * NewProductActivity.class); // Closing all previous
					 * activities i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 * startActivity(i);
					 */
				}
			} catch (Exception e) {
				dialog = true;
				// komunikat = "B³¹d w po³¹czeniu.";
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			if (dialog) {
				// showDialog(ALERT_DIALOG_ID);
			} else {
				// dismiss the dialog after getting all products
				setProgressBarIndeterminateVisibility(false);
				adapter = new MyListAdapter(Ulubione.this, wszystkiePrzepisy);
				lv.setAdapter(adapter);
			}
		}
	}
}
