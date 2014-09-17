package com.softpartner.kolektorproduktow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.softpartner.kolektorproduktow.narzedzia.JSONParser;
import com.softpartner.kolektorproduktow.narzedzia.MyListAdapter;
import com.softpartner.kolektorproduktow.narzedzia.MyOnItemClickListener;

public class Skaner implements OnClickListener, TextWatcher {

	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	private FragmentActivity root;
	private Button scanBtn;
	private EditText searchEdit;
	private ListView lv;
	private SzukajProduktow szukajProduktow;
	private String url_szukaj_produktow;
	private ProgressBar pBar;
	private List<Produkt> produkty;
	private MyListAdapter adapter;
	private MyOnItemClickListener myOnItemClickListener;
	private View dodaj_button_layout;
	private TextView dodaj_istniejacy, dodaj_nowy;
	private boolean executed = false;
	MyApp app;

	public Skaner(ListFragment _root) {
		this.root = _root.getActivity();
		app = (MyApp) root.getApplicationContext();
		produkty = new ArrayList<Produkt>();
		adapter = new MyListAdapter(root, produkty);
		myOnItemClickListener = new MyOnItemClickListener(root);
		pBar = (ProgressBar) root.findViewById(R.id.szukaj_progress_bar);
		url_szukaj_produktow = root.getString(R.string.url_szukaj_produktow);
		scanBtn = (Button) root.findViewById(R.id.scan_button);
		searchEdit = (EditText) root.findViewById(R.id.search_edittext);
		dodaj_istniejacy = (TextView) root.findViewById(R.id.dodaj_istniejacy);
		dodaj_nowy = (TextView) root.findViewById(R.id.dodaj_nowy);
		dodaj_button_layout = (View) root
				.findViewById(R.id.include_dodaj_button);
		lv = _root.getListView();
		szukajProduktow = new SzukajProduktow();
		// szukajProduktow.execute();

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(myOnItemClickListener);
		scanBtn.setOnClickListener(this);
		searchEdit.addTextChangedListener(this);
		dodaj_istniejacy.setOnClickListener(this);
		dodaj_nowy.setOnClickListener(this);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			// String scanFormat = scanningResult.getFormatName();
			// formatTxt.setText("FORMAT: " + scanFormat);
			searchEdit.setText(scanContent);
		} else {
			Toast toast = Toast.makeText(root.getApplicationContext(),
					root.getString(R.string.brak_skanu), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == scanBtn) {
			IntentIntegrator scanIntegrator = new IntentIntegrator(root);
			scanIntegrator.initiateScan();
		} else if (v == dodaj_istniejacy) {
			app.schowek = searchEdit.getText().toString();
			Toast.makeText(root.getApplicationContext(),
					root.getString(R.string.zapamietano_kod) + app.schowek,
					Toast.LENGTH_SHORT).show();
			Toast.makeText(root.getApplicationContext(),
					root.getString(R.string.znajdz_aby_dodac),
					Toast.LENGTH_LONG).show();

		} else if (v == dodaj_nowy) {

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (executed)
			szukajProduktow.cancel(true);
		szukajProduktow = new SzukajProduktow();
		szukajProduktow.execute();
	}

	private class SzukajProduktow extends AsyncTask<String, Void, String> {

		protected void onPreExecute() {
			dodaj_button_layout.setVisibility(View.GONE);
			pBar.setVisibility(View.VISIBLE);
			executed = true;
			produkty.clear();
		}

		protected String doInBackground(String... urls) {
			String result = "";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", "" + app.getData()));
			params.add(new BasicNameValuePair("key", searchEdit.getText()
					.toString()));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_szukaj_produktow,
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
						Produkt p = new Produkt();
						p.ID = c.getString("id");
						p.nazwa = c.getString("nazwa");
						p.cena = c.getString("cena");
						p.ilosc = c.getString("ilosc");
						p.waga = c.getString("waga");
						p.opis = c.getString("opis");
						JSONArray kody = c.getJSONArray("kody");
						for (int j = 0; j < kody.length(); j++) {
							p.dodajKod(kody.getString(j));
						}
						if (!produkty.contains(p))
							produkty.add(p);
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
				Log.e("DEBUG_TAG", "Szukaj produkow: " + e);
			}
			return result;
		}

		protected void onPostExecute(String result) {
			adapter.notifyDataSetChanged();
			pBar.setVisibility(View.GONE);
			if (produkty.size() == 0)
				dodaj_button_layout.setVisibility(View.VISIBLE);
			executed = false;
		}

		@Override
		protected void onCancelled() {
			executed = false;
			super.onCancelled();
		}
	}
}
