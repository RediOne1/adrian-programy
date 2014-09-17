package com.softpartner.kolektorproduktow.narzedzia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.softpartner.kolektorproduktow.MyApp;
import com.softpartner.kolektorproduktow.Produkt;
import com.softpartner.kolektorproduktow.R;
import com.softpartner.kolektorproduktow.WyswietlProdukt;

public class MyDialogFragment extends DialogFragment {

	private Activity root;
	private Produkt produkt;
	private String kod;
	private MyApp app;
	private ProgressDialog pDialog;
	private int success;
	JSONParser jParser = new JSONParser();
	private String url_dodaj_kod;

	public MyDialogFragment(Activity root, Produkt produkt, String kod) {
		this.root = root;
		this.produkt = produkt;
		this.kod = kod;
		url_dodaj_kod = root.getString(R.string.url_dodaj_kod);
		app = (MyApp) root.getApplicationContext();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		return new AlertDialog.Builder(getActivity())
				.setTitle(root.getString(R.string.komunikat))
				.setMessage(
						root.getString(R.string.czy_dodac1) + kod
								+ root.getString(R.string.czy_dodac2))
				.setPositiveButton(root.getString(R.string.tak),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								app.schowek = null;
								try {
									new DodajKod().execute().get();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Intent i = new Intent(root, WyswietlProdukt.class);
								i.putExtra("produkt", produkt);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								root.startActivity(i);
							}
						})
				.setNegativeButton(root.getString(R.string.nie),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								app.schowek = null;
								dialog.dismiss();
								Intent i = new Intent(root, WyswietlProdukt.class);
								i.putExtra("produkt", produkt);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								root.startActivity(i);
							}
						}).create();
	}

	class DodajKod extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(root);
			pDialog.setMessage(root.getString(R.string.dodawanie_kodu));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		@Override
		protected String doInBackground(String... args) {
			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("produkt_id", produkt.ID));
			params.add(new BasicNameValuePair("kod", kod));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_dodaj_kod,
						"POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				success = json.getInt("success");

				if (success == 1) {
					produkt.dodajKod(kod);
				} else {
				}
			} catch (Exception e) {

			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			if (success == 1) {
				Toast.makeText(root,
						root.getString(R.string.dodano_kod) + " " + kod,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(root,
						root.getString(R.string.nie_dodano_kodu) + " " + kod,
						Toast.LENGTH_LONG).show();
			}
			pDialog.dismiss();

		}
	}
}
