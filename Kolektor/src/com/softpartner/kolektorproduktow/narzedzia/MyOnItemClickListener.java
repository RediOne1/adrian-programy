package com.softpartner.kolektorproduktow.narzedzia;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.softpartner.kolektorproduktow.MyApp;
import com.softpartner.kolektorproduktow.Produkt;
import com.softpartner.kolektorproduktow.WyswietlProdukt;

public class MyOnItemClickListener implements OnItemClickListener {

	private MyApp app;
	private FragmentActivity root;

	public MyOnItemClickListener(FragmentActivity _root) {
		this.root = _root;
		app = (MyApp) root.getApplicationContext();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Produkt produkt = (Produkt) view.getTag();
		// Starting new intent
		if (app.schowek != null) {
			FragmentManager fm = root.getSupportFragmentManager();
			MyDialogFragment mDialog = new MyDialogFragment(root, produkt,app.schowek);
			mDialog.show(fm, "fragment_edit_name");
		} else {
			Intent i = new Intent(root, WyswietlProdukt.class);
			i.putExtra("produkt", produkt);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			root.startActivity(i);
		}
	}
}
