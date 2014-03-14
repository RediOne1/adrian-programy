package com.projekt.bazydanych;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Zamowienie extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	private final int ALERT_DIALOG_ID = 1;
	JSONParser jParser = new JSONParser();
	private ProgressDialog pDialog;
	private String komunikat;
	private String user_id;
	private String produktId;
	private String nazwa;
	private String model;
	private String Stilosc;
	private String producent;
	private int seek_progress;
	private int ilosc;
	private String Scena;
	private float cena;
	private String typ;
	private SeekBar Silosc;
	private TextView Tilosc;
	private EditText Eadres;
	private TextView Tkoszt;
	private Button Bzamow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zamowienie);
		Bundle bundle = getIntent().getExtras();
		user_id = bundle.getString("user_id");
		produktId = bundle.getString("produktId");
		nazwa = bundle.getString("nazwa");
		Stilosc = bundle.getString("ilosc");
		Scena = bundle.getString("cena");
		typ = bundle.getString("typ");
		producent = bundle.getString("producent");
		try {
			ilosc = Integer.parseInt(Stilosc);
			cena = Float.parseFloat(Scena);
		} catch (Exception e) {

		}
		Silosc = (SeekBar) findViewById(R.id.ilosc_seek);
		Tilosc = (TextView) findViewById(R.id.zamowienie_ilosc);
		Eadres = (EditText) findViewById(R.id.zamowienie_adres);
		Bzamow = (Button) findViewById(R.id.wyslij_zamowienie);
		Tkoszt = (TextView) findViewById(R.id.koszt);
		Silosc.setMax(ilosc);
		Silosc.setOnSeekBarChangeListener(this);
		Bzamow.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.zamowienie, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == Silosc) {
			seek_progress = progress;
			Tkoszt.setText("Koszt: " + (float)(progress * cena));
			Tilosc.setText("" + progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if(v == Bzamow){
			new WyslijZamowienie().execute();
		}

	}

	class WyslijZamowienie extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Zamowienie.this);
			pDialog.setMessage("Wysy³anie zamówienia");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idUzytkownika", user_id));
			params.add(new BasicNameValuePair("idProduktu", produktId));
			params.add(new BasicNameValuePair("adres", Eadres.getText().toString()));
			params.add(new BasicNameValuePair("typ", typ));
			params.add(new BasicNameValuePair("producent", producent));
			params.add(new BasicNameValuePair("model", model));
			params.add(new BasicNameValuePair("ilosc", ""+seek_progress));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(
						"http://softpartner.pl/projekt/zamowienie.php",
						"POST", params);
				komunikat = json.getString("message");
			} catch (Exception e) {
				komunikat = "" + e;
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

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case ALERT_DIALOG_ID:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setTitle("Komunikat")
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
		}
		return null;
	}

}
