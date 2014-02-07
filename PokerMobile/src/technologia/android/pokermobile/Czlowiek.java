package technologia.android.pokermobile;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Czlowiek extends Gracz {

	protected GameActivity context;
	private Handler mHandler = new Handler();

	private int ile;
	private ArrayList<HashMap<String, Object>> Karty;

	Czlowiek(GameActivity _context, String pseudo, int _index) {
		pseudonim = pseudo;
		context = _context;
		index = _index;
		wezKarte(context.t);
	}

	public void Wymiana(GameActivity _context) {
		context = _context;
		context.polecenie.setText("Przekaz telefon graczowi:\n" + pseudonim);
		context.pokazCzern(true);
		wyswietlGracza();
	}

	public String toString() {
		return pseudonim;
	}

	@Override
	public void Licytuj(GameActivity _context) {
		context = _context;
		l = context.l;
		context.pokazCzern(true);
		context.polecenie.setText("Przeka¿ telefon graczowi\n" + pseudonim);
		wyswietlGracza();
	}

	private void wyswietlGracza() {
		if (context.etap == context.I_wymiana) {
			ukryj_przyciski(View.GONE, View.GONE, View.GONE, View.GONE,
					View.GONE, View.GONE, View.VISIBLE);
		} else if (StanKonta < l.minimalna_stawka) {
			ukryj_przyciski(View.GONE, View.GONE, View.GONE, View.GONE,
					View.VISIBLE, View.VISIBLE, View.GONE);
			context.raise_seekBar.setEnabled(false);
		} else {
			if (l.licytacja == l.bet) {
				context.tekstObokStawki.setText("Twoja stawka to:");
				ukryj_przyciski(View.VISIBLE, View.VISIBLE, View.GONE,
						View.GONE, View.VISIBLE, View.GONE, View.GONE);
			} else if (l.licytacja == l.raise) {
				context.tekstObokStawki.setText("Zwieksz stawke:");
				ukryj_przyciski(View.GONE, View.GONE, View.VISIBLE,
						View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
			} else if (l.licytacja == l.allin) {
				context.tekstObokStawki.setText("Nastapilo zagranie all-in");
				ukryj_przyciski(View.GONE, View.GONE, View.GONE, View.GONE,
						View.VISIBLE, View.VISIBLE, View.GONE);
				context.raise_seekBar.setEnabled(false);
			}
		}
		context.min_stawka.setText("" + l.minimalna_stawka);
		context.raise_seekBar.setProgress(ile_postawil);
		context.raise_seekBar.setMax(StanKonta);
		context.stanKonta.setText("" + StanKonta);
		context.pula.setText("" + l.pula);
		Karty = new ArrayList<HashMap<String, Object>>();
		context.lista_kart.removeAllViews();
		for (int j = 0; j < 5; j++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			int liczba = karta[j].liczba;
			map.put("liczba", karta[j].liczby[liczba]);
			int kolor = karta[j].kolor;
			switch (karta[j].kolor) {
			case 0:
				map.put("kolor1", R.drawable.kier);
				map.put("kolor2", R.drawable.kier);
				map.put("kolor3", R.drawable.kier);
				map.put("kolor4", R.drawable.kier);
				break;
			case 1:
				map.put("kolor1", R.drawable.karo);
				map.put("kolor2", R.drawable.karo);
				map.put("kolor3", R.drawable.karo);
				map.put("kolor4", R.drawable.karo);
				break;
			case 2:
				map.put("kolor1", R.drawable.pik);
				map.put("kolor2", R.drawable.pik);
				map.put("kolor3", R.drawable.pik);
				map.put("kolor4", R.drawable.pik);
				break;
			case 3:
				map.put("kolor1", R.drawable.trefl);
				map.put("kolor2", R.drawable.trefl);
				map.put("kolor3", R.drawable.trefl);
				map.put("kolor4", R.drawable.trefl);
				break;
			}
			// adding HashList to ArrayList
			Karty.add(map);
		}
		ListAdapter adapter = new SimpleAdapter(
				context,
				Karty,
				R.layout.karta,
				new String[] { "liczba", "kolor1", "kolor2", "kolor3", "kolor4" },
				new int[] { R.id.karta_liczba, R.id.karta_kolor1,
						R.id.karta_kolor2, R.id.karta_kolor3, R.id.karta_kolor4 });

		// updating listview
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			item.setOnClickListener(context);
			item.setTag("Nie_wybrana");
			item.setId(i);
			context.lista_kart.addView(item);
		}

	}

	private void ukryj_przyciski(int icheck, int ibet, int iraise, int icall,
			int ifold, int iallin, int iwymien) {

		context.check_button.setVisibility(icheck);
		context.bet_button.setVisibility(ibet);
		context.raise_button.setVisibility(iraise);
		context.call_button.setVisibility(icall);
		context.fold_button.setVisibility(ifold);
		context.allin_button.setVisibility(iallin);
		context.wymien_button.setVisibility(iwymien);
	}

	public void Obstaw(GameActivity _context, int _ile) {
		context = _context;
		l = context.l;
		ile = _ile;
		if (ile < l.minimalna_stawka && l.licytacja != l.allin) {
			Toast.makeText(context, "Stawka musi byæ wiêksza od minimalnej.",
					Toast.LENGTH_SHORT).show();
		} else {
			context.raise_seekBar.setEnabled(false);
			context.check_button.setEnabled(false);
			context.bet_button.setEnabled(false);
			context.raise_button.setEnabled(false);
			context.fold_button.setEnabled(false);
			context.allin_button.setEnabled(false);
			l.pula -= ile_postawil;
			ile_postawil = ile;
			l.minimalna_stawka = ile;
			if (l.licytacja == l.allin)
				l.minimalna_stawka = 0;
			l.gracz_najwyzej = index;
			new Thread(new Runnable() {
				public void run() {
					try {
						synchronized (this) {
							wait(100);
						}
					} catch (Exception e) {
					}
					int pomocnicza = 100;
					while (ile > 0) {
						if (ile / pomocnicza >= 1) {
							l.pula += pomocnicza;
							ile -= pomocnicza;
						} else
							pomocnicza /= 100;
						try {
							synchronized (this) {
								wait(1);
							}
						} catch (Exception e) {
						}
						mHandler.post(new Runnable() {
							public void run() {
								context.raise_seekBar.setProgress(ile);
								context.stawka_gracza.setText("" + ile);
								context.pula.setText("" + l.pula);
							}
						});

					}
					mHandler.post(new Runnable() {
						public void run() {
							context.pokazCzern(true);
							context.gra();
						}
					});
				}
			}).start();
		}
	}
}
