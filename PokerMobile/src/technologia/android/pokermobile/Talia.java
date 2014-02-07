package technologia.android.pokermobile;

import java.util.ArrayList;
import java.util.Random;

public class Talia {

	public ArrayList<Karta> karty;
	Random losowa = new Random();

	Talia() {
		karty = new ArrayList<Karta>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				karty.add(new Karta(i, j));
			}
		}

		/** Tasujemy karty */
		Karta pomocnicza;
		int index_1, index_2;
		for (int i = 0; i < losowa.nextInt(100) + 50; i++) {

			index_1 = losowa.nextInt(karty.size() - 1);
			index_2 = losowa.nextInt(karty.size() - 1);

			pomocnicza = karty.get(index_2);
			karty.set(index_2, karty.get(index_1));
			karty.set(index_1, pomocnicza);

		}
	}

	public Karta wezKarte() {
		return karty.remove(0);
	}

	public Karta wymien(Karta k) {
		karty.add(k);
		return karty.remove(0);
	}
}
