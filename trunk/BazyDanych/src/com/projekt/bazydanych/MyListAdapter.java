package com.projekt.bazydanych;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {
	private Activity activity;
	private Context mContext;
	private ArrayList<HashMap<String, String>> dane;
	private static LayoutInflater inflater = null;
	private Typeface MyBold;
	private Typeface MyNormal;

	public MyListAdapter(Activity a,
			ArrayList<HashMap<String, String>> wszystkiePrzepisy) {
		activity = a;
		dane = wszystkiePrzepisy;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return dane.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.produkt_list, null);

		TextView Tnazwa = (TextView) vi.findViewById(R.id.lista_produkt_nazwa);
		TextView Tmodel = (TextView) vi.findViewById(R.id.lista_produkt_model);
		TextView Tcena = (TextView) vi.findViewById(R.id.lista_produkt_cena);
		TextView Tprodukt_id = (TextView) vi.findViewById(R.id.lista_produkt_id);

		HashMap<String, String> map = new HashMap<String, String>();
		map = dane.get(position);
		// Setting all values in listview
		Tnazwa.setText(map.get("nazwa"));
		Tmodel.setText(map.get("model"));
		Tcena.setText(map.get("cena")+"z³");
		Tprodukt_id.setText(map.get("produktId"));
		return vi;
	}

};
