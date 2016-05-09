package rs.tickettracker.listeners;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import rs.tickettracker.dialogs.AddMatchDialog;

/**
 * Created by gisko on 29-Apr-16.
 */
public class OpenModalListener implements View.OnClickListener {

    AppCompatActivity activity;
    Fragment fragment;

    public OpenModalListener(AppCompatActivity activity, Fragment fragment){
        this.activity= activity;
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        AddMatchDialog amd = new AddMatchDialog(fragment);
        amd.show(activity.getFragmentManager(), "Dialog Fragment");
    }
}
