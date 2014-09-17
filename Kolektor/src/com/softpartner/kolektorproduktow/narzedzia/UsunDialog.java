package com.softpartner.kolektorproduktow.narzedzia;

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
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.softpartner.kolektorproduktow.MyApp;
import com.softpartner.kolektorproduktow.Produkt;
import com.softpartner.kolektorproduktow.R;

public class UsunDialog extends DialogFragment {

	private Activity root;
	private Produkt produkt;
	private ProgressDialog pDialog;
	private int success;
	JSONParser jParser = new JSONParser();
	private MyApp app;
	private String url_usun_produkt;

	public UsunDialog(Activity root, Produkt produkt) {
		this.root = root;
		this.produkt = produkt;
		app = (MyApp) root.getApplicationContext();
		url_usun_produkt = root.getString(R.string.url_usun_produkt);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		return new AlertDialog.Builder(getActivity())
				.setTitle(root.getString(R.string.komunikat))
				.setMessage(
						root.getString(R.string.czy_usunac) + produkt.nazwa
								+ "?")
				.setPositiveButton(root.getString(R.string.tak),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								try {
									new UsunProdukt().execute();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						})
				.setNegativeButton(root.getString(R.string.nie),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();
	}

	class UsunProdukt extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(root);
			pDialog.setMessage(root.getString(R.string.usuwanie_produktu));
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
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_usun_produkt,
						"POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				success = json.getInt("success");
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
						root.getString(R.string.usunieto) + produkt.nazwa,
						Toast.LENGTH_SHORT).show();
				root.finish();
			} else {
				Toast.makeText(root,
						root.getString(R.string.nie_usunieto) + produkt.nazwa,
						Toast.LENGTH_SHORT).show();
			}
			app.skaner.szukaj();
			pDialog.dismiss();

		}
	}
}
