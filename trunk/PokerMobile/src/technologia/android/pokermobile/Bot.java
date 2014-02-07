package technologia.android.pokermobile;

import android.util.Log;

public class Bot extends Gracz {

	private static final int Poker = 8;
	private static final int Czworka = 7;
	private static final int Ful = 6;
	private static final int Kolor = 5;
	private static final int Strit = 4;
	private static final int Trojka = 3;
	private static final int Dwie_pary = 2;
	private static final int Para = 1;
	private static final int Brak_figur = 0;
	private int wartosci[];
	Figury f;

	protected GameActivity context;

	Bot(GameActivity _context, int _index) {
		f = new Figury();
		index = _index;
		context = _context;
		wezKarte(context.t);
		pseudonim = "Bot" + index;

	}

	public void Wymiana(GameActivity _context) {
		context = _context;
		t = context.t;
		wymienKarty();
		context.gra();

	}

	public String toString() {
		return "Bot";
	}

	@Override
	public void Licytuj(GameActivity _context) {
		context = _context;
		l = context.l;
		wartosci = f.wyszukajFigure(karta);
		int stawka = l.minimalna_stawka;

		if (wartosci[0] <= 3) {
			if (l.licytacja == l.allin) {
				fold();
				return;
			} else if (StanKonta < l.minimalna_stawka) {
				l.licytacja = l.allin;
				fold();
				return;
			}
		} else {
			if (l.licytacja == l.allin) {
				stawka = StanKonta;
				Obstaw(context, stawka);
				return;
			}
		}
		switch (wartosci[0]) {
		case Poker:
			if (l.minimalna_stawka < (StanKonta * 0.80))
				stawka = (int) (StanKonta * 0.80);
			else
				stawka = l.minimalna_stawka;
			break;
		case Czworka:
			if (l.minimalna_stawka < (StanKonta * 0.75))
				stawka = (int) (StanKonta * 0.75);
			else
				stawka = l.minimalna_stawka;
			break;
		case Ful:
			if (l.minimalna_stawka < (StanKonta * 0.70))
				stawka = (int) (StanKonta * 0.70);
			else
				stawka = l.minimalna_stawka;
			break;
		case Kolor:
			if (l.minimalna_stawka < (StanKonta * 0.65))
				stawka = (int) (StanKonta * 0.65);
			else if (l.minimalna_stawka > (StanKonta * 0.90)) {
				fold();
				return;
			} else
				stawka = l.minimalna_stawka;
			break;
		case Strit:
			if (l.minimalna_stawka < (StanKonta * 0.65))
				stawka = (int) (StanKonta * 0.65);
			else if (l.minimalna_stawka > (StanKonta * 0.90)) {
				fold();
				return;
			} else
				stawka = l.minimalna_stawka;
			break;
		case Trojka:
			if (l.minimalna_stawka < (StanKonta * 0.45))
				stawka = (int) (StanKonta * 0.45);
			else if (l.minimalna_stawka > (StanKonta * 0.70)) {
				fold();
				return;
			} else
				stawka = l.minimalna_stawka;
			break;
		case Dwie_pary:
			if (l.minimalna_stawka < (StanKonta * 0.30))
				stawka = (int) (StanKonta * 0.30);
			else if (l.minimalna_stawka > (StanKonta * 0.55)) {
				fold();
				return;
			} else
				stawka = l.minimalna_stawka;
			break;
		case Para:
			if (l.minimalna_stawka < (StanKonta * 0.15))
				stawka = (int) (StanKonta * 0.15);
			else if (l.minimalna_stawka > (StanKonta * 0.40)) {
				fold();
				return;
			} else
				stawka = l.minimalna_stawka;
			break;
		case Brak_figur:
			if (l.minimalna_stawka > (StanKonta * 0.30)) {
				fold();
				return;
			} else
				stawka = l.minimalna_stawka;
			break;
		}

		Log.d("test", pseudonim + " postawi³: " + stawka);
		Obstaw(context, stawka);
		context.gra();

	}

	public void fold() {
		czy_gra = false;
		Log.d("test", pseudonim + " podda³ siê");
		l.ilosc_licytujacych--;
		context.gra();
	}

	public void Obstaw(GameActivity _context, int _ile) {
		l.pula += _ile - ile_postawil;
		ile_postawil = _ile;
		l.minimalna_stawka = _ile;
		if(l.licytacja == l.bet)
			l.licytacja = l.raise;
		l.gracz_najwyzej = index;

	}

	public void wymienKarty() {

		// sprawdza co ma
		int ileWiekszych = 0;
		wartosci = f.wyszukajFigure(karta);

		switch (wartosci[0]) {
		case Poker:
			// nic
			break;
		case Czworka:
			// nic
			break;
		case Ful:
			// nic
			break;
		case Kolor:
			// nic
			break;
		case Strit:
			// nic
			break;
		case Trojka:
			for (int i = 0; i < 5; i++) {
				if ((karta[i].liczba == wartosci[2])
						|| (karta[i].liczba == wartosci[3])) {
					karta[i] = t.wymien(karta[i]);
				}
			}
			break;
		case Dwie_pary:
			for (int i = 0; i < 5; i++) {
				if (karta[i].liczba == wartosci[3]) {
					karta[i] = t.wymien(karta[i]);
				}
			}
			break;
		case Para:
			for (int i = 0; i < 5; i++) {
				if (karta[i].liczba != wartosci[1]) {
					karta[i] = t.wymien(karta[i]);
				}
			}
			break;
		case Brak_figur:
			for (int i = 0; i < 5; i++) {
				if (karta[i].liczba > 8)
					ileWiekszych++;
			}
			if (ileWiekszych > 3) { // jesli wiekszych niz 10 jest 4 lub 5 to
									// wymien 3 najmniejsze
				for (int i = 0; i < 5; i++) {
					if ((karta[i].liczba == wartosci[1])
							|| (karta[i].liczba == wartosci[2])
							|| (karta[i].liczba == wartosci[3])) {
						karta[i] = t.wymien(karta[i]);
					}
				}
			} // >= 3 to wynien tylko mniejsze od waleta
			else {
				int licznik = 0;
				for (int i = 0; i < 5; i++) {
					if (karta[i].liczba < 9) {
						karta[i] = t.wymien(karta[i]);
						licznik++;
					}
					if (licznik == 4)
						break;
				}
			}
			break;
		}

		karta = uloz_karty(karta);
	}
}
