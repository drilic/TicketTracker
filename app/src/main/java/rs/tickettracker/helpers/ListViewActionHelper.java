package rs.tickettracker.helpers;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import rs.tickettracker.R;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.adapters.TicketListAdapter;
import rs.tickettracker.listeners.DeleteTicketAction;
import rs.tickettracker.listeners.EditTicketAction;

/**
 * Created by gisko on 09-May-16.
 */
public class ListViewActionHelper {

    public static void longClickAction(Activity activity, View view, long id, TicketListAdapter arrayAdapter, TabFragmentAdapter
            tabMenager) {
        final Button editButton = (Button) view.findViewById(R.id.editTicketButton);
        final Button deleteButton = (Button) view.findViewById(R.id.deleteTicketButton);
        if (editButton.getVisibility() == View.VISIBLE) {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            return;
        } else {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
        final long ticketId = id;
        editButton.setOnClickListener(new EditTicketAction());
        deleteButton.setOnClickListener(new DeleteTicketAction(activity, id, arrayAdapter, tabMenager, editButton, deleteButton));
    }
}
