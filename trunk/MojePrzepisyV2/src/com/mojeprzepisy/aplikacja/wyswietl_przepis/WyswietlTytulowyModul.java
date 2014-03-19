package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.ImageLoader;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class WyswietlTytulowyModul extends WyswietlPrzepis {

	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;

	private final String URL = "http://softpartner.pl/moje_przepisy2/wyswietl_tytulowy_modul.php";
	private Przepis przepis;
	private ImageView zdjecie;
	private TextView tytul;
	private Activity root;
	private ProgressBar progress;
	private TextView kategoria;
	private RatingBar ocena;
	private TextView ilosc_ocen;
	private TextView trudnosc;
	private TextView czas;

	public WyswietlTytulowyModul(Activity _root, Przepis _przepis) {
		this.przepis = _przepis;
		this.root = _root;
		zdjecie = (ImageView) root.findViewById(R.id.zdjecie_maly_layout);
		tytul = (TextView) root.findViewById(R.id.przepis_tytul_maly_layout);
		kategoria = (TextView) root.findViewById(R.id.kategoria_maly_layout);
		ocena = (RatingBar) root.findViewById(R.id.ratingbar_maly_layout);
		ilosc_ocen = (TextView) root.findViewById(R.id.ilosc_ocen_maly_layout);
		trudnosc = (TextView) root.findViewById(R.id.trudnosc_maly_layout);
		czas = (TextView) root.findViewById(R.id.czas_maly_layout);
		progress = (ProgressBar) root
				.findViewById(R.id.przepis_tytulowy_modul_progressbar);
		tytul.setText(przepis.tytul);
		kategoria.setText(przepis.kategoria);
		ocena.setRating(przepis.ocena);
		ilosc_ocen.setText("" + przepis.ilosc_ocen);
		trudnosc.setText(przepis.trudnosc);
		czas.setText(przepis.czas);
		new ImageLoader(root).DisplayImage(przepis.zdjecie, zdjecie);
		tytul = (TextView) new MyTypeFace(tytul, root).MyBold();
		kategoria = (TextView) new MyTypeFace(kategoria, root).MyNormal();
		trudnosc = (TextView) new MyTypeFace(trudnosc, root).MyNormal();
		czas = (TextView) new MyTypeFace(czas, root).MyNormal();
	}

	public void odswiez() {
		new OdswiezDane().execute();
	}

	class OdswiezDane extends AsyncTask<Float, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Float... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", ""
					+ przepis.przepisID));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					dane = json.getJSONArray("dane");

					// looping through All Products
					for (int i = 0; i < dane.length(); i++) {
						JSONObject c = dane.getJSONObject(i);
						String ocena = c.getString("ocena");
						String ilosc_ocen = c.getString("ilosc_ocen");
						String trudnosc = c.getString("trudnosc");
						String czas = c.getString("czas");
						String kategoria = c.getString("kategoria");
						String zdjecie = c.getString("zdjecie");
						String tytul = c.getString("tytul");
						publishProgress(ocena, ilosc_ocen, trudnosc, czas,
								kategoria, zdjecie, tytul);
					}
				} else {
				}
			} catch (Exception e) {
				Log.d("DEBUG_TAG", "Wyswietl tytulowy modul: " + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			progress.setVisibility(View.GONE);
		}

		@Override
		protected void onProgressUpdate(String... args) {
			try {
				ocena.setRating(Float.parseFloat(args[0]));
				ilosc_ocen.setText("" + Integer.parseInt(args[1]));
				trudnosc.setText(args[2]);
				czas.setText(args[3]);
				kategoria.setText(args[4]);
				new ImageLoader(root).DisplayImage(args[5], zdjecie);
				tytul.setText(args[6]);
			} catch (Exception e) {

			}

		}
	}
}
