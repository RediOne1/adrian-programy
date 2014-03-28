package com.mojeprzepisy.aplikacja;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.RejestracjaActivity.WyslijMail;
import com.mojeprzepisy.aplikacja.dodaj_przepis.DodajPrzepisActivity;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class StronaGlowna extends Fragment implements OnClickListener {

	private View root;
	private View dodajPrzepisButton;
	private View wybierzKategorie;
	private View rejestracja;
	private View zaloguj;
	private View wyloguj;

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
		Zaloguj logowanie = new Zaloguj(getActivity());
		LosowyPrzepis losowyPrzepis = new LosowyPrzepis(getActivity());
		zaloguj = (View) root.findViewById(R.id.zaloguj_layout);
		rejestracja = (View) root.findViewById(R.id.rejestracja_layout);
		dodajPrzepisButton = (View) root
				.findViewById(R.id.dodaj_przepis_linearLayout);
		wybierzKategorie = (View) root
				.findViewById(R.id.Wybierz_kategorie_linearLayout);
		wyloguj = (View) root.findViewById(R.id.wyloguj_layout);
		wyloguj.setOnClickListener(this);
		zaloguj.setOnClickListener(logowanie);
		rejestracja.setOnClickListener(this);
		wybierzKategorie.setOnClickListener(this);
		dodajPrzepisButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == dodajPrzepisButton) {
			Intent i = new Intent(getActivity().getApplicationContext(),
					DodajPrzepisActivity.class);
			i.putExtra("edytuj", false);
			startActivity(i);
		} else if (v == wybierzKategorie) {
			Intent i = new Intent(getActivity().getApplicationContext(),
					WybierzKategorie.class);
			startActivity(i);
		} else if (v == rejestracja) {
			Intent i = new Intent(getActivity().getApplicationContext(),
					RejestracjaActivity.class);
			startActivity(i);
		}else if(v==wyloguj){
			MyApp app = (MyApp) getActivity().getApplicationContext();
			app.setData(-1);
			wyloguj.setVisibility(View.GONE);
			dodajPrzepisButton.setVisibility(View.GONE);
			rejestracja.setVisibility(View.VISIBLE);
			zaloguj.setVisibility(View.VISIBLE);
		}
	}

}
