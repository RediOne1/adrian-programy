package com.projekt.mygenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

public class FreakyGenerator extends Generator {

	private static final int MAX_ILOSC_OBSZAROW = 2000;
	private static final int GESTOSC_LINI = 2;
	private static final int KOLOR_LINII = Color.BLACK;
	private static final float MINIMALNA_SZEROKOSC_OBSZARU = 50;
	private static final float MINIMALNA_WYSOKOSC_OBSZARU = 50;

	private static final long serialVersionUID = -1341784248038743032L;
	private Random r;
	private Paint paint;
	private List<Obszar> obszary = new LinkedList<Obszar>();

	private class Obszar {
		private float startX, startY, stopX, stopY;

		private Obszar(float startX, float startY, float stopX, float stopY) {
			this.startX = startX;
			this.startY = startY;
			this.stopX = stopX;
			this.stopY = stopY;
		}

		private float getWidth() {
			return stopX - startX;
		}

		private float getHeight() {
			return stopY - startY;
		}

		private void fill(boolean poziomo) {
			if (poziomo) {
				int n = (int) this.getHeight();
				for (int i = 0; i < n; i += GESTOSC_LINI)
					canvas.drawLine(startX, startY + i, stopX, startY + i,
							paint);
			} else {
				int n = (int) this.getWidth();
				for (int i = 0; i < n; i += GESTOSC_LINI)
					canvas.drawLine(startX + i, startY, startX + i, stopY,
							paint);
			}
		}
	}

	@Override
	public Bitmap generate(long seed, int w, int h) {
		init(w, h);
		paint = new Paint();
		paint.setColor(KOLOR_LINII);
		r = new Random(seed);
		obszary.add(new Obszar(0, 0, w, h));
		int n = r.nextInt(MAX_ILOSC_OBSZAROW / 2);
		for (int i = 0; i < n; i++) {
			split(obszary.remove(0));
		}
		for (Obszar o : obszary)
			o.fill(r.nextBoolean());
		return bitmap;
	}

	private void split(Obszar o) {
		Obszar o1 = null, o2 = null;
		if (o.getWidth() > o.getHeight()) {
			if (o.getWidth() > 2 * MINIMALNA_SZEROKOSC_OBSZARU) {
				float new_width = randomFloat(MINIMALNA_SZEROKOSC_OBSZARU,
						o.getWidth() - MINIMALNA_SZEROKOSC_OBSZARU);
				o1 = new Obszar(o.startX, o.startY, o.startX + new_width,
						o.stopY);
				o2 = new Obszar(o.startX + new_width, o.startY, o.stopX,
						o.stopY);
			}
		} else {
			if (o.getHeight() > 2 * MINIMALNA_WYSOKOSC_OBSZARU) {
				float new_height = randomFloat(MINIMALNA_WYSOKOSC_OBSZARU,
						o.getHeight() - MINIMALNA_WYSOKOSC_OBSZARU);
				o1 = new Obszar(o.startX, o.startY, o.stopX, o.startY
						+ new_height);
				o2 = new Obszar(o.startX, o.startY + new_height, o.stopX,
						o.stopY);
			}
		}
		if (o1 != null && o2 != null) {
			obszary.add(o1);
			obszary.add(o2);
		} else
			obszary.add(o);
	}

	private float randomFloat(float a, float b) {
		float rand = r.nextFloat() * (b - a) + a;
		return rand;
	}

	@Override
	public String toString() {
		return "Adrian";
	}
}
