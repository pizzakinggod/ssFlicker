package com.ssmomonga.ssflicker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ssmomonga.ssflicker.data.App;
import com.ssmomonga.ssflicker.data.AppList;
import com.ssmomonga.ssflicker.data.AppWidgetInfo;
import com.ssmomonga.ssflicker.data.IntentAppInfo;
import com.ssmomonga.ssflicker.data.MenuList;
import com.ssmomonga.ssflicker.data.Pointer;
import com.ssmomonga.ssflicker.db.SQLiteDAO;
import com.ssmomonga.ssflicker.dlg.Drawer;
import com.ssmomonga.ssflicker.dlg.VolumeDialog;
import com.ssmomonga.ssflicker.proc.Launch;
import com.ssmomonga.ssflicker.set.BootSettings;
import com.ssmomonga.ssflicker.set.WindowOrientationParams;
import com.ssmomonga.ssflicker.set.WindowParams;
import com.ssmomonga.ssflicker.view.ActionWindow;
import com.ssmomonga.ssflicker.view.AppWidgetLayer;
import com.ssmomonga.ssflicker.view.DockWindow;
import com.ssmomonga.ssflicker.view.OnFlickListener;
import com.ssmomonga.ssflicker.view.PointerWindow;

public class FlickerActivity extends Activity {

	private static FrameLayout fl_all;
	private static AppWidgetLayer app_widget_layer;
	private static DockWindow dock_window;
	private static PointerWindow pointer_window;
	private static ActionWindow action_window;
	
	private static Drawer drawer;
	private static VolumeDialog volumeDialog;
	
	private static SQLiteDAO sdao;
	private static Launch l;
	private static boolean homeKey;
	private static Pointer[] pointerList;
	private static App[][] appListList;
	
