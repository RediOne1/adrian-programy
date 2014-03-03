package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Krok;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Skladnik;

public class WyswietlSkladniki extends WyswietlPrzepis {

	private LinearLayout linearLayout;
	private Przepis przepis;
	private Handler mHandler = new Handler();
	private List<Skladnik> skladniki;

	public WyswietlSkladniki(Activity root, Przepis _przepis) {
		this.przepis = _przepis;
		linearLayout = (LinearLayout) root
				.findViewById(R.id.wyswietl_skladniki_linearLayout);
		skladniki = new ArrayList<Skladnik>();
		new Thread(new Runnable() {
			public void run() {
				final String temp[] = przepis.skladniki.split(";");
				for (int i = 0; i < temp.length; i++) {
					if (temp[i].length() == 0)
						continue;
					mHandler.post(new Runnable() {
						public void run() {
						}
					});
				}
			}
		}).start();
	}
}
