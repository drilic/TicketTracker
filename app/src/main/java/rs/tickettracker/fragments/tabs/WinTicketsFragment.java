package rs.tickettracker.fragments.tabs;

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
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.adapters.TicketListAdapter;
import rs.tickettracker.helpers.ListViewActionHelper;


public class WinTicketsFragment extends ListFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    TicketListAdapter arrayAdapter;
    TabFragmentAdapter tabMenager;

    public WinTicketsFragment(TabFragmentAdapter test) {
        // Required empty public constructor
        this.tabMenager = test;
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
        arrayAdapter = new TicketListAdapter(getActivity(), R.layout.list_ticket_view, Ticket.getAllWin());
        setListAdapter(arrayAdapter);
        getListView().addFooterView(getLayoutInflater(savedInstanceState).inflate(R.layout.list_footer_view, null), null, false);
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), TicketDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void updateAdapter(Ticket t) {
        if (arrayAdapter.contains(t.getId())) {
            arrayAdapter.remove(t);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewActionHelper.longClickAction(getActivity(), view, id, arrayAdapter, tabMenager);
        return true;
    }
}
