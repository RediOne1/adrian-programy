package com.aplikacja.podrywacz;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ZadaniaFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.zadania_fragment, container,
				false);
		//int i = getArguments().getInt(ARG_PLANET_NUMBER);
		return rootView;
	}

}
