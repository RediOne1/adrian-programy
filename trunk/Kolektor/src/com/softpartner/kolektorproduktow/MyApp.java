package com.softpartner.kolektorproduktow;

import android.app.Application;

public class MyApp extends Application {
	private String data = "-1";
	private boolean premium = false;
	public GoogleLogin gl = null;
	public String schowek = null;
	public Skaner skaner = null;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	public boolean getPremium(){
		return premium;
	}
}