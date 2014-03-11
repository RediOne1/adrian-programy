import java.security.Key;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

public class MyRC4 {
	public static byte[] keyBytes = { 0b00001, 0x23, 0x45, 0x67, (byte) 0x89,
			(byte) 0xab, (byte) 0xcd, (byte) 0xef };

	public static Key key = null;

	public MyRC4(Key _key) {
		this.key = _key;
	}

	static byte[] encrypt(byte[] cleartext) {
		StreamCipher rc4 = new RC4Engine();
		KeyParameter keyParam = new KeyParameter(keyBytes);
		rc4.init(true, keyParam);
		byte[] ciphertext = new byte[cleartext.length];
		rc4.processBytes(cleartext, 0, cleartext.length, ciphertext, 0);
		return ciphertext;
	}

	public static byte[] decrypt(byte[] ciphertext1) {
		byte[] odszyfrowany = new byte[ciphertext1.length];
		StreamCipher rc4 = new RC4Engine();
		KeyParameter keyParam = new KeyParameter(keyBytes);
		rc4.init(true, keyParam);
		for (int i = 0; i < ciphertext1.length; i++) {
			odszyfrowany[i] = rc4.returnByte(ciphertext1[i]);
		}
		return odszyfrowany;
	}
}
