package technologia.android.pokermobile;

import android.os.Handler;
import android.widget.Toast;

public class Licytacja {
	public int licytacja = 0;
	public final int bet = 0;
	public final int raise = 1;
	public final int allin = 2;

	public int najwyzsza_stawka = 0;
	public int gracz_najwyzej;
	public int minimalna_stawka;
	public int pula = 0;
	public int ilosc_licytujacych;
	protected GameActivity context;
	private Handler mHandler = new Handler();

	Licytacja(int wpisowe, int ilu_licytujacych) {
		minimalna_stawka = wpisowe;
		ilosc_licytujacych = ilu_licytujacych;
	}
}
