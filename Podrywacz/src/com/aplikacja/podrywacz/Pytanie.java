package com.aplikacja.podrywacz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class Pytanie implements OnCheckedChangeListener {

	private View vi;
	private TextView tv;
	private Activity root;
	private LayoutInflater inflater;
	public String tresc;
	public List<RadioButton> odpowiedzi;
	public RadioGroup radio_group;
	public int zaznaczona_odpowiedz = 0;
	public int ilosc_odpowiedzi = 0;

	public Pytanie(Activity root) {
		this.root = root;
		inflater = (LayoutInflater) root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi = inflater.inflate(R.layout.radio_szablon, null);
		odpowiedzi = new ArrayList<RadioButton>();
		radio_group = (RadioGroup) vi.findViewById(R.id.radioGroup1);
		tv = (TextView) vi.findViewById(R.id.pytanie_textview);
		radio_group.setOnCheckedChangeListener(this);
	}

	public void setPytanie(String text) {
		tresc = text;
		tv.setText(text);
	}

	public void dodajOdpowiedz(String text) {
		ilosc_odpowiedzi++;
		RadioButton radio_button = new RadioButton(root);
		odpowiedzi.add(radio_button);
		radio_group.addView(radio_button);
		radio_button.setText(text);
		radio_button.setTag("" + ilosc_odpowiedzi);
	}

	public View getView() {
		return vi;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId != -1) {
			RadioButton rb = (RadioButton) root.findViewById(checkedId);
			zaznaczona_odpowiedz = Integer.parseInt(rb.getTag().toString());
		}
	}
}
