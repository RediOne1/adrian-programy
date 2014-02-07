package technologia.android.pokermobile;


public class Karta {
	public String kolory[] = { "kier", "karo", "pik", "trefl" };
	public String liczby[] = { "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"J", "D", "K", "A" };

	public int kolor, liczba;

	Karta(int kolor, int liczba) {
		this.kolor = kolor;
		this.liczba = liczba;
	}

	public @Override
	String toString() {
		return liczby[liczba] + "_" + kolory[kolor];
	}

	public int getLiczba() {
		return liczba;
	}

	public int getKolor() {
		return kolor;
	}
}
