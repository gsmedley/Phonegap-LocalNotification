package com.phonegap.plugin.localnotification;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class that helps to store the options that can be specified per alarm.
 * 
 * @author dvtoever
 */
public class AlarmOptions {

    /*
     * Options that can be set when this plugin is invoked
     */
    private Calendar cal = Calendar.getInstance();
    private String alarmTitle = "";
    private String alarmSubTitle = "";
    private String alarmTicker = "";
    private boolean repeatDaily = false;
    private String notificationId = "";
    private String iconName = "";

    /**
     * Parse options passed from javascript part of this plugin.
     * 
     * @param optionsArr
     *            JSON Array containing the list options.
     */
    public void parseOptions(JSONArray optionsArr) {
    	final JSONObject options = optionsArr.optJSONObject(0);
   // NSNumber *repeatInterval    =  [command.arguments objectAtIndex:2];
   // NSString *soundName         =  [command.arguments objectAtIndex:3];
 

        // Parse millisecond date
        Long milliseconds = options.optLong("date");
        cal.setTimeInMillis(milliseconds);
        

        String optString = options.optString("message");
        if (!"".equals(optString)) {
        String lines[] = optString.split("\\r?\\n");
        alarmTitle = lines[0];
        if (lines.length > 1)
            alarmSubTitle = lines[1];
        }
        
        alarmTicker = options.optString("ticker");
        iconName = options.optString("icon");
        notificationId = options.optString("id");
  
    }

    public Calendar getCal() {
    return cal;
    }

    public void setCal(Calendar cal) {
    this.cal = cal;
    }

    public String getAlarmTitle() {
    return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
    this.alarmTitle = alarmTitle;
    }

    public String getAlarmSubTitle() {
    return alarmSubTitle;
    }

    public void setAlarmSubTitle(String alarmSubTitle) {
    this.alarmSubTitle = alarmSubTitle;
    }

    public String getAlarmTicker() {
    return alarmTicker;
    }

    public void setAlarmTicker(String alarmTicker) {
    this.alarmTicker = alarmTicker;
    }

    public boolean isRepeatDaily() {
    return repeatDaily;
    }

    public void setRepeatDaily(boolean repeatDaily) {
    this.repeatDaily = repeatDaily;
    }

    public String getIconName() {
        return iconName;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
    	this.notificationId = notificationId;
    }

}