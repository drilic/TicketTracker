package rs.tickettracker.listeners;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import rs.tickettracker.dialogs.AddMatchDialog;

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
       activity.getSupportFragmentManager().popBackStack();
        Log.i(SaveTicketListener.class.getSimpleName(), "Save ticket");
    }
}
