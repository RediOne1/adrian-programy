import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Kryptogram {

	public String dane;

	public List<Znak> znaki;

	public Kryptogram(String plik) throws FileNotFoundException {
		znaki = new ArrayList<Znak>();
		File file = new File(plik);
		Scanner in = new Scanner(file);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while (in.hasNextLine()) {
			line = in.nextLine();
			sb.append(line + " ");
		}
		dane = sb.toString();
		String temp[] = dane.split(" ");
		for (String s : temp) {
			znaki.add(new Znak(s));
		}
	}

	@Override
	public String toString() {
		String wynik = "";
		for (int i = 0; i < znaki.size(); i++) {
			wynik += znaki.get(i);
		}
		return wynik;
	}
}
