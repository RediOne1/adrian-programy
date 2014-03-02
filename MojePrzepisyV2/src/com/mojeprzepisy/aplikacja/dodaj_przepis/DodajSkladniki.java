package com.mojeprzepisy.aplikacja.dodaj_przepis;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class DodajSkladniki extends DodajPrzepisActivity implements OnClickListener {

	private Button dodajSkladnikButton;
	public LinearLayout linearLayout;
	public List<Skladnik> skladniki = new LinkedList<Skladnik>();

	public DodajSkladniki(){
		
	}
	public DodajSkladniki(Activity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		dodajSkladnikButton = (Button) root
				.findViewById(R.id.dodaj_skladnik_button);
		linearLayout = (LinearLayout) root
				.findViewById(R.id.skladniki_linearLayout);
		dodajSkladnikButton.setOnClickListener(this);
		dodajSkladnikButton = (Button) new MyTypeFace(dodajSkladnikButton, root).MyNormal();
	}

	@Override
	public void onClick(View v) {
		if (v == dodajSkladnikButton) {
			Skladnik skladnik = new Skladnik(root, linearLayout, DodajSkladniki.this);
			skladniki.add(skladnik);
			linearLayout.addView(skladnik.toView());	
			//wypisz();
		}
	}
	public String getSkladniki(){
		String wynik = "";
		for(int i=0;i<skladniki.size();i++){
			if(skladniki.get(i)==null)
				continue;
			wynik+=skladniki.get(i)+";";
		}
		return wynik;
	}
}
