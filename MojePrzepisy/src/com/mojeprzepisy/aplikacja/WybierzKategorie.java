package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WybierzKategorie extends Activity implements OnClickListener {

	private String pseudonim;
	private int user_id;
	private JSONParser jParser = new JSONParser();
	private String kategorie[];
	private ArrayList<HashMap<String, String>> Kategorie;
	private LinearLayout layout;
	private String url_ile_przepisow, url_ile_przepisow_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		url_ile_przepisow = getString(R.string.url_ile_przepisow);
		url_ile_przepisow_user = getString(R.string.url_ile_przepisow_user);
		setContentView(R.layout.activity_wybierz_kategorie);
		Bundle bundle = getIntent().getExtras();
		pseudonim = bundle.getString("pseudonim", "");
		user_id = bundle.getInt("user_id", 0);
		Typeface MyBold = Typeface.createFromAsset(
				getBaseContext().getAssets(), "fonts/SEGOEPRB.TTF");
		TextView tv = (TextView) findViewById(R.id.lista_kategoria);
		tv.setTypeface(MyBold);
		Kategorie = new ArrayList<HashMap<String, String>>();
		kategorie = getResources().getStringArray(R.array.kategorie);
		layout = (LinearLayout) findViewById(R.id.kategorie);
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
		int info = v.getId();
		Intent i = new Intent(getApplicationContext(), Lista_przepisy.class);
		/*
		 * if (info == 0 && user_id != 0) { i.putExtra("kategoria", "user"); }
		 * else if (user_id != 0) info--;
		 */
		i.putExtra("kategoria", kategorie[info]);
		i.putExtra("pseudonim", pseudonim);
		i.putExtra("user_id", user_id);
		startActivity(i);

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
			// Building Parameters
			// getting JSON string from URL
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JSONObject json;
				/*
				 * if (user_id != 0) { params.add(new
				 * BasicNameValuePair("autorID", "" + user_id)); json =
				 * jParser.makeHttpRequest(url_ile_przepisow_user, "POST",
				 * params); map.put("kategoria", "Moje przepisy" + "(" +
				 * json.getString("ilosc") + ")");
				 * 
				 * Kategorie.add(map); }
				 */

				for (int j = 0; j < kategorie.length; j++) {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("kategoria", kategorie[j]));

					json = jParser.makeHttpRequest(url_ile_przepisow, "POST",
							params);

					String dane = kategorie[j] + "(" + json.getString("ilosc")
							+ ")";
					publishProgress(dane);
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
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("kategoria", progress[0]);
			Kategorie.add(map);
			ListAdapter adapter = new SimpleAdapter(WybierzKategorie.this,
					Kategorie, R.layout.kategoria,
					new String[] { "kategoria" },
					new int[] { R.id.lista_kategoria });
			layout.removeAllViews();
			for (int i = 0; i < adapter.getCount(); i++) {
				View item = adapter.getView(i, null, null);
				item.setOnClickListener(WybierzKategorie.this);
				item.setId(i);
				layout.addView(item);
			}
		}
	}
}
