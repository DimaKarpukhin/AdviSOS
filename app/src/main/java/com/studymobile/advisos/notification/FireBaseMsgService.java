package com.studymobile.advisos.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.studymobile.advisos.Activities.ActivityRegistration;
import com.studymobile.advisos.R;


public class FireBaseMsgService  extends FirebaseMessagingService
{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "technoWeb")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setColor(0xffff7700)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri);
        Intent resultIntent = new Intent(this, ActivityRegistration.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ActivityRegistration.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(1, notificationBuilder.build());
    }

}
