package com.aplikacja.podrywacz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.View;

public class MyXmlPraser {

	private Activity root;
	private XmlResourceParser mAnkieta;
	private Pytanie pytanie;
	private List<Pytanie> pytania;

	public MyXmlPraser(Activity _root) {
		this.root = _root;
		mAnkieta = root.getResources().getXml(R.xml.ankieta);
		pytania = new ArrayList<Pytanie>();
	}
	public void odczytaj(){
		try {
			int eventType = -1;
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_DOCUMENT) {
					Log.d("MyXmlPraser", "Początek dokumentu");
				} else if (eventType == XmlResourceParser.START_TAG) {
					String strName = mAnkieta.getName();
					if (strName.equals("pytanie")) {
						pytanie = new Pytanie(root);
						String tresc_pytania = mAnkieta.getAttributeValue(null,
								"name");
						pytanie.setPytanie(tresc_pytania);
						Log.d("MyXmlPraser", strName + ": " + pytanie.tresc);
					}
				} else if (eventType == XmlResourceParser.TEXT) {
					String strName = mAnkieta.getName();
					pytanie.dodajOdpowiedz(mAnkieta.getText());
					Log.d("MyXmlPraser", "odpowiedz: " + mAnkieta.getText());
				} else if (eventType == XmlResourceParser.END_TAG) {
					String strName = mAnkieta.getName();
					if (strName.equals("pytanie")) {
						Log.d("MyXmlPraser", "Koniec " + strName);
						pytania.add(pytanie);
					}
				}
				eventType = mAnkieta.next();
			}

			Log.d("MyXmlPraser", "Koniec dokumentu");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Pytanie> getPytania(){
		return pytania;
	}

}
