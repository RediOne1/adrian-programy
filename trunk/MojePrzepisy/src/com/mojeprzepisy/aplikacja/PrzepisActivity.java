package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PrzepisActivity extends Activity {

	private String pseudonim;
	private int user_id;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private int przepisID;
	private String url_przepis;
	private String url_usun_przepis;
	private String url_ocen;
	private String url_ulubione;
	private ArrayList<HashMap<String, String>> Przepis;
	public String komunikat;
	private static final int ALERT_DIALOG_ID = 1;
	private final static int CUSTOM_DIALOG_ID = 2;
	private final static int USUN_DIALOG_ID = 3;
	private TextView tytul;
	private TextView kategoria;
	private TextView trudnosc;
	private TextView czas;
	private TextView opis;
	private TextView autor;
	private TextView dodano;
	private ImageView ZdjecieView;
	private String StrAutorID;
	private int autorID;
	private String StrAutor;
	private String StrTytul;
	private String StrOpis;
	private String StrKategoria;
	private String StrTrudnosc;
	private String StrCzas;
	private String StrDodano;
	private String skladniki;
	private String StrZdjecie;
	private Drawable zdjecie;
	private ProgressDialog pDialog;
	private boolean dialog;
	private Typeface MyNormal;
	private Typeface MyBold;
	private ImageLoader imageLoader;
	private ImageView addFavourite;
	private RatingBar rating; // mini
	private RatingBar ratingAktywny; // duzy
	private RatingBar rating2; // dialog
	private String StrOcena;
	int IntIloscOcen;
	private TextView IloscOcen;
	private WyslijZdjecie wyslij;
	private float ocena;
	private RelativeLayout relative;
	boolean x;
	boolean oceniony;
	private boolean ulubiony;
	SharedPreferences ulubione;
	private TextView ocenaTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_przepis);
		imageLoader = new ImageLoader(
				PrzepisActivity.this.getApplicationContext());
		url_przepis = getString(R.string.url_przepis);
		url_usun_przepis = getString(R.string.url_usun_przepis);
		url_ocen = getString(R.string.url_ocen);
		url_ulubione = getString(R.string.url_ulubione);
		Bundle bundle = getIntent().getExtras();
		wyslij = new WyslijZdjecie();
		autorID = bundle.getInt("autorID", -1);
		przepisID = bundle.getInt("przepisID");
		pseudonim = bundle.getString("pseudonim", null);
		user_id = bundle.getInt("user_id", 0);
		tytul = (TextView) findViewById(R.id.tytul);
		if (user_id == autorID) {
			TextView kod = (TextView) findViewById(R.id.kodPrzepisu);
			kod.setText("Twój nr. przepisu to " + przepisID);
		}
		MyNormal = Typeface.createFromAsset(getBaseContext().getAssets(),
				"fonts/SEGOEPRB.TTF");
		MyBold = Typeface.createFromAsset(getBaseContext().getAssets(),
				"fonts/SEGOEPRB.TTF");
		addFavourite = (ImageView) findViewById(R.id.addFavourite);
		ocenaTv = (TextView) findViewById(R.id.twoja_ocena);
		ratingAktywny = (RatingBar) findViewById(R.id.ratingBar2);
		relative = (RelativeLayout) findViewById(R.id.OcenRelativeLayout);
		Przepis = new ArrayList<HashMap<String, String>>();
		IloscOcen = (TextView) findViewById(R.id.iloscOcen);
		rating = (RatingBar) findViewById(R.id.ratingBar1);
		kategoria = (TextView) findViewById(R.id.kategoriaPrzepis);
		trudnosc = (TextView) findViewById(R.id.trudnosc);
		czas = (TextView) findViewById(R.id.czas);
		opis = (TextView) findViewById(R.id.Opis);
		autor = (TextView) findViewById(R.id.autor);
		dodano = (TextView) findViewById(R.id.dodano);
		ZdjecieView = (ImageView) findViewById(R.id.zdjecie);
		new WyswietlPrzepis().execute();
		relative.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (user_id != 0)
					showDialog(CUSTOM_DIALOG_ID);
				else {
					komunikat = "Tylko zalogowani u¿ytkownicy mog¹ oceniaæ przepisy.";
					x = false;
					showDialog(ALERT_DIALOG_ID);
				}
			}
		});
		addFavourite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (user_id != 0)
					ulubioneOnline();
				else
					ulubioneOffline();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.przepis, menu);

		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		if (autorID == user_id) {
			menu.add("Edytuj").setIcon(android.R.drawable.ic_menu_edit)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			menu.add("Usun").setIcon(android.R.drawable.ic_menu_delete)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
		} else if (item.toString() == "Edytuj") {
			Intent i = new Intent(getApplicationContext(), DodajPrzepis.class);
			i.putExtra("edytuj", true);
			i.putExtra("user_id", user_id);
			i.putExtra("przepisID", przepisID);
			i.putExtra("tytul", StrTytul);
			i.putExtra("opis", StrOpis);
			i.putExtra("kategoria", StrKategoria);
			i.putExtra("trudnosc", StrTrudnosc);
			i.putExtra("czas", StrCzas);
			i.putExtra("skladniki", skladniki);
			i.putExtra("zdjecie", StrZdjecie);
			startActivity(i);
		} else if (item.toString() == "Usun") {
			showDialog(USUN_DIALOG_ID);
		}

		return super.onOptionsItemSelected(item);
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
									if (x)
										finish();
								}
							}).create();
			return alertDialog;
		case USUN_DIALOG_ID:
			AlertDialog alertDialog2 = new AlertDialog.Builder(this)
					.setMessage(
							"Czy na pewno chcesz usun¹æ przepis: " + StrTytul
									+ "?")
					.setNegativeButton("Nie",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									new UsunPrzepis().execute();
								}
							}).create();
			return alertDialog2;
		case CUSTOM_DIALOG_ID:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.ocen,
					(ViewGroup) findViewById(R.id.root));
			rating2 = (RatingBar) layout.findViewById(R.id.ratingBar1);
			rating2.setRating(ratingAktywny.getRating());

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			// KOnfiguracja okna AlertDialog
			builder.setTitle("Oceñ przepis");
			builder.setNegativeButton("Anuluj",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Wymuszamy zamkniêcie i usuniêcie okna, tak by nie
							// mo¿na
							// by³o ponownie z niego skorzystaæ.
							removeDialog(CUSTOM_DIALOG_ID);
						}
					});
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							removeDialog(CUSTOM_DIALOG_ID);
							ocena = rating2.getRating();
							new OcenPrzepis().execute();
						}
					});
			// Twrozymy obiekt AlertDialog i zwracamy go.
			AlertDialog passwordDialog = builder.create();
			return passwordDialog;
		}
		return null;
	}

	class WyswietlPrzepis extends AsyncTask<String, String, String> {

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
			params.add(new BasicNameValuePair("przepisID", "" + przepisID));
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_przepis, "POST",
						params);

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
						StrAutor = c.getString("autor");
						StrTytul = c.getString("tytul");
						StrOpis = c.getString("opis");
						StrKategoria = c.getString("kategoria");
						StrTrudnosc = c.getString("trudnosc");
						StrCzas = c.getString("czas");
						StrDodano = c.getString("dodano");
						skladniki = c.getString("skladniki");
						StrZdjecie = c.getString("zdjecie");
						StrOcena = c.getString("ocena");
						String StrIloscOcen = c.getString("IloscOcen");
						IntIloscOcen = Integer.parseInt(StrIloscOcen);
						// Metody metody = new Metody();
						// zdjecie = metody.wczytaj_obrazek(StrZdjecie);

						// creating new HashMap

						String skladnikiTab[] = skladniki.split(";");

						// adding each child node to HashMap key => value
						for (int j = 0; j < skladnikiTab.length; j++) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("skladniki", skladnikiTab[j]);
							// adding HashList to ArrayList
							Przepis.add(map);
						}
						if (user_id != 0)
							ulubiony = json.getBoolean("ulubiony");
						else {
							ulubione = getSharedPreferences("Ulubione", 0);
							ulubiony = ulubione.getBoolean("" + przepisID,
									false);
						}
						oceniony = json.getBoolean("oceniony");
						if (oceniony) {
							ocena = Float.parseFloat(json.getString("ocena"));
						}

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
				x = true;
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
				if (oceniony) {
					ratingAktywny.setRating(ocena);
					ocenaTv.setText("Twoja ocena");
				}
				Drawable myDrawable;
				if (ulubiony) {
					myDrawable = getResources().getDrawable(R.drawable.heart);
					addFavourite.setImageDrawable(myDrawable);
				} else {
					myDrawable = getResources()
							.getDrawable(R.drawable.heart_bw);
					addFavourite.setImageDrawable(myDrawable);
				}
				// dismiss the dialog after getting all products
				// updating UI from Background Thread
				float mini_rating = Float.parseFloat(StrOcena);
				rating.setRating(mini_rating);
				TextView sreOcena = (TextView) findViewById(R.id.sredniaOcena);
				sreOcena.setText("Ocena: " + mini_rating);
				IloscOcen.setText("" + IntIloscOcen);
				tytul.setText(StrTytul);
				kategoria.setText("kategoria: " + StrKategoria);
				trudnosc.setText(StrTrudnosc);
				czas.setText(StrCzas);
				StrOpis = StrOpis.replace("\n", "<br>");
				opis.setText(Html.fromHtml(StrOpis));
				autor.setText("przez: " + StrAutor);
				dodano.setText("dodano: " + StrDodano);
				tytul.setTypeface(MyBold);
				imageLoader.DisplayImage(StrZdjecie, ZdjecieView);

				// if(zdjecie!=null){
				// ZdjecieView.setImageDrawable(zdjecie);
				// }

				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
						ListAdapter adapter = new SimpleAdapter(
								PrzepisActivity.this, Przepis,
								R.layout.skladniki,
								new String[] { "skladniki" },
								new int[] { R.id.skladnik });
						// updating listview
						for (int i = 0; i < adapter.getCount(); i++) {
							View item = adapter.getView(i, null, null);
							layout.addView(item);
						}
					}
				});
			}
			setProgressBarIndeterminateVisibility(false);
		}
	}

	class DodajUsunUlubione extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PrzepisActivity.this);
			if (ulubiony)
				pDialog.setMessage("Trwa dodawanie do ulubionych...");
			else
				pDialog.setMessage("Trwa usuwanie z ulubionych...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			dialog = false;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", "" + przepisID));
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			params.add(new BasicNameValuePair("lubie", "" + ulubiony));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_ulubione, "POST",
						params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");
				if (success == 1) {
				} else {
					dialog = true;
					komunikat = "Nie uda³o siê dodaæ przepisu do ulubionych.";
				}
			} catch (Exception e) {
				Log.d("DEBUG_TAG", "" + e);
				dialog = true;
				komunikat = "B³¹d w po³¹czeniu.";
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (dialog) {
				showDialog(ALERT_DIALOG_ID);
			} else {
				if (ulubiony)
					Toast.makeText(PrzepisActivity.this,
							"Dodano do ulubionych", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(PrzepisActivity.this,
							"Usuniêto z ulubionych.", Toast.LENGTH_SHORT)
							.show();
			}
		}
	}

	class OcenPrzepis extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PrzepisActivity.this);
			pDialog.setMessage("Dodawanie oceny...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			dialog = false;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", "" + przepisID));
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			params.add(new BasicNameValuePair("ocena", "" + ocena));
			params.add(new BasicNameValuePair("oceniony", "" + oceniony));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_ocen, "POST",
						params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");
				if (success == 1) {

				} else {
					dialog = true;
					komunikat = "Nie uda³o siê oceniæ przepisu.";
				}
			} catch (Exception e) {
				Log.d("DEBUG_TAG", "" + e);
				dialog = true;
				komunikat = "B³¹d w po³¹czeniu.";
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (dialog) {
				showDialog(ALERT_DIALOG_ID);
			} else {
				float srednia = rating.getRating();
				float wynik = oceniony ? (srednia * (IntIloscOcen - 1) + ocena)
						/ IntIloscOcen : (srednia * IntIloscOcen + ocena)
						/ (IntIloscOcen + 1);
				rating.setRating(wynik);
				if (!oceniony)
					IloscOcen.setText("" + (IntIloscOcen + 1));
				ratingAktywny.setRating(ocena);
				ocenaTv.setText("Twoja ocena");
				// dismiss the dialog after getting all products
				setProgressBarIndeterminateVisibility(false);
				Toast.makeText(PrzepisActivity.this,
						"Twoja ocena to: " + ocena, Toast.LENGTH_SHORT).show();

			}
			oceniony = true;
		}
	}

	class UsunPrzepis extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PrzepisActivity.this);
			pDialog.setMessage("Usuwanie przepisu...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			dialog = false;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", "" + przepisID));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_usun_przepis,
						"POST", params);
				wyslij.usunZdjecie(StrTytul);
			} catch (Exception e) {
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			finish();
		}
	}

	public void ulubioneOnline() {
		Drawable myDrawable;
		if (ulubiony) {
			myDrawable = getResources().getDrawable(R.drawable.heart_bw);
			addFavourite.setImageDrawable(myDrawable);
			ulubiony = false;
		} else {
			myDrawable = getResources().getDrawable(R.drawable.heart);
			addFavourite.setImageDrawable(myDrawable);
			ulubiony = true;
		}
		new DodajUsunUlubione().execute();
	}

	public void ulubioneOffline() {
		ulubione = getSharedPreferences("Ulubione", 0);
		SharedPreferences.Editor edytorPref = ulubione.edit();
		Drawable myDrawable;
		if (ulubiony) {
			edytorPref.remove("" + przepisID);
			edytorPref.remove("Int " + przepisID);
			edytorPref.commit();
			myDrawable = getResources().getDrawable(R.drawable.heart_bw);
			addFavourite.setImageDrawable(myDrawable);
			ulubiony = false;
			Toast.makeText(PrzepisActivity.this, "Usuniêto z ulubionych",
					Toast.LENGTH_SHORT).show();
		} else {
			edytorPref.putBoolean("" + przepisID, true);
			edytorPref.putInt("Int " + przepisID, przepisID);
			edytorPref.commit();
			myDrawable = getResources().getDrawable(R.drawable.heart);
			addFavourite.setImageDrawable(myDrawable);
			ulubiony = true;
			Toast.makeText(PrzepisActivity.this, "Dodano do ulubionych",
					Toast.LENGTH_SHORT).show();
		}
	}
}
