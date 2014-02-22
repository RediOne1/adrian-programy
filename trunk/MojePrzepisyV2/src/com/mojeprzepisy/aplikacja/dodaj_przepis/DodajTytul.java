package com.mojeprzepisy.aplikacja.dodaj_przepis;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.R;

public class DodajTytul extends DodajPrzepisActivity implements OnClickListener {

	private DodajPrzepisActivity root;
	private TextView tytul_tv;
	private EditText tytul_et;
	private ImageView save_image;

	public DodajTytul(DodajPrzepisActivity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		tytul_tv = (TextView) root.findViewById(R.id.dodaj_tytul_textview);
		tytul_et = (EditText) root.findViewById(R.id.dodaj_tytul_edittext);
		save_image = (ImageView) root.findViewById(R.id.dodaj_tytul_save);
		save_image.setOnClickListener(this);
		tytul_tv.setOnClickListener(this);
		tytul_et.setOnClickListener(this);
		tytul_et.setVisibility(View.GONE);
		save_image.setVisibility(View.GONE);
	}

	String temp_tytul;
	@Override
	public void onClick(View v) {
		if (v == tytul_tv) {
			temp_tytul = tytul_tv.getText().toString();
			tytul_tv.setVisibility(View.GONE);
			save_image.setVisibility(View.VISIBLE);
			tytul_et.setVisibility(View.VISIBLE);
			//tytul_et.setText(temp_tytul);
			tytul_et.requestFocus();
		} else if (v == save_image) {
			temp_tytul = tytul_et.getText().toString();
			tytul_et.setVisibility(View.GONE);
			tytul_tv.setVisibility(View.VISIBLE);
			if(temp_tytul.length()==0)
				temp_tytul = root.getResources().getString(R.string.dodaj_tytul);
			tytul_tv.setText(temp_tytul);
			save_image.setVisibility(View.GONE);
		}
	}
}
