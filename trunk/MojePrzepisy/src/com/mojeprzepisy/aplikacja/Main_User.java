package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main_User extends Activity implements OnClickListener {

	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private ArrayList<HashMap<String, String>> najOceniane;
	private ArrayList<HashMap<String, String>> ostDodane;
	private final static int DOSTEPNE_WKROTCE = 1;
	private final static int ALERT_DIALOG_ID = 2;
	private final static int INFO_DIALOG_ID = 3;
	private int x = 0;
	private String pseudonim;
	private int user_id;
	private TextView zalogowany_jako;
	private String url_najwyzej_oceniane;
	private String url_ostatnio_dodane;
	boolean dialog;
	private String komunikat;
	private MyListAdapter adapter;
	private ImageView dodaj;
	private ImageView wszystkie;
	private ImageView share;
	private ImageView favourite;
	private LinearLayout layout1;
	private LinearLayout layout2;
	private SharedPreferences ustawienia;
	private CheckBox niePokazuj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_user);
		url_najwyzej_oceniane = getString(R.string.url_najwyzej_oceniane);
		url_ostatnio_dodane = getString(R.string.url_ostatnio_dodane);
		najOceniane = new ArrayList<HashMap<String, String>>();
		ostDodane = new ArrayList<HashMap<String, String>>();
		zalogowany_jako = (TextView) findViewById(R.id.zalogowany_jako);
		Bundle bundle = getIntent().getExtras();
		pseudonim = bundle.getString("pseudonim");
		user_id = bundle.getInt("user_id");
		zalogowany_jako.setText("Zalogowany jako: " + pseudonim
				+ "\nTwój kod to: " + (user_id + 200));
		wszystkie = (ImageView) findViewById(R.id.all_icon);
		wszystkie.setOnClickListener(this);
		share = (ImageView) findViewById(R.id.share_icon);
		share.setOnClickListener(this);
		favourite = (ImageView) findViewById(R.id.favourite_icon);
		favourite.setOnClickListener(this);
		dodaj = (ImageView) findViewById(R.id.compose_icon);
		dodaj.setOnClickListener(this);
		new NajwyzejOceniane().execute();
		new OstatnioDodane().execute();
		Toast.makeText(Main_User.this, "Zalogowany jako: " + pseudonim,
				Toast.LENGTH_SHORT).show();
		ustawienia = getSharedPreferences("MyCustomSharedPreferences", 0);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_user, menu);
		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences.Editor edytorPref;
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
		}
		switch (item.getItemId()) {
		case R.id.wyloguj:
			ustawienia = getSharedPreferences("MyCustomSharedPreferences", 0);
			edytorPref = ustawienia.edit();
			edytorPref.remove("Haslo");
			edytorPref.remove("zapamietaj");
			edytorPref.commit();
			Intent i = new Intent(getApplicationContext(), StartActivity.class);
			finish();
			startActivity(i);
			break;
		case R.id.odswiez:
			najOceniane = new ArrayList<HashMap<String, String>>();
			ostDodane = new ArrayList<HashMap<String, String>>();
			layout1.removeAllViewsInLayout();
			layout2.removeAllViewsInLayout();
			new NajwyzejOceniane().execute();
			new OstatnioDodane().execute();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case INFO_DIALOG_ID:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.info,
					(ViewGroup) findViewById(R.id.root));
			niePokazuj = (CheckBox) layout.findViewById(R.id.niePokazuj);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if (niePokazuj.isChecked()) {
								ustawienia = getSharedPreferences(
										"MyCustomSharedPreferences", 0);
								SharedPreferences.Editor edytorPref = ustawienia
										.edit();
								edytorPref.putBoolean("niePokazuj", true);
								edytorPref.commit();
							}
							removeDialog(INFO_DIALOG_ID);
						}
					});
			// Twrozymy obiekt AlertDialog i zwracamy go.
			AlertDialog passwordDialog = builder.create();
			return passwordDialog;
		case ALERT_DIALOG_ID:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setMessage(komunikat)
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									removeDialog(ALERT_DIALOG_ID);
								}
							}).create();
			return alertDialog;
		case DOSTEPNE_WKROTCE:
			AlertDialog alertDialog2 = new AlertDialog.Builder(this)
					.setMessage(komunikat)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									removeDialog(DOSTEPNE_WKROTCE);
								}
							}).create();
			return alertDialog2;
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		if (v == dodaj) {
			Intent i = new Intent(getApplicationContext(), DodajPrzepis.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
		} else if (v == wszystkie) {
			Intent i = new Intent(getApplicationContext(),
					WybierzKategorie.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
		} else if (v == share) {
			komunikat = "Opcja udostêpniania dostêpna ju¿ wkrótce ;-)";
			showDialog(DOSTEPNE_WKROTCE);
		} else if (v == favourite) {
			Intent i = new Intent(Main_User.this, Ulubione.class);
			i.putExtra("user_id", user_id);
			i.putExtra("pseudonim", pseudonim);
			startActivity(i);
		} else {
			String przepID = ((TextView) v.findViewById(R.id.przepisID))
					.getText().toString();
			String autID = ((TextView) v.findViewById(R.id.autorID)).getText()
					.toString();
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
	}

	class NajwyzejOceniane extends AsyncTask<String, String, String> {

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
			dialog = false;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(
						url_najwyzej_oceniane, "POST", params);

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
						najOceniane.add(map);
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
				komunikat = "B³¹d w po³¹czeniu.";
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
				layout1 = (LinearLayout) findViewById(R.id.listView1);
				adapter = new MyListAdapter(Main_User.this, najOceniane);
				// updating listview
				for (int i = 0; i < adapter.getCount(); i++) {
					View item = adapter.getView(i, null, null);
					item.setOnClickListener(Main_User.this);
					item.setTag(adapter.getView(i, null, null));
					layout1.addView(item);
				}
			}
		}
	}

	class OstatnioDodane extends AsyncTask<String, String, String> {

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
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_ostatnio_dodane,
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
						ostDodane.add(map);
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
				komunikat = "B³¹d w po³¹czeniu.";
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
				layout2 = (LinearLayout) findViewById(R.id.listView2);
				adapter = new MyListAdapter(Main_User.this, ostDodane);
				// updating listview
				for (int i = 0; i < adapter.getCount(); i++) {
					View item = adapter.getView(i, null, null);
					item.setOnClickListener(Main_User.this);
					item.setTag(adapter.getView(i, null, null));
					layout2.addView(item);
				}
			}
		}
	}

}
