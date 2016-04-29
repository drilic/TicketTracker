package rs.tickettracker.listeners;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import rs.tickettracker.R;
import rs.tickettracker.fragments.AddTicketFragment;

/**
 * Created by gisko on 29-Apr-16.
 */
public class AddTicketListener implements View.OnClickListener {

    FragmentManager fragmentManager;
    ActionBar actionBar;
    AppCompatActivity activity;

    public AddTicketListener(AppCompatActivity activity) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.actionBar = activity.getSupportActionBar();
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction newFragmentTransaction = fragmentManager.beginTransaction();
        newFragmentTransaction.addToBackStack(activity.getResources().getString(R.string.add_new_ticket))
                .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim, R.anim.enter_anim, R.anim.exit_anim)
                .replace(R.id.containerView, new AddTicketFragment())
                .commit();
        actionBar.setTitle(activity.getResources().getString(R.string.add_new_ticket));
    }
}
