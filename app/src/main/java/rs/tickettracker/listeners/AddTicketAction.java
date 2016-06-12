package rs.tickettracker.listeners;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import rs.tickettracker.R;
import rs.tickettracker.fragments.AddTicketFragment;
import rs.tickettracker.helpers.BackstackHelper;

/**
 * Action that is triggered when user click on Add Ticket button/drawer.
 */
public class AddTicketAction implements View.OnClickListener {

    FragmentManager fragmentManager;
    ActionBar actionBar;
    AppCompatActivity activity;

    public AddTicketAction(AppCompatActivity activity) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.actionBar = activity.getSupportActionBar();
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        BackstackHelper.FragmentTransaction(fragmentManager.beginTransaction(),
                activity.getResources().getString(R.string.add_new_ticket), new AddTicketFragment());
        actionBar.setTitle(activity.getResources().getString(R.string.add_new_ticket));
    }
}
