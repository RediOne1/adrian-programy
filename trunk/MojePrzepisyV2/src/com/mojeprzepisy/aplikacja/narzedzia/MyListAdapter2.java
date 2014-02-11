package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.ArrayList;
import java.util.HashMap;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.R.id;
import com.mojeprzepisy.aplikacja.R.layout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter2 extends BaseAdapter {
	private Activity activity;
	private Context mContext;
	private ArrayList<HashMap<String, String>> dane;
	private static LayoutInflater inflater = null;
	private Typeface MyBold;
	private Typeface MyNormal;
	private ImageLoader imageLoader;

	public MyListAdapter2(Activity a,
			ArrayList<HashMap<String, String>> wszystkiePrzepisy) {
		activity = a;
		dane = wszystkiePrzepisy;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			vi = inflater.inflate(R.layout.list_single_item, null);

		//TextView przepisID = (TextView) vi.findViewById(R.id.przepisID);
		//TextView autorID = (TextView) vi.findViewById(R.id.autorID);
		TextView Tytul = (TextView) vi.findViewById(R.id.single_item_tytul);
		TextView kategoria = (TextView) vi.findViewById(R.id.single_item_kategoria);
		TextView nr = (TextView) vi.findViewById(R.id.nr_najwyzej_oceniane);
		ImageView image_view = (ImageView) vi.findViewById(R.id.single_item_image);
		//RatingBar rating = (RatingBar) vi.findViewById(R.id.single_item_tytul);

		HashMap<String, String> map = new HashMap<String, String>();
		map = dane.get(position);
		// Setting all values in listview
		//autorID.setText(map.get("autorID"));
		//przepisID.setText(map.get("przepisID"));
		nr.setText((position+1)+".");
		Tytul.setText(map.get("tytul"));
		//Tytul.setTypeface(MyBold);
		kategoria.setText(map.get("kategoria"));
		imageLoader.DisplayImage(map.get("zdjecie"), image_view);
		//kategoria.setTypeface(MyNormal);
		//float ocena = Float.parseFloat(map.get("ocena"));
		return vi;
	}

};
