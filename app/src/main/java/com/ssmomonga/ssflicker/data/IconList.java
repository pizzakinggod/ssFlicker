package com.ssmomonga.ssflicker.data;

import android.content.Context;
import android.content.res.Resources;

import com.ssmomonga.ssflicker.R;

/**
 * IconList
 */
public class IconList {
	
	public static final int TARGET_ICON_POINTER = 0;
	public static final int TARGET_ICON_APP = 2;
	
	public static final int LABEL_ICON_TYPE_ORIGINAL = 0;		//ポインタ、アプリのオリジナルアイコンやファンクション名
	public static final int LABEL_ICON_TYPE_MULTI_APPS = 1;		//ポインタのマルチアプリアイコン
	public static final int LABEL_ICON_TYPE_APP = 2;			//ポインタのアプリアイコン
	public static final int LABEL_ICON_TYPE_ACTIVITY = 3;		//アプリのアプリアイコン、ラベル
	public static final int LABEL_ICON_TYPE_LEGACY_SHORTCUT = 4;		//アプリのショートカットアイコン、ラベル
	public static final int LABEL_ICON_TYPE_APPWIDGET = 5;		//アプリのウィジェットアイコン、ラベル
	public static final int LABEL_ICON_TYPE_CUSTOM = 6;			//ポインタ、アプリのカスタム
	public static final int LABEL_ICON_TYPE_APPSHORTCUT = 7;		//アプリのアプリショートカットアイコン、ラベル

	/**
	 * getIconTypeList()
	 *
	 * @param context
	 * @param iconTarget
	 * @param pointerType
	 * @return
	 */
	public static CharSequence[] getIconTypeList(Context context, int iconTarget, int pointerType) {
		Resources r = context.getResources();
		
		//カスタムポインタの場合
		if ((iconTarget == IconList.TARGET_ICON_POINTER) && pointerType == Pointer.POINTER_TYPE_CUSTOM) {
			return new CharSequence[] {
					r.getString(R.string.original_icon),
					r.getString(R.string.multi_app_icon),
					r.getString(R.string.app_icon),
					r.getString(R.string.image) };

		//カスタムポインタ以外の場合
		} else {
			return new CharSequence[] {
					r.getString(R.string.original_icon),
					r.getString(R.string.image) };			
		}
	}

	/**
	 * getOriginalIconList()
	 *
	 * @param context
	 * @return
	 */
	public static BaseData[] getOriginalIconList(Context context) {
		Resources r = context.getResources();
		return new BaseData[] {
				new BaseData(null, r.getDrawable(R.mipmap.icon_00_pointer_custom, null), 0),
				new BaseData(null, r.getDrawable(R.mipmap.icon_10_app_launcher, null), 10),
				new BaseData(null, r.getDrawable(R.mipmap.icon_11_app_send, null), 11),
				new BaseData(null, r.getDrawable(R.mipmap.icon_12_app_legacy_shortcut, null), 12),
				new BaseData(null, r.getDrawable(R.mipmap.icon_13_app_appwidget, null), 13),
				new BaseData(null, r.getDrawable(R.mipmap.icon_14_app_function, null), 14),
				new BaseData(null, r.getDrawable(R.mipmap.icon_20_function_wifi, null), 20), 
				new BaseData(null, r.getDrawable(R.mipmap.icon_21_function_bluetooth, null), 21),
				new BaseData(null, r.getDrawable(R.mipmap.icon_22_function_sync, null), 22),
				new BaseData(null, r.getDrawable(R.mipmap.icon_23_function_rotate, null), 23),
				new BaseData(null, r.getDrawable(R.mipmap.icon_24_function_search, null), 24),
				new BaseData(null, r.getDrawable(R.mipmap.icon_30_menu_menu, null), 30),
				new BaseData(null, r.getDrawable(R.mipmap.icon_31_menu_flicker, null), 31),
				new BaseData(null, r.getDrawable(R.mipmap.icon_32_menu_editor, null), 32),
				new BaseData(null, r.getDrawable(R.mipmap.icon_33_menu_settings, null), 33),
				new BaseData(null, r.getDrawable(R.mipmap.icon_34_menu_android_settings, null), 34),
				new BaseData(null, r.getDrawable(R.mipmap.icon_40_edit_add, null), 40),
				new BaseData(null, r.getDrawable(R.mipmap.icon_41_edit_open_close, null), 41),
				new BaseData(null, r.getDrawable(R.mipmap.icon_42_edit_delete, null), 42),
				new BaseData(null, r.getDrawable(R.mipmap.icon_43_edit_left, null), 43),
				new BaseData(null, r.getDrawable(R.mipmap.icon_44_edit_up, null), 44),
				new BaseData(null, r.getDrawable(R.mipmap.icon_45_edit_right, null), 45),
				new BaseData(null, r.getDrawable(R.mipmap.icon_46_edit_down, null), 46),
				new BaseData(null, r.getDrawable(R.mipmap.icon_50_about_information, null), 50),

				new BaseData(null, r.getDrawable(R.mipmap.icon_90_unused_home, null),90),
				new BaseData(null, r.getDrawable(R.mipmap.icon_91_unused_recent, null), 91),
				new BaseData(null, r.getDrawable(R.mipmap.icon_92_unused_task, null), 92),
				new BaseData(null, r.getDrawable(R.mipmap.icon_93_unused_silent_mode, null), 93),
				new BaseData(null, r.getDrawable(R.mipmap.icon_94_unused_volume, null), 94),
				new BaseData(null, r.getDrawable(R.mipmap.icon_95_unused_airplane_mode, null), 95),
				new BaseData(null, r.getDrawable(R.mipmap.icon_96_unused_android, null), 96)
		};
	}

	/**
	 * getAppIconsList()
	 *
	 * @param appList
	 * @return
	 */
	public static BaseData[] getAppIconsList(App[] appList) {
		BaseData[] icons = new BaseData[App.FLICK_APP_COUNT];
		for (int i = 0; i < App.FLICK_APP_COUNT; i ++) {
			App app = appList[i];
			if (app != null) {
				icons[i] = new BaseData(null, app.getIcon(), i);
			}
		}
		return icons;
	}
	
}