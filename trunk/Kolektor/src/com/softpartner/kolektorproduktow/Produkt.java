package com.softpartner.kolektorproduktow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Produkt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3414137406919035627L;
	public String ID = "", nazwa = "", waga = "", cena = "", ilosc = "",
			opis = "";
	public List<String> kody_kreskowe;

	public Produkt() {
		kody_kreskowe = new ArrayList<String>();
	}

	public void dodajKod(String kod) {
		kody_kreskowe.add(kod);
	}

	public String getKody() {
		String wynik = "";
		for (int i = 0; i < kody_kreskowe.size(); i++) {
			wynik += kody_kreskowe.get(i);
			if (i < kody_kreskowe.size() - 1)
				wynik += "\n";
		}
		return wynik;
	}
}
