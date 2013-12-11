package com.mojeprzepisy.aplikacja;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class DodajPrzepis extends Activity implements OnClickListener {

	private String pseudonim;
	private int user_id;
	private int przepisID = 0;
	private int success;
	private MultiAutoCompleteTextView skladnikiDodaj;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private static final int POTWIERDZ_DIALOG_ID = 2;
	private static final int ALERT_DIALOG_ID = 1;
	private ImageLoader imageLoader;
	ProgressDialog pDialog;
	private ImageView zdjecie;
	private TextView htmlView;
	private TextView opisTytul;
	private EditText tytulDodaj;
	private EditText urlDodaj;
	private EditText opisDodaj;
	private String skladniki[];
	private String pomocnicza[];
	private String komunikat;
	private Button zrobZdjecie;
	private Button wybierzZdjecie;
	private Spinner kategoriaDodaj;
	private Spinner trudnoscDodaj;
	private Spinner czasDodaj;
	private boolean dialog;
	private String url_multi_complete;
	private String url_dodaj_przepis;
	private String url_edytuj_przepis;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int REQ_CODE_PICK_IMAGE = 101;
	private Bitmap zdjecieBitmap = null;
	private String staryTytul;
	private String stareZdjecie;
	WyslijZdjecie wyslij;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dodaj_przepis);
		url_multi_complete = getString(R.string.url_multi_complete);
		url_dodaj_przepis = getString(R.string.url_dodaj_przepis);
		url_edytuj_przepis = getString(R.string.url_edytuj_przepis);
		tytulDodaj = (EditText) findViewById(R.id.tytulDodaj);
		wyslij = new WyslijZdjecie();
		skladnikiDodaj = (MultiAutoCompleteTextView) findViewById(R.id.skladnikiDodaj);
		Typeface MyBold = Typeface.createFromAsset(
				getBaseContext().getAssets(), "fonts/SEGOEPRB.TTF");
		urlDodaj = (EditText) findViewById(R.id.urlDodaj);
		htmlView = (TextView) findViewById(R.id.htmlView);
		kategoriaDodaj = (Spinner) findViewById(R.id.kategoriaDodaj);
		trudnoscDodaj = (Spinner) findViewById(R.id.trudnoscDodaj);
		czasDodaj = (Spinner) findViewById(R.id.czasDodaj);
		opisDodaj = (EditText) findViewById(R.id.opisDodaj);
		opisTytul = (TextView) findViewById(R.id.textView5);
		zrobZdjecie = (Button) findViewById(R.id.zrobZdjecie);
		zrobZdjecie.setTypeface(MyBold);
		wybierzZdjecie = (Button) findViewById(R.id.wybierzZdjecie);
		wybierzZdjecie.setTypeface(MyBold);
		opisTytul.setText(Html.fromHtml("Krok po kroku:  <b><u>?</u></b>"));
		zdjecie = (ImageView) findViewById(R.id.addFavourite);
		imageLoader = new ImageLoader(DodajPrzepis.this.getApplicationContext());
		imageLoader.DisplayImage("", zdjecie);
		zrobZdjecie.setOnClickListener(this);
		wybierzZdjecie.setOnClickListener(this);
		opisTytul.setOnClickListener(this);
		urlDodaj.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				imageLoader
						.DisplayImage(urlDodaj.getText().toString(), zdjecie);
			}

			// ... inne metody, które trzeba przes³oniæ nie musz¹ nic robiæ
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		skladnikiDodaj.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				wstawSkladniki();
			}

			// ... inne metody, które trzeba przes³oniæ nie musz¹ nic robiæ
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		opisDodaj.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String text = opisDodaj.getText().toString();
				text = text.replace("\n", "<br>\n");
				htmlView.setText(Html.fromHtml(text));
			}

			// ... inne metody, które trzeba przes³oniæ nie musz¹ nic robiæ
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		new UzupelnijMultiComplete().execute();

		Bundle bundle = getIntent().getExtras();
		user_id = bundle.getInt("user_id", 0);
		przepisID = bundle.getInt("przepisID", 0);
		if (bundle.getBoolean("edytuj", false)) {
			tytulDodaj.setText(bundle.getString("tytul"));
			staryTytul = bundle.getString("tytul");
			String text = bundle.getString("opis");
			text = text.replace("<br>", "\n");
			opisDodaj.setText(text);

			for (int i = 0; i < kategoriaDodaj.getCount(); i++) {
				if (kategoriaDodaj.getItemAtPosition(i).toString()
						.equals(bundle.getString("kategoria"))) {
					kategoriaDodaj.setSelection(i);
				}

			}
			for (int i = 0; i < trudnoscDodaj.getCount(); i++) {
				if (trudnoscDodaj.getItemAtPosition(i).toString()
						.equals(bundle.getString("trudnosc"))) {
					trudnoscDodaj.setSelection(i);
				}

			}
			for (int i = 0; i < czasDodaj.getCount(); i++) {
				if (czasDodaj.getItemAtPosition(i).toString()
						.equals(bundle.getString("czas"))) {
					czasDodaj.setSelection(i);
				}

			}
			skladnikiDodaj.setText(bundle.getString("skladniki"));
			stareZdjecie = bundle.getString("zdjecie", null);
			urlDodaj.setText(bundle.getString("zdjecie", null));

		}

	}

	@Override
	public void onClick(View v) {
		if (v == opisTytul) {
			komunikat = "To pole tekstowe obs³uguje prosty kod HTML";
			showDialog(ALERT_DIALOG_ID);
		} else if (v == zrobZdjecie) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else if (v == wybierzZdjecie) {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CODE_PICK_IMAGE:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(filePath, o);
				final int REQUIRED_SIZE = 220;
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				int scale = 1;
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				Bitmap yourSelectedImage2 = BitmapFactory.decodeFile(filePath,
						o2);
				zdjecie.setImageBitmap(yourSelectedImage2);
				zdjecieBitmap = yourSelectedImage2;
			}
			break;

		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				byte[] byteArray = stream.toByteArray();

				zdjecie.setImageBitmap(bmp);
				zdjecieBitmap = bmp;

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
			break;
		}
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
									if (success == 1 && przepisID != 0) {
										Intent i = new Intent(
												getApplicationContext(),
												PrzepisActivity.class);
										// sending pid to next activity
										i.putExtra("autorID", user_id);
										i.putExtra("przepisID", przepisID);
										i.putExtra("pseudonim", pseudonim);
										i.putExtra("user_id", user_id);
										finish();
										// starting new activity and expecting
										// some response back
										startActivity(i);
									} else {
										removeDialog(ALERT_DIALOG_ID);
									}
								}
							}).create();
			return alertDialog;
		case POTWIERDZ_DIALOG_ID:
			AlertDialog alertDialog2 = new AlertDialog.Builder(this)
					.setMessage("Na pewno chcesz wys³aæ ten przepis?")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									removeDialog(POTWIERDZ_DIALOG_ID);
									if (przepisID == 0)
										new DodajNowyPrzepis().execute();
									else {
										new EdytujPrzepis().execute();
									}
								}
							})
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									removeDialog(POTWIERDZ_DIALOG_ID);
								}
							}).create();
			return alertDialog2;
		}
		return null;
	}

	public String formatujTytul(String tytul) {
		String formatTytul = null;
		formatTytul = tytul.replace(" ", "_");
		formatTytul = formatTytul.replace("/", "_");
		formatTytul = formatTytul.replace("¹", "a");
		formatTytul = formatTytul.replace("œ", "s");
		formatTytul = formatTytul.replace("æ", "c");
		formatTytul = formatTytul.replace("ñ", "n");
		formatTytul = formatTytul.replace("ó", "o");
		formatTytul = formatTytul.replace("Ÿ", "z");
		formatTytul = formatTytul.replace("¿", "z");
		formatTytul = formatTytul.replace("³", "l");
		formatTytul = formatTytul.replace("ê", "e");
		return formatTytul;
	}

	public void wstawSkladniki() {
		ArrayList<HashMap<String, String>> Skladniki = new ArrayList<HashMap<String, String>>();
		LinearLayout layout = (LinearLayout) findViewById(R.id.SkladnikiList);
		skladniki = skladnikiDodaj.getText().toString().split(";");

		// adding each child node to HashMap key => value
		for (int j = 0; j < skladniki.length; j++) {
			if (skladniki[j].equals(" "))
				continue;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("skladniki", skladniki[j]);
			// adding HashList to ArrayList
			Skladniki.add(map);
		}
		ListAdapter adapter = new SimpleAdapter(DodajPrzepis.this, Skladniki,
				R.layout.skladniki, new String[] { "skladniki" },
				new int[] { R.id.skladnik });
		// updating listview
		layout.removeAllViewsInLayout();
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			layout.addView(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dodaj_przepis, menu);
		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add("Dodaj").setIcon(android.R.drawable.ic_menu_save)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
			finish();
		} else if (item.toString() == "Dodaj") {
			showDialog(POTWIERDZ_DIALOG_ID);
		}

		return super.onOptionsItemSelected(item);
	}

	class UzupelnijMultiComplete extends AsyncTask<String, String, String> {

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
				JSONObject json = jParser.makeHttpRequest(url_multi_complete,
						"POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					// products found
					// Getting Array of Products
					dane = json.getJSONArray("dane");
					String wszystkie_skladniki = "";
					// looping through All Products
					for (int i = 0; i < dane.length(); i++) {
						JSONObject c = dane.getJSONObject(i);

						// Storing each json item in variable
						String skladnik = c.getString("skladniki");
						wszystkie_skladniki += skladnik + ";";
					}
					pomocnicza = wszystkie_skladniki.split(";");
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
				String[] skladniki = pomocnicza;
				Set<String> uniqueWords = new HashSet<String>(
						Arrays.asList(skladniki));
				skladniki = uniqueWords.toArray(new String[0]);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						DodajPrzepis.this,
						android.R.layout.simple_dropdown_item_1line, skladniki);
				skladnikiDodaj.setTokenizer(new MyTokenizer());
				skladnikiDodaj.setAdapter(adapter);
				setProgressBarIndeterminateVisibility(false);
			}
		}
	}

	class DodajNowyPrzepis extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DodajPrzepis.this);
			pDialog.setMessage("Dodawanie przepisu...");
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
			params.add(new BasicNameValuePair("strona", "" + false));
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			params.add(new BasicNameValuePair("tytul", tytulDodaj.getText()
					.toString()));
			params.add(new BasicNameValuePair("kategoria", kategoriaDodaj
					.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("trudnosc", trudnoscDodaj
					.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("czas", czasDodaj
					.getSelectedItem().toString()));
			String skladnikiPomocnicza = skladnikiDodaj.getText().toString();
			while (skladnikiPomocnicza.contains("; "))
				skladnikiPomocnicza = skladnikiPomocnicza.replace("; ", ";");
			params.add(new BasicNameValuePair("skladniki", skladnikiPomocnicza));
			String text = opisDodaj.getText().toString();
			text = text.replace("\n", "<br>");
			params.add(new BasicNameValuePair("opis", text));
			if (zdjecieBitmap == null) {
				params.add(new BasicNameValuePair("zdjecie", urlDodaj.getText()
						.toString()));

			} else {
				wyslij = new WyslijZdjecie(zdjecieBitmap,
						formatujTytul(tytulDodaj.getText().toString()));
				params.add(new BasicNameValuePair(
						"zdjecie",
						"http://softpartner.pl/moje_przepisy/zdjecia/"
								+ formatujTytul(tytulDodaj.getText().toString())
								+ ".jpg"));
			}
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_dodaj_przepis,
						"POST", params);
				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				success = json.getInt("success");

				if (success == 1) {
					przepisID = json.getInt("przepisID");
					komunikat = "Dodano przepis";
					if (zdjecieBitmap != null)
						wyslij.executeMultipartPost();
				} else {
					komunikat = "Podany tytu³ jest ju¿ zajêty";
				}
			} catch (Exception e) {
				dialog = true;
				komunikat = "B³¹d w po³¹czeniu.";
				Log.d("DEBUG_TAG", "" + e);
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			showDialog(ALERT_DIALOG_ID);

		}
	}

	class EdytujPrzepis extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DodajPrzepis.this);
			pDialog.setMessage("Edytowanie przepisu...");
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
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			params.add(new BasicNameValuePair("przepisID", "" + przepisID));
			params.add(new BasicNameValuePair("tytul", tytulDodaj.getText()
					.toString()));
			params.add(new BasicNameValuePair("kategoria", kategoriaDodaj
					.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("trudnosc", trudnoscDodaj
					.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("czas", czasDodaj
					.getSelectedItem().toString()));
			String skladnikiPomocnicza = skladnikiDodaj.getText().toString();
			while (skladnikiPomocnicza.contains("; "))
				skladnikiPomocnicza = skladnikiPomocnicza.replace("; ", ";");
			params.add(new BasicNameValuePair("skladniki", skladnikiPomocnicza));
			String text = opisDodaj.getText().toString();
			text = text.replace("\n", "<br>");
			params.add(new BasicNameValuePair("opis", text));
			wyslij = new WyslijZdjecie();
			if ((zdjecieBitmap == null)) {
				if (stareZdjecie.equals(urlDodaj.getText().toString())) {
					Log.d("DEBUG_TAG", "dodano url");
					params.add(new BasicNameValuePair("zdjecie", urlDodaj
							.getText().toString()));
				} else if (!formatujTytul(staryTytul).equals(formatujTytul(tytulDodaj.getText()
						.toString()))) {
					Log.d("DEBUG_TAG", "zmieniono tytul");
					wyslij.zmienTytul(formatujTytul(staryTytul), formatujTytul(tytulDodaj.getText()
							.toString()));
				}
			} else {

				Log.d("DEBUG_TAG", "zrobiono nowe zdjecie");
				imageLoader.remove(stareZdjecie);
				wyslij.usunZdjecie(formatujTytul(staryTytul));
				wyslij = new WyslijZdjecie(zdjecieBitmap,
						formatujTytul(tytulDodaj.getText().toString()));
				params.add(new BasicNameValuePair(
						"zdjecie",
						"http://softpartner.pl/moje_przepisy/zdjecia/"
								+ formatujTytul(tytulDodaj.getText().toString())
								+ ".jpg"));
			}
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_edytuj_przepis,
						"POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				success = json.getInt("success");

				if (success == 1) {
					przepisID = json.getInt("przepisID");
					komunikat = "Edycja zakoñczona sukcesem";
					if (zdjecieBitmap != null)
						wyslij.executeMultipartPost();
				} else {
					komunikat = "Edycja zakoñczona niepowodzeniem :-(";
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
			showDialog(ALERT_DIALOG_ID);

		}
	}

}
