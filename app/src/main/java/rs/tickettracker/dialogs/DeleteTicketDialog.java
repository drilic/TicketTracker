package rs.tickettracker.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.fragments.tabs.ActiveTicketsFragment;
import rs.tickettracker.fragments.tabs.AllTicketsFragment;
import rs.tickettracker.fragments.tabs.LoseTicketsFragment;
import rs.tickettracker.fragments.tabs.WinTicketsFragment;

/**
 * Confirmation dialog for deleting tickets.
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
        builder.setTitle(getActivity().getResources().getString(R.string.confirm));
        final Ticket t = Ticket.load(Ticket.class, ticketId);
        builder.setMessage(getActivity().getResources().getString(R.string.are_you_sure_to_delete)+" " + t.ticketName + "?");
        builder.setPositiveButton(getActivity().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                removeTicketFromTab(t);
                t.delete();
                dialog.dismiss();
            }

        });
        builder.setNegativeButton(getActivity().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

    /**
     * Delete ticker from Ticket list on each tab, based on status of ticket.
     * @param t - that need to be deleted.
     */
    private void removeTicketFromTab(Ticket t) {
        switch (t.status.status) {
            case "Active":
                if (tabMenager.getFragmentByPosition(1) != null)
                    ((ActiveTicketsFragment) tabMenager.getFragmentByPosition(1)).updateAdapter(t);
                break;
            case "Win":
                if (tabMenager.getFragmentByPosition(2) != null)
                    ((WinTicketsFragment) tabMenager.getFragmentByPosition(2)).updateAdapter(t);
                break;
            case "Lose":
                if (tabMenager.getFragmentByPosition(3) != null)
                    ((LoseTicketsFragment) tabMenager.getFragmentByPosition(3)).updateAdapter(t);
                break;
        }
        if (tabMenager.getFragmentByPosition(0) != null)
            ((AllTicketsFragment) tabMenager.getFragmentByPosition(0)).updateAdapter(t);
    }
}
