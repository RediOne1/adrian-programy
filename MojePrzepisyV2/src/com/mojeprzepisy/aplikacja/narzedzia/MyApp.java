package com.mojeprzepisy.aplikacja.narzedzia;

import android.app.Application;

public class MyApp extends Application {
	private int data = -1;
	private String pseudonim = "";

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public String getPseudonim() {
		return pseudonim;
	}

	public void setPseudonim(String s) {
		this.pseudonim = s;
	}
}