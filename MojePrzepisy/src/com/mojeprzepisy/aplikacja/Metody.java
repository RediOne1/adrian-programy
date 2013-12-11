package com.mojeprzepisy.aplikacja;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Metody {
	
	private static final int IO_BUFFER_SIZE = 4 * 1024;

	public Drawable wczytaj_obrazek(String imageSrc) {
		BufferedInputStream in;
		BufferedOutputStream out;
		try {
			in = new BufferedInputStream(new URL(imageSrc).openStream(),
					IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();

			final byte[] data = dataStream.toByteArray();
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			final Drawable image = new BitmapDrawable(bitmap);
			return image;
		} catch (Exception e) {
			Log.e("Sieæ", "Nie uda³o siê pobraæ obrazka", e);
		}
		return null;
	}

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}
}
