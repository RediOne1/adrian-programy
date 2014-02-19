package com.mojeprzepisy.aplikacja;

public class Przepis {

	public int autorID;
	public int przepisID;
	public String tytul;
	public String kategoria;
	public float ocena;
	public int ilosc_ocen;
	public String trudnosc;
	public String ile_osob;
	public String zdjecie;

	public Przepis() {

	}

	/**
	 * Konstruktor obiektu przepis dodajacy dane o przepisie do obiektu
	 * @param _autorID nr ID autora przepisu
	 * @param _przepisID nr ID przepisu
	 * @param _tytul Tytul przepisu
	 * @param _kategoria kategoria
	 * @param _ocena zostanie automatycznie zamieniona i zapisana jako float
	 * @param _ilosc_ocen zostanie automatycznie zamieniona na int
	 * @param _trudnosc
	 * @param _ile_osob
	 * @param _zdjecie zostanie zapisane jako adres URL w Stringu
	 */
	public Przepis(String _autorID, String _przepisID, String _tytul,
			String _kategoria, String _ocena, String _ilosc_ocen,
			String _trudnosc, String _ile_osob, String _zdjecie) {
		this.autorID = String2Int(_autorID);
		this.przepisID = String2Int(_przepisID);
		this.tytul = _tytul;
		this.kategoria = _kategoria;
		this.ocena = String2Float(_ocena);
		this.ilosc_ocen = String2Int(_ilosc_ocen);
		this.trudnosc = _trudnosc;
		this.ile_osob = _ile_osob;
		this.zdjecie = _zdjecie;
	}

	private int String2Int(String string) {
		int Int;
		try {
			Int = Integer.parseInt(string);
		} catch (Exception e) {
			Int = 0;
		}
		return Int;
	}

	private float String2Float(String string) {
		float _Float;
		try {
			_Float = Float.parseFloat(string);
		} catch (Exception e) {
			_Float = 0;
		}
		return _Float;
	}
}
