package com.softpartner.kolektorproduktow.narzedzia;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.softpartner.kolektorproduktow.Produkt;
import com.softpartner.kolektorproduktow.WyswietlProdukt;

public class MyOnItemClickListener implements OnItemClickListener {
	Context context;

	public MyOnItemClickListener(Context _context) {
		this.context = _context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Produkt produkt = (Produkt) view.getTag();
		// Starting new intent
		Intent i = new Intent(context, WyswietlProdukt.class);
		i.putExtra("produkt", produkt);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
