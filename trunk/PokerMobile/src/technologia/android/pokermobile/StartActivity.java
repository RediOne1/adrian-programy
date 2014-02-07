package technologia.android.pokermobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity implements OnSeekBarChangeListener,
		OnClickListener {

	private SeekBar seekCzlowiek;
	private SeekBar seekBot;
	private TextView textCzlowiek;
	private TextView textBot;
	private int ileCzlowiek = 0;
	private int ileBot = 0;
	private int ileZetonow = 0;
	private int ileWpisowe = 0;
	private Button start;
	private EditText EditText_ileZetonow;
	private EditText EditText_ileWpisowe;
	private String coZle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		seekCzlowiek = (SeekBar) findViewById(R.id.seekCzlowiek);
		seekBot = (SeekBar) findViewById(R.id.seekBot);
		textCzlowiek = (TextView) findViewById(R.id.textCzlowiek);
		textBot = (TextView) findViewById(R.id.textBot);
		start = (Button) findViewById(R.id.start);
		EditText_ileZetonow = (EditText) findViewById(R.id.ileZetonow);
		EditText_ileWpisowe = (EditText) findViewById(R.id.ileWpisowe);
		seekCzlowiek.setOnSeekBarChangeListener(this);
		seekBot.setOnSeekBarChangeListener(this);
		start.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == seekCzlowiek) {
			ileCzlowiek = progress;
			textCzlowiek.setText("" + ileCzlowiek);
			seekBot.setMax(4 - ileCzlowiek);
		} else if (seekBar == seekBot) {
			ileBot = progress;
			textBot.setText("" + ileBot);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if (v == start && sprawdzGotowosc()) {
			Intent i = new Intent(getApplicationContext(), GameActivity.class);
			i.putExtra("ileCzlowiek", ileCzlowiek);
			i.putExtra("ileBot", ileBot);
			i.putExtra("ileZetonow", ileZetonow);
			i.putExtra("ileWpisowe", ileWpisowe);
			startActivity(i);
		} else {
			Toast.makeText(StartActivity.this, coZle, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public boolean sprawdzGotowosc() {
		if (ileCzlowiek == 0 && ileBot == 0) {
			coZle = "Podaj liczbe graczy.";
			return false;
		}
		try {
			String ileZetonowStr = EditText_ileZetonow.getText().toString();
			ileZetonow = Integer.parseInt(ileZetonowStr);
			String ileWpisoweStr = EditText_ileWpisowe.getText().toString();
			ileWpisowe = Integer.parseInt(ileWpisoweStr);
		} catch (Exception e) {
			if (ileZetonow == 0)
				coZle = "Podaj ilosc zetonow";
			else
				coZle = "Podaj wartosc wpisowego.";
			return false;
		}

		if ((ileZetonow / 2) < ileWpisowe) {
			coZle = "Maksymalne wpisowe to " + (ileZetonow / 2);
			return false;
		}

		return true;
	}
}
