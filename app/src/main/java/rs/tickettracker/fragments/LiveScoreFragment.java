package rs.tickettracker.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;
import model.Match;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchLiveScoreListAdapter;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.helpers.SyncHelper;
import rs.tickettracker.sync.LiveScoreTask;


public class LiveScoreFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    public LiveScoreFragment() {
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
        return inflater.inflate(R.layout.fragment_live_score, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] date = {"-3", "-2", "-1", "Today", "+1", "+2", "+3"};
        final MaterialSpinner spinner = ComponentsHelper.createSpinner(date, view, R.id.date, 4);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final Set<String> selections = sharedPreferences.getStringSet(getResources().getString(R.string.pref_leagues_list_type), null);

        spinner.post(new Runnable() {
            public void run() {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        int day = 0;
                        if (position == -1) {
                            return;
                        } else {
                            if (position > 3) {
                                day = position - 2;
                            } else if (position == 3) {
                                day = 1;
                            } else {
                                day = position - 3;
                            }
                        }

                        new LiveScoreTask(getActivity()).execute(selections, day);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
            }
        });
    }

}
