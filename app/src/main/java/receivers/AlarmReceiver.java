package receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import entities.Alarm;
import quartzo.com.dublinbusalarm.LivePainelActivity;
import quartzo.com.dublinbusalarm.R;
import utils.AlarmPersistence;

/**
 * Created by victor on 27/08/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(context, LivePainelActivity.class);

        // Sets an ID for the notification
        //int mNotificationId = 001;

        Alarm myData = Alarm.create(intent.getStringExtra("myDataSerialized"));

        it.putExtra("mNotificationId", myData.getId());

        it.putExtra("myDataSerialized", intent.getStringExtra("myDataSerialized"));

        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        it,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );



        long[] pattern = {1000, 1000, 1000, 1000, 1000};
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setCategory(Notification.CATEGORY_ALARM)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Bus " + myData.getBus().getRoute() + " is due in " + myData.getTimeDue() + " min")
                        .setContentText("Time to go to bus stop " + myData.getBus().getStop())
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(resultPendingIntent)
                        .addAction(android.R.drawable.ic_menu_view, "View details", resultPendingIntent)
                        .setLights(Color.RED, 1, 1);

        if(myData.isVibrate()){
            mBuilder.setVibrate(pattern);
        }

        if(myData.isSound()){
            mBuilder.setSound(uri);
        }

        myData.setIsActive(false);
        AlarmPersistence.saveAlarm(myData,context);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        Notification notification = mBuilder.build();

        notification.flags =  Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        mNotifyMgr.notify(myData.getId(), notification);
    }


}