package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.Kategoria;
import com.mojeprzepisy.aplikacja.narzedzia.Szukaj;
import com.mojeprzepisy.aplikacja.narzedzia.WybierzKategorieListAdapter;

public class WybierzKategorie extends ListActivity implements OnClickListener {

	private String pseudonim;
	private int user_id;
	private JSONParser jParser = new JSONParser();
	private String kategorie[];
	private List<Kategoria> Kategorie;
	private String url_ile_przepisow, url_ile_przepisow_user;
	private WybierzKategorieListAdapter adapter;
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wybierz_kategorie);
		url_ile_przepisow = getString(R.string.url_ile_przepisow);
		url_ile_przepisow_user = getString(R.string.url_ile_przepisow_user);
		Kategorie = new ArrayList<Kategoria>();
		adapter = new WybierzKategorieListAdapter(WybierzKategorie.this, Kategorie);
		kategorie = getResources().getStringArray(R.array.kategorie);
		lv = getListView();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getApplicationContext(),
						Lista_przepisy.class);
				Kategoria k = (Kategoria) view.getTag();
				i.putExtra("kategoria", k);
				startActivity(i);
			}
		});
		for (String s : kategorie) {
			Kategoria k = new Kategoria(s);
			Kategorie.add(k);
		}
		lv.invalidateViews();
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
			lv.invalidateViews();
		}
	}
}
