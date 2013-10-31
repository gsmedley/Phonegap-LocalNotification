package com.phonegap.plugin.localnotification;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Helper class for the LocalNotification plugin. This class is reused by the
 * AlarmRestoreOnBoot.
 * 
 * @see LocalNotification
 * @see AlarmRestoreOnBoot
 * 
 * @author dvtoever
 */
public class AlarmHelper {

    private Context ctx;

    public AlarmHelper(Context context) {
	this.ctx = context;
    }

    /**
     * @see LocalNotification#add(boolean, String, String, String, int,
     *      Calendar)
     */
    public boolean addAlarm(String repeat, String alarmTitle, String alarmSubTitle, String alarmTicker,
    		String iconName, String notificationId, Calendar cal) {

	final long triggerTime = cal.getTimeInMillis();
	final Intent intent = new Intent(this.ctx, AlarmReceiver.class);
	final int hour = cal.get(Calendar.HOUR_OF_DAY);
	final int min = cal.get(Calendar.MINUTE);

	intent.setAction("" + notificationId);
	intent.putExtra(AlarmReceiver.TITLE, alarmTitle);
	intent.putExtra(AlarmReceiver.SUBTITLE, alarmSubTitle);
	intent.putExtra(AlarmReceiver.TICKER_TEXT, alarmTicker);
	intent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId);
	intent.putExtra(AlarmReceiver.ICON_NAME, iconName);
	
	intent.putExtra(AlarmReceiver.HOUR_OF_DAY, hour);
	intent.putExtra(AlarmReceiver.MINUTE, min);

	final PendingIntent sender = PendingIntent.getBroadcast(this.ctx, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	/* Get the AlarmManager service */
	final AlarmManager am = getAlarmManager();
	
	if (!repeat.isEmpty()) {
		long interval = AlarmManager.INTERVAL_DAY;
	    if( repeat.equalsIgnoreCase("minute"))
	    	interval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
	    else if( repeat.equalsIgnoreCase("hour"))
	    	interval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
	    else if( repeat.equalsIgnoreCase("day"))
	    	interval = AlarmManager.INTERVAL_DAY;
	    else if( repeat.equalsIgnoreCase("week"))
	    	interval = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
	    
	    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, sender);
	} else {
	    am.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
	}

	return true;
    }

    /**
     * @see LocalNotification#cancelNotification(int)
     */
    public boolean cancelAlarm(String notificationId) {
	/*
	 * Create an intent that looks similar, to the one that was registered
	 * using add. Making sure the notification id in the action is the same.
	 * Now we can search for such an intent using the 'getService' method
	 * and cancel it.
	 */
	final Intent intent = new Intent(this.ctx, AlarmReceiver.class);
	intent.setAction("" + notificationId);

	final PendingIntent pi = PendingIntent.getBroadcast(this.ctx, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	final AlarmManager am = getAlarmManager();

	try {
	    am.cancel(pi);
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

    /**
     * @see LocalNotification#cancelAllNotifications()
     */
    public boolean cancelAll(SharedPreferences alarmSettings) {
	final Map<String, ?> allAlarms = alarmSettings.getAll();
	final Set<String> alarmIds = allAlarms.keySet();

	for (String alarmId : alarmIds) {
	    Log.d(LocalNotification.PLUGIN_NAME, "Canceling notification with id: " + alarmId);
	    String alarmInt = alarmId;
	    cancelAlarm(alarmInt);
	}

	return true;
    }

    private AlarmManager getAlarmManager() {
	final AlarmManager am = (AlarmManager) this.ctx.getSystemService(Context.ALARM_SERVICE);

	return am;
    }
}