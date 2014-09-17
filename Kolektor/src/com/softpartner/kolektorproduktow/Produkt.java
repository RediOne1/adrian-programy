package com.softpartner.kolektorproduktow;

import java.util.ArrayList;
import java.util.List;

public class Produkt {

	public String ID = "", nazwa = "", waga = "", cena = "", ilosc = "",
			opis = "";
	public List<String> kody_kreskowe;

	public Produkt() {
		kody_kreskowe = new ArrayList<String>();
	}

	public void dodajKod(String kod) {
		kody_kreskowe.add(kod);
	}
}
