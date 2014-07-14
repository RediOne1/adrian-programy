package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.dodaj_przepis.DodajPrzepisActivity;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;

public class WyswietlPrzepis extends Activity implements OnClickListener {

	public final int OCEN_DIALOG_ID = 1;
	private Przepis przepis;
	public WyswietlTytulowyModul wyswietlTytulowyModul;
	private WyswietlSkladniki wyswietlSkladniki;
	private WyswietlOpis wyswietlOpis;
	private OcenPrzepis ocenPrzepis;
	private RatingBar rating2;
	private MyApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wyswietl_przepis);
		app = (MyApp) getApplicationContext();
		przepis = (Przepis) getIntent().getSerializableExtra("przepis");
		wyswietlTytulowyModul = new WyswietlTytulowyModul(WyswietlPrzepis.this,
				przepis);
		wyswietlSkladniki = new WyswietlSkladniki(WyswietlPrzepis.this, przepis);
		wyswietlOpis = new WyswietlOpis(WyswietlPrzepis.this, przepis);
		ocenPrzepis = new OcenPrzepis(WyswietlPrzepis.this, przepis,
				WyswietlPrzepis.this);
		// showDialog(OCEN_DIALOG_ID);
	}

	public void edytuj() {
		przepis.tytul = wyswietlTytulowyModul.getTytul();
		przepis.zdjecie = wyswietlTytulowyModul.getImage();
		przepis.kategoria = wyswietlTytulowyModul.getKategoria();
		przepis.trudnosc = wyswietlTytulowyModul.getTrudnosc();
		przepis.czas = wyswietlTytulowyModul.getCzas();
		przepis.skladniki = wyswietlSkladniki.getSkladniki();
		przepis.opis = wyswietlOpis.getOpis();
		Intent i = new Intent(this, DodajPrzepisActivity.class);
		i.putExtra("przepis", przepis);
		i.putExtra("edytuj", true);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wyswietl_przepis, menu);
		if (przepis.autorID == app.getData()) {
			menu.add("Edytuj").setIcon(android.R.drawable.ic_menu_edit)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			menu.add("Usun").setIcon(android.R.drawable.ic_menu_delete)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString() == "Edytuj") {
			edytuj();
		}
		return super.onOptionsItemSelected(item);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case OCEN_DIALOG_ID:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.ocen,
					(ViewGroup) findViewById(R.id.ocen_root));
			rating2 = (RatingBar) layout.findViewById(R.id.ratingBar1);
			rating2.setRating(ocenPrzepis.ocen_ratingbar.getRating());

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			// KOnfiguracja okna AlertDialog
			builder.setTitle(R.string.ocen_przepis);
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Wymuszamy zamkni�cie i usuni�cie okna, tak by nie
							// mo�na
							// by�o ponownie z niego skorzysta�.
							removeDialog(OCEN_DIALOG_ID);
						}
					});
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							removeDialog(OCEN_DIALOG_ID);
							float ocena = rating2.getRating();
							ocenPrzepis.ocen(ocena);
						}
					});
			// Twrozymy obiekt AlertDialog i zwracamy go.
			AlertDialog passwordDialog = builder.create();
			return passwordDialog;
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ocen_relative_layout) {
			showDialog(1);
		}
	}
}
