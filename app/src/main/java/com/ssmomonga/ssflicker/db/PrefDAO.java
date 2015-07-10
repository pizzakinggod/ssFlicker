package com.ssmomonga.ssflicker.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.ssmomonga.ssflicker.R;
import com.ssmomonga.ssflicker.data.App;
import com.ssmomonga.ssflicker.data.AppList;
import com.ssmomonga.ssflicker.data.IntentAppInfo;
import com.ssmomonga.ssflicker.set.DeviceSettings;

public class PrefDAO {
	
	private static final int PREF_VERSION_NUM = 2;

	public static final String LAUNCH_BY_DEFAULT = "launch_by_default";
	public static final String HOME_KEY = "home_key";
	public static final String HOME_KEY_ANOTHER_HOME = "home_key_another_home";
	public static final String HOME_KEY_CLICK_MODE = "home_key_click_mode";
	public static final String HOME_KEY_CLICK_INTERVAL = "home_key_click_interval";
	public static final String NOW = "now";
	public static final String SEARCH_KEY = "search_key";
	
	public static final String LAUNCH_FROM_OVERLAY ="launch_from_overlay";
	public static final String OVERLAY = "overlay";
	public static final String OVERLAY_POINT_0 = "overlay_point_0";
	public static final String OVERLAY_POINT_SIDE_0 = "overlay_point_side_0";
	public static final String OVERLAY_POINT_POSITION_0 = "overlay_point_position_0";
	public static final String OVERLAY_POINT_WIDTH_0 = "overlay_point_width_0";
	public static final String OVERLAY_POINT_1 = "overlay_point_1";
	public static final String OVERLAY_POINT_SIDE_1 = "overlay_point_side_1";
	public static final String OVERLAY_POINT_POSITION_1 = "overlay_point_position_1";
	public static final String OVERLAY_POINT_WIDTH_1 = "overlay_point_width_1";
	public static final String OVERLAY_POINT_BACKGROUND_COLOR = "overlay_point_background_color";
	public static final String OVERLAY_POINT_ACTION = "overlay_point_action";
	public static final String OVERLAY_FOREGROUND = "overlay_foreground";

	public static final String STATUSBAR = "statusbar";

	public static final String WINDOW_BACKGROUND_COLOR = "window_background_color";
	public static final String POINTER_WINDOW_POSITION_PORTRAIT = "pointer_window_position_portrait";
	public static final String DOCK_WINDOW_POSITION_PORTRAIT = "dock_window_position_portrait";
	public static final String POINTER_WINDOW_POSITION_LANDSCAPE = "pointer_window_position_landscape";
	public static final String DOCK_WINDOW_POSITION_LANDSCAPE = "dock_window_position_landscape";

	public static final String ICON_SIZE = "icon_size";
	public static final String TEXT_VISIBILITY = "text_visibility";
	public static final String TEXT_COLOR = "text_color";
	public static final String TEXT_SIZE = "text_size";

	public static final String VIBRATE = "vibrate";
	public static final String STATUSBAR_VISIBILITY = "statusbar_visibility";
	public static final String INVISIBLE_APPWIDGET_BACKGROUND_VISIBILITY = "invisible_appwidget_background_visibility";

	public static final String ABOUT = "about";
	public static final String BACKUP_RESTORE = "backup_restore";
	public static final String DONATION = "donation";
	
	public static final String PREF_VERSION = "pref_version";
	
