package com.eqsis.notifications.FCM_Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.eqsis.notifications.Activity.SplashActivity;

public class ClickActionHelper {
    public static void startActivity(String className, Bundle extras, Context context){
        Class cls = null;
        try {
            cls = Class.forName(className);
        }catch(ClassNotFoundException e){
            //means you made a wrong input in firebase console
            Log.d("Wrong firebase input", "startActivity:  com.eqsis.notifications.FCM_Services");
        }
        Intent i = new Intent(context, cls);
        if (extras!=null)
            i.putExtras(extras);
        context.startActivity(i);

    }
}