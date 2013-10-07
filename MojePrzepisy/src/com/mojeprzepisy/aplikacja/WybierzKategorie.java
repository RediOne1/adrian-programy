package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WybierzKategorie extends Activity implements OnClickListener {

	private String pseudonim;
	private int user_id;
	private String kategorie[];
	private ArrayList<HashMap<String, String>> Kategorie;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wybierz_kategorie);
		Bundle bundle = getIntent().getExtras();
		pseudonim = bundle.getString("pseudonim", "");
		user_id = bundle.getInt("user_id", 0);
		Typeface MyBold = Typeface.createFromAsset(
				getBaseContext().getAssets(), "fonts/SEGOEPRB.TTF");
		TextView tv = (TextView) findViewById(R.id.lista_kategoria);
		tv.setTypeface(MyBold);
		Kategorie = new ArrayList<HashMap<String, String>>();
		kategorie = getResources().getStringArray(R.array.kategorie);
		layout = (LinearLayout) findViewById(R.id.kategorie);
		wyswietlKategorie();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wybierz_kategorie, menu);

		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
		}

		return super.onOptionsItemSelected(item);
	}

	public void wyswietlKategorie() {
		for (int j = 0; j < kategorie.length; j++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("kategoria", kategorie[j]);
			// adding HashList to ArrayList
			Kategorie.add(map);
		}
		ListAdapter adapter = new SimpleAdapter(WybierzKategorie.this,
				Kategorie, R.layout.kategoria, new String[] { "kategoria" },
				new int[] { R.id.lista_kategoria });

		// updating listview
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			item.setOnClickListener(this);
			item.setId(i);
			layout.addView(item);
		}
	}

	@Override
	public void onClick(View v) {
		int info = v.getId();
		Intent i = new Intent(getApplicationContext(), Lista_przepisy.class);
		i.putExtra("kategoria", kategorie[info]);
		i.putExtra("pseudonim", pseudonim);
		i.putExtra("user_id", user_id);
		startActivity(i);

	}

}
