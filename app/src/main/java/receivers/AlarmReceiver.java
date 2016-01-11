package receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import entities.Alarm;
import quartzo.com.dublinbusalarm.LivePainelActivity;
import quartzo.com.dublinbusalarm.MainActivity;
import quartzo.com.dublinbusalarm.R;
import utils.Constants;

/**
 * Created by victor on 27/08/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(context, LivePainelActivity.class);

        it.putExtra("myDataSerialized", intent.getStringExtra("myDataSerialized"));

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        it,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Alarm myData = Alarm.create(intent.getStringExtra("myDataSerialized"));

        long[] pattern = {0, 100, 1000};
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("Bus " + myData.getBus() +  " is due in " + myData.getTimeDue() + " min")
                        .setContentText("See more details bus stop " + myData.getBusStop())
                        .setContentIntent(resultPendingIntent)
                        .setVibrate(pattern)
                        .setSound(uri);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        Notification notification = mBuilder.build();

        notification.flags =  Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        mNotifyMgr.notify(mNotificationId, notification);
    }


}