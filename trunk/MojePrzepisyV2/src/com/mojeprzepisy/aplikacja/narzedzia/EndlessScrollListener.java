package com.mojeprzepisy.aplikacja.narzedzia;

import android.app.Activity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class EndlessScrollListener implements OnScrollListener {

	private int visibleThreshold = 1;
	private int currentPage = 0;
	private int previousTotal = 0;
	private int przepisow_na_strone = 10;
	private boolean loading = false;
	private Activity a;
	private ListView lv;
	private TopListZapytanie top_list_zapytanie;
	String params[];

	public EndlessScrollListener(Activity _a, ListView _lv, String adresURL) {
		this.lv = _lv;
		this.a = _a;
		top_list_zapytanie = new TopListZapytanie(a, lv);
		params = new String[3];
		params[0] = adresURL;
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d("DEBUG_TAG","onScroll");
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				currentPage++;
			}
		}
		if (!loading
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			// I load the next page of gigs using a background task,
			// but you can call any function here.
			params[1] = "" + (przepisow_na_strone * currentPage);
			params[2] = "" + (przepisow_na_strone);
			top_list_zapytanie.wykonaj(params);
			loading = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}
