package com.phonegap.plugin.localnotification;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
/**
 * This plugin utilizes the Android AlarmManager in combination with StatusBar
 * notifications. When a local notification is scheduled the alarm manager takes
 * care of firing the event. When the event is processed, a notification is put
 * in the Android status bar.
 * 
 * @author Daniel van 't Oever
 */
public class LocalNotification extends CordovaPlugin {

	public static final String PLUGIN_NAME = "LocalNotification";

    /**
     * Delegate object that does the actual alarm registration. Is reused by the
     * AlarmRestoreOnBoot class.
     */
    private AlarmHelper alarm = null;

    @Override
    public boolean execute(String action, JSONArray optionsArr, CallbackContext callbackContext) {
    alarm = new AlarmHelper(cordova.getActivity());
    Log.d(PLUGIN_NAME, "Plugin execute called with action: " + action);

    boolean result = false;
    
    /*
     * Determine which action of the plugin needs to be invoked
     */
   
    if (action.equalsIgnoreCase("addNotification")) {
        final AlarmOptions alarmOptions = new AlarmOptions();
        alarmOptions.parseOptions(optionsArr);
        String alarmId = alarmOptions.getNotificationId();
        final String daily = alarmOptions.getRepeat();
        final String title = alarmOptions.getAlarmTitle();
        final String subTitle = alarmOptions.getAlarmSubTitle();
        final String ticker = alarmOptions.getAlarmTicker();
        final String icon = alarmOptions.getIconName();

        persistAlarm(alarmId, optionsArr);
        this.add(daily, title, subTitle, ticker, icon, alarmId, alarmOptions.getCal());
        callbackContext.success();
        result = true;
    } else if (action.equalsIgnoreCase("cancelNotification")) {
    	String alarmId = optionsArr.optString(0);
        unpersistAlarm(alarmId);
        this.cancelNotification(alarmId);
        callbackContext.success();
        result = true;
    } else if (action.equalsIgnoreCase("cancelAllNotifications")) {
        unpersistAlarmAll();
        this.cancelAllNotifications();
        callbackContext.success();
        result = true;
    }

    return result;
    }

    /**
     * Set an alarm
     * 
     * @param repeatDaily
     *            If true, the alarm interval will be set to one day.
     * @param alarmTitle
     *            The title of the alarm as shown in the Android notification
     *            panel
     * @param alarmSubTitle
     *            The subtitle of the alarm
     * @param icon
     *            name of the icon resource
     * @param alarmId
     *            The unique ID of the notification
     * @param cal
     *            A calendar object that represents the time at which the alarm
     *            should first be started
     * @return A pluginresult.
     */
    public PluginResult add(String repeat, String alarmTitle, String alarmSubTitle, String alarmTicker,
    		 String icon,  String alarmId, Calendar cal) {

    Log.d(PLUGIN_NAME, "Adding " + repeat + " notification: '" + alarmTitle + alarmSubTitle + "' with id: "
        + alarmId + " at : " +  (new SimpleDateFormat("dd-MM-yy:HH:mm:SS Z")).format(cal.getTime()) );

    boolean result = alarm.addAlarm(repeat, alarmTitle, alarmSubTitle,  alarmTicker, icon, alarmId, cal);
    if (result) {
        return new PluginResult(PluginResult.Status.OK);
    } else {
        return new PluginResult(PluginResult.Status.ERROR);
    }
    }

    /**
     * Cancel a specific notification that was previously registered.
     * 
     * @param notificationId
     *            The original ID of the notification that was used when it was
     *            registered using addNotification()
     */
    public PluginResult cancelNotification(String notificationId) {
    Log.d(PLUGIN_NAME, "cancelNotification: Canceling event with id: " + notificationId);

    boolean result = alarm.cancelAlarm(notificationId);
    if (result) {
        return new PluginResult(PluginResult.Status.OK);
    } else {
        return new PluginResult(PluginResult.Status.ERROR);
    }
    }

    /**
     * Cancel all notifications that were created by this plugin.
     */
    public PluginResult cancelAllNotifications() {
    Log.d(PLUGIN_NAME, "cancelAllNotifications: cancelling all events for this application");
    /*
     * Android can only unregister a specific alarm. There is no such thing
     * as cancelAll. Therefore we rely on the Shared Preferences which holds
     * all our alarms to loop through these alarms and unregister them one
     * by one.
     */
    final SharedPreferences alarmSettings = cordova.getActivity().getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE);
    final boolean result = alarm.cancelAll(alarmSettings);

    if (result) {
        return new PluginResult(PluginResult.Status.OK);
    } else {
        return new PluginResult(PluginResult.Status.ERROR);
    }
    }

    /**
     * Persist the information of this alarm to the Android Shared Preferences.
     * This will allow the application to restore the alarm upon device reboot.
     * Also this is used by the cancelAllNotifications method.
     * 
     * @see #cancelAllNotifications()
     * 
     * @param optionsArr
     *            The assumption is that parseOptions has been called already.
     * 
     * @return true when successfull, otherwise false
     */
    private boolean persistAlarm(String alarmId, JSONArray optionsArr) {
    final Editor alarmSettingsEditor = cordova.getActivity().getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE).edit();

    alarmSettingsEditor.putString(alarmId, optionsArr.toString());

    return alarmSettingsEditor.commit();
    }

    /**
     * Remove a specific alarm from the Android shared Preferences
     * 
     * @param alarmId
     *            The Id of the notification that must be removed.
     * 
     * @return true when successfull, otherwise false
     */
    private boolean unpersistAlarm(String alarmId) {
    final Editor alarmSettingsEditor = cordova.getActivity().getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE).edit();

    alarmSettingsEditor.remove(alarmId);

    return alarmSettingsEditor.commit();
    }

    /**
     * Clear all alarms from the Android shared Preferences
     * 
     * @return true when successfull, otherwise false
     */
    private boolean unpersistAlarmAll() {
    final Editor alarmSettingsEditor = cordova.getActivity().getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE).edit();

    alarmSettingsEditor.clear();

    return alarmSettingsEditor.commit();
    }
}