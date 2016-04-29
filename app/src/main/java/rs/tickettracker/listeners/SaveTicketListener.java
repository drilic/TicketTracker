package rs.tickettracker.listeners;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import rs.tickettracker.dialogs.AddMatchDialog;
import rs.tickettracker.helpers.BackstackHelper;

/**
 * Created by gisko on 29-Apr-16.
 */
public class SaveTicketListener implements View.OnClickListener {

    AppCompatActivity activity;

    public SaveTicketListener(AppCompatActivity activity){
        this.activity= activity;
    }

    @Override
    public void onClick(View v) {
        BackstackHelper.isFragmentBackPressed(activity);
    }
}
