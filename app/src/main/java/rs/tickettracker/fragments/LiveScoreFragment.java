package rs.tickettracker.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Iterator;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;
import rs.tickettracker.R;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.sync.tasks.LiveScoreTask;


public class LiveScoreFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    static int day = 1;
    Set<String> selections;
    MaterialSpinner spinner;

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
    public void onResume() {
        super.onResume();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> newSelections = sharedPreferences.getStringSet(getResources().getString(R.string.pref_leagues_list_type), null);
        if (day != -100) { //dummy flag
            if (selections.size() != newSelections.size()) {
                selections = newSelections;
                new LiveScoreTask(getActivity()).execute(selections, day);
            } else {
                for (String s : selections) {
                    if (!newSelections.contains(s)) {
                        selections = newSelections;
                        new LiveScoreTask(getActivity()).execute(selections, day);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] date = getResources().getStringArray(R.array.date_list);
        if (savedInstanceState != null) {
            int selected = savedInstanceState.getInt("selected");
            spinner = ComponentsHelper.createSpinner(date, view, R.id.date, selected);
        } else {
            spinner = ComponentsHelper.createSpinner(date, view, R.id.date, 5);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        selections = sharedPreferences.getStringSet(getResources().getString(R.string.pref_leagues_list_type), null);
        spinner.post(new Runnable() {
            public void run() {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position == -1) {
                            day = -100;
                            return;
                        }
                        day = Integer.parseInt(getResources().getStringArray(R.array.date_values_list)[position]);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (spinner != null) {
            int selected = spinner.getSelectedItemPosition();
            outState.putInt("selected", selected);
        }
    }
}
