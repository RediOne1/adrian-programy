package com.softpartner.kolektorproduktow.narzedzia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.softpartner.kolektorproduktow.R;

public class ItemClickDialogFragment extends DialogFragment {
	
	private Activity root;

	public ItemClickDialogFragment(Activity root) {
		this.root = root;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    return new AlertDialog.Builder(getActivity())
	            .setTitle("title")
	            .setPositiveButton(root.getString(R.string.tak),
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        // do something...
	                    }
	                }
	            )
	            .setNegativeButton(root.getString(R.string.nie),
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        dialog.dismiss();
	                    }
	                }
	            )
	            .create();
	}
}
