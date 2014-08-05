package com.mojeprzepisy.aplikacja;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.narzedzia.EndlessScrollListener;
import com.mojeprzepisy.aplikacja.narzedzia.MyOnItemClickListener;
import com.mojeprzepisy.aplikacja.narzedzia.TopListZapytanie;

public class OstatnioDodane extends ListFragment {
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.ostatnio_dodane, container, false);
		return rootView;
	}

	ListView lv;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lv = getListView();
		lv.setOnScrollListener(new EndlessScrollListener(getActivity(),lv,"http://softpartner.pl/moje_przepisy2/ostatnio_dodane.php"));
		lv.setOnItemClickListener(new MyOnItemClickListener(getActivity().getApplicationContext()));
	}
}
