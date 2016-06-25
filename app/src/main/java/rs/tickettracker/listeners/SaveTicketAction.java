package rs.tickettracker.listeners;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import model.Match;
import model.Status;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.activities.MainActivity;
import rs.tickettracker.activities.TicketDetailActivity;
import rs.tickettracker.adapters.MatchAddTicketListAdapter;
import rs.tickettracker.helpers.BackstackHelper;
import rs.tickettracker.helpers.GlobalStaticValuesHelper;
import rs.tickettracker.helpers.StatusHelper;
import rs.tickettracker.sync.tasks.SyncTask;

/**
 * Action that is triggered when user click on save ticket on 'Add ticket' page after editing or
 * creating new ticket.
 */
public class SaveTicketAction implements View.OnClickListener {

    AppCompatActivity activity;
    MatchAddTicketListAdapter array;
    Ticket currentTicket;
    Menu myMenu;

    public SaveTicketAction(AppCompatActivity activity, MatchAddTicketListAdapter array, Ticket currentTicket, Menu myMenu) {
        this.activity = activity;
        this.array = array;
        this.currentTicket = currentTicket;
        this.myMenu = myMenu;
    }

    @Override
    public void onClick(View v) {
        if (currentTicket != null) {
            if (currentTicket.status.status.equals("Win") ||
                    currentTicket.status.status.equals("Lose")) {
                Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.check_status_state), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        EditText ticketName = (EditText) activity.findViewById(R.id.add_ticket_name);
        if (ticketName.getText().toString().trim().equals("")) {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.name_required), Toast.LENGTH_SHORT).show();
            return;
        }
        if (ticketName.getText().toString().length() > 30) {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.max_length_30), Toast.LENGTH_SHORT).show();
            return;
        }
        EditText ticketGain = (EditText) activity.findViewById(R.id.add_ticket_gain);
        Status s = new Select().from(Status.class).where("status = ?", "Active").executeSingle();

        double gain = 0;
        try {
            gain = Double.parseDouble(ticketGain.getText().toString());
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.possible_gain_double), Toast.LENGTH_SHORT).show();
            return;
        }
        if (array.getAllMatches().size() == 0) {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.cant_save_without_matches), Toast.LENGTH_SHORT).show();
            return;
        } else if (array.getAllMatches().size() > 20) {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.max_matches_per_ticket), Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentTicket == null) {
            currentTicket = new Ticket(ticketName.getText().toString(), s, gain);
        } else {
            currentTicket.ticketName = ticketName.getText().toString();
            currentTicket.possibleGain = gain;
        }
        currentTicket.save();
        List<Match> originalList = new Select().from(Match.class).where("ticket = ?", currentTicket.getId()).execute();
        ActiveAndroid.beginTransaction();
        try {
            if (originalList.size() > 0) {
                originalList.removeAll(array.getAllMatches());
                if (originalList.size() > 0) {
                    for (Match m : originalList) {
                        m.delete();
                    }
                }
            }

            for (Match m : array.getAllMatches()) {
                m.ticket = currentTicket;
                m.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        if (activity instanceof MainActivity) {
            BackstackHelper.isFragmentBackPressed(activity);
        } else if (activity instanceof TicketDetailActivity) {
            activity.getSupportFragmentManager().popBackStack();
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(currentTicket.ticketName);
            FrameLayout statusFrame = (FrameLayout) activity.findViewById(R.id.statusPanel);
            TextView statusValue = (TextView) activity.findViewById(R.id.detailStatus);
            statusValue.setText(currentTicket.status.status);
            statusFrame.setBackgroundColor(activity.getResources().getColor(StatusHelper.getStatusColor(currentTicket.status.status, activity.getApplicationContext())));
            statusFrame.setVisibility(View.VISIBLE);
            for (int i = 0; i < myMenu.size(); i++)
                myMenu.getItem(i).setVisible(true);
        }

        if (GlobalStaticValuesHelper.SYNC_TICKET_AFTER_SAVE)
            new SyncTask(activity, false, activity.getApplicationContext()).execute();
    }
}
