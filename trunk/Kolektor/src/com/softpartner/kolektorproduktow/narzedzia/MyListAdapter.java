package com.softpartner.kolektorproduktow.narzedzia;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softpartner.kolektorproduktow.Produkt;
import com.softpartner.kolektorproduktow.R;

public class MyListAdapter extends BaseAdapter {
	private Activity activity;
	private List<Produkt> dane;
	private static LayoutInflater inflater = null;

	// private ImageLoader imageLoader;

	public MyListAdapter(Activity a, List<Produkt> wszystkieProdukty) {
		activity = a;
		dane = wszystkieProdukty;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return dane.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.list_single_item, null);

		TextView produktID = (TextView) vi.findViewById(R.id.single_item_produktID);
		TextView nazwa = (TextView) vi.findViewById(R.id.single_item_nazwa);
		TextView kody = (TextView) vi.findViewById(R.id.single_item_kody);
		TextView waga = (TextView) vi.findViewById(R.id.single_item_waga);
		TextView ilosc = (TextView) vi.findViewById(R.id.single_item_ilosc);
		TextView cena = (TextView) vi.findViewById(R.id.single_item_cena);
		TextView nr = (TextView) vi.findViewById(R.id.single_item_nr);

		Produkt produkt;
		produkt = dane.get(position);
		// Setting all values in listview
		produktID.setText(produkt.ID);
		nr.setText((position + 1) + ".");
		nazwa.setText(produkt.nazwa);
		kody.setText(produkt.getKody());
		waga.setText(activity.getString(R.string.waga) + produkt.waga);
		ilosc.setText(activity.getString(R.string.ilosc) + produkt.ilosc);
		cena.setText(activity.getString(R.string.cena) + produkt.cena);
		vi.setTag(produkt);
		// kategoria.setTypeface(MyNormal);
		// float ocena = Float.parseFloat(map.get("ocena"));
		return vi;
	}
};
