package com.softpartner.kolektorproduktow.narzedzia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.softpartner.kolektorproduktow.MyApp;
import com.softpartner.kolektorproduktow.Produkt;
import com.softpartner.kolektorproduktow.R;
import com.softpartner.kolektorproduktow.WyswietlProdukt;

public class MyOnItemClickListener implements OnItemClickListener {

	private static final int ALERT_DIALOG_ID = 123;
	private MyApp app;
	private Activity root;

	public MyOnItemClickListener(Activity root) {
		this.root = root;
		app = (MyApp) root.getApplicationContext();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Produkt produkt = (Produkt) view.getTag();
		// Starting new intent
		Intent i = new Intent(root, WyswietlProdukt.class);
		i.putExtra("produkt", produkt);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (app.schowek != null) {
			root.showDialog(ALERT_DIALOG_ID);
		} else {
			root.startActivity(i);
		}
	}
}
