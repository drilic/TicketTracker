package rs.tickettracker.helpers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
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

    public static void longClickAction(Activity activity, View view, long id, TabFragmentAdapter
            tabManager) {
        Button editButton = (Button) view.findViewById(R.id.editTicketButton);
        Button deleteButton = (Button) view.findViewById(R.id.deleteTicketButton);
        if (editButton.getVisibility() == View.VISIBLE) {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            return;
        } else {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
        editButton.setOnClickListener(new EditTicketAction((AppCompatActivity) activity, id));
        deleteButton.setOnClickListener(new DeleteTicketAction(activity, id, tabManager, editButton, deleteButton));
    }
}
