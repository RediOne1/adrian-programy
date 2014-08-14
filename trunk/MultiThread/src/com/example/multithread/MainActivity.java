package com.example.multithread;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener {

	public GestureDetector gestureDetector;
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gestureDetector = new GestureDetector(MainActivity.this, new GestureListener());
		image = (ImageView) findViewById(R.id.imageView1);
		image.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d("DEBUG_TAG",event.toString());
		return gestureDetector.onTouchEvent(event);
	}

	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD
							&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight();
						} else {
							onSwipeLeft();
						}
					}
				} else {
					if (Math.abs(diffY) > SWIPE_THRESHOLD
							&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffY > 0) {
							onSwipeBottom();
						} else {
							onSwipeTop();
						}
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	public void onSwipeTop() {
		Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
	}

	public void onSwipeRight() {
		Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
	}

	public void onSwipeLeft() {
		Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
	}

	public void onSwipeBottom() {
		Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
	}

}
