package com.projekt.bazydanych;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button rejestracja;
	private Button zaloguj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rejestracja = (Button) findViewById(R.id.rejestracja_button);
		zaloguj = (Button) findViewById(R.id.login_button);

		rejestracja.setOnClickListener(this);
		zaloguj.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == rejestracja) {
			Intent i = new Intent(getApplicationContext(), Rejestracja.class);
			startActivity(i);
		} else if (v == zaloguj) {
			Intent i = new Intent(getApplicationContext(), Login.class);
			startActivity(i);
		}
	}
}
