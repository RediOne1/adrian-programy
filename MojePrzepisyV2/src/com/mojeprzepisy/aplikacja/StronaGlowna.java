package com.mojeprzepisy.aplikacja;

import com.mojeprzepisy.aplikacja.dodaj_przepis.DodajPrzepisActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class StronaGlowna extends Fragment implements OnClickListener{

	private ImageView zdjecie;
	private TextView tytul2;
	private TextView kategoria2;
	private RatingBar rate;
	private View root;
	private View rootView;
	private Button dodajPrzepisButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.strona_glowna, container,
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
		rate.setRating((float) 4.5);
		dodajPrzepisButton = (Button) root.findViewById(R.id.strona_glowna_dodaj_przepis);
		dodajPrzepisButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == dodajPrzepisButton){
			Intent i = new Intent(getActivity().getApplicationContext(), DodajPrzepisActivity.class);
			startActivity(i);
		}
	}
}
