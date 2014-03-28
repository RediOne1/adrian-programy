package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Skladnik;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;

public class WyswietlSkladniki extends WyswietlPrzepis {

	private LinearLayout linearLayout;
	private Przepis przepis;
	private Activity root;
	private ProgressBar progress;
	private Handler mHandler = new Handler();
	private List<Skladnik> skladniki;
	private String URL = "http://softpartner.pl/moje_przepisy2/pobierz_skladniki.php";
	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	private Skladnik skladnik;
	String skladniki_tab[];

	public WyswietlSkladniki(Activity _root, Przepis _przepis) {
		this.root = _root;
		linearLayout = (LinearLayout) root
				.findViewById(R.id.wyswietl_skladniki_linearLayout);
		progress = (ProgressBar) root.findViewById(R.id.wyswietl_skladniki_progressbar);
		this.przepis = _przepis;
		skladniki = new ArrayList<Skladnik>();
		new PobierzSkladniki().execute();
	}
	public List<Skladnik> getSkladniki(){
		return skladniki;
	}
	class PobierzSkladniki extends AsyncTask<String, Skladnik, String> {
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
			params.add(new BasicNameValuePair("przepisID", ""
					+ przepis.przepisID));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(URL, "POST", params);
				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					dane = json.getJSONArray("dane");
					JSONObject c = dane.getJSONObject(0);
					String temp = c.getString("skladniki");
					Log.d("Wyswietl skladniki", "Pobrano sk�adnik: " + temp);
					skladniki_tab = temp.split(";");
					root.runOnUiThread(new Runnable() {
						public void run() {
							for (int i = 0; i < skladniki_tab.length; i++) {
								publishProgress(new Skladnik("- "+skladniki_tab[i]));
							}
						}
					});

				} else {
				}
			} catch (Exception e) {
				Log.e("Wyswietl skladniki", "" + e);
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
		protected void onProgressUpdate(Skladnik... progress) {
			skladnik = progress[0];
			root.runOnUiThread(new Runnable() {
				public void run() {
					skladniki.add(skladnik);
					linearLayout.addView(skladnik.wyswietl(root, linearLayout));
				}
			});
		}
	}
}
