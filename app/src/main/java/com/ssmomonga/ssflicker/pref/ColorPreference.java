package com.ssmomonga.ssflicker.pref;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.ssmomonga.ssflicker.R;
import com.ssmomonga.ssflicker.db.PrefDAO;
import com.ssmomonga.ssflicker.dlg.ColorPicker;

/**
 * ColorPreference
 */
public class ColorPreference extends Preference {
	
	private ColorPicker colorPicker;
	private GradientDrawable gd_color;
	
	private int color;
	
	/**
	 * Constructor
	 *
	 * @param context
	 * @param attrs
	 */
	public ColorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWidgetLayoutResource(R.layout.color_preference);
		gd_color = new GradientDrawable();
		gd_color.setShape(GradientDrawable.OVAL);
	}
	
	/**
	 * onGetDefaultValue()
	 *
	 * @param array
	 * @param index
	 * @return
	 */
	 @Override
     protected Object onGetDefaultValue(TypedArray array, int index) {
		int defaultValue = array.getInteger(index, 0);
		return defaultValue;
     }
	
	/**
	 * onSetInitialValue()
	 *
	 * @param restorePersistedValue
	 * @param defaultValue
	 */
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		//設定済の場合は設定を復元
		if (restorePersistedValue) {
			color = getPersistedInt(0);

		//未設定の場合はデフォルト値を取ってくる。
		} else {
			color = (Integer) defaultValue;
			persistInt(color);
		}
	}
	
	/**
	 * onBindView()
	 *
	 * @param view
	 */
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);

		ImageView iv_color = (ImageView) view.findViewById(R.id.iv_color);
		iv_color.setImageDrawable(gd_color);
		setColor(color);
		
		setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				int colorType = 0;
				if (preference.getKey().equals(PrefDAO.WINDOW_BACKGROUND_COLOR)) {
					colorType = ColorPicker.COLOR_TYPE_WINDOW_BACKGROUND;
				} else if (preference.getKey().equals(PrefDAO.TEXT_COLOR)) {
					colorType = ColorPicker.COLOR_TYPE_TEXT;
				} else if (preference.getKey().equals(PrefDAO.OVERLAY_POINT_BACKGROUND_COLOR)) {
					colorType = ColorPicker.COLOR_TYPE_OVERLAY_POINT_BACKGROUND;
				}

				final Context context = getContext();
				colorPicker = new ColorPicker(context, colorType) {
					@Override
					public void onSettings(int newColor) {
						if (ColorPreference.this.callChangeListener(newColor)) {
							color = newColor;
							Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
							editor.putInt(ColorPreference.this.getKey(), color);
							editor.commit();
							setColor(color);
						}
					}
				};
				colorPicker.show();
				return false;
			}
		});
	}

	/**
	 * onDependencyChanged
	 *
	 * @param dependency
	 * @param disableDependent
	 */
	@Override
	public void onDependencyChanged(Preference dependency, boolean disableDependent) {
		super.onDependencyChanged(dependency, disableDependent);
		if (!disableDependent) {
			gd_color.setAlpha(255);
		} else {
			gd_color.setAlpha(128);
		}		
	}

	/**
	 * setEnabled()
	 *
	 * @param enabled
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			gd_color.setAlpha(255);
		} else {
			gd_color.setAlpha(128);
		}
	}

	/**
	 * dismissColorPicker()
	 */
	public void dismissColorPicker() {
		if (colorPicker != null && colorPicker.isShowing()) colorPicker.dismiss();
	}
	
	/**
	 * setColor()
	 *
	 * @param color
	 */
	private void setColor(int color) {
		gd_color.setColor(color);
	}

}