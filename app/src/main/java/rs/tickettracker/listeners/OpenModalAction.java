package rs.tickettracker.listeners;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import rs.tickettracker.R;
import rs.tickettracker.dialogs.AddMatchDialog;
import rs.tickettracker.helpers.SyncHelper;

/**
 * Action that is triggered when user click on add match button on 'Add ticket' page.
 */
public class OpenModalAction implements View.OnClickListener {

    AppCompatActivity activity;
    Fragment fragment;

    public OpenModalAction(AppCompatActivity activity, Fragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        if (SyncHelper.getConnectivityStatus(activity.getApplicationContext())) {
            AddMatchDialog amd = new AddMatchDialog();
            amd.setTargetFragment(fragment, 1000);
            amd.show(activity.getSupportFragmentManager(), "Dialog Fragment");
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.check_settings_or_net_conn), Toast.LENGTH_SHORT).show();
        }
    }
}
