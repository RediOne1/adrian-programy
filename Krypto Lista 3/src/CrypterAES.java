import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * keytool -genseckey -alias "myKey" -keystore KEYSTORE.jks -storepass
 * "password" -storetype "JCEKS" -keyalg AES -keysize 128 -keypass "password"
 */

public class CrypterAES {
	public CrypterAES(String _tryb_szyfrowania, String _sciezka,
			String _id_klucza) {
		this.tryb_szyfrowania = _tryb_szyfrowania;
		this.sciezka = _sciezka;
		this.id_klucza = _id_klucza;
	}

	public static Key key = null;
	private static final String HASLO_JKS = "password";
	private static String tryb_szyfrowania;
	private static String sciezka;
	private static String id_klucza;
	private static final int ENCRYPT = Cipher.ENCRYPT_MODE;
	private static final int DECRYPT = Cipher.DECRYPT_MODE;
	static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	static IvParameterSpec ivspec = new IvParameterSpec(iv);

	public static void Crypt(String source, String destination, int mode)
			throws Exception {
		FileInputStream fis = new FileInputStream(new File(source));
		FileOutputStream fos = new FileOutputStream(new File(destination));
		Cipher cipher = Cipher.getInstance(tryb_szyfrowania);
		if (tryb_szyfrowania.split("/")[1].equals("ECB"))
			cipher.init(mode, key);
		else
			cipher.init(mode, key, ivspec);
		if (mode == ENCRYPT) {
			CipherOutputStream cout = new CipherOutputStream(fos, cipher);
			byte[] buf = new byte[1024];
			int read;
			while ((read = fis.read(buf)) != -1)
				cout.write(buf, 0, read);
			fis.close();
			cout.flush();
			cout.close();
		} else {
			CipherInputStream cin = new CipherInputStream(fis, cipher);
			byte[] buf = new byte[1024];
			int read = 0;
			while ((read = cin.read(buf)) != -1)
				fos.write(buf, 0, read);
			cin.close();
			fos.flush();
			fos.close();
		}
	}

	public CipherInputStream decryptAudio(String source, Key key2)
			throws Exception {
		FileInputStream fis = new FileInputStream(new File(source));
		Cipher cipher = Cipher.getInstance(tryb_szyfrowania);
		if (tryb_szyfrowania.split("/")[1].equals("ECB"))
			cipher.init(Cipher.DECRYPT_MODE, key2);
		else
			cipher.init(Cipher.DECRYPT_MODE, key2, ivspec);
		CipherInputStream cin = new CipherInputStream(fis, cipher);
		return cin;

	}
}
