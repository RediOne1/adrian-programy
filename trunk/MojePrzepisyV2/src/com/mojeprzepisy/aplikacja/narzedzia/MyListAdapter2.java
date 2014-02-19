package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;

public class MyListAdapter2 extends BaseAdapter {
	private Activity activity;
	private Context mContext;
	private List<Przepis> dane;
	private static LayoutInflater inflater = null;
	private Typeface MyBold;
	private Typeface MyNormal;
	private ImageLoader imageLoader;

	public MyListAdapter2(Activity a,
			List<Przepis> wszystkiePrzepisy) {
		activity = a;
		dane = wszystkiePrzepisy;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
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

		TextView przepisID = (TextView) vi.findViewById(R.id.single_item_przepisID);
		TextView autorID = (TextView) vi.findViewById(R.id.single_item_autorID);
		TextView Tytul = (TextView) vi.findViewById(R.id.single_item_tytul);
		TextView kategoria = (TextView) vi.findViewById(R.id.single_item_kategoria);
		TextView nr = (TextView) vi.findViewById(R.id.nr_najwyzej_oceniane);
		ImageView image_view = (ImageView) vi.findViewById(R.id.single_item_image);
		//RatingBar rating = (RatingBar) vi.findViewById(R.id.single_item_tytul);

		Przepis przepis;
		przepis = dane.get(position);
		// Setting all values in listview
		autorID.setText(""+przepis.autorID);
		przepisID.setText(""+przepis.przepisID);
		nr.setText((position+1)+".");
		Tytul.setText(przepis.tytul);
		//Tytul.setTypeface(MyBold);
		kategoria.setText(przepis.kategoria);
		imageLoader.DisplayImage(przepis.zdjecie, image_view);
		//kategoria.setTypeface(MyNormal);
		//float ocena = Float.parseFloat(map.get("ocena"));
		return vi;
	}

};