	private static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				l.launchAnotherHome(homeKey);
			}
		}
	};

	/*
	 * onCreate()
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sdao = new SQLiteDAO(this);
		l = new Launch(this);

		setContentView(R.layout.flicker_activity);
		setInitialLayout();
		
		pointerList = sdao.selectPointerTable();
		appListList = sdao.selectAppTable();
		dock_window.setApp(appListList[Pointer.DOCK_POINTER_ID]);
		pointer_window.setPointer(pointerList);

	}

	/*
	 * onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (homeKey = new BootSettings(this).isHomeKey()) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			registerReceiver(mReceiver, filter);
		}
		
		setLayout();
		setOrientationLayout();
		viewAppWidgetAll();
	}

	/*
	 * onConfigurationChanged()
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		dock_window.removeAllViews();
		dock_window.setInitialLayout();
		dock_window.setApp(appListList[Pointer.DOCK_POINTER_ID]);		
		WindowParams params = new WindowParams(this);
		dock_window.setLayout(params);
    	setOrientationLayout();
		dock_window.setOnFlickListener(new OnDockFlickListener(this), new OnMenuFlickListener(this));
		
		viewAppWidgetAll();
	}

	/*
	 * onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		if (homeKey) unregisterReceiver(mReceiver);
		volumeDialog = l.getVolumeDialog();
		if (volumeDialog != null && volumeDialog.isShowing()) volumeDialog.dismiss();
		if (drawer != null && drawer.isShowing()) drawer.dismiss();
		if (!isFinishing()) finish();
	}

	/*
	 * onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		app_widget_layer.stopListening();
	}


	/*
	 * onInitialLayout()
	 */
	private void setInitialLayout() {
		fl_all = (FrameLayout) findViewById(R.id.fl_all);
		app_widget_layer = (AppWidgetLayer) findViewById(R.id.app_widget_layer);
		dock_window = (DockWindow) findViewById(R.id.dock_window);
		pointer_window = (PointerWindow) findViewById(R.id.pointer_window);
		action_window = (ActionWindow) findViewById(R.id.action_window);
		setOnFlickListener();
	}
	
	/*
	 * setOnFlickListener()
	 */
	private void setOnFlickListener() {
		fl_all.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				finish();
				return false;
			}
		});
		dock_window.setOnFlickListener(new OnDockFlickListener(this), new OnMenuFlickListener(this));
		pointer_window.setOnFlickListener(new OnPointerFlickListener(this));
	}
	
	/*
	 * setLayout()
	 */
	private void setLayout() {
		WindowParams params = new WindowParams(this);
		if (params.isStatusbarVisibility()) getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dock_window.setLayout(params);
		pointer_window.setLayout(params);
		action_window.setLayout(params);
	}
	
	/*
	 * setOrientationLayout()
	 */
	private void setOrientationLayout() {
		WindowOrientationParams params = new WindowOrientationParams(this);
		app_widget_layer.setLayoutParams(params.getAppWidgetLayerLP());
		dock_window.setOrientation(params.getDockWindowOrientation());
		dock_window.setLayoutParams(params.getDockWindowLP());
		pointer_window.setLayoutParams(params.getPointerWindowLP());
		action_window.setLayoutParams(params.getActionWindowLP());
	}

	/*
	 * viewAppWidgetAll
	 */
	private void viewAppWidgetAll() {
		app_widget_layer.removeAllViews();

		int appWidgetList[][] = sdao.selectAppWidgets();
		for (int[] appWidget : appWidgetList) {
			int pointerId = appWidget[0];
			int appId = appWidget[1];
			AppWidgetInfo appWidgetInfo = appListList[pointerId][appId].getAppWidgetInfo();
			viewAppWidget(pointerId, appId, appWidgetInfo, false);

		}
	}

	/*
	 * viewAppWidget()
	 */
	private void viewAppWidget(int pointerId, int appId, AppWidgetInfo appWidgetInfo, boolean update) {
		
		if (appWidgetInfo.getAppWidgetProviderInfo() != null) {

			if ((!update && appWidgetInfo.getAppWidgetUpdateTime() != 0) ||
					(update && appWidgetInfo.getAppWidgetUpdateTime() == 0)) {
				app_widget_layer.addView(appWidgetInfo);
			} else {
				app_widget_layer.removeView(appWidgetInfo);
			}

			if (update) {
				long updateTime = appWidgetInfo.getAppWidgetUpdateTime() == 0 ? updateTime = System.currentTimeMillis() : 0;
				appListList[pointerId][appId].getAppWidgetInfo().setAppWidgetUpdateTime(updateTime);
				sdao.updateAppWidgetUpdateTime(appWidgetInfo.getAppWidgetId(), updateTime);
			}
			
		} else {
			
			if ((!update && appWidgetInfo.getAppWidgetUpdateTime() != 0) ||
					(update && appWidgetInfo.getAppWidgetUpdateTime() == 0)) {

				Toast.makeText(this, R.string.view_appwidget_error, Toast.LENGTH_SHORT).show();
				long updateTime = 0;
				appListList[pointerId][appId].getAppWidgetInfo().setAppWidgetUpdateTime(updateTime);
				sdao.updateAppWidgetUpdateTime(appWidgetInfo.getAppWidgetId(), updateTime);
			}

		}
	}

	/*
	 * OnDockFlickListener
	 */
	private class OnDockFlickListener extends OnFlickListener {

		public OnDockFlickListener(Context context) {
			super(context);
		}

		private int pointerId;
		private int appId;
		private App dock;		
		
		@Override
		public void setId(int id) {
			pointerId = Pointer.DOCK_POINTER_ID;
			appId = id;
			dock = appListList[pointerId][appId];
		}
		
		@Override
		public boolean isData() {
			return dock != null;
		}
		
		@Override
		public void onDown(int position) {
			dock_window.setDockPointed(true, appId);		
		}
		
		@Override
		public void onMove(int oldPosition, int position) {
			if (oldPosition == -1 ) {
				dock_window.setDockPointed(false, appId);				
			} else if (position == -1) {
				dock_window.setDockPointed(true, appId);				
			}
		}
		
		@Override
		public void onUp(int position, Rect r) {
			if (position == -1 ) {
				dock_window.setDockPointed(false, appId);
				if (dock.getAppType() != App.APP_TYPE_APPWIDGET) {
					l.launch(dock, r);
				} else {
					viewAppWidget(pointerId, appId, dock.getAppWidgetInfo(), true);
				}
			}
		}

		
		@Override
		public void onCancel(int position) {}
	}


	/*
	 * OnPointerFlickListener
	 */
	private class OnPointerFlickListener extends OnFlickListener {

		public OnPointerFlickListener(Context context) {
			super(context);
		}

		private int pointerId;
		private Pointer pointer;

		@Override
		public void setId(int id) {
			pointerId = id;
			pointer = pointerList[pointerId];
		}

		@Override
		public boolean isData() {
			return pointer != null;
		}

		@Override
		public void onDown(int position) {
			pointer_window.setPointerPointed(true, pointerId);
			action_window.setActionPointed(true, -1, position);

			switch (pointer.getPointerType()) {
				case Pointer.POINTER_TYPE_HOME:
					appListList[pointerId] = AppList.getIntentAppList(FlickerActivity.this, IntentAppInfo.INTENT_APP_TYPE_HOME, App.FLICK_APP_COUNT);
					break;
				
				case Pointer.POINTER_TYPE_RECENT:
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						Toast.makeText(FlickerActivity.this, R.string.recent_pointer_error, Toast.LENGTH_SHORT).show();
					} else {
						appListList[pointerId] = AppList.getTaskAppList(FlickerActivity.this, IntentAppInfo.INTENT_APP_TYPE_RECENT);
					}
					break;
			
				case Pointer.POINTER_TYPE_TASK:
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						Toast.makeText(FlickerActivity.this, R.string.task_pointer_error, Toast.LENGTH_SHORT).show();
					} else {
						appListList[pointerId] = AppList.getTaskAppList(FlickerActivity.this, IntentAppInfo.INTENT_APP_TYPE_TASK);
					}
					break;
			}
			
			action_window.setApp(pointer, appListList[pointerId]);
			action_window.setVisibility(View.VISIBLE);
		}
		

		@Override
		public void onMove(int oldPosition, int position) {
			action_window.setActionPointed(true, oldPosition, position);
		}

		
		@Override
		public void onUp(int position, Rect r) {
			pointer_window.setPointerPointed(false, pointerId);
			action_window.setActionPointed(false, position, -1);
			action_window.setVisibility(View.GONE);
			
			if (position != -1) {
				App app = appListList[pointerId][position];
				if (app != null) {
					if (app.getAppType() != App.APP_TYPE_APPWIDGET) {
						l.launch(app, r);
					} else {
						viewAppWidget(pointerId, position, app.getAppWidgetInfo(), true);
					}
				}
			}
		}

		@Override
		public void onCancel(int position) {}
	}
	
	/*
	 * OnMenuFlickListener
	 */
	private class OnMenuFlickListener extends OnFlickListener {

		public OnMenuFlickListener(Context context) {
			super(context);
		}

		@Override
		public void setId(int id) {
		}
		
		@Override
		public boolean isData() {
			return true;
		}

		@Override
		public void onDown(int position) {
			dock_window.setMenuPointed(true);			
			action_window.setActionPointed(true, -1, position);
			action_window.setMenuForFlick(FlickerActivity.this);
			action_window.setVisibility(View.VISIBLE);
		}

		@Override
		public void onMove(int oldPosition, int position) {
			action_window.setActionPointed(true, oldPosition, position);
		}

		@Override
		public void onUp(int position, Rect r) {
			dock_window.setMenuPointed(false);
			action_window.setActionPointed(false, position, -1);
			action_window.setVisibility(View.INVISIBLE);
			menu(position);
		}

		@Override
		public void onCancel(int position) {
			dock_window.setMenuPointed(false);
			action_window.setActionPointed(false, position, -1);
			action_window.setVisibility(View.INVISIBLE);
		}
	}

	/*
	 * menu()
	 */
	private void menu(int position) {
		switch (position) {
			case MenuList.MENU_DRAWER:
				drawer = new Drawer(this);
				drawer.execute();
				break;
		
			case MenuList.MENU_ANDROID_SETTINGS:
				l.launchAndroidSettings();
				break;
		
			case MenuList.MENU_SSFLICKER_SETTINGS:
				l.launchPrefActivity();
				break;
		
			case MenuList.MENU_EDIT_MODE:
				l.launchEditorActivity();
				Toast.makeText(this, R.string.enter_edit_mode, Toast.LENGTH_SHORT).show();
				break;
		
			default:
				break;
		}
	}

}