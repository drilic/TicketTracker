package rs.tickettracker.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import model.Bet;
import model.League;
import model.Match;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.adapters.TicketListAdapter;
import rs.tickettracker.asyncTasks.GetMatchesTask;
import rs.tickettracker.fragments.tabs.ActiveTicketsFragment;
import rs.tickettracker.fragments.tabs.LoseTicketsFragment;
import rs.tickettracker.fragments.tabs.WinTicketsFragment;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.listeners.interfaces.GetMatchFromDialogListener;

/**
 * Created by gisko on 26-Apr-16.
 */
public class DeleteTicketDialog extends DialogFragment {

    long ticketId;
    TicketListAdapter arrayAdapter;
    TabFragmentAdapter tabMenager;
    Button editButton;
    Button deleteButton;

    public DeleteTicketDialog(long ticketId, TicketListAdapter arrayAdapter, TabFragmentAdapter tabMenager, Button editButton, Button deleteButton) {
        this.ticketId = ticketId;
        this.arrayAdapter = arrayAdapter;
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
        builder.setTitle("Confirm");
        final Ticket t = Ticket.load(Ticket.class, ticketId);
        builder.setMessage("Are you sure that u want to delete " + t.ticketName + "?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                arrayAdapter.remove(t);
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                getTab(t);
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

    private void getTab(Ticket t) {
        switch (t.status.status) {
            case "Active":
                ((ActiveTicketsFragment) tabMenager.getFragmentByPosition(1)).updateAdapter(t);
                break;
            case "Win":
                ((WinTicketsFragment) tabMenager.getFragmentByPosition(2)).updateAdapter(t);
                break;
            case "Lose":
                ((LoseTicketsFragment) tabMenager.getFragmentByPosition(3)).updateAdapter(t);
                break;
        }
    }
}
