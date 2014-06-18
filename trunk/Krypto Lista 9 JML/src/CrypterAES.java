import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.security.Key;
import java.security.KeyStore;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

/**
 * keytool -genseckey -alias "myKey" -keystore KEYSTORE.jks -storepass
 * "password" -storetype "JCEKS" -keyalg AES -keysize 128 -keypass "password"
 */

//javadoc -private -use assignable -use requires -use ensures plik.java -d doc

public class CrypterAES {

	public static/* @ spec_public @ */Key key = null;
	public static/* @ spec_public @ */int n = 1;
	private static/* @ spec_public @ */final String HASLO_JKS = "password";
	private static/* @ spec_public @ */String tryb_szyfrowania;
	private static/* @ spec_public @ */String sciezka_plik;
	private static/* @ spec_public @ */String id_klucza;
	private static/* @ spec_public @ */final int ENCRYPT = Cipher.ENCRYPT_MODE;
	private static/* @ spec_public @ */final int DECRYPT = Cipher.DECRYPT_MODE;
	static/* @ spec_public @ */byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0 };
	static/* @ spec_public @ */IvParameterSpec ivspec = new IvParameterSpec(iv);


 	//@ assignable mode;
    //@ requires mode == ENCRYPT || mode == DECRYPT
	public static void Crypt(String source, String destination, int mode)
			throws Exception {
		FileInputStream fis = new FileInputStream(new File(source));
		BufferedReader reader = new BufferedReader(new FileReader(source));
		FileOutputStream fos = new FileOutputStream(new File(destination));
		Cipher cipher = Cipher.getInstance(tryb_szyfrowania);
		if (tryb_szyfrowania.split("/")[1].equals("ECB"))
			cipher.init(mode, key);
		else
			cipher.init(mode, key, ivspec);
		if (mode == ENCRYPT) {
			CipherOutputStream cout = new CipherOutputStream(fos, cipher);
			String temp;
			// @ ensures wczyta n linii.			
			while (((temp = reader.readLine() + "\n") != null) && n > 0) {
				cout.write(temp.getBytes());
				n--;
			}
			cout.flush();
			cout.close();
		} else {
			CipherInputStream cin = new CipherInputStream(fis, cipher);
			byte[] buf = new byte[1024];
			int read = 0;
			while ((read = cin.read(buf)) != -1)
				fos.write(buf, 0, read);
			cin.close();
		}
		reader.close();

	}
	//@ assignable mode;
	//@ assignable n;
	//@ assignable tryb_szyfrowania;
	//@ assignable id_klucza;
	//@ assignable sciezka_plik;
	//@ assignable key;
    //@ requires args.length() == 4
	//@ ensures HASLO = password
	//@ ensures n > 0
	public static void main(String[] args) throws Exception {
		// @ ensures typ == ECB || typ==CTR || typ==OFB;
		tryb_szyfrowania = "AES/" + args[0] + "/PKCS5Padding";
		String sciezka_keystore = args[1];
		id_klucza = args[2];
		sciezka_plik = args[3];
		System.out.print("Podaj hasło klucza: ");
		String HASLO;
		Scanner odczyt = new Scanner(System.in);
		HASLO = odczyt.nextLine();
		System.out.print("Ile wczytać lini? ");
		n = odczyt.nextInt();
		odczyt.close();
		final KeyStore keyStore = KeyStore.getInstance("JCEKS");
		keyStore.load(new FileInputStream(new File(sciezka_keystore)),
				HASLO_JKS.toCharArray());
		key = keyStore.getKey(id_klucza, HASLO.toCharArray());
		CrypterAES.Crypt(sciezka_plik, sciezka_plik + ".enc", ENCRYPT);
		CrypterAES.Crypt(sciezka_plik + ".enc", "Dec_" + sciezka_plik, DECRYPT);
	}
}
