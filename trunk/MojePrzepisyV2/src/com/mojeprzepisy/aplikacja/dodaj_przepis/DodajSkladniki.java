package com.mojeprzepisy.aplikacja.dodaj_przepis;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class DodajSkladniki extends DodajPrzepisActivity implements OnClickListener {

	private Button dodajSkladnikButton;
	private LinearLayout linearLayout;
	private Activity root;

	public DodajSkladniki(Activity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		dodajSkladnikButton = (Button) root
				.findViewById(R.id.dodaj_skladnik_button);
		linearLayout = (LinearLayout) root
				.findViewById(R.id.skladniki_linearLayout);
		dodajSkladnikButton.setOnClickListener(this);
		dodajSkladnikButton = (Button) new MyTypeFace(dodajSkladnikButton, root).MyBold();
	}

	@Override
	public void onClick(View v) {
		if (v == dodajSkladnikButton) {
			Skladnik skladnik = new Skladnik(root, linearLayout);
			linearLayout.addView(skladnik.toView());			
		}
	}
}
