package com.mojeprzepisy.aplikacja.dodaj_przepis;

import com.mojeprzepisy.aplikacja.R;

import android.app.Activity;
import android.widget.Spinner;
import android.widget.Switch;

public class DodajDodatkoweDane extends DodajPrzepisActivity{
	
	public Switch widoczny;
	public Spinner kategoria;
	public Spinner trudnosc;
	public Spinner czas;
	
	public DodajDodatkoweDane(Activity dodajPrzepisActivity){
		root = dodajPrzepisActivity;		
		widoczny = (Switch) root.findViewById(R.id.dodatkowe_dane_widoczny);
		kategoria = (Spinner) root.findViewById(R.id.dodatkowe_dane_kategoria);
		trudnosc = (Spinner) root.findViewById(R.id.dodatkowe_dane_trudnosc);
		czas = (Spinner) root.findViewById(R.id.dodatkowe_dane_czas);
	}

	public DodajDodatkoweDane(Activity root, String trudnosc2,
			String kategoria2, String kategoria3) {
		// TODO Auto-generated constructor stub
	}

}
