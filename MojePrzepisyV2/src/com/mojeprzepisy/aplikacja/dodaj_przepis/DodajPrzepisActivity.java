package com.mojeprzepisy.aplikacja.dodaj_przepis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.widget.ImageView;

import com.mojeprzepisy.aplikacja.R;

public class DodajPrzepisActivity extends Activity {

	public DodajZdjecie dodajZdjecie;
	public DodajTytul dodajTytul;
	public DodajSkladniki dodajSkladniki;
	public ImageView imageview;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int REQ_CODE_PICK_IMAGE = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dodaj_przepis_layout);
		imageview = (ImageView) findViewById(R.id.dodaj_tytul_save);
		dodajZdjecie = new DodajZdjecie(DodajPrzepisActivity.this);
		dodajTytul = new DodajTytul(DodajPrzepisActivity.this);
		dodajSkladniki = new DodajSkladniki(DodajPrzepisActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dodaj_przepis, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dodajZdjecie.dodajPrzepisActivityResult(requestCode, resultCode, data);
	}
}
