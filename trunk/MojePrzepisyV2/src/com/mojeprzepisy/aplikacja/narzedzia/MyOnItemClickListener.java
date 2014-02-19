package com.mojeprzepisy.aplikacja.narzedzia;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.MainActivity;
import com.mojeprzepisy.aplikacja.R;

public class MyOnItemClickListener implements OnItemClickListener {
	Context context;
	
	public MyOnItemClickListener(Context _context){
		this.context = _context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// getting values from selected ListItem
		String przepID = ((TextView) view.findViewById(R.id.single_item_przepisID))
				.getText().toString();
		String autID = ((TextView) view.findViewById(R.id.single_item_autorID)).getText()
				.toString();
		int autorID = 0;
		int przepisID = 0;
		try {
			autorID = Integer.parseInt(autID);
			przepisID = Integer.parseInt(przepID);
		} catch (Exception e) {
		}

		// Starting new intent
		Intent i = new Intent(context, MainActivity.class);
		// sending pid to next activity
		i.putExtra("autorID", autorID);
		i.putExtra("przepisID", przepisID);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//i.putExtra("pseudonim", pseudonim);
		//i.putExtra("user_id", user_id);

		// starting new activity and expecting some response back
		context.startActivity(i);
	}
}
