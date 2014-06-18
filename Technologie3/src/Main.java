import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static String flaga = "01111110";
	public static int dlugosc = 0;

	public static String wczytajPlik(String plik) throws IOException {
		String wynik = new String();
		String temp = new String();

		File inputFile = new File(plik);

		try {
			// Wczytuję dane wejściowe
			BufferedReader reader = new BufferedReader(
					new FileReader(inputFile));

			// wczytaj całą zawartość inputFile do zmiennej data
			while ((temp = reader.readLine()) != null) {
				wynik += temp;
				dlugosc = temp.length();

			}

			reader.close();

		} catch (FileNotFoundException e) {
			System.out.println("Nie znaleziono pliku");
			System.exit(0);
		}

		// sprawdzaj czy wczytano dane

		if (wynik.isEmpty()) {
			System.out.println("Plik źródłowy jest pusty");
			System.exit(0);
		}
		return wynik;
	}

	public static void zapisz(String source, String destination)
			throws IOException {
		File file = new File(destination);
		try {
			file.delete();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(source);
			writer.close();

		} catch (FileNotFoundException e) {
			System.out.println("Nie odnaleziono pliku " + destination);
			System.exit(0);
		}
	}

	public static String code(String source) {
		int i = 0;
		String piece, crc, wynik = "";
		while (i < source.length()) {
			if (source.length() - i > dlugosc)
				piece = source.substring(i, i + dlugosc);
			else
				piece = source.substring(i, source.length());
			
			crc = check_CRC(piece);

			piece += crc;

			piece = piece.replace("11111", "111110");

			piece = flaga + piece + flaga;

			i += dlugosc;
			wynik += piece;
		}
		return wynik;
	}
	public static String decode(String source){
		String wynik="";
        
        source = source.replace(flaga+flaga, " ");
        source = source.replace(flaga, "");
        
        String data[] = source.split(" ");
        
        for (int i=0; i<data.length; i++) {
            data[i] = data[i].replace("111110", "11111");
        }

        for (String s: data) {
            String crc = s.substring(s.length()-8);
            String frame = s.substring(0, s.length()-8);
            if (crc.equals(check_CRC(frame))) {
                wynik += frame;
            } else {
                System.out.println("Ramka: "+ frame+ " posiada nieprawidłowy crc");
            }
        }               
		return wynik;
	}

	public static void main(String... args) throws IOException {

		if (args.length > 0) {
			if (args[0].equals("code")) {
				String temp = wczytajPlik("Z.txt");
				zapisz(code(temp), "W.txt");
			} else if (args[0].equals("decode")) {
				String temp = wczytajPlik("W.txt");
				zapisz(decode(temp), "Z.txt");
			}
		}
	}

	static public String check_CRC(String input) {
		StringBuffer frame = new StringBuffer();
		frame.append(input);
		String generator = "10101010";

		frame.append("00000000");

		for (int i = 0; i < frame.length() - 8; i++) {

			String s = frame.substring(i, i + 8);
			String o = "";
			if (s.startsWith("1")) {

				for (int j = 0; j < generator.length(); j++) {
					o += XOR(s.charAt(j), generator.charAt(j));
				}

				frame.replace(i, i + 8, o);
			}
		}
		return frame.toString().substring(frame.length() - 8);
	}

	public static String XOR(char a, char b) {
		return a == b ? "0" : "1";
	}

}