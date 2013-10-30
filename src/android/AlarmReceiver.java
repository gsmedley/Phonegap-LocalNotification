package com.phonegap.plugin.localnotification;




import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * The alarm receiver is triggered when a scheduled alarm is fired. This class
 * reads the information in the intent and displays this information in the
 * Android notification bar. The notification uses the default notification
 * sound and it vibrates the phone.
 * 
 * @author dvtoever
 */
public class AlarmReceiver extends BroadcastReceiver {

	public static final String TITLE = "ALARM_TITLE";
	public static final String SUBTITLE = "ALARM_SUBTITLE";
	public static final String TICKER_TEXT = "ALARM_TICKER";
	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
	public static final String ICON_NAME = "ICON_NAME";
	

	/* Contains time in 24hour format 'HH:mm' e.g. '04:30' or '18:23' */
	public static final String HOUR_OF_DAY = "HOUR_OF_DAY";
	public static final String MINUTE = "MINUTES";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("AlarmReceiver", "AlarmReceiver invoked!");

		final Bundle bundle = intent.getExtras();
		final NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// Retrieve notification details from the intent
		final String tickerText = bundle.getString(TICKER_TEXT);
		final String notificationTitle = bundle.getString(TITLE);
		final String notificationSubText = bundle.getString(SUBTITLE);
		final String iconName = bundle.getString(ICON_NAME);
			
		// parse notification id
		int notificationId = 0;

		try {
			notificationId = Integer.parseInt(bundle.getString(NOTIFICATION_ID));
		} catch (Exception e) {
			Log.d("AlarmReceiver", "Unable to process alarm with id: " + bundle.getString(NOTIFICATION_ID));
		}

		// get the package & app name, used to tell notification what to launch when tapped
		String packageName = context.getPackageName();	
		String appName;
	    PackageManager packageManager = context.getPackageManager();
		try {
			ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
			appName  = (String) packageManager.getApplicationLabel(ai);
		} catch (NameNotFoundException e) {
			appName = "";
		}
		
		Log.d("AlarmReceiver", appName + " " + packageName);
		
		int iconId = context.getResources().getIdentifier(iconName , "drawable", packageName);
		
		// make pending intent to launch app when notification is clicked
		Intent alarmIntent;		
		ComponentName owner = new ComponentName(packageName, packageName + "." + appName );
		if( Build.VERSION.SDK_INT >= 11 ) {	
			alarmIntent = Intent.makeMainActivity( owner );
		} else {			
			alarmIntent = new Intent(Intent.ACTION_MAIN);
			alarmIntent.setComponent(owner);
			alarmIntent.addCategory(Intent.CATEGORY_LAUNCHER);			
		}
		
		alarmIntent.setFlags( Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | 
			                  Intent.FLAG_ACTIVITY_NEW_TASK );
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0, alarmIntent, 0);
        
        Notification notification;
        
		// Construct the notification object
		if( Build.VERSION.SDK_INT >= 11 ) {		
			notification = new Notification.Builder(context)
	        .setContentTitle(notificationTitle)
	        .setContentText(notificationSubText)
	        .setContentIntent(contentIntent)
	        .setSmallIcon(iconId)
	        .setTicker(tickerText)
	        .build();		
		} else {
			notification = new Notification(iconId, tickerText,  System.currentTimeMillis());		
	        notification.defaults |= Notification.DEFAULT_SOUND;
	        notification.vibrate = new long[] { 0, 100, 200, 300 };
	        notification.setLatestEventInfo(context, notificationTitle, notificationSubText, contentIntent);
		}
        
        /*
         * If you want all reminders to stay in the notification bar, you should
         * generate a random ID. If you want do replace an existing
         * notification, make sure the ID below matches the ID that you store in
         * the alarm intent.
         */
		
        notificationMgr.notify(notificationId, notification);

		
	}
}