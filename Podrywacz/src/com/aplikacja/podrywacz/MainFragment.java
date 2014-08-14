package com.aplikacja.podrywacz;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
	private TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main_fragment, container,
				false);
		tv = (TextView) rootView.findViewById(R.id.tv_wynik_ankiety);
		wyswietlWynik();
		// int i = getArguments().getInt(ARG_PLANET_NUMBER);
		return rootView;
	}

	public void wyswietlWynik() {
		mySharedPreferences pref = new mySharedPreferences(getActivity());
		int x = 0;
		int i = 0;
		String wynik = "";
		while (x != -1) {
			x = pref.getInt("" + i);
			if (x != -1)
				wynik += "odpowiedz nr " + (i + 1) + ": " + x + "\n";
			i++;
		}
		tv.setText(wynik);
	}
}
