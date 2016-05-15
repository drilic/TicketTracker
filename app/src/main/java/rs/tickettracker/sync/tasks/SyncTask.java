package rs.tickettracker.sync.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.activeandroid.query.Select;

import java.util.List;

import model.Ticket;
import rs.tickettracker.activities.MainActivity;
import rs.tickettracker.fragments.interfaces.FragmentUpdateInterface;
import rs.tickettracker.helpers.SyncHelper;

/**
 * Created by gisko on 04-May-16.
 */
public class SyncTask extends AsyncTask<Object, Void, Void> {

    ProgressDialog dialog = null;
    AppCompatActivity activity = null;
    boolean showNotification = false;
    Context context;

    public SyncTask(Activity activity, boolean showNotification, Context context) {
        this.dialog = new ProgressDialog(activity);
        this.activity = (AppCompatActivity) activity;
        this.showNotification = showNotification;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.setMessage("Sync in progress...");
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (showNotification) {
            Intent ints = new Intent(MainActivity.SYNC_DATA);
//            ints.putExtra(RESULT_CODE, status);
            context.sendBroadcast(ints);
        } else {
            if (activity != null) {
                for (Fragment f : activity.getSupportFragmentManager().getFragments()) {
                    if (f != null && f instanceof FragmentUpdateInterface)
                        ((FragmentUpdateInterface) f).updateArrayAdapter();
                }
            }
        }
    }

    @Override
    protected Void doInBackground(Object... params) {
        model.Status active = new Select().from(model.Status.class).where("status = ?", "Active").executeSingle();
        List<Ticket> tickets = new Select().from(Ticket.class).where("status = ?", active.getId()).execute();
        for (Ticket t : tickets) {
            SyncHelper.updateTicket(t);
        }

        return null;
    }

}