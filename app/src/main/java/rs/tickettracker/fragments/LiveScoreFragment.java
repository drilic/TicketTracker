package rs.tickettracker.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import model.Match;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.activities.TicketDetailActivity;
import rs.tickettracker.adapters.MatchLiveScoreListAdapter;
import rs.tickettracker.adapters.TicketListAdapter;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.helpers.SyncHelper;


public class LiveScoreFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private View globalView;

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
        ComponentsHelper.createSpinner(date, view, R.id.date, 4);

        globalView = view;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> selections = sharedPreferences.getStringSet(getResources().getString(R.string.pref_leagues_list_type), null);
        for (String checkedLeague : selections) {
            consultPreferencies(checkedLeague);
        }
    }

    private void consultPreferencies(String leagueName) {
        ListView listView = null;
        TextView textView = null;
        MatchLiveScoreListAdapter arrayAdapter = null;
        List<Match> matches = null;
        switch (leagueName) {
            case "premier_league":
                listView = (ListView) globalView.findViewById(R.id.premierList);
                textView = (TextView) globalView.findViewById(R.id.premierText);
                matches = SyncHelper.findAllMatchesForLeague(9,398);
                arrayAdapter = new MatchLiveScoreListAdapter(getActivity(), R.layout.list_match_live_score_view, matches);
                listView.setAdapter(arrayAdapter);
                ComponentsHelper.setDynamicHeight(listView);
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                break;
            case "ligue_1":
                listView = (ListView) globalView.findViewById(R.id.ligue1List);
                textView = (TextView) globalView.findViewById(R.id.ligue1Text);
                matches = SyncHelper.findAllMatchesForLeague(9,396);
                arrayAdapter = new MatchLiveScoreListAdapter(getActivity(), R.layout.list_match_live_score_view, matches);
                listView.setAdapter(arrayAdapter);
                ComponentsHelper.setDynamicHeight(listView);
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                break;
            case "primera_division":
                listView = (ListView) globalView.findViewById(R.id.primeraList);
                textView = (TextView) globalView.findViewById(R.id.primeraText);
                matches = SyncHelper.findAllMatchesForLeague(9,399);
                arrayAdapter = new MatchLiveScoreListAdapter(getActivity(), R.layout.list_match_live_score_view, matches);
                listView.setAdapter(arrayAdapter);
                ComponentsHelper.setDynamicHeight(listView);
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                break;
            case "seria_a":
                listView = (ListView) globalView.findViewById(R.id.seriaAList);
                textView = (TextView) globalView.findViewById(R.id.seriaAText);
                matches = SyncHelper.findAllMatchesForLeague(9,401);
                arrayAdapter = new MatchLiveScoreListAdapter(getActivity(), R.layout.list_match_live_score_view, matches);
                listView.setAdapter(arrayAdapter);
                ComponentsHelper.setDynamicHeight(listView);
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                break;
            case "bundesliga":
                listView = (ListView) globalView.findViewById(R.id.bundesList);
                textView = (TextView) globalView.findViewById(R.id.bundesText);
                matches = SyncHelper.findAllMatchesForLeague(9,394);
                arrayAdapter = new MatchLiveScoreListAdapter(getActivity(), R.layout.list_match_live_score_view, matches);
                listView.setAdapter(arrayAdapter);
                ComponentsHelper.setDynamicHeight(listView);
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                break;
        }
    }


}
