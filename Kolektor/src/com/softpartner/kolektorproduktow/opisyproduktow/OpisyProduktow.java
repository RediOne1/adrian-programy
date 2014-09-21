package com.softpartner.kolektorproduktow.opisyproduktow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;

import com.softpartner.kolektorproduktow.R;
import com.softpartner.kolektorproduktow.narzedzia.XMLParser;

public class OpisyProduktow {
	private Activity root;

	// Opisyproduktow.pl user key:
	private static final String KEY = "df9d3eb90f093ffc5872ca0ecdde54a4";
	// All static variables
	private static String URL;

	private XMLParser parser;
	// XML node keys
	static final String KEY_ITEM = "item"; // parent node
	static final String KEY_NAME = "name";
	static final String KEY_COST = "cost";
	static final String KEY_DESC = "description";

	public OpisyProduktow(Activity _root) {
		this.root = _root;
		URL = root.getString(R.string.url_opisy_produktow);
		parser = new XMLParser();

	}

	private NodeList getNodeList(String method, String... id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key", KEY));
		params.add(new BasicNameValuePair("method", method));
		if (id.length % 2 == 0 && id.length > 0) {
			for (int i = 0; i < id.length; i += 2) {
				params.add(new BasicNameValuePair(id[i], id[i + 1]));
			}
		}
		String xml = parser.makeHttpRequest(URL, "GET", params); // getting XML
		Document doc = parser.getDomElement(xml); // getting DOM element

		NodeList nl = doc.getElementsByTagName(method);
		Node n = nl.item(0);
		NodeList nl2 = n.getChildNodes();
		return nl2;
	}

	public List<ProductOP> getProducts(String... id) {
		List<ProductOP> produkty = new ArrayList<ProductOP>();
		NodeList nl = getNodeList("getProducts", id);
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			ProductOP p = new ProductOP();
			p.product_id = parser.getValue(e, "product_id");
			p.product_name = parser.getValue(e, "product_name");
			p.producer_id = parser.getValue(e, "producer_id");
			p.producer_name = parser.getValue(e, "producer_name");
			p.brand_id = parser.getValue(e, "brand_id");
			p.brand_name = parser.getValue(e, "brand_name");
			p.ean = parser.getValue(e, "ean");
			p.category_id = parser.getValue(e, "category_id");
			p.category_name = parser.getValue(e, "category_name");
			produkty.add(p);
		}
		return produkty;
	}

	public List<Producer> getProducers() {
		List<Producer> producenci = new ArrayList<Producer>();
		NodeList nl = getNodeList("getProducers");
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			Producer p = new Producer();
			p.producer_id = parser.getValue(e, "producer_id");
			p.producer_name = parser.getValue(e, "producer_name");
			producenci.add(p);
		}
		return producenci;
	}

	public List<Brand> getBrands() {
		List<Brand> brands = new ArrayList<Brand>();
		NodeList nl = getNodeList("getBrands");

		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			Brand b = new Brand();
			b.brand_id = parser.getValue(e, "brand_id");
			b.brand_name = parser.getValue(e, "brand_name");
			brands.add(b);
		}
		return brands;
	}
}
