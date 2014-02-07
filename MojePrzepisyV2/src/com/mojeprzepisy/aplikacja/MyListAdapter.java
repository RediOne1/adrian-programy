package com.mojeprzepisy.aplikacja;

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
	public ImageLoader imageLoader;
	private Typeface MyBold;
	private Typeface MyNormal;

	public MyListAdapter(Activity a,
			ArrayList<HashMap<String, String>> wszystkiePrzepisy) {
		activity = a;
		dane = wszystkiePrzepisy;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		MyBold = Typeface.createFromAsset(activity.getAssets(), "fonts/SEGOEPRB.TTF");
		MyNormal = Typeface.createFromAsset(activity.getAssets(), "fonts/SEGOEPR.TTF");
		imageLoader = new ImageLoader(activity.getApplicationContext());
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
			vi = inflater.inflate(R.layout.przepis_maly_layout, null);

		TextView przepisID = (TextView) vi.findViewById(R.id.przepisID);
		TextView autorID = (TextView) vi.findViewById(R.id.autorID);
		TextView Tytul = (TextView) vi.findViewById(R.id.przepis_tytul_maly_layout);
		TextView kategoria = (TextView) vi.findViewById(R.id.kategoria_maly_layout);
		ImageView image_view = (ImageView) vi.findViewById(R.id.zdjecie_maly_layout);
		RatingBar rating = (RatingBar) vi.findViewById(R.id.ratingbar_maly_layout);

		HashMap<String, String> map = new HashMap<String, String>();
		map = dane.get(position);
		// Setting all values in listview
		autorID.setText(map.get("autorID"));
		przepisID.setText(map.get("przepisID"));
		Tytul.setText(map.get("tytul"));
		Tytul.setTypeface(MyBold);
		kategoria.setText(map.get("kategoria"));
		kategoria.setTypeface(MyNormal);
		float ocena = Float.parseFloat(map.get("ocena"));
		rating.setRating(ocena);
		imageLoader.DisplayImage(map.get("zdjecie"), image_view);
		return vi;
	}

};
