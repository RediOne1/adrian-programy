import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Scanner;

import javax.crypto.Cipher;

public class Main {

	private static final String HASLO_JKS = "password";
	private static String tryb_szyfrowania;
	private static String sciezka;
	private static String id_klucza;
	private static final int ENCRYPT = Cipher.ENCRYPT_MODE;
	private static final int DECRYPT = Cipher.DECRYPT_MODE;

	public static void main(String[] args) throws Exception {
		tryb_szyfrowania = args[0];
		sciezka = args[1];
		id_klucza = args[2];
		CrypterAES crypter = new CrypterAES(tryb_szyfrowania, sciezka,
				id_klucza);
		String sciezka2 = "KEYSTORE.jks";
		System.out.print("Podaj hasło klucza: ");
		String HASLO;
		Scanner odczyt = new Scanner(System.in);
		HASLO = odczyt.nextLine();
		odczyt.close();
		final KeyStore keyStore = KeyStore.getInstance("JCEKS");
		keyStore.load(new FileInputStream(new File(sciezka2)),
				HASLO_JKS.toCharArray());
		crypter.key = keyStore.getKey(id_klucza, HASLO.toCharArray());
		crypter.Crypt(sciezka, sciezka + ".enc", ENCRYPT);
		crypter.Crypt(sciezka + ".enc", "Dec_" + sciezka, DECRYPT);
	}
}
