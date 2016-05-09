package rs.tickettracker.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import model.Ticket;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.adapters.TicketListAdapter;
import rs.tickettracker.dialogs.DeleteTicketDialog;
import rs.tickettracker.fragments.tabs.ActiveTicketsFragment;
import rs.tickettracker.fragments.tabs.LoseTicketsFragment;
import rs.tickettracker.fragments.tabs.WinTicketsFragment;

/**
 * Created by gisko on 09-May-16.
 */
public class DeleteTicketAction implements View.OnClickListener {

    Activity activity;
    long ticketId;
    TicketListAdapter arrayAdapter;
    TabFragmentAdapter tabMenager;
    Button editButton;
    Button deleteButton;

    public DeleteTicketAction(Activity activity, long ticketId, TicketListAdapter arrayAdapter, TabFragmentAdapter tabMenager, Button editButton, Button deleteButton) {
        this.activity = activity;
        this.ticketId = ticketId;
        this.arrayAdapter = arrayAdapter;
        this.tabMenager = tabMenager;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
    }

    @Override
    public void onClick(View v) {
        DeleteTicketDialog dialog = new DeleteTicketDialog(ticketId, arrayAdapter, tabMenager, editButton, deleteButton);
        dialog.show(activity.getFragmentManager(), "Dialog Fragment");
    }
}
