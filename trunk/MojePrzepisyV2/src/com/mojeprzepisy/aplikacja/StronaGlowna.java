package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class StronaGlowna extends Fragment {

	View root;
	ImageView zdjecie;
	TextView tytul2;
	TextView kategoria2;
	RatingBar rate;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.strona_glowna, container,
				false);
		this.root = rootView;
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		zdjecie = (ImageView) root.findViewById(R.id.zdjecie_maly_layout);
		tytul2 = (TextView) root.findViewById(R.id.przepis_tytul_maly_layout);
		kategoria2 = (TextView) root.findViewById(R.id.kategoria_maly_layout);
		rate = (RatingBar) root.findViewById(R.id.ratingbar_maly_layout);
		rate.setRating((float)4.5);
	}	
}
