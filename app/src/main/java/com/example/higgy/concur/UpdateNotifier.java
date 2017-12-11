package com.example.higgy.concur;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Fabi on 11.12.2017.
 */

public class UpdateNotifier {
    private static final int NOTI_ID = 7777;
    NotificationManager    notificationManager;
    NotificationCompat.Builder notificationBuilder;

    public UpdateNotifier(Context context) {
    notificationBuilder = new NotificationCompat.Builder(context)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Update!")
            .setAutoCancel(true);
    Intent resultIntent = new Intent(context, MainActivity.class);
    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent,0);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification(String text){
        notificationBuilder.setContentText(text);
        notificationManager.notify(NOTI_ID, notificationBuilder.build());
    }

    public void removeNotification(){
        notificationManager.cancel(NOTI_ID);
    }
}
