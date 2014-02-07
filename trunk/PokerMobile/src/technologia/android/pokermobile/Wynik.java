package technologia.android.pokermobile;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Wynik {
	protected GameActivity context;
	private Gracz gracz[];
	private ArrayList<HashMap<String, Object>> Karty;
	private Stol s;

	public void wyswietlWynik(GameActivity _context) {
		context = _context;
		gracz = context.gracz;
		s = new Stol(gracz);
		int KtoWygral = s.wynik();
		gracz[KtoWygral].StanKonta += context.l.pula;
		context.wynik_list.removeAllViews();
		context.next_round.setVisibility(View.VISIBLE);
		context.black.setVisibility(View.GONE);
		context.stol.setVisibility(View.GONE);
		LinearLayout linear;
		for (int i = 0; i < gracz.length; i++) {
			linear = new LinearLayout(context);
			Karty = new ArrayList<HashMap<String, Object>>();
			context.lista_kart.removeAllViews();
			for (int j = 0; j < 5; j++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				int liczba = gracz[i].karta[j].liczba;
				map.put("liczba", gracz[i].karta[j].liczby[liczba]);
				int kolor = gracz[i].karta[j].kolor;
				switch (gracz[i].karta[j].kolor) {
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
			ListAdapter adapter = new SimpleAdapter(context, Karty,
					R.layout.karta, new String[] { "liczba", "kolor1",
							"kolor2", "kolor3", "kolor4" }, new int[] {
							R.id.karta_liczba, R.id.karta_kolor1,
							R.id.karta_kolor2, R.id.karta_kolor3,
							R.id.karta_kolor4 });

			// updating listview
			for (int x = 0; x < adapter.getCount(); x++) {
				View item = adapter.getView(x, null, null);
				linear.addView(item);
			}
			TextView tv = new TextView(context);
			tv.setTextSize(25);
			tv.setTextAppearance(context, android.R.attr.textAppearanceLarge);
			gracz[i].StanKonta -= gracz[i].ile_postawil;
			tv.setText(gracz[i].pseudonim + ":  stan konta("+gracz[i].StanKonta+")");
			context.wynik_list.addView(tv);
			HorizontalScrollView hsv = new HorizontalScrollView(context);
			hsv.addView(linear);
			context.wynik_list.addView(hsv);
		}
		TextView tv = new TextView(context);
		tv.setTextSize(40);
		tv.setTextAppearance(context, android.R.attr.textAppearanceLarge);
		if (KtoWygral == -1) {
			tv.setText("Remis");
			context.l.pula = 0;
		} else {
			tv.setText("Wygral " + gracz[KtoWygral].pseudonim);
		}
		context.wynik_list.addView(tv);
	}
}
