import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class Main {

	public static Strona strona;
	public static void main(String[] args) {
		strona = new Strona();
		String podpis = getSign(getFileHash());

		if (Strona.sprawdz(podpis) == true)
			System.out.println("Podpis poprawny!");
		else
			System.out.println("Podpis niepoprawny!");

		System.out.println("Koniec");

	}

	public static String getFileHash() {

		byte[] mdBytes = new byte[1024];
		byte[] dataBytes = new byte[1024];

		int nread = 0;

		StringBuffer hexString = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream("plik.txt");
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			while ((nread = fis.read(dataBytes)) != -1)
				md.update(dataBytes, 0, nread);

			mdBytes = md.digest();

			String hex;
			for (int i = 0; i < mdBytes.length; i++) {
				hex = Integer.toHexString(0xff & mdBytes[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		strona.hash = hexString.toString();
		return hexString.toString();
	}

	private static String getSign(String hash) {

		String https_url = "https://beacon.nist.gov/rest/record/last";
		URL url;
		String podpis = null;
		try {

			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			// dump all the content
			String content = strona.getContent(con);
			strona.time = strona.getTime(content);
			podpis = hash + strona.time + strona.getSeed(content);

			System.out.println("ts = [" + podpis + ", " + strona.time + "]");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return podpis;
	}
}
