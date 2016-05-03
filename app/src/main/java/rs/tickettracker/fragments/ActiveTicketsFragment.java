package rs.tickettracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.activities.TicketDetailActivity;
import rs.tickettracker.adapters.TicketListAdapter;


public class ActiveTicketsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public ActiveTicketsFragment() {
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
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TicketListAdapter arrayAdapter = new TicketListAdapter(getActivity(), R.layout.list_ticket_view, Ticket.getAllActive());
        setListAdapter(arrayAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), TicketDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
