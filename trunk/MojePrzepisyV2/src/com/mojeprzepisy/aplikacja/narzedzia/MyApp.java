package com.mojeprzepisy.aplikacja.narzedzia;

import android.app.Application;

public class MyApp extends Application {
	private int data = -1;

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}
}