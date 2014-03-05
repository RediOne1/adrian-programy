package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Krok;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Skladnik;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;

public class WyswietlOpis extends WyswietlPrzepis {
	private LinearLayout linearLayout;
	private Przepis przepis;
	private Activity root;
	private List<Krok> kroki;
	private String URL = "http://softpartner.pl/moje_przepisy2/pobierz_opis.php";
	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	private Krok krok;
	String kroki_tab[];

	public WyswietlOpis(Activity _root, Przepis _przepis) {
		this.root = _root;
		linearLayout = (LinearLayout) root
				.findViewById(R.id.wyswietl_opis_linearLayout);
		this.przepis = _przepis;
		kroki = new ArrayList<Krok>();
		new PobierzSkladniki().execute();
	}

	class PobierzSkladniki extends AsyncTask<String, Krok, String> {
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
					String temp = c.getString("opis");
					Log.d("Wyswietl opis", "Pobrano opis: " + temp);
					kroki_tab = temp.split(";;");
					root.runOnUiThread(new Runnable() {
						public void run() {
							for (int i = 0; i < kroki_tab.length; i++) {
								if (kroki_tab[i].length() == 0)
									continue;
								String temp2[] = kroki_tab[i].split(";");
								publishProgress(new Krok(temp2[0],temp2[1]));
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
		}

		@Override
		protected void onProgressUpdate(Krok... progress) {
			krok = progress[0];
			root.runOnUiThread(new Runnable() {
				public void run() {
					kroki.add(krok);
					linearLayout.addView(krok.wyswietl(root, linearLayout));
				}
			});
		}
	}
}
