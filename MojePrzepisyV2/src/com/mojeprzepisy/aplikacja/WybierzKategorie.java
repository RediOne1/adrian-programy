package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.Kategoria;
import com.mojeprzepisy.aplikacja.narzedzia.Szukaj;

public class WybierzKategorie extends Activity implements OnClickListener {

	private String pseudonim;
	private int user_id;
	private JSONParser jParser = new JSONParser();
	private String kategorie[];
	private List<Kategoria> Kategorie;
	private LinearLayout layout;
	private String url_ile_przepisow, url_ile_przepisow_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		url_ile_przepisow = getString(R.string.url_ile_przepisow);
		url_ile_przepisow_user = getString(R.string.url_ile_przepisow_user);
		setContentView(R.layout.activity_wybierz_kategorie);
		Kategorie = new ArrayList<Kategoria>();
		kategorie = getResources().getStringArray(R.array.kategorie);
		layout = (LinearLayout) findViewById(R.id.kategorie);
		for (String s : kategorie) {
			Kategoria k = new Kategoria(s);
			Kategorie.add(k);
			layout.addView(k.toView(WybierzKategorie.this, layout));
		}
		new WyswietlKategorie().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wybierz_kategorie, menu);

		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		/*
		 * int info = v.getId(); Intent i = new Intent(getApplicationContext(),
		 * Lista_przepisy.class); i.putExtra("kategoria", kategorie[info]);
		 * i.putExtra("pseudonim", pseudonim); i.putExtra("user_id", user_id);
		 * startActivity(i);
		 */

	}

	class WyswietlKategorie extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JSONObject json;

				for (int i = 0; i < Kategorie.size(); i++) {
					params = new ArrayList<NameValuePair>();
					Kategoria k = Kategorie.get(i);
					params.add(new BasicNameValuePair("kategoria", k.toString()));
					json = jParser.makeHttpRequest(url_ile_przepisow, "POST",
							params);
					k.ilosc = json.getInt("ilosc");
					Kategorie.set(i, k);
					publishProgress();
					// adding HashList to ArrayList
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
			setProgressBarIndeterminateVisibility(false);
		}

		protected void onProgressUpdate(String... progress) {
			layout.removeAllViews();
			for (Kategoria k : Kategorie) {
				layout.addView(k.toView(WybierzKategorie.this, layout));
			}

		}
	}
}
