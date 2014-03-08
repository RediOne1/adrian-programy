import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

public class Sabre20090709 {
	static byte[] encrypt(byte[] cleartext) {
		byte[] keyBytes = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
				(byte) 0xcd, (byte) 0xef };
		StreamCipher rc4 = new RC4Engine();
		KeyParameter keyParam = new KeyParameter(keyBytes);
		rc4.init(true, keyParam);
		byte[] ciphertext = new byte[cleartext.length];
		rc4.processBytes(cleartext, 0, cleartext.length, ciphertext, 0);
		return ciphertext;
	}

	static byte[] decrypt(byte[] cipertext) {

		return null;
	}

	public static void print(String title, byte[] bytes) {
		System.out.printf("%20s : ", title);
		for (byte b : bytes) {
			System.out.printf("%02x ", b & 0xff);
		}
		System.out.println();

	}

	public static void main(String[] args) throws Exception {

		byte[] cleartext1 = args[0].getBytes("utf-8");
		byte[] ciphertext1 = encrypt(cleartext1);
		print("Cleartext 1", cleartext1);
		print("Ciphertext 1", ciphertext1);

		/*byte[] keyBytes = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
				(byte) 0xcd, (byte) 0xef };
		StreamCipher rc4 = new RC4Engine();
		KeyParameter keyParam = new KeyParameter(keyBytes);
		rc4.init(true, keyParam);
		for (int i = 0; i < ciphertext1.length; i++) {
			System.out.printf("%x ", rc4.returnByte(ciphertext1[i]));
		}

		/*
		 * byte[] cleartext2 =
		 * "Now is the time for all good men to come to the aid of the parts."
		 * .getBytes("utf-8"); byte[] ciphertext2 = encrypt(cleartext2);
		 * print("Ciphertext 2", ciphertext2);
		 * 
		 * int n = Math.min(ciphertext1.length, ciphertext2.length); byte[]
		 * xorOfCleartext = new byte[n]; for (int i = 0; i < n; i++) {
		 * xorOfCleartext[i] = (byte) (cleartext1[i] ^ cleartext2[i]);
		 * 
		 * }
		 * 
		 * print("xor cleartext", xorOfCleartext);
		 * 
		 * byte[] xorOfCiphertext = new byte[n];
		 * 
		 * for (int i = 0; i < n; i++)
		 * 
		 * {
		 * 
		 * xorOfCiphertext[i] = (byte) (ciphertext1[i] ^ ciphertext2[i]);
		 * 
		 * }
		 * 
		 * print("xor ciphertext", xorOfCleartext);
		 * 
		 * byte[] extreactedCleartext2 = new byte[n];
		 * 
		 * for (int i = 0; i < n; i++)
		 * 
		 * {
		 * 
		 * extreactedCleartext2[i] = (byte) (xorOfCiphertext[i] ^
		 * cleartext1[i]);
		 * 
		 * }
		 * 
		 * print("extracted cleartext2", extreactedCleartext2);
		 * 
		 * System.out.println(new String(extreactedCleartext2, "utf-8"));
		 */
	}

}