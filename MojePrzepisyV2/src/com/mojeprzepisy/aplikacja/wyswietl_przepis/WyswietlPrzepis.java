package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;

public class WyswietlPrzepis extends Activity {
	
	private Przepis przepis;
	private WyswietlTytulowyModul wyswietlTytulowyModul;
	private WyswietlSkladniki wyswietlSkladniki;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wyswietl_przepis);
		przepis = (Przepis) getIntent().getSerializableExtra("przepis");
		wyswietlTytulowyModul = new WyswietlTytulowyModul(WyswietlPrzepis.this, przepis);
		wyswietlSkladniki = new WyswietlSkladniki(WyswietlPrzepis.this, przepis);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wyswietl_przepis, menu);
		return true;
	}

}
