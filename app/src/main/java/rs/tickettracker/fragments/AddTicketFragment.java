package rs.tickettracker.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import model.Match;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchAddTicketListAdapter;
import rs.tickettracker.listeners.EditTicketAction;
import rs.tickettracker.listeners.OpenModalAction;
import rs.tickettracker.listeners.SaveTicketAction;
import rs.tickettracker.listeners.interfaces.GetMatchFromDialogListener;

/**
 * Created by gisko on 27-Apr-16.
 */
public class AddTicketFragment extends ListFragment implements GetMatchFromDialogListener,
        AdapterView.OnItemLongClickListener {

    MatchAddTicketListAdapter arrayAdapter = null;
    long ticketId;
    Ticket myTicket;

    public AddTicketFragment() {
        // Required empty public constructor
        this.ticketId = -1; //for EDIT
    }

    public AddTicketFragment(long id) {
        // Required empty public constructor
        this.ticketId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_ticket, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ticketId == -1) {
            arrayAdapter = new MatchAddTicketListAdapter(getActivity(), R.layout.list_match_add_ticket, new ArrayList<Match>());
        } else {
            myTicket = Ticket.load(Ticket.class, ticketId);
            List<Match> matches = new Select().from(Match.class).where("ticket = ?", myTicket.getId()).execute();
            arrayAdapter = new MatchAddTicketListAdapter(getActivity(), R.layout.list_match_add_ticket, matches);
            EditText ticketName = (EditText) view.findViewById(R.id.add_ticket_name);
            EditText ticketGain = (EditText) view.findViewById(R.id.add_ticket_gain);
            ticketName.setText(myTicket.ticketName);
            ticketGain.setText(String.valueOf(myTicket.possibleGain));
        }

        setListAdapter(arrayAdapter);
        getListView().addFooterView(getLayoutInflater(savedInstanceState).inflate(R.layout.list_footer_view, null), null, false);
        getListView().setOnItemLongClickListener(this);

        FloatingActionButton addMatchFab = (FloatingActionButton) view.findViewById(R.id.add_match_fab);
        addMatchFab.setOnClickListener(new OpenModalAction((AppCompatActivity) getActivity(), this));
        addMatchFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Add new match on ticket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

        FloatingActionButton saveTicketFab = (FloatingActionButton) view.findViewById(R.id.save_ticket_fab);
        saveTicketFab.setOnClickListener(new SaveTicketAction((AppCompatActivity) getActivity(), arrayAdapter, myTicket));
        saveTicketFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Save current ticket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

    }

    @Override
    public void getMatchFromDialog(Match match) {
        arrayAdapter.add(match);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Button deleteButton = (Button) view.findViewById(R.id.deleteMatchButton);
        if (deleteButton.getVisibility() == View.VISIBLE) {
            deleteButton.setVisibility(View.GONE);
            return true;
        } else {
            deleteButton.setVisibility(View.VISIBLE);
        }
        Match tempMatch = null;
        if (id != -1) {
            tempMatch = Match.load(Match.class, id);
        }
        final Match m = tempMatch;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m == null) {
                    arrayAdapter.removeByPosition(position);
                } else {
                    arrayAdapter.remove(m);
                }
                deleteButton.setVisibility(View.GONE);

            }
        });

        return true;
    }
}
