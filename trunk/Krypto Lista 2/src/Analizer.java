import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Analizer {

	public static List<Kryptogram> kryptogramy;

	public static char klucze[] = new char[195];

	// kryptogramy.get(14) jest najdluzszy = 195 znakow

	public static void main(String[] args) {
		kryptogramy = new ArrayList<Kryptogram>();
		for (int i = 1; i <= 17; i++) {
			try {
				kryptogramy.add(new Kryptogram(i + ".txt"));
			} catch (Exception e) {

			}
		}
		System.out.println(kryptogramy.get(16).length());
		/*
		 * ZgodnaKoncowka zk = sprawdzKoncowki('.');
		 * xorujKryptogramyNaPozycji(zk.pozycja, zk.klucz);
		 */

		new test();
		/*
		 * for (char c = 'a'; c <= 'z'; c++) SzukajZnaku(c); for (char c = 'A';
		 * c <= 'Z'; c++) SzukajZnaku(c); SzukajZnaku('-'); SzukajZnaku(',');
		 * SzukajZnaku('!'); SzukajZnaku('('); SzukajZnaku(')');
		 * SzukajZnaku('?');
		 */
		try {
			test.start_test(kryptogramy);
		} catch (Exception e) {
		};
		//for (int i = 0; i < klucze.length; i++) {
		//	xorKryptogramy(i);
		//}
		//for (int i = 0; i < klucze.length; i++) {
		//	xorujKryptogramyNaPozycji(i, klucze[i]);
			//System.out.println(i + ".  " + klucze[i]);
		//}
		zapisz();
	}

	public static void xorKryptogramy(int pozycja) {
		for (Kryptogram k1 : kryptogramy) {
			int temp = 0;
			int index = 0;
			for (Kryptogram k2 : kryptogramy) {
				try {
					if (((k1.znaki.get(pozycja).znak_ascii ^ k2.znaki
							.get(pozycja).znak_ascii) <= 90)
							&& (k1.znaki.get(pozycja).znak_ascii ^ k2.znaki
									.get(pozycja).znak_ascii) >= 65) {
						temp++;
					}
					if (temp >= 12) {
						index++;
						klucze[pozycja] = (char) (k1.znaki.get(pozycja).znak_ascii ^ 32);
						break;
					}
				} catch (Exception e) {
					continue;
				}
			}
			if (index == 0) {
				//klucze[pozycja] = 32;
			}
		}
	}

	public static void SzukajZnaku(char znak) {
		char temp_klucz;
		boolean zly;
		int max_dlugosc = 0;
		int najdluzszy = 0;
		for (int i = 0; i < kryptogramy.size(); i++) {
			if (kryptogramy.get(i).znaki.size() > max_dlugosc) {
				max_dlugosc = kryptogramy.get(i).znaki.size();
				najdluzszy = i;
			}
		}
		for (int i = 0; i < max_dlugosc; i++) {
			zly = false;
			temp_klucz = (char) (znak ^ kryptogramy.get(najdluzszy).znaki
					.get(i).znak_ascii);
			for (Kryptogram k : kryptogramy) {
				if (i >= k.znaki.size())
					continue;
				char znak_na_pozycji = k.znaki.get(i).znak_ascii;
				if (!czyDobryZnak((char) (znak_na_pozycji ^ temp_klucz)))
					zly = true;
			}
			if (!zly) {
				// klucze[i] += temp_klucz + " ";
			}
		}
	}

	public static boolean czyDobryZnak(char znak) {
		if ((znak >= 'a' && znak <= 'z') || (znak >= 'A' && znak <= 'Z'))
			return true;
		else if (znak == ',' || znak == '(' || znak == ')' || znak == '!'
				|| znak == '?' || znak == '-' || znak == '.' || znak == ':')
			return true;
		return false;
	}

	public static ZgodnaKoncowka sprawdzKoncowki(char koncowka) {
		int i = 0;
		int x = 0;
		int y = 0;
		char t_klucz = 0;
		int pozycja = 0;
		for (Kryptogram k : kryptogramy) {
			int ilosc_zgodnych_koncowek = 0;
			i++;
			char last_znak = k.znaki.get(k.znaki.size() - 1).znak_ascii;
			char temp_klucz = (char) (last_znak ^ koncowka);
			for (Kryptogram k2 : kryptogramy) {
				char last_znak2 = k2.znaki.get(k2.znaki.size() - 1).znak_ascii;
				if (koncowka((char) (last_znak2 ^ temp_klucz)))
					ilosc_zgodnych_koncowek++;
			}
			if (ilosc_zgodnych_koncowek > y) {
				x = i;
				y = ilosc_zgodnych_koncowek;
				t_klucz = temp_klucz;
				pozycja = k.znaki.size() - 1;
			}
		}
		System.out.printf("Najbardziej zgodny klucz: %d, ilosc zgodnych %d \n",
				x, y);
		return new ZgodnaKoncowka(x, pozycja, t_klucz);
	}

	public static void print() {
		int i = 0;
		for (Kryptogram k : kryptogramy) {
			System.out.printf("Kryptogram %d: %s\n\n", ++i, k);
		}
	}

	public static void xorujKryptogramyNaPozycji(int pozycja, char klucz) {
		for (int i = 0; i < kryptogramy.size(); i++) {
			try {
				char temp = (char) (kryptogramy.get(i).znaki.get(pozycja).znak_ascii ^ klucz);
				kryptogramy.get(i).znaki.get(pozycja).znak_ascii = temp;
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static boolean koncowka(char c) {
		char endChar[] = { '.', '!', '?' };
		for (char x : endChar) {
			if (x == c)
				return true;
		}
		return false;
	}

	private static void zapisz() {
		try {
			PrintWriter zapis = new PrintWriter("wynik.txt");
			for (int i = 0; i < kryptogramy.size(); i++) {
				// zapis.println("hej");
				zapis.print("kryptogram " + (i + 1) + ": ");
				zapis.printf("%s\n\n", kryptogramy.get(i));
			}
			zapis.close();
		} catch (Exception e) {
		}
	}
}
