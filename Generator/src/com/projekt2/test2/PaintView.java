package com.projekt2.test2;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class PaintView extends SurfaceView {

	private ArrayList<Oval> ovals;
	private ArrayList<Line> linie;
	private ArrayList<mPoint> punkty;
	private Paint paint = new Paint();

	public PaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ovals = new ArrayList<Oval>();
		linie = new ArrayList<Line>();
		punkty = new ArrayList<mPoint>();
		paint = new Paint();
	}

	public void Oval(float x, float y, float width, float height, int Color) {
		RectF rect = new RectF(x - width, y - height, x + width, y + height);
		Oval o = new Oval(rect, Color);
		ovals.add(o);
		invalidate();
	}

	public void Line(float startX, float startY, float stopX, float stopY,
			int Color) {
		Line l = new Line(startX, startY, stopX, stopY, Color);
		linie.add(l);
		invalidate();
	}

	public void Point(float x, float y, int Color) {
		mPoint p = new mPoint(x, y, Color);
		punkty.add(p);
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		paint.setColor(Color.RED);
		for (Oval o : ovals) {
			paint.setColor(o.kolor);
			canvas.drawOval(o.figura, paint);
		}
		for (Line l : linie) {
			paint.setColor(l.Color);
			canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, paint);
		}
		for (mPoint p : punkty) {
			paint.setColor(p.Color);
			canvas.drawPoint(p.x, p.y, paint);
		}
	}

}
