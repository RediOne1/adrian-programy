package com.softpartner.kolektorproduktow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class Scandit extends Activity implements ScanditSDKListener,
		OnClickListener {

	private MyApp app;
	ScanditSDKAutoAdjustingBarcodePicker picker;
	private Button scanBtn;
	private EditText searchBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadUI();
	}
	private void loadUI(){
		setContentView(R.layout.activity_scandit);
		app = (MyApp) getApplicationContext();
		picker = new ScanditSDKAutoAdjustingBarcodePicker(this,
				app.ScanditAppKey, ScanditSDKBarcodePicker.CAMERA_FACING_BACK);
		scanBtn = (Button) findViewById(R.id.scandit_scanBtn);
		searchBar = (EditText) findViewById(R.id.scandit_searchBar);
		
		scanBtn.setOnClickListener(this);		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		picker.stopScanning();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scandit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void didCancel() {
		Toast.makeText(this, "didCancel", Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub

	}

	@Override
	public void didManualSearch(String entry) {
		Toast.makeText(this, "didManualSearch", Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub

	}

	@Override
	public void didScanBarcode(String barcode, String symbology) {
		loadUI();
		searchBar.setText(barcode);
	}

	@Override
	public void onClick(View v) {
		if (v == scanBtn) {
			setContentView(picker);
			picker.getOverlayView().addListener(this);
			picker.startScanning();
		}
	}
}
