package rs.tickettracker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.activeandroid.ActiveAndroid;

import rs.tickettracker.helpers.SyncHelper;
import rs.tickettracker.sync.tasks.SyncTask;

/**
 * Is used to sync data periodically (depends of settings from user performance) and if something
 * change to send to SyncReceiver new data.
 */
public class SyncService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (SyncHelper.getConnectivityStatus(getApplicationContext())) {
            ActiveAndroid.initialize(getApplicationContext());
            new SyncTask(null, true, getApplicationContext()).execute();
        }
        stopSelf();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
