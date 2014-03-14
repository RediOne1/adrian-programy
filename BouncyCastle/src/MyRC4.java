import java.security.Key;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

public class MyRC4 {
	public static Key key = null;
	public static StreamCipher rc4;
	public static KeyParameter keyParam;

	public MyRC4(Key _key) {
		this.key = _key;
		rc4 = new RC4Engine();
		keyParam = new KeyParameter(key.getEncoded());
	}

	static byte[] forCrypt(boolean forEncrypt, byte[] text){
		byte[] wynik = new byte[text.length];
		rc4.init(forEncrypt, keyParam);
		rc4.processBytes(text, 0, text.length, wynik, 0);
		return wynik;		
	}
	static byte[] encrypt(byte[] cleartext) {
		rc4.init(true, keyParam);
		byte[] ciphertext = new byte[cleartext.length];
		rc4.processBytes(cleartext, 0, cleartext.length, ciphertext, 0);
		return ciphertext;
	}

	static byte[] decrypt(byte[] ciphertext1) {
		byte[] odszyfrowany = new byte[ciphertext1.length];
		rc4.init(false, keyParam);
		for (int i = 0; i < ciphertext1.length; i++) {
			odszyfrowany[i] = rc4.returnByte(ciphertext1[i]);
		}
		return odszyfrowany;
	}
}
