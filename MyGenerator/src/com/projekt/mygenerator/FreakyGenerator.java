package com.projekt.mygenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FreakyGenerator extends Generator {

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

		private void fill(int color) {
			paint.setColor(color);
			canvas.drawRect(startX, startY, stopX, stopY, paint);
			Bitmap t_bitmap = Bitmap.createBitmap((int)getWidth(), (int)getHeight(), Bitmap.Config.ARGB_8888);
	        Canvas t_canvas = new Canvas(t_bitmap);
	        
		}
	}

	@Override
	public Bitmap generate(long seed, int w, int h) {
		init(w, h);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		r = new Random(seed);
		obszary.add(new Obszar(0, 0, w, h));
		int n = r.nextInt(1000);
		for(int i = 0;i<n;i++){
			split(obszary.remove(0));
		}
		for (Obszar o : obszary)
			o.fill(r.nextBoolean() ? Color.BLACK : Color.WHITE);
		return bitmap;
	}

	private void split(Obszar o) {
		Obszar o1, o2;
		if (o.getWidth() > o.getHeight()) {
			float new_width = r.nextFloat() * o.getWidth();
			o1 = new Obszar(o.startX, o.startY, o.startX + new_width, o.stopY);
			o2 = new Obszar(o.startX + new_width, o.startY, o.stopX, o.stopY);
		} else {
			float new_height = r.nextFloat() * o.getHeight();
			o1 = new Obszar(o.startX, o.startY, o.stopX, o.startY + new_height);
			o2 = new Obszar(o.startX, o.startY + new_height, o.stopX, o.stopY);
		}
		obszary.add(o1);
		obszary.add(o2);
	}

	@Override
	public String toString() {
		return "Adrian";
	}
}
