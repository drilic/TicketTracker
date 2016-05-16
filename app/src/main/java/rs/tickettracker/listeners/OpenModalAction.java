package rs.tickettracker.listeners;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import rs.tickettracker.dialogs.AddMatchDialog;
import rs.tickettracker.helpers.SyncHelper;

/**
 * Created by gisko on 29-Apr-16.
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
            AddMatchDialog amd = new AddMatchDialog(fragment);
            amd.show(activity.getFragmentManager(), "Dialog Fragment");
        } else {
            Toast.makeText(activity, "Check settings or net connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
