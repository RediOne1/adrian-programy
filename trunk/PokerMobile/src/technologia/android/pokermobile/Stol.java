package technologia.android.pokermobile;
public class Stol {

	private static final String figury[] = { "Brak_figur", "Para", "Dwie_pary",
			"Trojka", "Strit", "Kolor", "Ful", "Czworka", "Poker" };

	private static final int Poker = 8;
	private static final int Czworka = 7;
	private static final int Ful = 6;
	private static final int Kolor = 5;
	private static final int Strit = 4;
	private static final int Trojka = 3;
	private static final int Dwie_pary = 2;
	private static final int Para = 1;
	private static final int Brak_figur = 0;
	Figury f;

	public Gracz gracz[];

	Stol(Gracz gracz[]) {
		f = new Figury();
		this.gracz = gracz;
	}
	private int ktoWygral(int PosiadaneFigury[][], int wskaznik, int gracze[]) {
		int max = -1, temp, wygral = 0;
		for (int i = 0; i < gracze.length; i++) {
			if (gracze[i] == 1) {
				temp = PosiadaneFigury[i][wskaznik];
				if (temp > max)
					max = temp;
			}
		}
		int licznik = 0;
		int ktoWygral[] = new int[gracze.length];
		for (int i = 0; i < gracze.length; i++) {
			if (PosiadaneFigury[i][wskaznik] == max) {
				ktoWygral[i]++;
				wygral = i;
				licznik++;
			}
		}
		if (licznik == 1) {
			return wygral;
		} else {
			return -1;
		}
	}

	public int wynik() {		
		int PosiadaneFigury[][] = new int[gracz.length][6];
		int najwyzsza_figura = 0;
		int wygrywa = 0;
		for (int i = 0; i < gracz.length; i++) {
			PosiadaneFigury[i] = f.wyszukajFigure(gracz[i].karta);
			if(!gracz[i].czy_gra){
				PosiadaneFigury[i][0]=0;
				PosiadaneFigury[i][5]=0;
			}
			if (PosiadaneFigury[i][0] > najwyzsza_figura) {
				najwyzsza_figura = PosiadaneFigury[i][0];
				wygrywa = i;
			}

		}
		int gracze_z_ta_sama_figura[] = new int[gracz.length];
		int licznik = 0;
		for (int i = 0; i < gracz.length; i++) {
			if (PosiadaneFigury[i][0] == najwyzsza_figura) {
				gracze_z_ta_sama_figura[i]++;
				licznik++;
			}
		}
		if (licznik == 1) {
			return wygrywa;
		} else {
			switch (najwyzsza_figura) {
			case Poker:
				wygrywa = ktoWygral(PosiadaneFigury, 1, gracze_z_ta_sama_figura);
				break;
			case Czworka:
				wygrywa = ktoWygral(PosiadaneFigury, 2, gracze_z_ta_sama_figura);
				break;
			case Ful:
				wygrywa = ktoWygral(PosiadaneFigury, 1, gracze_z_ta_sama_figura);
				break;
			case Kolor:
				wygrywa = ktoWygral(PosiadaneFigury, 5, gracze_z_ta_sama_figura);
				break;
			case Strit:
				wygrywa = 	ktoWygral(PosiadaneFigury, 1, gracze_z_ta_sama_figura);
				break;
			case Trojka:
				wygrywa = ktoWygral(PosiadaneFigury, 1, gracze_z_ta_sama_figura);
				break;
			case Dwie_pary:
				wygrywa = ktoWygral(PosiadaneFigury, 1, gracze_z_ta_sama_figura);
				break;
			case Para:
				wygrywa = ktoWygral(PosiadaneFigury, 1, gracze_z_ta_sama_figura);
				break;
			case Brak_figur:
				wygrywa = ktoWygral(PosiadaneFigury, 5, gracze_z_ta_sama_figura);
				break;
			}
		}
		return wygrywa;
	}
}
