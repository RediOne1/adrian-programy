package com.projekt2.test2;

public class Line {
	public float startX;
	public float startY;
	public float stopX;
	public float stopY;
	public int Color;

	public Line(float startX, float startY, float stopX, float stopY, int Color) {
		this.startX = (float)startX;
		this.startY = (float)startY;
		this.stopX = (float)stopX;
		this.stopY = (float)stopY;
		this.Color = Color;
	}
}
