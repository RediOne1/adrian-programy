package com.softpartner.kolektorproduktow.opisyproduktow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.softpartner.kolektorproduktow.R;

//Product OpisyProduktow
public class ProductOP implements Serializable, Parcelable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6954329694016501848L;
	private Activity root;
	private static LayoutInflater inflater;
	private View vi;
	public Map<String, String> dane = new HashMap<String, String>();
	public String product_id = null;
	public String product_name = null;
	public String producer_id = null;
	public String producer_name = null;
	public String brand_id = null;
	public String brand_name = null;
	public String ean = null;
	public String category_id = null;
	public String category_name = null;
	public String mainMeasure = null;
	public String product_type = null;
	public String short_product_description = null;
	public String line_description = null;
	public String zdjecie = null;
	public String description = null;
	public String brand_description = null;
	public String product_description = null;
	public String series = null;

	public ProductOP(Activity _root) {
		this.root = _root;
	}

	public void addElement(String key, String value) {
		dane.put(key, value);
	}

	public List<View> getElementsViewList() {
		List<View> views = new LinkedList<View>();
		for (Map.Entry<String, String> entry : dane.entrySet()) {
			views.add(toView(entry.getKey(), entry.getValue()));
		}
		return views;
	}

	private View toView(String name, String value) {
		inflater = (LayoutInflater) root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi = inflater.inflate(R.layout.wyswietl_produkt_element, null);
		TextView name_tv = (TextView) vi
				.findViewById(R.id.wyswietl_element_name);
		TextView value_tv = (TextView) vi
				.findViewById(R.id.wyswietl_element_value);
		name_tv.setText(name);
		value_tv.setText(value);
		return vi;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
