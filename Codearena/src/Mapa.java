import java.util.HashMap;
import java.util.Map;

public class Mapa {
	
	private static Mapa instance = null;
	
    public static Mapa getInstance() {
        if (instance == null) {
            instance = new Mapa();
        }
        return instance;
    }
	
	Map<Wspolrzedne, Pole> mapa = new HashMap<Wspolrzedne, Pole>();

	public boolean contains(Wspolrzedne w) {
		return mapa.containsKey(w);
	}

	public void add(Wspolrzedne w, Pole p) {
		mapa.put(w, p);
	}

	public Pole getValue(Wspolrzedne key) {
		return mapa.get(key);
	}

	public void setValue(Wspolrzedne key, Pole value) {
		if (mapa.containsKey(key)) {
			mapa.remove(key);
			mapa.put(key, value);
		}
	}

}
