Cordova/PhoneGap Local Notification Plugin
------------------------------------------

A Cordova/PhoneGap 3.0.0+ plugin to create local notifications on iOS and Android.

iOS version originally by [Olivier Lesnicki](https://github.com/olivierlesnicki/cordova-ios-LocalNotification). 
Android version originally by [Drew Dahlman](https://github.com/DrewDahlman/Phonegap-LocalNotification/blob/master/readme.md).

Changes
-------

- Rationalize iOS/Android parameters
- Support notifification icon - Android
- Tap Notifification to open app - Android
- Add other repeat periods - Android

TO DO
-----

- Test ios changes


Installing the plugin
---------------------

Install the core plugin files via the [Command-line Interface](http://docs.phonegap.com/en/3.0.0/guide_cli_index.md.html#The%20Command-line%20Interface):

    $ phonegap plugin add https://github.com/gsmedley/Phonegap-LocalNotification.git


Using the plugin
----------------

Within the `www/js/index.js` file, or any other included js files, the following will trigger a local notification after 5 seconds:

    localNotifier.addNotification({
        date            : new Date(new Date().getTime() + 5000),
        message         : "This is a local notification.",
        id              : '1'  , // reusing an id will replace an existing notification
        repeat          : "" ,  // minute, hour, day, week

        // android only
        ticker          : "Alarm Ticker",
        icon            : "small_notification_icon",  // resource name

        // ios only
        sound           : "horn.caf" , // name of sound file
        hasAction       : true,
        action          : 'View',
        badge           : 0 ,        
        background      : function(notificationId){  console.log( "addNotification background callback") },
        foreground      : function(notificationId){   console.log( "addNotification foreground callback")  }
    });

To cancel a notification:

    localNotifier.cancelNotification(1);

To cancel all notifications:

    localNotifier.cancelAllNotifications();

Uninstalling the plugin
-----------------------

To uninstall the plugin and its components, use:

    $ phonegap plugin remove localnotification


