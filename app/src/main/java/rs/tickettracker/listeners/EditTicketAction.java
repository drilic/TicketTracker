package rs.tickettracker.listeners;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.fragments.AddTicketFragment;
import rs.tickettracker.fragments.interfaces.FragmentUpdateInterface;
import rs.tickettracker.fragments.tabs.MainTabFragment;
import rs.tickettracker.helpers.BackstackHelper;

/**
 * Action that is triggered when user click on edit ticket button.
 */
public class EditTicketAction implements View.OnClickListener {

    FragmentManager fragmentManager;
    ActionBar actionBar;
    AppCompatActivity activity;
    long id;


    public EditTicketAction(AppCompatActivity activity, long id) {
        this.id = id;
        this.fragmentManager = activity.getSupportFragmentManager();
        this.actionBar = activity.getSupportActionBar();
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Ticket t = Ticket.load(Ticket.class, id);
        if (t != null) {
            if (t.status.status.equals("Win") ||
                    t.status.status.equals("Lose")) {
                Toast.makeText(activity, activity.getResources().getString(R.string.check_status_state), Toast.LENGTH_SHORT).show();
                Button editButton = (Button) v.findViewById(R.id.editTicketButton);
                if (editButton != null) {
                    editButton.setVisibility(View.GONE);
                    for (Fragment f : fragmentManager.getFragments()) {
                        if (f instanceof FragmentUpdateInterface)
                            ((FragmentUpdateInterface) f).reloadTicketAdapter();
                    }
                }
                return;
            }
        }
        BackstackHelper.FragmentTransaction(fragmentManager.beginTransaction(),
                activity.getResources().getString(R.string.add_new_ticket), new AddTicketFragment(id, null));
        actionBar.setTitle(activity.getResources().getString(R.string.edit_ticket));
    }
}
