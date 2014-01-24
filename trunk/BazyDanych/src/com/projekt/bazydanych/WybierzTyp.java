package com.projekt.bazydanych;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class WybierzTyp extends Activity implements OnClickListener{
	
	private Spinner typ;
	private Button dalej;
	private int user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		user_id = bundle.getInt("id");
		setContentView(R.layout.activity_wybierz_typ);
		typ = (Spinner) findViewById(R.id.spinner1);
		dalej = (Button) findViewById(R.id.next_button);
		dalej.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wybierz_typ, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v ==dalej){
			Intent i = new Intent(
					getApplicationContext(),
					PokazTyp.class);
			// sending pid to next activity
			i.putExtra("user_id", ""+user_id);
			i.putExtra("typ", typ.getSelectedItem().toString());
			startActivity(i);
		}
	}
	
	

}
