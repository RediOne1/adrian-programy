package com.projekt2.test2;

import java.util.Random;

import com.projekt.test1.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Canvas extends Activity {
	private PaintView pv;
	private Context context;
	private long seed;
	private ProgressBar progressBar;
	private TextView komunikat;
	private Random random;
	private int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canvas);
		context = getApplicationContext();
		Bundle bundle = getIntent().getExtras();
		seed = bundle.getLong("seed", 0);
		random = new Random(seed);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		pv = (PaintView) findViewById(R.id.paintView1);
		komunikat = (TextView) findViewById(R.id.komunikat);
		/*
		 * if (seed <= 50) kreski(); else new Algorytm().execute();
		 */
		nowy();
	}

	private void kreski() {
		for (int i = 0; i < height; i += 3) {
			komunikat.setText("Dodawanie kresek");
			pv.Line(0, i, width, i + seed, Color.WHITE);
		}
	}

	private void nowy() {
		int wys = 0;
		while (wys < height) {
			boolean poziomy = random.nextBoolean();
			if (poziomy) {
				int n = random.nextInt(height - wys + 1);
				for (int i = wys; i < wys + n; i += 3) {
					pv.Line(0, i, width, i, Color.WHITE);
				}
				wys += n;
			} else {
				int n = random.nextInt(height - wys + 1);
				for (int i = 0; i < width; i += 3) {
					pv.Line(i, wys, i, wys + n, Color.WHITE);
				}
				wys += n;
			}
		}
	}

	private class Algorytm extends AsyncTask<String, Integer, String> {
		protected int ilosc_duzych_elementow, ilosc_malych_elementow, kreski,
				piksele;
		protected int suma;

		public Algorytm() {
			ilosc_duzych_elementow = random.nextInt(50) + 10;
			ilosc_malych_elementow = random.nextInt(50) + 10;
			kreski = random.nextInt(500) + 3000;
			piksele = random.nextInt(15000) + 10000;
			suma = ilosc_duzych_elementow + ilosc_malych_elementow + kreski
					+ piksele;
		}

		protected void onPreExecute() {
			progressBar.setMax(suma);
			progressBar.setVisibility(View.VISIBLE);
			komunikat.setVisibility(View.VISIBLE);
		}

		protected int getColor() {
			if (seed > 5000)
				return Color.rgb(random.nextInt(256), random.nextInt(256),
						random.nextInt(256));
			else
				return random.nextBoolean() ? Color.WHITE : Color.BLACK;

		}

		protected String doInBackground(String... urls) {
			int progress = 0;
			for (int i = 0; i < ilosc_duzych_elementow; i++, progress++) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					public void run() {
						komunikat.setText("Dodawanie dużych elementów");
						pv.Oval(random.nextInt(width), random.nextInt(height),
								random.nextInt(width / 2),
								random.nextInt(height / 2), getColor());
					}
				});
				publishProgress(progress);
			}
			for (int i = 0; i < ilosc_malych_elementow; i++, progress++) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					public void run() {
						int rozmiar = random.nextInt(width / 4);
						komunikat.setText("Dodawanie małych elementów");
						pv.Oval(random.nextInt(width), random.nextInt(height),
								rozmiar, rozmiar, getColor());
					}
				});
				publishProgress(progress);
			}
			for (int i = 0; i < kreski; i++, progress++) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					public void run() {
						komunikat.setText("Dodawanie kresek");
						pv.Line(random.nextInt(width), random.nextInt(height),
								random.nextInt(width), random.nextInt(height),
								getColor());
					}
				});
				publishProgress(progress);
			}
			for (int i = 0; i < piksele; i++, progress++) {
				runOnUiThread(new Runnable() {
					public void run() {
						komunikat.setText("Dodawanie pikseli");
						pv.Point(random.nextInt(width), random.nextInt(height),
								getColor());
					}
				});
				publishProgress(progress);
			}

			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			progressBar.setProgress(progress[0]);
		}

		protected void onPostExecute(String result) {
			komunikat.setText("");
			progressBar.setVisibility(View.GONE);
			komunikat.setVisibility(View.GONE);
		}
	}
}
