package rs.tickettracker.sync.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import model.Ticket;
import rs.tickettracker.adapters.TicketListAdapter;

/**
 * Get all tickets from database asynchronously.
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
