package rs.tickettracker.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.tickettracker.R;
import rs.tickettracker.dialogs.AddMatchDialog;

/**
 * Created by gisko on 27-Apr-16.
 */
public class AddTicketFragment extends Fragment {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ticket, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton addMatchFab = (FloatingActionButton) view.findViewById(R.id.add_match_fab);
        addMatchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMatchDialog amd = new AddMatchDialog();
                // Show DialogFragment
                amd.show(getActivity().getFragmentManager(), "Dialog Fragment");
            }
        });
        addMatchFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Add new match on ticket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

        FloatingActionButton saveTicketFab = (FloatingActionButton) view.findViewById(R.id.save_ticket_fab);
        saveTicketFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Save current ticket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

    }

}
