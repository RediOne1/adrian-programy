package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class StronaGlowna extends MainActivity {

	View root;
	ImageView zdjecie;
	TextView tytul2;
	TextView kategoria2;
	RatingBar rate;
	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;

	StronaGlowna(View _root) {
		root = _root;
		przepisDnia();
	}

	private void przepisDnia() {
		View layout = root.findViewById(R.id.include1);
		zdjecie = (ImageView) layout.findViewById(R.id.zdjecie_maly_layout);
		tytul2 = (TextView) layout.findViewById(R.id.przepis_tytul_maly_layout);
		kategoria2 = (TextView) layout.findViewById(R.id.kategoria_maly_layout);
		rate = (RatingBar) layout.findViewById(R.id.ratingbar_maly_layout);
		//new WyswietlWszystkiePrzepisy().execute();
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
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("autorID", "1"));
			params.add(new BasicNameValuePair("przepisID", "1"));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(
						"http://softpartner.pl/moje_przepisy2/przepis.php", "POST", params);

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
						float oc = Float.parseFloat(ocena);
						// creating new HashMap

						// adding each child node to HashMap key => value
						ImageLoader imageLoader = new ImageLoader(getApplicationContext());
						imageLoader.DisplayImage(StrZdjecie, zdjecie);
						tytul2.setText(tytul);
						kategoria2.setText(kategoria);

						// adding HashList to ArrayList
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
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
		}
	}
}
