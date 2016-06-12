package rs.tickettracker.listeners;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.dialogs.DeleteTicketDialog;

/**
 * Action that is triggered when user click on delete ticket button.
 */
public class DeleteTicketAction implements View.OnClickListener {

    Activity activity;
    long ticketId;
    TabFragmentAdapter tabMenager;
    Button editButton;
    Button deleteButton;

    public DeleteTicketAction(Activity activity, long ticketId, TabFragmentAdapter tabMenager, Button editButton, Button deleteButton) {
        this.activity = activity;
        this.ticketId = ticketId;
        this.tabMenager = tabMenager;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
    }

    @Override
    public void onClick(View v) {
        DeleteTicketDialog dialog = new DeleteTicketDialog(ticketId, tabMenager, editButton, deleteButton);
        dialog.show(activity.getFragmentManager(), "Dialog Fragment");
    }
}
