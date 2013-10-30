/*!
 * Cordova/PhoneGap 3.0.0+ LocalNotification Plugin
 * Original author: Olivier Lesnicki
 */

//------------------------------------------------------------------------------
// object that we're exporting
//------------------------------------------------------------------------------
var localNotifier = module.exports;

//------------------------------------------------------------------------------
localNotifier.addNotification = function(options) {
        
    var defaults = {
                
        date            : new Date(new Date().getTime() + 5000),
        message         : "This is a local notification.",
        id              : '1'  ,

        // android only
        ticker          : "Alarm Ticker",
        icon            : "small_notification_icon",

        // ios only
        repeat          : "" ,
        sound           : "horn.caf" ,
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
        defaults.date = Math.round(defaults.date.getTime()/1000);
    }

    for (var key in defaults) { console.log( "-- " + key + ": " +  defaults[key] ) }
     
    cordova.exec(
        function(params) {
            window.setTimeout(function(){
                if(typeof defaults.foreground == 'function'){
                  if(params.appState == "active") {
                    defaults.foreground(params.notificationId);
                    return;
                  }
                }
                if(typeof defaults.background == 'function'){
                  if(params.appState != "active") {
                    defaults.background(params.notificationId);
                    return;
                  }
                }
            }, 1);
        }, 
        function(err){
          console.log("ERROR in cordova.exec");
          console.log(err);
        }, 
        "LocalNotification" , 
        "addNotification"   , 
        [
            defaults
        ]
    );
                    
};

//------------------------------------------------------------------------------  
localNotifier.cancelNotification = function(id, callback) {
    cordova.exec(callback, null, "LocalNotification", "cancelNotification", [id]);
};

//------------------------------------------------------------------------------  
localNotifier.cancelAllNotifications = function(callback) {
    cordova.exec(callback, null, "LocalNotification", "cancelAllNotifications", []);
};