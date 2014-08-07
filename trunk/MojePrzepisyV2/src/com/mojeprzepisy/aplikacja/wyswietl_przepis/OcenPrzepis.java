package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;

public class OcenPrzepis extends WyswietlPrzepis {
	

	private final String URL = "http://softpartner.pl/moje_przepisy2/pobierz_ocene_uzytkownika.php";
	private final String URL2 = "http://softpartner.pl/moje_przepisy2/wyslij_ocene.php";

	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	private int user_id;
	private ProgressBar progress;
	private float ocena;
	private Activity root;
	private WyswietlPrzepis wyswietlPrzepis;
	private Przepis przepis;
	private TextView ocen_tv;
	private RelativeLayout rl;
	public RatingBar ocen_ratingbar;
	private MyApp app;

	public OcenPrzepis(Activity _root, Przepis _przepis,
			WyswietlPrzepis _wyswietlPrzepis) {
		this.wyswietlPrzepis = _wyswietlPrzepis;
		this.root = _root;
		this.przepis = _przepis;
		app = (MyApp) root.getApplicationContext();
		user_id = app.getData();
		rl = (RelativeLayout) root.findViewById(R.id.ocen_relative_layout);
		if(user_id == -1)
			rl.setVisibility(View.GONE);
		ocen_tv = (TextView) root.findViewById(R.id.ocen_przepis_textview);
		rl.setOnClickListener(wyswietlPrzepis);
		progress = (ProgressBar) root
				.findViewById(R.id.ocen_przepis_progressbar);
		ocen_ratingbar = (RatingBar) root
				.findViewById(R.id.ocen_przepis_ratingbar);
		new PobierzOcene().execute();
	}

	public void ocen(float _ocena) {
		this.ocena = _ocena;
		new WyslijOcene().execute(ocena);
	}

	class PobierzOcene extends AsyncTask<String, Float, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", "" + user_id));
			params.add(new BasicNameValuePair("przepisID", ""
					+ przepis.przepisID));
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
						String ocena = c.getString("ocena");
						float ocenaF = Float.parseFloat(ocena);
						publishProgress(ocenaF);
					}
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
			progress.setVisibility(View.GONE);
		}

		@Override
		protected void onProgressUpdate(Float... progress) {
			ocen_tv.setText(root.getResources().getString(R.string.twoja_ocena));
			ocen_ratingbar.setRating(progress[0]);
		}
	}

	class WyslijOcene extends AsyncTask<Float, Float, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		@Override
		protected String doInBackground(Float... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", "" + user_id));
			params.add(new BasicNameValuePair("przepisID", "" + przepis.przepisID));
			params.add(new BasicNameValuePair("ocena", "" + args[0]));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(URL2, "POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
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
			ocen_ratingbar.setRating(ocena);
			MyApp app = (MyApp) root.getApplicationContext();
			app.reloadActivity();
			wyswietlPrzepis.wyswietlTytulowyModul.odswiez();
		}
	}
}
