public class Liczba {
	private int dziesietny;
	private String znaki[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "a", "b", "c", "d", "e", "f" };
	private boolean zamieniono = false;

	Liczba() {

	}

	Liczba(int dziesietny) {
		this.dziesietny = dziesietny;
	}

	public int getDziesietny() {
		return this.dziesietny;
	}
	public boolean zamieniono(){
		return this.zamieniono;
	}

	public String Zamiana(int podstawa) {
		zamieniono = false;
		String wynik = "";
		int reszta[] = new int[20];
		int m = dziesietny;
		int x = podstawa;
		int i = 0;
		while (m != 0) {
			reszta[i] = m % x;
			m /= x;
			i++;
		}
		for (int j = i - 1; j >= 0; j--) {
			wynik += znaki[reszta[j]];
		}
		zamieniono = true;
		return wynik;
	}
}
