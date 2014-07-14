package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;

public class DrawerKategorie {

	public ListView lv;
	public Activity root;
	public String kategorie[];
	private JSONParser jParser = new JSONParser();
	private String url_ile_przepisow, url_ile_przepisow_user;
	public ArrayAdapter<String> adapter;

	public DrawerKategorie(Activity _root, ListView _lv) {
		this.lv = _lv;
		this.root = _root;
		url_ile_przepisow = root.getResources().getString(R.string.url_ile_przepisow);
		kategorie = root.getResources().getStringArray(R.array.kategorie);
		adapter = new ArrayAdapter<String>(root, R.layout.drawer_list_item,
				kategorie);
		lv.setAdapter(adapter);
		new WyswietlKategorie().execute();
	}

	class WyswietlKategorie extends AsyncTask<String, String, String> {

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
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JSONObject json;

				for (int i = 0; i < kategorie.length; i++) {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("kategoria", kategorie[i]));
					json = jParser.makeHttpRequest(url_ile_przepisow, "POST",
							params);
					kategorie[i] += " (" + json.getInt("ilosc") + ")";
					Log.d("DEBUG_TAG",kategorie[i]);
					publishProgress();
				}
			} catch (Exception e) {
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// updating listview
		}

		protected void onProgressUpdate(String... progress) {
			lv.invalidateViews();
		}
	}
}
