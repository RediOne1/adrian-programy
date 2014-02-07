package technologia.android.pokermobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	public int etap = 0;
	private final int pobieranie_pseudonimu = 0;
	public final int I_licytacja = 1;
	public final int I_wymiana = 2;
	public final int II_licytacja = 3;
	private final int wynik = 4;
	public int index = 0;
	public int allin_index = 0;
	private int aktywni;

	private int ileCzlowiek = 0;
	private int ileBot = 0;
	private int iloscGraczy;
	private int ileZetonow = 0;
	private int ileWpisowe = 0;
	public TextView stanKonta;

	private final int Odpadasz_z_gry = 1;
	private final int FOLD_DIALOG = 2;

	public String komunikat;
	public RelativeLayout black;
	public RelativeLayout stol;
	public Talia t;
	private Animation fade_in;
	private Animation fade_out;
	public TextView polecenie;
	public TextView min_stawka;
	public TextView stawka_gracza;
	public TextView tekstObokStawki;
	public TextView tekstObokMinStawki;
	public TextView etap_nazwa;
	public int stawka_gracza_int;
	public TextView pula;
	public EditText EditText_pseudo;
	public Button next;
	public String temp_pseudonim;
	public Gracz gracz[];
	public Button check_button;
	public Button bet_button;
	public Button raise_button;
	public Button call_button;
	public Button fold_button;
	public Button allin_button;
	public Button wymien_button;
	public Button next_round;
	public SeekBar raise_seekBar;
	public LinearLayout wynik_list;
	public int ilosc_licytujacych;
	public Licytacja l;
	public LinearLayout lista_kart;

	boolean kazdy_licytowal = false;

	public void cos() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		t = new Talia();
		wynik_list = (LinearLayout) findViewById(R.id.wynik_list);
		etap_nazwa = (TextView) findViewById(R.id.etap_nazwa);
		black = (RelativeLayout) findViewById(R.id.black);
		next_round = (Button) findViewById(R.id.next_round);
		stol = (RelativeLayout) findViewById(R.id.stol);
		min_stawka = (TextView) findViewById(R.id.min_stawka);
		tekstObokMinStawki = (TextView) findViewById(R.id.textView4);
		stawka_gracza = (TextView) findViewById(R.id.stawka_gracza);
		tekstObokStawki = (TextView) findViewById(R.id.text_obok_stawki_gracza);
		pula = (TextView) findViewById(R.id.pula);
		lista_kart = (LinearLayout) findViewById(R.id.listaKart);
		fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		polecenie = (TextView) findViewById(R.id.polecenie_textview);
		EditText_pseudo = (EditText) findViewById(R.id.pseudonim);
		check_button = (Button) findViewById(R.id.check_button);
		bet_button = (Button) findViewById(R.id.bet_button);
		raise_button = (Button) findViewById(R.id.raise_button);
		call_button = (Button) findViewById(R.id.call_button);
		fold_button = (Button) findViewById(R.id.fold_button);
		allin_button = (Button) findViewById(R.id.allin_button);
		wymien_button = (Button) findViewById(R.id.wymien_button);
		raise_seekBar = (SeekBar) findViewById(R.id.raise_seekBar);
		stanKonta = (TextView) findViewById(R.id.stan_konta);
		raise_seekBar.setOnSeekBarChangeListener(this);
		next_round.setOnClickListener(this);
		check_button.setOnClickListener(this);
		bet_button.setOnClickListener(this);
		raise_button.setOnClickListener(this);
		call_button.setOnClickListener(this);
		fold_button.setOnClickListener(this);
		allin_button.setOnClickListener(this);
		wymien_button.setOnClickListener(this);
		next_round.setVisibility(View.GONE);

		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
		pokazCzern(false);

		Bundle bundle = getIntent().getExtras();
		ileCzlowiek = bundle.getInt("ileCzlowiek", 0);
		ileBot = bundle.getInt("ileBot", 0);
		ileZetonow = bundle.getInt("ileZetonow", 0);
		ileWpisowe = bundle.getInt("ileWpisowe", 0);
		aktywni = ileCzlowiek + ileBot;

		stworzGraczy();
		if (ileCzlowiek == 0) {
			index = -1;
			etap = I_licytacja;
			gra();
		}
	}

	public void gra() {
		if (aktywni <= 1) {
			Intent i = new Intent(getApplicationContext(), EndActivity.class);
			startActivity(i);
			finish();
		}
		switch (etap) {
		case I_licytacja:
			tekstObokMinStawki.setVisibility(View.VISIBLE);
			min_stawka.setVisibility(View.VISIBLE);
			raise_seekBar.setVisibility(View.VISIBLE);
			etap_nazwa.setText("Licytacja");
			licytacja();
			break;
		case I_wymiana:
			kazdy_licytowal = false;
			etap_nazwa.setText("Wymiana");
			raise_seekBar.setVisibility(View.INVISIBLE);
			min_stawka.setVisibility(View.INVISIBLE);
			tekstObokMinStawki.setVisibility(View.INVISIBLE);
			tekstObokStawki.setText("Mozesz wymienic 4 karty:");
			wymiana();
			break;
		case II_licytacja:
			min_stawka.setVisibility(View.VISIBLE);
			raise_seekBar.setVisibility(View.VISIBLE);
			tekstObokMinStawki.setVisibility(View.VISIBLE);
			tekstObokStawki.setText("Zwieksz stawke:");
			etap_nazwa.setText("Licytacja");
			licytacja();
			break;
		case wynik:
			new Wynik().wyswietlWynik(this);
			break;
		}
	}

	private void stworzGraczy() {

		iloscGraczy = ileCzlowiek + ileBot;
		l = new Licytacja(ileWpisowe, iloscGraczy);
		gracz = new Gracz[iloscGraczy];

		if (ileCzlowiek != 0) {
			pokazCzern(true);
			polecenie.setText("Gracz " + (index + 1) + ":");
		}

		for (int i = 0; i < ileBot; i++) {
			gracz[i + ileCzlowiek] = new Bot(this, i + ileCzlowiek);
			gracz[i + ileCzlowiek].StanKonta = ileZetonow;
		}

	}

	public void licytacja() {
		index++;
		if (index == iloscGraczy) {
			kazdy_licytowal = true;
		}
		index %= iloscGraczy;
		if ((gracz[index].StanKonta <= 0) && (gracz[index].czy_gra)) {
			gracz[index].czy_gra = false;
			aktywni--;
			gra();
			return;
		}
		if (l.licytacja == l.allin) {
			allin_index++;
			kazdy_licytowal = false;
			if (allin_index == iloscGraczy) {
				etap++;
				index = -1;
				gra();
				return;
			}
		}
		if (!gracz[index].czy_gra) {
			gra();
			return;
		}
		if (kazdy_licytowal) {
			boolean takie_same = true;
			int x = gracz[0].ile_postawil;
			for (int i = 0; i < iloscGraczy; i++) {
				if (!gracz[i].czy_gra)
					continue;
				if (gracz[i].ile_postawil == x)
					continue;
				else {
					takie_same = false;
				}
			}
			if (takie_same) {
				etap++;
				index = -1;
				gra();
				return;
			}
		}
		gracz[index].Licytuj(this);
	}

	public void wymiana() {
		index++;
		if (index == iloscGraczy) {
			if (l.licytacja == l.allin)
				etap++;
			etap++;
			index = -1;
			kazdy_licytowal = false;
			gra();
			return;
		}
		if (gracz[index].czy_gra) {
			gracz[index].Wymiana(this);
		} else
			gra();
	}

	public void pokazCzern(boolean b) {
		if (b) {
			black.startAnimation(fade_in);
			black.setVisibility(View.VISIBLE);
			raise_seekBar.setEnabled(false);
			check_button.setEnabled(false);
			bet_button.setEnabled(false);
			raise_button.setEnabled(false);
			fold_button.setEnabled(false);
			allin_button.setEnabled(false);
		} else {
			raise_seekBar.setEnabled(true);
			check_button.setEnabled(true);
			bet_button.setEnabled(true);
			raise_button.setEnabled(true);
			fold_button.setEnabled(true);
			allin_button.setEnabled(true);
			black.startAnimation(fade_out);
			black.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		stawka_gracza.setText("");
		if (v == next) {
			switch (etap) {
			case pobieranie_pseudonimu:
				temp_pseudonim = EditText_pseudo.getText().toString();
				gracz[index] = new Czlowiek(this, temp_pseudonim, index);
				gracz[index].StanKonta = ileZetonow;
				EditText_pseudo.setText(null);
				index++;
				if (index == ileCzlowiek) {
					etap++;
					EditText_pseudo.setVisibility(View.GONE);
					index = -1;
					gra();
				} else
					polecenie.setText("Gracz " + (index + 1) + ":");
				break;

			default:
				pokazCzern(false);
			}
		} else if (v == check_button) {
			gra();
		} else if (v == fold_button) {
			komunikat = "Czy na pewno chcesz zrezygnowac?";
			showDialog(FOLD_DIALOG);
		} else if (v == bet_button) {
			l.licytacja++;
			gracz[index].Obstaw(this, stawka_gracza_int);
		} else if (v == raise_button) {
			gracz[index].Obstaw(this, stawka_gracza_int);
		} else if (v == call_button) {
			gracz[index].Obstaw(this, l.minimalna_stawka);
		} else if (v == allin_button) {
			l.licytacja = l.allin;
			gracz[index].Obstaw(this, gracz[index].StanKonta);
		} else if (v == wymien_button) {
			if (gracz[index].ileDoWymiany > 4) {
				Toast.makeText(GameActivity.this,
						"Mozesz wymienic maksymalnie 4 karty",
						Toast.LENGTH_SHORT).show();
			} else {
				gracz[index].wymien(this);
				gra();
			}
		} else if (v == next_round) {
			etap = I_licytacja;
			index = -1;
			l.minimalna_stawka = ileWpisowe;
			l.licytacja = l.bet;
			l.pula = 0;
			kazdy_licytowal = false;
			allin_index = 0;
			wynik_list.removeAllViews();
			t = new Talia();
			for (int i = 0; i < gracz.length; i++) {
				gracz[i].czy_gra = true;
				gracz[i].ile_postawil = 0;
				gracz[i].wezKarte(t);
			}
			stol.setVisibility(View.VISIBLE);
			next_round.setVisibility(View.GONE);
			gra();
		}
		if (etap > pobieranie_pseudonimu && l.ilosc_licytujacych == 1) {
			etap++;
			l.ilosc_licytujacych = 0;
			index = 0;
			gra();
		}
		if (v.getId() >= 0 && v.getId() < 5) {
			if (gracz[index].doWymiany[v.getId()] == 0) {
				gracz[index].ileDoWymiany++;
				stawka_gracza.setText("" + gracz[index].ileDoWymiany);
				gracz[index].doWymiany[v.getId()]++;
				v.setBackgroundResource(R.color.black);
			} else {
				v.setBackgroundDrawable(null);
				gracz[index].ileDoWymiany--;
				stawka_gracza.setText("" + gracz[index].ileDoWymiany);
				gracz[index].doWymiany[v.getId()]--;
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == raise_seekBar) {
			stawka_gracza_int = progress;
			stawka_gracza.setText("" + progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Odpadasz_z_gry:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setTitle("Komunikat")
					.setMessage(komunikat)
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									pokazCzern(true);
									removeDialog(Odpadasz_z_gry);
								}
							}).create();
			return alertDialog;
		case FOLD_DIALOG:
			AlertDialog alertDialog2 = new AlertDialog.Builder(this)
					.setTitle("Komunikat")
					.setMessage(komunikat)
					.setNeutralButton("Nie",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									removeDialog(FOLD_DIALOG);
								}
							})
					.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									gracz[index].czy_gra = false;
									l.ilosc_licytujacych--;
									gra();
									removeDialog(FOLD_DIALOG);
								}
							}).create();
			return alertDialog2;

		}
		return null;
	}

}
