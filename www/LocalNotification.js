/*!
 * Cordova/PhoneGap 3.0.0+ LocalNotification Plugin
 * Original author: Olivier Lesnicki
 */

//------------------------------------------------------------------------------
// object that we're exporting
//------------------------------------------------------------------------------
var localNotifier = module.exports;

//------------------------------------------------------------------------------
localNotifier.addNotification = function(options, callback, errorCallback) {
        
    var defaults = {
                
        date            : new Date(new Date().getTime() + 5000),
        message         : "This is a local notification.",
        id              : '1'  ,
        repeat          : "" ,  // minute, hour, day, week

        // android only
        ticker          : "",
        icon            : "",  // resource name

        // ios only
        sound           : "" ,
        hasAction       : true,
        action          : 'View',
        badge           : 0 ,        
        background      : function(notificationId){  console.log( "addNotification background callback") },
        foreground      : function(notificationId){   console.log( "addNotification foreground callback")  }
              
    };
        
    if(options){
        for (var key in defaults) {
            if (typeof options[key] !== "undefined"){
            defaults[key] = options[key];          

            }
        }
    }
    
    if (typeof defaults.date == 'object') {
        defaults.date = defaults.date.getTime();
    }

     
    cordova.exec( callback, errorCallback, "LocalNotification", "addNotification" ,  [ defaults ]  );
                    
};

//------------------------------------------------------------------------------  
localNotifier.cancelNotification = function(id, callback, errorCallback) {
    cordova.exec(callback, errorCallback, "LocalNotification", "cancelNotification", [id]);
};

//------------------------------------------------------------------------------  
localNotifier.cancelAllNotifications = function(callback, errorCallback) {
    cordova.exec(callback, errorCallback, "LocalNotification", "cancelAllNotifications", []);
};