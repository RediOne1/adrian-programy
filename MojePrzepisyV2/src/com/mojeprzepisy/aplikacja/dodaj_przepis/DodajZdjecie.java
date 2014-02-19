package com.mojeprzepisy.aplikacja.dodaj_przepis;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mojeprzepisy.aplikacja.R;

public class DodajZdjecie extends DodajPrzepisActivity implements
		OnClickListener {

	private ImageView imageview;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int REQ_CODE_PICK_IMAGE = 101;

	public DodajZdjecie() {
		imageview = (ImageView) findViewById(R.id.imageView1);
		imageview.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == imageview) {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CODE_PICK_IMAGE:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaColumns.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(filePath, o);
				final int REQUIRED_SIZE = 220;
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				int scale = 1;
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				Bitmap yourSelectedImage2 = BitmapFactory.decodeFile(filePath,
						o2);
				imageview.setImageBitmap(yourSelectedImage2);
				// zdjecieBitmap = yourSelectedImage2;
			}
			break;

		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				byte[] byteArray = stream.toByteArray();

				imageview.setImageBitmap(bmp);
				// zdjecieBitmap = bmp;

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
			break;
		}
	}
}
