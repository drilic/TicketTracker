package rs.tickettracker.listeners;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import model.Match;
import model.Status;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchAddTicketListAdapter;
import rs.tickettracker.dialogs.AddMatchDialog;
import rs.tickettracker.helpers.BackstackHelper;
import rs.tickettracker.helpers.StatusHelper;

/**
 * Created by gisko on 29-Apr-16.
 */
public class SaveTicketAction implements View.OnClickListener {

    AppCompatActivity activity;
    MatchAddTicketListAdapter array;
    Ticket currentTicket;
    static int UPDATE = -1;

    public SaveTicketAction(AppCompatActivity activity, MatchAddTicketListAdapter array, Ticket currentTicket) {
        this.activity = activity;
        this.array = array;
        this.currentTicket = currentTicket;
    }

    @Override
    public void onClick(View v) {
        EditText ticketName = (EditText) activity.findViewById(R.id.add_ticket_name);
        EditText ticketGain = (EditText) activity.findViewById(R.id.add_ticket_gain);
        Status s = new Select().from(Status.class).where("status = ?", "Active").executeSingle();

        double gain = Double.parseDouble(ticketGain.getText().toString());
        if (currentTicket == null) {
            UPDATE = -1;
            currentTicket = new Ticket(ticketName.getText().toString(), s, gain);
        } else {
            UPDATE = 1;
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
        if (UPDATE == -1) {
            BackstackHelper.isFragmentBackPressed(activity);
        } else {
            activity.getSupportFragmentManager().popBackStack();
            activity.getSupportActionBar().setTitle(currentTicket.ticketName);
            FrameLayout statusFrame = (FrameLayout) activity.findViewById(R.id.statusPanel);
            TextView statusValue = (TextView) activity.findViewById(R.id.detailStatus);
            statusValue.setText(currentTicket.status.status);
            statusFrame.setBackgroundColor(activity.getResources().getColor(StatusHelper.getStatusColor(t.status.status, activity.getApplicationContext())));
            statusFrame.setVisibility(View.VISIBLE);
        }

    }
}
