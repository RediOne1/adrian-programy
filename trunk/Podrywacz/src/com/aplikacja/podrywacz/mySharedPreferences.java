package com.aplikacja.podrywacz;

import android.app.Activity;
import android.content.SharedPreferences;

public class mySharedPreferences {
	private SharedPreferences preferences;
	private Activity root;

	public mySharedPreferences(Activity root) {
		this.root = root;
		preferences = root.getSharedPreferences("My_Preferences",
				Activity.MODE_PRIVATE);
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.putBoolean(key, value);
		preferencesEditor.commit();
	}

	public void putFloat(String key, float value) {
		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.putFloat(key, value);
		preferencesEditor.commit();
	}

	public void putLong(String key, long value) {
		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.putLong(key, value);
		preferencesEditor.commit();
	}

	public void putInt(String key, int value) {
		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.putInt(key, value);
		preferencesEditor.commit();
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.putString(key, value);
		preferencesEditor.commit();
	}

	public void remove(String key) {
		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.remove(key);
		preferencesEditor.commit();
	}
	public void clear(){
		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.clear();
		preferencesEditor.commit();
	}

	public boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}

	public int getInt(String key) {
		return preferences.getInt(key, 0);
	}

	public float getFloat(String key) {
		return preferences.getFloat(key, 0);
	}

	public long getLong(String key) {
		return preferences.getLong(key, 0);
	}
}
