package rs.tickettracker.sync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import rs.tickettracker.R;
import rs.tickettracker.activities.MainActivity;
import rs.tickettracker.activities.TicketDetailActivity;

/**
 * Sync receiver is used as broadcast receiver to listen if somethings change and if it does, to
 * send notification about that change.
 */
public class SyncReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (intent.getAction().equals(MainActivity.SYNC_DATA)) {
            String notificationMessage = intent.getExtras().getString("MESSAGE_TEXT");
            long ticketId = intent.getExtras().getLong("ticketId");
            Intent newIntent = new Intent(context, TicketDetailActivity.class);
            newIntent.putExtra("id", ticketId);
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), newIntent, 0);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Ticket Tracker")
                    .setContentText(notificationMessage)
                    .setSmallIcon(R.drawable.splash_img)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .addAction(R.drawable.dots_horizontal, "Details", pIntent)
                    .build();

            mNotificationManager.notify((int) ticketId, notification);
        }
    }
}
