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

	private Button dodaj_krok;
	public LinearLayout linearLayout;
	public List<Krok> kroki = new LinkedList<Krok>();

	public DodajOpis() {

	}

	public DodajOpis(Activity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		dodaj_krok = (Button) root.findViewById(R.id.dodaj_opis_button);
		linearLayout = (LinearLayout) root
				.findViewById(R.id.tytul_linearlayout);
		dodaj_krok = (Button) new MyTypeFace(dodaj_krok, root).MyNormal();
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

	public String getOpis() {
		String wynik = "";
		for (int i = 0; i < kroki.size(); i++) {
			wynik += kroki.get(i).tytul + ";" + kroki.get(i).opis + ";;";
		}
		return wynik;
	}
}
