package technologia.android.pokermobile;

import java.util.Arrays;

import android.util.Log;
import android.widget.Toast;

public abstract class Gracz {

	public Karta karta[] = new Karta[5];
	public Licytacja l;
	public int StanKonta;
	public String pseudonim;
	public int ile_postawil;
	public int index;
	public boolean czy_gra = true;
	public Talia t;
	public GameActivity context;
	public int doWymiany[] = new int[5];
	public int ileDoWymiany = 0;

	public void wezKarte(Talia t) {
		this.t = t;
		for (int i = 0; i < 5; i++) {
			karta[i] = t.wezKarte();
		}
		karta = uloz_karty(karta);
	}

	public Karta[] uloz_karty(Karta karta[]) {
		Komparator komp = new Komparator();
		Arrays.sort(karta, komp);
		return karta;
	}

	public abstract void Licytuj(GameActivity _context);

	public abstract void Wymiana(GameActivity _context);

	public abstract void Obstaw(GameActivity _context, int _ile);

	public void wymien(GameActivity _context) {
		context = _context;
		for (int i = 0; i < 5; i++) {
			if (doWymiany[i] == 1) {
				doWymiany[i]--;
				karta[i] = context.t.wymien(karta[i]);
			}
		}
		ileDoWymiany = 0;
		karta = uloz_karty(karta);
	}

	public abstract String toString();
}
