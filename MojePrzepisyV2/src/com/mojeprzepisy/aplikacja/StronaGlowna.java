package com.mojeprzepisy.aplikacja;

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

import com.mojeprzepisy.aplikacja.dodaj_przepis.DodajPrzepisActivity;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class StronaGlowna extends Fragment implements OnClickListener {

	private View root;
	private View dodajPrzepisButton;
	private View wybierzKategorie;

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
		LosowyPrzepis losowyPrzepis = new LosowyPrzepis(getActivity());
		dodajPrzepisButton = (View) root
				.findViewById(R.id.dodaj_przepis_linearLayout);
		wybierzKategorie = (View) root.findViewById(R.id.Wybierz_kategorie_linearLayout);
		wybierzKategorie.setOnClickListener(this);
		dodajPrzepisButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == dodajPrzepisButton) {
			Intent i = new Intent(getActivity().getApplicationContext(),
					DodajPrzepisActivity.class);
			startActivity(i);
		} else if(v==wybierzKategorie){
			Intent i = new Intent(getActivity().getApplicationContext(),
					WybierzKategorie.class);
			startActivity(i);
		}
	}
}
