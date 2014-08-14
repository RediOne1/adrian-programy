package com.aplikacja.podrywacz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Ankieta implements OnClickListener {
	private Activity root;
	private MyXmlPraser mXmlPraser;
	private List<Pytanie> pytania;
	List<List<Pytanie>> lista_stron;
	private LinearLayout li;
	private Button poprzedni, nastepny;
	private TextView strona;
	private int ilosc_stron = 1, aktualna_strona = 1;

	public Ankieta(Activity _root) {
		this.root = _root;

		li = (LinearLayout) root.findViewById(R.id.ankieta_linear_layout);
		poprzedni = (Button) root.findViewById(R.id.poprzedni_button);
		nastepny = (Button) root.findViewById(R.id.nastepny_button);
		strona = (TextView) root.findViewById(R.id.strona_textview);
		poprzedni.setOnClickListener(this);
		nastepny.setOnClickListener(this);

		mXmlPraser = new MyXmlPraser(root);
		mXmlPraser.odczytaj();
		pytania = mXmlPraser.getPytania();
		podzielNaStrony();
		wyswietlStrony();
	}

	private void wyswietlStrony() {
		strona.setText(aktualna_strona + "/" + ilosc_stron);
		List<Pytanie> temp = lista_stron.get(aktualna_strona - 1);
		li.removeAllViews();
		for (Pytanie v : temp) {
			li.addView(v.getView());
		}
		if (aktualna_strona == 1)
			poprzedni.setVisibility(View.GONE);
		else
			poprzedni.setVisibility(View.VISIBLE);

		if (aktualna_strona == ilosc_stron) {
			String s = root.getResources().getString(R.string.zakoncz_ankiete);
			nastepny.setText(s);
		} else {
			String s = root.getResources().getString(R.string.nastepny);
			nastepny.setText(s);
		}

	}

	private boolean sprawdzOdpowiedzi() {
		List<Pytanie> temp = lista_stron.get(aktualna_strona - 1);
		for (Pytanie p : temp)
			if (p.zaznaczona_odpowiedz == 0)
				return false;
		return true;

	}

	public void podzielNaStrony() {
		lista_stron = new ArrayList<List<Pytanie>>();
		List<Pytanie> temp = new ArrayList<Pytanie>();
		int height = 0;
		for (int i = 0; i < pytania.size(); i++) {
			height += getHeight(pytania.get(i).getView());
			if (height < getScreenHeight())
				temp.add(pytania.get(i));
			else {
				lista_stron.add(temp);
				height = 0;
				temp = new ArrayList<Pytanie>();
				height += getHeight(pytania.get(i).getView());
				temp.add(pytania.get(i));
			}
		}
		lista_stron.add(temp);
		ilosc_stron = lista_stron.size();

	}

	public int getScreenHeight() {
		WindowManager wm = (WindowManager) root.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.y;
	}

	public int getHeight(View v) {
		v.measure(v.getWidth(), v.getHeight());
		return v.getMeasuredHeight();
	}

	public void zakonczAnkiete() {
		int ilosc_odpowiedzi = pytania.get(0).ilosc_odpowiedzi;
		int odpowiedzi[] = new int[ilosc_odpowiedzi];
		for (int j = 0; j < pytania.size(); j++) {
			int x = pytania.get(j).zaznaczona_odpowiedz;
			if (x == 0)
				continue;
			else
				odpowiedzi[x - 1]++;
		}
		mySharedPreferences pref = new mySharedPreferences(root);
		pref.clear();
		for (int j = 0; j < odpowiedzi.length; j++) {
			pref.putInt("" + j, odpowiedzi[j]);
		}
		pref.putInt("" + odpowiedzi.length, -1);
		Intent i = new Intent(root, MyDrawer.class);
		root.startActivity(i);
	}

	@Override
	public void onClick(View v) {
		if (v == poprzedni) {
			aktualna_strona--;
			wyswietlStrony();
		} else if (v == nastepny) {
			if (sprawdzOdpowiedzi()) {
				if (((Button) (v))
						.getText()
						.toString()
						.equals(root.getResources()
								.getString(R.string.nastepny))) {
					aktualna_strona++;
					wyswietlStrony();
				} else
					zakonczAnkiete();
			} else {
				Toast.makeText(root.getApplicationContext(), "Odpowiedz na wszystkie pytania", Toast.LENGTH_SHORT).show();
			}
		}

	}
}
