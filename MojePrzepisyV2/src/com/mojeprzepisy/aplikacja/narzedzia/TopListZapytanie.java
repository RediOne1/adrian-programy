package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.ListView;

public class TopListZapytanie{

	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	private ListView lv;
	private ArrayList<HashMap<String, String>> wszystkiePrzepisy;
	private MyListAdapter2 adapter;
	private Activity a;

	public TopListZapytanie(Activity _a, ListView _lv) {
		this.lv = _lv;
		this.a = _a;
		wszystkiePrzepisy = new ArrayList<HashMap<String, String>>();
		adapter = new MyListAdapter2(a, wszystkiePrzepisy);
		lv.setAdapter(adapter);
	}

	public void wykonaj(String... params) {
		new NajwyzejOcenianeZapytania().execute(params);
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	class NajwyzejOcenianeZapytania extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			String URL = args[0];
			String minLimit = args[1];
			String ilePrzepisow = args[2];

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("minLimit", minLimit));
			params.add(new BasicNameValuePair("ilePrzepisow", ilePrzepisow));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

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
					adapter.notifyDataSetChanged();
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
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			lv.invalidateViews();
		}
	}
}
