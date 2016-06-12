package rs.tickettracker.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.activities.MainActivity;
import rs.tickettracker.activities.TicketDetailActivity;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.adapters.TicketListAdapter;
import rs.tickettracker.fragments.interfaces.FragmentUpdateInterface;
import rs.tickettracker.sync.tasks.GetTicketFromDBTask;

/**
 * Lose tickets fragment used one of four tab fragments where are placed all lose tickets from
 * database.
 */
public class LoseTicketsFragment extends ListFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, FragmentUpdateInterface {

    TicketListAdapter arrayAdapter;
    TabFragmentAdapter tabMenager;

    public LoseTicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
            if (MainActivity.NEED_SYNC) {
                for (Fragment f : getParentFragment().getChildFragmentManager().getFragments()) {
                    if (f != null && f instanceof FragmentUpdateInterface)
                        ((FragmentUpdateInterface) f).reloadTicketAdapter();
                }
                MainActivity.NEED_SYNC = false;
            }
        }
    }

    public LoseTicketsFragment(TabFragmentAdapter tabManager) {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Ticket> tickets = null;
        try {
            tickets = new GetTicketFromDBTask(getActivity()).execute("lose").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        arrayAdapter = new TicketListAdapter(getActivity(), R.layout.list_ticket_view, tickets, tabMenager);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        List<Long> selectedIds = TicketListAdapter.selectedIds;
        if (selectedIds.contains(id)) {
            selectedIds.remove(id);
        } else {
            selectedIds.add(id);
        }
        arrayAdapter.notifyDataSetChanged();
        ((MainTabFragment)getParentFragment()).reloadTicketAdapter();
        return true;
    }

    /**
     * Remove ticket from adapter.
     * @param t - ticket that need to be removed.
     */
    public void updateAdapter(Ticket t) {
        if (arrayAdapter.contains(t.getId())) {
            arrayAdapter.remove(t);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (arrayAdapter != null) {
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

    @Override
    public void reloadTicketAdapter() {
        if (arrayAdapter != null) {
            arrayAdapter.clear();
            List<Ticket> tickets = null;
            try {
                tickets = new GetTicketFromDBTask(getActivity()).execute("lose").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            arrayAdapter.addAll(tickets);
        }
    }
}
