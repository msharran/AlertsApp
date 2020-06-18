package com.eqsis.notifications.FCM_Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.eqsis.notifications.Activity.AlertActivity;
import com.eqsis.notifications.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

/**
 * Created by Sharran on 6/6/2018.
 */

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if(remoteMessage.getData().size() > 0){
            Map<String, String> data = remoteMessage.getData();
            Log.d("FCM clickaction", String.valueOf(data));
            try {
                com.eqsis.notifications.Utils.Constants.alertId=new JSONArray(data.get("push_notification_ids")) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (data.containsKey("click_action")) {

                ClickActionHelper.startActivity(data.get("click_action"), null, this);
            }
        }


        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();


        //then here we can use the title and body to build a notification
         displayNotification(title, body);
    }

    public void displayNotification(String title, String body) {


        Intent resultIntent = new Intent(this, AlertActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);




        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /*
         * The first parameter is the notification id
         * better don't give a literal here (right now we are giving a int literal)
         * because using this id we can modify it later
         * */
        if (mNotifyMgr != null) {
            mNotifyMgr.notify((int) System.currentTimeMillis(), mBuilder.build());
        }
    }
}

