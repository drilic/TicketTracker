package rs.tickettracker.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.activeandroid.query.Select;

import model.Match;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchDetailTicketListAdapter;

/**
 * Fragment for ticket details. It shows ticket name, ticket status and all matches played on
 * current ticket. Also, for each match on tickets, shown their details.
 */
public class TicketDetailFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private long ticket_id;

    public TicketDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ticket_id = getArguments().getLong("ticket_id");
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Ticket t = new Select().from(Ticket.class).where("_id= ?", ticket_id).executeSingle();
        MatchDetailTicketListAdapter arrayAdapter = new MatchDetailTicketListAdapter(getActivity(),
                R.layout.list_match_for_ticket_view, Match.getAllMatchesFromTicket(t));
        setListAdapter(arrayAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
