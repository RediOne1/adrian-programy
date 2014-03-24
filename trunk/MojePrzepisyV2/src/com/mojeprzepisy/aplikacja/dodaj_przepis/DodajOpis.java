package com.mojeprzepisy.aplikacja.dodaj_przepis;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class DodajOpis extends DodajPrzepisActivity implements OnClickListener {

	private View dodaj_krok;
	public LinearLayout linearLayout;
	public List<Krok> kroki = new LinkedList<Krok>();

	public DodajOpis() {

	}

	public DodajOpis(Activity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		dodaj_krok = (View) root.findViewById(R.id.dodaj_opis_button);
		linearLayout = (LinearLayout) root
				.findViewById(R.id.tytul_linearlayout);
		dodaj_krok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == dodaj_krok) {
			Krok krok = new Krok(root, linearLayout, DodajOpis.this);
			kroki.add(krok);
			linearLayout.addView(krok.toView());
		}
	}
	@Override
	public String toString() {
		String wynik = "";
		for (int i = 0; i < kroki.size(); i++) {
			if ((kroki.get(i).opis == null) || (kroki.get(i).opis.length() < 2))
				continue;
			wynik += ";krok;" + kroki.get(i).tytul + ";opis;"
					+ kroki.get(i).opis;
		}
		return wynik;
	}
}
