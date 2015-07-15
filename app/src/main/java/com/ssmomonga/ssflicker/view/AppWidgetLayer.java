package com.ssmomonga.ssflicker.view;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ssmomonga.ssflicker.data.AppWidgetInfo;
import com.ssmomonga.ssflicker.set.AppWidgetHostSettings;
import com.ssmomonga.ssflicker.set.AppWidgetParams;

/**
 * AppWidgetLayer
 */
public class AppWidgetLayer extends FrameLayout {
	
	private Context context;
	private static AppWidgetHost host;
	
	/**
	 * Constructor
	 *
	 * @param context
	 * @param attrs
	 */
	public AppWidgetLayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		host = new AppWidgetHost(context, AppWidgetHostSettings.APPWIDGET_HOST_ID);
		host.startListening();
	}
	
	/**
	 * addView()
	 *
	 * @param appWidgetInfo
	 */
	public void addView(AppWidgetInfo appWidgetInfo) {

		AppWidgetProviderInfo info = appWidgetInfo.getAppWidgetProviderInfo();
		int appWidgetId = appWidgetInfo.getAppWidgetId();
		AppWidgetParams params = new AppWidgetParams(context, appWidgetInfo);
		
		AppWidgetHostView appWidgetHostView = host.createView(context, appWidgetId, info);
		appWidgetHostView.setLayoutParams(params.getAppWidgetLP());
		appWidgetHostView.setAppWidget(appWidgetId, info);

		LinearLayout ll = new LinearLayout(context);
		int[] padding = params.getAppWidgetPositionPadding();
		ll.setPadding(padding[0], padding[1], padding[2], padding[3]);
		ll.setId(appWidgetId);
		
		ll.addView(appWidgetHostView);
		addView(ll);
		
	}
	
	/**
	 * removeView()
	 *
	 * @param appWidgetInfo
	 */
	public void removeView(AppWidgetInfo appWidgetInfo) {
		removeView((LinearLayout) findViewById(appWidgetInfo.getAppWidgetId()));
	}

	/**
	 * stopListenening();
	 *
	 */
	public void stopListening() {
		host.stopListening();
	}
	
}