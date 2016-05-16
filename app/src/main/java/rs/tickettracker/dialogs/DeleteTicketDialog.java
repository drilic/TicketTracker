package rs.tickettracker.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import model.Ticket;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.fragments.tabs.ActiveTicketsFragment;
import rs.tickettracker.fragments.tabs.AllTicketsFragment;
import rs.tickettracker.fragments.tabs.LoseTicketsFragment;
import rs.tickettracker.fragments.tabs.WinTicketsFragment;

/**
 * Created by gisko on 26-Apr-16.
 */
public class DeleteTicketDialog extends DialogFragment {

    long ticketId;
    TabFragmentAdapter tabMenager;
    Button editButton;
    Button deleteButton;

    public DeleteTicketDialog(long ticketId, TabFragmentAdapter tabMenager, Button editButton, Button deleteButton) {
        this.ticketId = ticketId;
        this.tabMenager = tabMenager;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        final Ticket t = Ticket.load(Ticket.class, ticketId);
        builder.setMessage("Are you sure that u want to delete " + t.ticketName + "?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                removeTicketFromTab(t);
                t.delete();
                dialog.dismiss();
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alert = builder.create();
        return alert;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void removeTicketFromTab(Ticket t) {
        switch (t.status.status) {
            case "Active":
                if (((ActiveTicketsFragment) tabMenager.getFragmentByPosition(1)) != null)
                    ((ActiveTicketsFragment) tabMenager.getFragmentByPosition(1)).updateAdapter(t);
                break;
            case "Win":
                if (((WinTicketsFragment) tabMenager.getFragmentByPosition(2)) != null)
                    ((WinTicketsFragment) tabMenager.getFragmentByPosition(2)).updateAdapter(t);
                break;
            case "Lose":
                if (((LoseTicketsFragment) tabMenager.getFragmentByPosition(3)) != null)
                    ((LoseTicketsFragment) tabMenager.getFragmentByPosition(3)).updateAdapter(t);
                break;
        }
        if (((AllTicketsFragment) tabMenager.getFragmentByPosition(0)) != null)
            ((AllTicketsFragment) tabMenager.getFragmentByPosition(0)).updateAdapter(t);
    }
}
