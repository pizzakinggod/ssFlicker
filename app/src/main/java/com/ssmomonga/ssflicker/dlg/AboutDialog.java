package com.ssmomonga.ssflicker.dlg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ssmomonga.ssflicker.R;

/**
 * AboutDialog
 */
public class AboutDialog extends AlertDialog {

	/**
	 * Constructor
	 *
	 * @param context
	 */
	public AboutDialog(Context context) {
		super(context);
		setInitialLayout();
	}

	/**
	 * setInitialLayout()
	 */
	private void setInitialLayout() {

		Context context = getContext();
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View customTitle = inflater.inflate(R.layout.about_dialog_title, null);
		setCustomTitle(customTitle);
		
		View view = inflater.inflate(R.layout.about_dialog, null);
		setView(view);
		
		try {
			TextView tv_app_ver = customTitle.findViewById(R.id.tv_app_ver);
			String versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_META_DATA).versionName;
			tv_app_ver.setText(versionName);
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		setButton(BUTTON_NEGATIVE, context.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {}
		});

	}

}