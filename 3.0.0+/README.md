Cordova/PhoneGap Local Notification Plugin
==========================================

A Cordova/PhoneGap 3.0.0+ plugin to create local notifications on iOS, originally by [Olivier Lesnicki](https://github.com/olivierlesnicki/cordova-ios-LocalNotification). This may be used to 
schedule notifications or other functions that trigger at some point in the future.

Installing the plugin
---------------------

In `platforms/ios/CordovaLib/Classes/CDVPlugin.m` uncomment the following line:

		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didReceiveLocalNotification:) name:CDVLocalNotification object:nil];

In `platforms/ios/CordovaLib/Classes/CDVPlugin.m` uncomment the following lines at the end of the file:

    - (void)didReceiveLocalNotification:(NSNotification *)notification
    {
       // UILocalNotification* localNotification = [notification object]; // get the payload as a LocalNotification
    }

In `platforms/ios/CordovaLib/Classes/CDVPlugin.h` uncomment the following line:

    - (void)didReceiveLocalNotification:(NSNotification *)notification;

Note: The original instructions said to place the `.caf` sound files into the `Resources` folder in Xcode. I have yet to make the sound work, so I'm not sure if this is accurate.

Aside from the audio, the installation is complete.

Using the plugin
----------------

Within the `www/js/index.js` file, or any other included js files, the following will trigger a local notification after 5 seconds:

    localNotifier.addNotification({
    	fireDate        : Math.round(new Date().getTime()/1000 + 5),
    	alertBody       : "This is a new local notification.",
    	repeatInterval  : "daily",
    	soundName       : "horn.caf",
    	badge           : 0,
    	notificationId  : 123,
    	foreground      : function(notificationId){ 
    		alert("Hello World! This alert was triggered by notification " + notificationId); 
    	},
    	background	: function(notificationId){
    		alert("Hello World! This alert was triggered by notification " + notificationId);
    	}    		
    });