package rs.tickettracker.sync.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.List;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.activities.MainActivity;
import rs.tickettracker.fragments.interfaces.FragmentUpdateInterface;
import rs.tickettracker.helpers.SyncHelper;

/**
 * Background process that update all Active tickets from database. Getting new data on internet API.
 */
public class SyncTask extends AsyncTask<Object, Void, Void> {

    ProgressDialog dialog = null;
    AppCompatActivity activity = null;
    boolean showNotification = false;
    Context context;
    SharedPreferences sharedPreferences;

    public SyncTask(Activity activity, boolean showNotification, Context context) {
        if (activity != null) {
            this.dialog = new ProgressDialog(activity);
            this.activity = (AppCompatActivity) activity;
        }
        this.showNotification = showNotification;
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.setMessage(activity.getResources().getString(R.string.sync_in_progress));
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (activity != null) {
            for (Fragment f : activity.getSupportFragmentManager().getFragments()) {
                if (f != null && f instanceof FragmentUpdateInterface)
                    ((FragmentUpdateInterface) f).reloadTicketAdapter();
            }
        }
    }

    @Override
    protected Void doInBackground(Object... params) {
        model.Status active = new Select().from(model.Status.class).where("status = ?", "Active").executeSingle();
        List<Ticket> tickets = new Select().from(Ticket.class).where("status = ?", active.getId()).execute();
        if (tickets.size() == 0) {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, activity.getResources().getString(R.string.no_active_tickets), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
        MainActivity.NEED_SYNC = true;
        for (Ticket t : tickets) {
            SyncHelper.updateTicket(t, showNotification, context);
        }
        return null;
    }
}
