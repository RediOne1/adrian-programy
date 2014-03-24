package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.R;

public class WybierzKategorieListAdapter extends BaseAdapter {
	private Activity activity;
	private List<Kategoria> dane;
	private static LayoutInflater inflater = null;

	public WybierzKategorieListAdapter(Activity a,
			List<Kategoria> wszystkieKategorie) {
		activity = a;
		dane = wszystkieKategorie;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			vi = inflater.inflate(R.layout.kategoria, null);

		TextView nazwa = (TextView) vi.findViewById(R.id.nazwa_kategoria_tv);

		Kategoria kategoria = dane.get(position);
		nazwa.setText(kategoria.toString());
		vi.setTag(kategoria);
		return vi;
	}
};
