package rs.tickettracker.fragments.tabs;

import android.app.Activity;
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


public class ActiveTicketsFragment extends ListFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    TicketListAdapter arrayAdapter;
    TabFragmentAdapter tabMenager;

    public ActiveTicketsFragment(TabFragmentAdapter tabManager) {
        // Required empty public constructor
        this.tabMenager = tabManager;
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
        arrayAdapter = new TicketListAdapter(getActivity(), R.layout.list_ticket_view, Ticket.getAllActive());
        setListAdapter(arrayAdapter);
        getListView().addFooterView(getLayoutInflater(savedInstanceState).inflate(R.layout.list_footer_view, null), null, false);
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), TicketDetailActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 1000);
    }

    public void updateAdapter(Ticket t) {
        if (arrayAdapter.contains(t.getId())) {
            arrayAdapter.remove(t);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewActionHelper.longClickAction(getActivity(), view, id, tabMenager);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1000) {
            long id = data.getExtras().getLong("retId");
            arrayAdapter.removeById(id);
            arrayAdapter.notifyDataSetChanged();
            Ticket myT = Ticket.load(Ticket.class, id);
            if (myT != null)
                myT.delete();
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
