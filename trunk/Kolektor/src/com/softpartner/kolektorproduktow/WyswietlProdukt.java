package com.softpartner.kolektorproduktow;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softpartner.kolektorproduktow.narzedzia.ImageLoader;
import com.softpartner.kolektorproduktow.opisyproduktow.OpisyProduktow;
import com.softpartner.kolektorproduktow.opisyproduktow.ProductOP;

public class WyswietlProdukt extends FragmentActivity {
	private ProductOP produkt;
	private TextView nazwa_tv;
	private LinearLayout linearLayout;
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wyswietl_produkt);
		Bundle bundle = getIntent().getExtras();
		String barcode = bundle.getString("barcode", null);
		String pid = bundle.getString("pid", null);
		OpisyProduktow op = new OpisyProduktow(this);
		try {
			if (barcode != null)
				produkt = op.getProduct("ean", barcode).get(0);
			else
				produkt = op.getProduct("pid", pid).get(0);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iv = (ImageView) findViewById(R.id.wyswietl_produkt_zdjecie);
		nazwa_tv = (TextView) findViewById(R.id.wyswietl_nazwa);
		linearLayout = (LinearLayout) findViewById(R.id.wyswietl_przepis_rootLinear);

		List<View> views = produkt.getElementsViewList();
		for (View v : views) {
			linearLayout.addView(v);
		}
		new ImageLoader(this).DisplayImage(produkt.zdjecie, iv);

		nazwa_tv.setText(produkt.dane.get("product_name"));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wyswietl_produkt, menu);
		menu.add("Usun").setIcon(android.R.drawable.ic_menu_delete)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString().equals("Usun")) {
			FragmentManager fm = getSupportFragmentManager();
			// UsunDialog dialog = new UsunDialog(this, produkt);
			// dialog.show(fm, "usun");
		}
		return super.onOptionsItemSelected(item);
	}
}
