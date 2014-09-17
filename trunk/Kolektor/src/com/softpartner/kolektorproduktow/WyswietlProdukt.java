package com.softpartner.kolektorproduktow;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.TextView;

public class WyswietlProdukt extends ActionBarActivity {
	private Produkt produkt;
	private TextView nazwa_tv, kod_tv, cena_tv, waga_tv, ilosc_tv, opis_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wyswietl_produkt);
		produkt = (Produkt) getIntent().getSerializableExtra("produkt");

		nazwa_tv = (TextView) findViewById(R.id.wyswietl_nazwa);
		kod_tv = (TextView) findViewById(R.id.wyswietl_kody);
		cena_tv = (TextView) findViewById(R.id.wyswietl_cena);
		waga_tv = (TextView) findViewById(R.id.wyswietl_waga);
		ilosc_tv = (TextView) findViewById(R.id.wyswietl_ilosc);
		opis_tv = (TextView) findViewById(R.id.wyswietl_opis);
	
		nazwa_tv.setText(produkt.nazwa);
		kod_tv.setText(produkt.getKody());
		cena_tv.setText(produkt.cena);
		waga_tv.setText(produkt.waga);
		ilosc_tv.setText(produkt.ilosc);
		opis_tv.setText(produkt.opis);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wyswietl_produkt, menu);
		return true;
	}
}
