package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.ImageLoader;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class WyswietlTytulowyModul extends WyswietlPrzepis {
	private ImageView zdjecie;
	private TextView tytul;
	private TextView kategoria;
	private RatingBar ocena;
	private TextView ilosc_ocen;
	private TextView trudnosc;
	private TextView czas;

	public WyswietlTytulowyModul(Activity root, Przepis przepis) {
		zdjecie = (ImageView) root.findViewById(R.id.zdjecie_maly_layout);
		tytul = (TextView) root.findViewById(R.id.przepis_tytul_maly_layout);
		kategoria = (TextView) root.findViewById(R.id.kategoria_maly_layout);
		ocena = (RatingBar) root.findViewById(R.id.ratingbar_maly_layout);
		ilosc_ocen = (TextView) root.findViewById(R.id.ilosc_ocen_maly_layout);
		trudnosc = (TextView) root.findViewById(R.id.trudnosc_maly_layout);
		czas = (TextView) root.findViewById(R.id.czas_maly_layout);
		tytul.setText(przepis.tytul);
		kategoria.setText(przepis.kategoria);
		ocena.setRating(przepis.ocena);
		ilosc_ocen.setText(""+przepis.ilosc_ocen);
		trudnosc.setText(przepis.trudnosc);
		czas.setText(przepis.czas);
		new ImageLoader(root).DisplayImage(przepis.zdjecie, zdjecie);
		tytul = (TextView) new MyTypeFace(tytul, root).MyBold();
		kategoria = (TextView) new MyTypeFace(kategoria,root).MyNormal();
		trudnosc = (TextView) new MyTypeFace(trudnosc, root).MyNormal();
		czas = (TextView) new MyTypeFace(czas, root).MyNormal();
	}
}
