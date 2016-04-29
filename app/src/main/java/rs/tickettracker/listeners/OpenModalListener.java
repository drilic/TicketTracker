package rs.tickettracker.listeners;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import rs.tickettracker.dialogs.AddMatchDialog;

/**
 * Created by gisko on 29-Apr-16.
 */
public class OpenModalListener implements View.OnClickListener {

    AppCompatActivity activity;

    public OpenModalListener(AppCompatActivity activity){
        this.activity= activity;
    }

    @Override
    public void onClick(View v) {
        AddMatchDialog amd = new AddMatchDialog();
        amd.show(activity.getFragmentManager(), "Dialog Fragment");
    }
}
