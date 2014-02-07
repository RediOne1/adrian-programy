package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Lista_przepisy extends ListActivity {

	private String pseudonim;
	private int user_id;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private ArrayList<HashMap<String, String>> wszystkiePrzepisy;
	private ImageView image;
	private String kategoria;
	private final static int ALERT_DIALOG_ID = 2;
	private String url_wszystkie_przepisy;
	private String url_user_przepisy;
	private String komunikat;
	private boolean dialog = false;
	private MyListAdapter adapter;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_przepisy);
		url_wszystkie_przepisy = getString(R.string.url_wszystkie_przepisy);
		url_user_przepisy = getString(R.string.url_user_przepisy);
		Bundle bundle = getIntent().getExtras();
		kategoria = bundle.getString("kategoria");
		pseudonim = bundle.getString("pseudonim", null);
		user_id = bundle.getInt("user_id", 0);
		wszystkiePrzepisy = new ArrayList<HashMap<String, String>>();

		new WyswietlWszystkiePrzepisy().execute();

		lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String przepID = ((TextView) view.findViewById(R.id.przepisID))
						.getText().toString();
				String autID = ((TextView) view.findViewById(R.id.autorID))
						.getText().toString();
				int autorID = 0;
				int przepisID = 0;
				try {
					autorID = Integer.parseInt(autID);
					przepisID = Integer.parseInt(przepID);
				} catch (Exception e) {
				}

				// Starting new intent
				Intent i = new Intent(getApplicationContext(),
						PrzepisActivity.class);
				// sending pid to next activity
				i.putExtra("autorID", autorID);
				i.putExtra("przepisID", przepisID);
				i.putExtra("pseudonim", pseudonim);
				i.putExtra("user_id", user_id);

				// starting new activity and expecting some response back
				startActivity(i);
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case ALERT_DIALOG_ID:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setMessage(komunikat)
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).create();
			return alertDialog;
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wszystkie_przepisy, menu);

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
			dialog = false;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("kategoria", kategoria));
			// getting JSON string from URL
			try {
				JSONObject json;
				if (kategoria.equals("user")) {
					params.add(new BasicNameValuePair("autorID", "" + user_id));
					json = jParser.makeHttpRequest(url_user_przepisy, "POST",
							params);
				} else
					json = jParser.makeHttpRequest(url_wszystkie_przepisy,
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
				dialog = true;
				komunikat = "B³¹d w po³¹czeniu."+e;
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			if (dialog) {
				showDialog(ALERT_DIALOG_ID);
			} else {
				// dismiss the dialog after getting all products
				setProgressBarIndeterminateVisibility(false);
				adapter = new MyListAdapter(Lista_przepisy.this,
						wszystkiePrzepisy);
				lv.setAdapter(adapter);
			}

		}

	}

}
