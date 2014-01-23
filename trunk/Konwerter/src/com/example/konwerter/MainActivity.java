package com.example.konwerter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener,
		TextWatcher {

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "System zabi³ aplikacje", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Toast.makeText(this, "Aplikacja wstrzymana", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Toast.makeText(this, "Aplikacja uruchomiona ponownie",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "Aplikacja wznowiona", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Toast.makeText(this, "Zapis stanu aplikacji", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "Start aplikacji", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Toast.makeText(this, "Aplikacja zatrzymana", Toast.LENGTH_SHORT).show();
	}

	private ProgressDialog pDialog;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private float kurs;
	private TextView tv_kurs;
	private Spinner waluta;
	private String currency;
	private String value;
	private EditText kwota;
	private TextView tv_wynik;
	public static final String NOTIFY_KEY_1 = "NOTIFY_KEY_1";
	private static final int NOTIFY_1 = 0x1001;
	private static final int NOTIFY_2 = 0x1002;
	private static final int NOTIFY_3 = 0x1003;
	private static final int NOTIFY_4 = 0x1004;
	private static final int NOTIFY_5 = 0x1005;
	private NotificationManager notifier = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_kurs = (TextView) findViewById(R.id.kurs);
		waluta = (Spinner) findViewById(R.id.waluta);
		waluta.setOnItemSelectedListener(this);
		kwota = (EditText) findViewById(R.id.kwota);
		kwota.addTextChangedListener(this);
		tv_wynik = (TextView) findViewById(R.id.wynik);

		notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// utworzone poza przyciskiem, tak by moÅ¼na inkrementowaÄ‡ liczbÄ™
		// rysowanÄ… na ikonie
		final Notification notify = new Notification(R.drawable.ic_launcher,
				"Witamy!", System.currentTimeMillis());
		notify.icon = R.drawable.ic_launcher;
		notify.tickerText = "Witam!";
		notify.when = System.currentTimeMillis();
		Button notify1 = (Button) findViewById(R.id.notify1);
		notify1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				notify.number++;
				notify.flags |= Notification.FLAG_AUTO_CANCEL;
				Intent toLaunch = new Intent(MainActivity.this,
						MainActivity.class);
				PendingIntent intentBack = PendingIntent.getActivity(
						MainActivity.this, 0, toLaunch, 0);
				notify.setLatestEventInfo(MainActivity.this, "Czeœæ!",
						"To jest kolejny tekst.", intentBack);
				notifier.notify(NOTIFY_1, notify);
			}
		});
		Button notify2 = (Button) findViewById(R.id.notify2);
		notify2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Notification notify = new Notification(
						android.R.drawable.stat_notify_chat, "Wibrujemy!",
						System.currentTimeMillis());
				notify.flags |= Notification.FLAG_AUTO_CANCEL;
				notify.vibrate = new long[] {0, 300, 200, 300, 200, 120, 100,
						120, 100, 120, 200, 150, 150, 150, 150, 150, 150, 150,
						150, 500, 200, 600, };
				Intent toLaunch = new Intent(MainActivity.this,
						MainActivity.class);
				PendingIntent intentBack = PendingIntent.getActivity(
						MainActivity.this, 0, toLaunch, 0);
				notify.setLatestEventInfo(MainActivity.this, "Bzzyt!",
						"To wibruje Twój telefon.", intentBack);
				notifier.notify(NOTIFY_2, notify);
				// jest wiêcej sposobów na wibrowanie
				// Vibrator vibe = (Vibrator)
				// getSystemService(Context.VIBRATOR_SERVICE);
				// vibe.vibrate(500);
			}
		});
		Button notify3 = (Button) findViewById(R.id.notify3);
		notify3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				notify.flags |= Notification.FLAG_AUTO_CANCEL;
				notify.number++;
				notify.flags |= Notification.FLAG_SHOW_LIGHTS;
				if (notify.number < 2) {
					notify.ledARGB = Color.GREEN;
					notify.ledOnMS = 1000;
					notify.ledOffMS = 1000;
				} else if (notify.number < 3) {
					notify.ledARGB = Color.BLUE;
					notify.ledOnMS = 750;
					notify.ledOffMS = 750;
				} else if (notify.number < 4) {
					notify.ledARGB = Color.WHITE;
					notify.ledOnMS = 500;
					notify.ledOffMS = 500;
				} else {
					notify.ledARGB = Color.RED;
					notify.ledOnMS = 50;
					notify.ledOffMS = 50;
				}
				Intent toLaunch = new Intent(MainActivity.this,
						MainActivity.class);
				PendingIntent intentBack = PendingIntent.getActivity(
						MainActivity.this, 0, toLaunch, 0);
				notify.setLatestEventInfo(MainActivity.this, "Razi!",
						"To œwieci Twój telefon.", intentBack);
				notifier.notify(NOTIFY_3, notify);
			}
		});
		Button notifyRemote = (Button) findViewById(R.id.notifyRemote);
		notifyRemote.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				notify.flags |= Notification.FLAG_AUTO_CANCEL;
				RemoteViews remote = new RemoteViews(getPackageName(),
						R.layout.remote);
				remote.setTextViewText(R.id.text1, "To du¿y tekst!");
				remote.setTextViewText(R.id.text2, "Czerwony tekst na dole!");
				notify.contentView = remote;
				Intent toLaunch = new Intent(MainActivity.this,
						MainActivity.class);
				PendingIntent intentBack = PendingIntent.getActivity(
						MainActivity.this, 0, toLaunch, 0);
				notify.contentIntent = intentBack;
				notifier.notify(NOTIFY_5, notify);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class Wymien extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Pobieranie kursu");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {

			currency = waluta.getSelectedItem().toString();
			value = "1";
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("currency", currency));
			params.add(new BasicNameValuePair("value", value));
			// getting JSON string from URL
			try {
				String json = jParser.makeHttpRequest(
						"http://blockchain.info/tobtc", "GET", params);
				kurs = Float.parseFloat(json);

			} catch (Exception e) {
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			tv_kurs.setText("Kurs: " + kurs);
			zamien();
			pDialog.dismiss();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		new Wymien().execute();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		zamien();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	public void zamien() {
		try {
			float fkwota = Float.parseFloat(kwota.getText().toString());
			tv_wynik.setText("" + (fkwota * kurs));
		} catch (Exception e) {

		}
	}

}
