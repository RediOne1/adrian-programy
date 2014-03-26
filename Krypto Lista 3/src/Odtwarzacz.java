import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Scanner;

import org.bouncycastle.crypto.params.KeyParameter;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Odtwarzacz {
	/**
	 * keytool -genseckey -alias "audioKey" -keystore AUDIOKEYSTORE.jks
	 * -storepass "audio6030" -storetype "JCEKS" -keyalg AES -keysize 128
	 * -keypass "audio6030"
	 */
	public static void sound(InputStream... args) throws Exception {
		InputStream in = new FileInputStream(new File("imperial_march.wav"));
		if (args.length != 0)
			in = args[0];
		try {
			AudioStream as = new AudioStream(in);
			AudioPlayer.player.start(as);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static InputStream compress(InputStream in) throws IOException {
		final int BUFFER = 2048;
		byte buffer[] = new byte[BUFFER];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		out.close();
		return new ByteArrayInputStream(out.toByteArray());
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
				sb.append(line);
			}
			wynik = sb.toString();
		} catch (Exception e) {
			System.out.println(e);
			if (args.length != 0)
				wynik = args[0];
		}
		return wynik;
	}

	static byte[] keyBytes = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
			(byte) 0xcd, (byte) 0xef };
	KeyParameter keyParam = new KeyParameter(keyBytes);
	public static void main(String args[]) {
		try {
			String config[] = odczytaj("config.txt").split("/");
			final KeyStore keyStore = KeyStore.getInstance("JCEKS");
			keyStore.load(new FileInputStream(new File(config[0])),
					config[2].toCharArray());
			Key key = keyStore.getKey("audioKey", config[2].toCharArray());
			CrypterAES crypter = new CrypterAES("AES/ECB/PKCS5Padding", null,
					null);
			sound(compress(crypter.decryptAudio("zaszyfrowana_piosenka.adi",
					key)));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
