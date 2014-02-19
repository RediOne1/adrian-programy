package com.mojeprzepisy.aplikacja.dodaj_przepis;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.mojeprzepisy.aplikacja.R;

public class DodajPrzepisActivity extends Activity {
	
	public DodajZdjecie dodajZdjecie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dodaj_przepis_layout);
		//ImageView imageview = (ImageView) findViewById(R.id.imageView1);
		dodajZdjecie = new DodajZdjecie();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dodaj_przepis, menu);
		return true;
	}

}
