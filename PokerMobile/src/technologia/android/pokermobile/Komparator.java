package technologia.android.pokermobile;
import java.util.Comparator;

public class Komparator implements Comparator<Karta> {

	@Override
	public int compare(Karta k1, Karta k2) {
		if (k2 == null)
			return -1;
		if (k1.getLiczba() > k2.getLiczba())
			return 1;
		else if (k1.getLiczba() < k2.getLiczba())
			return -1;
		else
			return 0;
	}
}
