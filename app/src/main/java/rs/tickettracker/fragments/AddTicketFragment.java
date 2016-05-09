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

import java.util.ArrayList;

import model.Match;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchAddTicketListAdapter;
import rs.tickettracker.listeners.OpenModalAction;
import rs.tickettracker.listeners.SaveTicketAction;
import rs.tickettracker.listeners.interfaces.GetMatchFromDialogListener;

/**
 * Created by gisko on 27-Apr-16.
 */
public class AddTicketFragment extends ListFragment implements AdapterView.OnItemClickListener, GetMatchFromDialogListener {

    MatchAddTicketListAdapter arrayAdapter=null;

    public AddTicketFragment() {
        // Required empty public constructor
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
//        List<Match> myList = new Select().from(Match.class).execute();
        arrayAdapter = new MatchAddTicketListAdapter(getActivity(), R.layout.list_match_add_ticket, new ArrayList<Match>());
        setListAdapter(arrayAdapter);
        getListView().setOnItemClickListener(this);

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
        saveTicketFab.setOnClickListener(new SaveTicketAction((AppCompatActivity) getActivity(),arrayAdapter));
        saveTicketFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Save current ticket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

    }

    private void getMatch(Match m) {
        Log.i("***", m.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: EDIT/DELETE
    }

    @Override
    public void getMatchFromDialog(Match match) {
        Log.i("****", match.toString() + " Bet: " + match.bet.betName);
        arrayAdapter.add(match);
    }
}
