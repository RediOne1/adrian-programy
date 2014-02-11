package com.mojeprzepisy.aplikacja.przepis;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.R.layout;
import com.mojeprzepisy.aplikacja.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Przepis extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.przepis);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.przepis, menu);
		return true;
	}

}
