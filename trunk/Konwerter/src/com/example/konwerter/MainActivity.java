package com.example.konwerter;

import java.util.Calendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnDateChangedListener,
		OnClickListener, TextWatcher {

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "System zabi³ aplikacje", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Toast.makeText(this, "Aplikacja wstrzymana", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Toast.makeText(this, "Aplikacja uruchomiona ponownie",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "Aplikacja wznowiona", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Toast.makeText(this, "Zapis stanu aplikacji", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "Start aplikacji", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Toast.makeText(this, "Aplikacja zatrzymana", Toast.LENGTH_SHORT).show();
	}

	DatePicker data;
	public int konwersja = 0;
	private TextView waluta1;
	private TextView waluta2;
	private EditText wartosc1;
	private EditText wartosc2;
	private Button zamien;
	private Button clear;
	private EditText kurs;
	private int dzien;
	private int miesiac;
	private int rok;
	SharedPreferences kursy;
	private String miesiace[] = { "styczeñ", "luty", "marzec", "kwiecieñ",
			"maj", "czerwiec", "lipiec", "sierpieñ", "wrzesieñ", "paŸdziernik",
			"listopad", "grudzieñ" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		kursy = getSharedPreferences("MyCustomSharedPreferences", 0);
		data = (DatePicker) findViewById(R.id.data);
		waluta1 = (TextView) findViewById(R.id.waluta1);
		waluta2 = (TextView) findViewById(R.id.waluta2);
		wartosc1 = (EditText) findViewById(R.id.wartosc1);
		wartosc2 = (EditText) findViewById(R.id.wartosc2);
		clear = (Button) findViewById(R.id.clear);
		kurs = (EditText) findViewById(R.id.kurs);
		zamien = (Button) findViewById(R.id.zamien);
		zamien.setOnClickListener(this);
		kurs.addTextChangedListener(this);
		wartosc1.addTextChangedListener(this);
		clear.setOnClickListener(this);

		Calendar today = Calendar.getInstance();

		dzien = today.get(Calendar.DAY_OF_MONTH);
		miesiac = today.get(Calendar.MONTH);
		rok = today.get(Calendar.YEAR);

		data.init(rok, miesiac, dzien, this);

		ustaw_tytuly();
		String data2 = dzien + "." + miesiac + "." + rok;
		kurs.setText("" + get(data2));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		dzien = dayOfMonth;
		miesiac = monthOfYear;
		rok = year;
		String data = dzien + "." + miesiac + "." + rok;
		kurs.setText("" + get(data));
		Toast.makeText(
				this,
				"Data: " + dayOfMonth + " " + miesiace[monthOfYear] + " "
						+ year, Toast.LENGTH_SHORT).show();
		Toast.makeText(this, "Kurs: " + get(data),
				Toast.LENGTH_SHORT).show();

	}

	public void ustaw_tytuly() {
		switch (konwersja) {
		case 0:
			waluta1.setText("Z³otówki");
			waluta2.setText("Bitcoiny");
			break;
		case 1:
			waluta1.setText("Bitcoiny");
			waluta2.setText("Z³otówki");
			break;
		}
	}

	public void konwertuj() {
		float var1 = 0, var_kurs = 0, wynik;
		try {
			var1 = Float.parseFloat(wartosc1.getText().toString());
			var_kurs = Float.parseFloat(kurs.getText().toString());
			String data = dzien + "." + miesiac + "." + rok;
			put(data, var_kurs);
		} catch (Exception e) {

		}
		wynik = konwersja == 0 ? var1 * var_kurs : var1 / var_kurs;
		wartosc2.setText("" + wynik);
	}

	public void zamien() {
		konwersja = konwersja == 0 ? 1 : 0;
		String temp;
		temp = wartosc1.getText().toString();
		wartosc1.setText(wartosc2.getText().toString());
		wartosc2.setText(temp);
		ustaw_tytuly();
	}

	@Override
	public void onClick(View v) {
		if (v == zamien) {
			zamien();
		} else if (v == clear){
			clear();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		konwertuj();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}	
	

	public float get(String data) {
		float ref = kursy.getFloat(data, 1);
		return ref;
	}

	public void put(String data, float kurs) {
		SharedPreferences.Editor edytorPref = kursy.edit();
		if (!kursy.contains(data))
			edytorPref.remove(data);
		edytorPref.putFloat(data, kurs);
		edytorPref.commit();
	}

	public void remove(String data) {
		SharedPreferences.Editor edytorPref = kursy.edit();
		edytorPref.remove(data);
		edytorPref.commit();
	}

	public void clear() {
		SharedPreferences.Editor edytorPref = kursy.edit();
		edytorPref.clear();
		edytorPref.commit();
		Toast.makeText(this, "Usuniêto zapamiêtane kursy",
				Toast.LENGTH_SHORT).show();
	}
}
