package rs.tickettracker.sync.tasks;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import model.Match;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.TicketListAdapter;
import rs.tickettracker.helpers.LiveScoreAPIHelper;

/**
 * Created by gisko on 04-May-16.
 */
public class GetTicketFromDBTask extends AsyncTask<Object, Void, List<Ticket>> {

    Activity activity = null;
    TicketListAdapter arrayAdapter;

    public GetTicketFromDBTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Ticket> doInBackground(Object... params) {
        String ticketType = (String) params[0];
        switch (ticketType) {
            case "all":
                return Ticket.getAll();
            case "active":
                return Ticket.getAllActive();
            case "win":
                return Ticket.getAllWin();
            case "lose":
                return Ticket.getAllLose();
        }
        return Ticket.getAll();
    }

}
