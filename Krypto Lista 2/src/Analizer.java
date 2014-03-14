import java.util.ArrayList;
import java.util.List;

public class Analizer {
	public static List<Kryptogram> kryptogramy;

	public static void main(String[] args) {
		kryptogramy = new ArrayList<Kryptogram>();
		for (int i = 1; i <= 16; i++) {
			try {
				kryptogramy.add(new Kryptogram(i + ".txt"));
			} catch (Exception e) {

			}
		}
		for (int i = 0; i < kryptogramy.size(); i++) {
			System.out.printf("Kryptogram %d: %s\n\n", i + 1, kryptogramy.get(i));
		}
	}
}
