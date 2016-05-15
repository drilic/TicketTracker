package rs.tickettracker.sync;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import rs.tickettracker.helpers.SyncHelper;
import rs.tickettracker.sync.tasks.SyncTask;

/**
 * Created by gisko on 15-May-16.
 */
public class SyncService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean status = SyncHelper.getConnectivityStatus(getApplicationContext());
        if (status) {
            new SyncTask(null, true, getApplicationContext()).execute();
        }
        Log.i("***", "Start service");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
