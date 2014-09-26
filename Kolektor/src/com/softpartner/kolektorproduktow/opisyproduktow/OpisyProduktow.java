package com.softpartner.kolektorproduktow.opisyproduktow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

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

	class getNodeList extends AsyncTask<String, String, NodeList> {

		List<NameValuePair> params;
		String method;

		public getNodeList(String method, String... id) {
			this.method = method;
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("key", KEY));
			params.add(new BasicNameValuePair("method", method));
			if (id.length % 2 == 0 && id.length > 0) {
				for (int i = 0; i < id.length; i += 2) {
					params.add(new BasicNameValuePair(id[i], id[i + 1]));
				}
			}
		}

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
		protected NodeList doInBackground(String... args) {
			String xml = parser.makeHttpRequest(URL, "GET", params); // getting
																		// XML
			Document doc = parser.getDomElement(xml); // getting DOM element

			NodeList node_method = doc.getElementsByTagName(method);
			Node n = node_method.item(0);
			NodeList method_childs = n.getChildNodes();
			return method_childs;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
		}
	}

	public String getZdjecieURL(Element e) {
		String result = "";
		NodeList photoList = e.getElementsByTagName("medium_path");
		for (int i = 0; i < photoList.getLength(); i++) {
			if (photoList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element e2 = (Element) photoList.item(i);
				result = e2.getTextContent();
			}
		}
		return result;
	}

	public List<ProductOP> getProduct(String type, String value)
			throws InterruptedException, ExecutionException {
		List<ProductOP> produkty = new ArrayList<ProductOP>();
		NodeList nl = new getNodeList("getProduct", type, value).execute()
				.get();
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			ProductOP p = new ProductOP(root);
			NodeList nl2 = e.getChildNodes();
			for (int j = 0; j < nl2.getLength(); j++) {
				if (nl2.item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element e2 = (Element) nl2.item(j);
					if (e2.getNodeName().equals("media"))
						p.zdjecie = getZdjecieURL(e2);
					p.addElement(e2.getNodeName(), e2.getTextContent());
				}
			}
			produkty.add(p);
		}
		return produkty;
	}

	public List<ProductOP> getProducts(String... id)
			throws InterruptedException, ExecutionException {
		List<ProductOP> produkty = new ArrayList<ProductOP>();
		NodeList nl = new getNodeList("getProducts", id).get();
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			ProductOP p = new ProductOP(root);
			NodeList nl2 = e.getChildNodes();
			for (int j = 0; j < nl2.getLength(); j++) {
				if (nl2.item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element e2 = (Element) nl2.item(j);
					p.addElement(e2.getNodeName(), e2.getNodeValue());
					Log.d("DEBUG_TAG",
							e2.getNodeName() + " " + e2.getNodeValue());
				}
			}
			produkty.add(p);
		}
		return produkty;
	}

	public List<Producer> getProducers() throws InterruptedException,
			ExecutionException {
		List<Producer> producenci = new ArrayList<Producer>();
		NodeList nl = new getNodeList("getProducers").get();
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			Producer p = new Producer();
			p.producer_id = parser.getValue(e, "producer_id");
			p.producer_name = parser.getValue(e, "producer_name");
			producenci.add(p);
		}
		return producenci;
	}

	public List<Brand> getBrands() throws InterruptedException,
			ExecutionException {
		List<Brand> brands = new ArrayList<Brand>();
		NodeList nl = new getNodeList("getBrands").get();

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
