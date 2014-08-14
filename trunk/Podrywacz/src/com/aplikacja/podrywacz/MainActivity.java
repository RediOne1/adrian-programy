package com.aplikacja.podrywacz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {

	private Ankieta ankieta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ankieta_layout);
		ankieta = new Ankieta(this);
		//finish();
	}
}
