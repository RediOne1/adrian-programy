package com.mojeprzepisy.aplikacja.narzedzia;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;

public class WyslijZdjecie {

	Bitmap bm;
	String name;
	private JSONParser jParser = new JSONParser();
	private String url_zmien_tytul = "http://softpartner.pl/moje_przepisy/zmien_tytul.php";
	private String url_usun_zdjecie = "http://softpartner.pl/moje_przepisy/usun_zdjecie.php";

	public WyslijZdjecie(Bitmap bm, String name) {
		this.bm = bm;
		this.name = name;
	}

	WyslijZdjecie() {

	}

	public void executeMultipartPost() throws Exception {
		new Thread(new Runnable() {
			public void run() {
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.PNG, 100, bos);
					byte[] data = bos.toByteArray();
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost postRequest = new HttpPost(
							"http://softpartner.pl/moje_przepisy/wyslijZdjecie.php");
					ByteArrayBody bab = new ByteArrayBody(data, name);
					// File file= new File("/mnt/sdcard/forest.png");
					// FileBody bin = new FileBody(file);
					MultipartEntity reqEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					reqEntity.addPart("file", bab);
					// reqEntity.addPart("podpis", new StringBody("sfsdfsdf"));
					postRequest.setEntity(reqEntity);
					HttpResponse response = httpClient.execute(postRequest);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent(), "UTF-8"));
					String sResponse;
					StringBuilder s = new StringBuilder();

					while ((sResponse = reader.readLine()) != null) {
						s = s.append(sResponse);
					}
				} catch (Exception e) {
					// handle exception here
					Log.e(e.getClass().getName(), e.getMessage());
				}
			}
		}).start();
	}

	private String stary;
	private String nowy;

	public void zmienTytul(String stary, String nowy) {
		this.stary = stary;
		this.nowy = nowy;
		new zmienTytulClass().execute();
	}

	class zmienTytulClass extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("stary", stary));
			params.add(new BasicNameValuePair("nowy", nowy));

			try {
				JSONObject json = jParser.makeHttpRequest(url_zmien_tytul,
						"POST", params);

			} catch (Exception e) {
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {

		}
	}

	public void usunZdjecie(String tytul) {
		stary = tytul;
		new usunZdjecieClass().execute();
	}

	class usunZdjecieClass extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("stary", stary));

			try {
				JSONObject json = jParser.makeHttpRequest(url_usun_zdjecie,
						"POST", params);

			} catch (Exception e) {
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {

		}
	}
}
