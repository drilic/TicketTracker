package rs.tickettracker.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;
import rs.tickettracker.R;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.sync.tasks.LiveScoreTask;


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
    public void onResume() {
        super.onResume();
        //TODO: Reload live sync-a na on resume
        //TODO: IF size is 0 throw Toast
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] date = getResources().getStringArray(R.array.date_list);
        final MaterialSpinner spinner = ComponentsHelper.createSpinner(date, view, R.id.date, 4);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> selections = sharedPreferences.getStringSet(getResources().getString(R.string.pref_leagues_list_type), null);
        final Set<String> selectedList = selections;
        spinner.post(new Runnable() {
            public void run() {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        int day = 1;
                        if (position != -1)
                            day = Integer.parseInt(getResources().getStringArray(R.array.date_values_list)[position]);
                        new LiveScoreTask(getActivity()).execute(selectedList, day);
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
