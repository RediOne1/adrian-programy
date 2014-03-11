import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.security.KeyStore;
import java.util.Scanner;

public class Sabre20090709 {

	
	/**
	 * keytool -genseckey -alias "myKey" -keystore KEYSTORE.jks -storepass "password" -storetype "JCEKS" -keyalg AES -keysize 128
	 */
	private static String plik = null;

	private static void print(String title, byte[] bytes) {
		System.out.printf("%20s : ", title);
		for (byte b : bytes) {
			System.out.printf("%02x ", b);
		}
		System.out.println();
	}

	public static void main(String[] args) throws Exception {
		String dane = odczytaj(args);
		
		final KeyStore keyStore = KeyStore.getInstance("JCEKS");
		keyStore.load(new FileInputStream(new File("C:\\Users\\Adrian\\KEYSTORE.jks")), "password".toCharArray());
		final Key key = keyStore.getKey("myKey", "password".toCharArray());		
		MyRC4 mrc4 = new MyRC4(key);
		byte[] cleartext1 = dane.getBytes("utf-8");
		byte[] ciphertext1 = mrc4.encrypt(cleartext1);
		byte[] odszyfrowany = mrc4.decrypt(ciphertext1);
		print("key", key.getEncoded());
		print("Cleartext 1", cleartext1);
		print("Ciphertext 1", ciphertext1);
		print("Odszyfrowany", odszyfrowany);
		if (plik != null)
			zapisz(dane, cleartext1);
	}

	private static String odczytaj(String... args) {
		String wynik = "";
		try {
			File file = new File(args[0]);
			Scanner in = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while (in.hasNextLine()) {
				line = in.nextLine();
				sb.append(line + "\n");
			}
			wynik = sb.toString();
			plik = args[0];
		} catch (Exception e) {
			System.out.println(e);
			if (args.length != 0)
				wynik = args[0];
		}
		return wynik;
	}

	private static void zapisz(String oldData, byte... args) {
		try {
			PrintWriter zapis = new PrintWriter(plik);
			zapis.println("Dane wejœciowe: " + oldData);
			zapis.print("Zaszyfrowane: ");
			for (byte b : args) {
				zapis.printf("%02x ", b);
			}
			zapis.close();
		} catch (Exception e) {
		}
	}
}