	private Context context;
	private static SharedPreferences prefs;

/*
 *	Constructor
 */
	public PrefDAO(Context context) {
		this.context = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		checkPrefVersion();
	}
	
/*
 *	checkPrefVersion()
 */
	private void checkPrefVersion() {

		int oldVersion = prefs.getInt(PREF_VERSION, 1);
		int newVersion = PREF_VERSION_NUM;

		if (oldVersion < newVersion) {
			switch(oldVersion) {
				case 1:
					clearPref_2();
					break;
			}

		    Editor editor = prefs.edit();
		    editor.putInt(PREF_VERSION, newVersion);
		    editor.commit();

		}
		
	}
	
/*
 *	clearPref_2()
 */
	private void clearPref_2() {
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}
	
/*
 *	getHomeKeyAnotherHome()
 */
	public String getHomeKeyAnotherHome() {
		String anotherHome = prefs.getString(HOME_KEY_ANOTHER_HOME, null);
		if (anotherHome != null) {
			return anotherHome;
		} else {
			App app = AppList.getIntentAppList(context, IntentAppInfo.INTENT_APP_TYPE_HOME, 1)[0];
			if (app != null) {
				return app.getIntentAppInfo().getIntentUri();
			} else {
				return null;
			}
		}
	}
	
/*
 *	getHomeKeyClickMode()
 */
	public int getHomeKeyClickMode() {
		return Integer.parseInt(getRawHomeKeyClickMode());
	}
	
/*
 * getRawHomeKeyClickMode()
 */
	public String getRawHomeKeyClickMode() {
		return prefs.getString(HOME_KEY_CLICK_MODE, "1");
	}
	
/*
 *	getHomeKeyClickInterval()
 */
	public int getHomeKeyClickInterval() {
		return Integer.parseInt(getRawHomeKeyClickInterval());
	}
	
/*
 *	getRawHomeKeyClickInterval()
 */
	public String getRawHomeKeyClickInterval() {
		return prefs.getString(HOME_KEY_CLICK_INTERVAL, "500");
	}
	
/*
 *	isOverlay()
 */
	public boolean isOverlay() {
		return prefs.getBoolean(OVERLAY, false);
	}
	
/*
 *	setOverlay()
 */
	public void setOverlay(boolean b) {
		Editor editor = prefs.edit();
		editor.putBoolean(OVERLAY, b);
		editor.commit();
	}
	
/*
 *	isOverlayPoint()
 */
	public boolean isOverlayPoint(int overlayPointNumber) {
		switch (overlayPointNumber) {
			case 0:
				return prefs.getBoolean(OVERLAY_POINT_0, true);
			case 1:
				return prefs.getBoolean(OVERLAY_POINT_1, true);
			default:
				return prefs.getBoolean(OVERLAY_POINT_0, true);
		}
	}

/*
 *	getOverlayPointSide
 */
	public int getOverlayPointSide (int overlayPointNumber) {
		return Integer.parseInt(getRawOverlayPointSide(overlayPointNumber));
	}
		
/*
 *	getRawOverlayPointSide()
 */
	public String getRawOverlayPointSide(int overlayPointNumber) {
		switch (overlayPointNumber) {
			case 0:
				return prefs.getString(OVERLAY_POINT_SIDE_0, "0");
			case 1:
				return prefs.getString(OVERLAY_POINT_SIDE_1, "2");
			default:
				return prefs.getString(OVERLAY_POINT_SIDE_0, "0");
		}
	}

/*
 *	getOverlayPointPosition()
 */
	public int getOverlayPointPosition(int overlayPointNumber) {
		return Integer.parseInt(getRawOverlayPointPosition(overlayPointNumber));
	}

/*
 *	getRawOverlayPointPosition()
 */
	public String getRawOverlayPointPosition(int overlayPointNumber) {
		switch (overlayPointNumber) {
			case 0:
				return prefs.getString(OVERLAY_POINT_POSITION_0, "9");
			case 1:
				return prefs.getString(OVERLAY_POINT_POSITION_1, "9");
			default:
				return prefs.getString(OVERLAY_POINT_POSITION_0, "9");
			}
	}

/*
 *	getOverlayPointWidth()
 */
	public int getOverlayPointWidth(int overlayPointNumber) {
		return DeviceSettings.dimenToPixel(context, Integer.parseInt(getRawOverlayPointWidth(overlayPointNumber)));
	}

/*
 *	getRawOverlayPointWidth()
 */
	public String getRawOverlayPointWidth(int overlayPointNumber) {
		switch (overlayPointNumber) {
			case 0:
				return prefs.getString(OVERLAY_POINT_WIDTH_0, "16");
			case 1:
				return prefs.getString(OVERLAY_POINT_WIDTH_1, "16");
			default:
				return prefs.getString(OVERLAY_POINT_WIDTH_0, "16");
		}
	}
	
/*
 *	getRawOverlayPointAction()
 */
	public String getRawOverlayPointAction() {
		return prefs.getString(OVERLAY_POINT_ACTION, "0");
	}
	
/*
 *	getOverlayPointBackgroundColor()
 */
	public int getOverlayPointBackgroundColor() {
		return prefs.getInt(OVERLAY_POINT_BACKGROUND_COLOR, context.getResources().getColor(R.color.overlay_point_background_color_default_value));
	}
	
/*
 *	getOverlayPointAction
 */
	public int getOverlayPointAction() {
		return Integer.parseInt(getRawOverlayPointAction());
	}
	
/*
 *	isOverlayForeground()
 */
	public boolean isOverlayForeground() {
		return prefs.getBoolean(OVERLAY_FOREGROUND, true);
	}
	
/*
 *	isStatusbar()
 */
	public boolean isStatusbar() {
		return prefs.getBoolean(STATUSBAR, false);
	}

/*
 *	getWindowBackgroundColor()
 */
	public int getWindowBackgroundColor() {
		return prefs.getInt(WINDOW_BACKGROUND_COLOR, context.getResources().getColor(R.color.window_background_color_default_value));
	}
	
/*
 *	getPointerWindowPositionPortrait()
 */
	public int getPointerWindowPositionPortrait() {
		return Integer.parseInt(getRawPointerWindowPositionPortrait());
	}
	
/*
 *	getRawPointerWindowPositionPortrait()
 */
	public String getRawPointerWindowPositionPortrait() {
		return prefs.getString(POINTER_WINDOW_POSITION_PORTRAIT, "81");
	}
	
/*
 *	getDockWindowPositionPortrait()
 */
	public int getDockWindowPositionPortrait() {
		return Integer.parseInt(getRawDockWindowPositionPortrait());
	}
	
/*
 *	getRawDockWindowPositionPortrait()
 */
	public String getRawDockWindowPositionPortrait() {
		return prefs.getString(DOCK_WINDOW_POSITION_PORTRAIT, "80");
	}

/*
 *	getPointerWindowPositionLandscape()
 */
	public int getPointerWindowPositionLandscape() {
		return Integer.parseInt(getRawPointerWindowPositionLandscape());
	}
	
/*
 *	getRawPointerWindowPositionLandscape()
 */
	public String getRawPointerWindowPositionLandscape() {
		return prefs.getString(POINTER_WINDOW_POSITION_LANDSCAPE, "21");
	}
	
/*
 *	getDockWindowPositionLandscape()
 */
	public int getDockWindowPositionLandscape() {
		return Integer.parseInt(getRawDockWindowPositionLandscape());
	}
	
/*
 *	getRawDockWindowPositionLandscape()
 */
	public String getRawDockWindowPositionLandscape() {
		return prefs.getString(DOCK_WINDOW_POSITION_LANDSCAPE, "5");
	}

/*
 *	getIconSize()
 */
	public int getIconSize() {
		int iconSize = Integer.parseInt(getRawIconSize());
		if (iconSize > 56) {
			Editor editor = prefs.edit();
			editor.putString(ICON_SIZE, "40");
			editor.commit();
			iconSize = 40;
		}
		return DeviceSettings.dimenToPixel(context, iconSize);
	}
	
/*
 *	getIconPlusSize()
 */
	public int getIconPlusSize() {
		int iconSize = Integer.parseInt(getRawIconSize());
		if (iconSize > 56) {
			Editor editor = prefs.edit();
			editor.putString(ICON_SIZE, "40");
			editor.commit();
			iconSize = 40;
		}
		return DeviceSettings.dimenToPixel(context, iconSize + 16);
	}
	
/*
 *	getRawIconSize()
 */
	public String getRawIconSize() {
		return prefs.getString(ICON_SIZE, "40");
	}

/*
 *	isTextVisibility()
 */
	public boolean isTextVisibility() {
		return prefs.getBoolean(TEXT_VISIBILITY, true);
	}
	
/*
 *	getTextColor()
 */
	public int getTextColor() {
		return prefs.getInt(TEXT_COLOR, context.getResources().getColor(android.R.color.white));
	}

/*
 *	getTextSize()
 */
	public int getTextSize() {
		return Integer.parseInt(getRawTextSize());
	}
	
/*
 *	getRawTextSize()
 */
	public String getRawTextSize() {
		return prefs.getString(TEXT_SIZE, "10");
	}
	
/*
 *	getVibrateTime()
 */
	public int getVibrateTime() {
		if (isVibrate()) {
			return context.getResources().getInteger(R.integer.vibrate_time);
		} else {
			return 0;
		}
	}

/*
 *	isVibrate()
 */
	public boolean isVibrate() {
		boolean b = false;
		try {
			b = prefs.getBoolean(VIBRATE, false);
		} catch (Exception e) {
			e.printStackTrace();
			Editor editor = prefs.edit();
			editor.putBoolean(VIBRATE, false);
			editor.commit();
		}
		return b;
	}
	
/*
 *	isStatusbarVisibility()
 */
	public boolean isStatusbarVisibility() {
		return prefs.getBoolean(STATUSBAR_VISIBILITY, false);
	}
	
/*
 *	getInvisibleAppWidgetBackgroundVisibility()
 */
	public boolean isInvisibleAppWidgetBackgroundVisibility() {
		return prefs.getBoolean(INVISIBLE_APPWIDGET_BACKGROUND_VISIBILITY, false);
	}
	
/*
 *	getDonation()
 */
	public boolean getDonation() {
		return prefs.getBoolean(DONATION, false);
	}
	
/*
 *	setDonation()
 */
	public void setDonation(boolean b) {
		Editor editor = prefs.edit();
		editor.putBoolean(DONATION, b);
		editor.commit();
	}

}