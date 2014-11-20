import java.util.List;
import java.util.LinkedList;

public class Main {
	private static int n, poziom = 0;
	private static List<Integer> wykorzystane, wynik, zbior1, zbior2;

	private static void wypisz(int tab[]) {
		zbior1 = new LinkedList<Integer>();
		zbior2 = new LinkedList<Integer>();
		for (int i = 0; i < tab.length; i++) {
			if (wykorzystane.contains(tab[i]))
				zbior1.add(tab[i]);
			else
				zbior2.add(tab[i]);
		}
		System.out.print(" { ");
		for (int z : zbior1)
			System.out.print(z
					+ (zbior1.indexOf(z) == zbior1.size() - 1 ? " }" : ", "));
		System.out.print("     { ");
		for (int z : zbior2)
			System.out.print(z
					+ (zbior2.indexOf(z) == zbior2.size() - 1 ? " }\n" : ", "));

	}

	private static void permutacja(int ile, int tab[]) {
		if (poziom == ile) {
			wypisz(tab);
			return;
		}
		int i;
		for (i = 0; i < n; i++) {
			if (!wykorzystane.contains(tab[i])) {
				if (wykorzystane.size() > 0 && wykorzystane.get(0) > tab[i])
					continue;
				poziom++;
				wynik.add(tab[i]);
				wykorzystane.add(tab[i]);
				permutacja(ile, tab);
				wykorzystane.remove(wykorzystane.size() - 1);
				wynik.remove(wynik.size() - 1);
				poziom--;
			}
		}
	}

	public static void main(String[] args) {
		int tab[] = { 1, 2, 3, 4, 5, 6 };
		wykorzystane = new LinkedList<Integer>();
		wynik = new LinkedList<Integer>();
		n = tab.length;
		System.out.println("Zbior 1 | Zbior 2");
		for (int i = 1; i <= tab.length / 2; i++)
			permutacja(i, tab);
	}

}
