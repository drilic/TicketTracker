package rs.tickettracker.sync.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import model.Match;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchLiveScoreListAdapter;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.helpers.LiveScoreAPIHelper;

/**
 * Created by gisko on 04-May-16.
 */
public class LiveScoreTask extends AsyncTask<Object, Void, HashMap<String, List<Match>>> {

    Activity activity = null;
    ProgressDialog dialog = null;

    public LiveScoreTask(Activity activity) {
        this.activity = activity;
        this.dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading matches...");
        dialog.show();
        String[] leagues = activity.getResources().getStringArray(R.array.pref_leagues_values);
        for (String league : leagues) {
            setLayoutParameters(league, new ArrayList<Match>());
        }
    }

    @Override
    protected void onPostExecute(HashMap<String, List<Match>> matches) {
        boolean showMessage = true;
        for (String s : matches.keySet()) {
            if (setLayoutParameters(s, matches.get(s))) {
                showMessage = false;
            }
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (showMessage) {
            Toast.makeText(activity, "There is not any match for leagues.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected HashMap<String, List<Match>> doInBackground(Object... params) {
        Set<String> selectedLeagues = (Set<String>) params[0];
        int day = (int) params[1];
        HashMap<String, List<Match>> retVal = new HashMap<String, List<Match>>();
        for (String s : selectedLeagues) {
            retVal.put(s, getMatches(s, day));
        }
        return retVal;
    }


    private List<Match> getMatches(String leagueName, int day) {
        List<Match> matches = null;
        switch (leagueName) {
            case "premier_league":
                matches = LiveScoreAPIHelper.findAllMatchesForLeague(day, 398);
                break;
            case "ligue_1":
                matches = LiveScoreAPIHelper.findAllMatchesForLeague(day, 396);
                break;
            case "primera_division":
                matches = LiveScoreAPIHelper.findAllMatchesForLeague(day, 399);
                break;
            case "seria_a":
                matches = LiveScoreAPIHelper.findAllMatchesForLeague(day, 401);
                break;
            case "bundesliga":
                matches = LiveScoreAPIHelper.findAllMatchesForLeague(day, 394);
                break;
        }
        return matches;
    }

    private boolean setLayoutParameters(String leagueName, List<Match> matches) {
        ListView listView = null;
        TextView textView = null;
        switch (leagueName) {
            case "premier_league":
                listView = (ListView) activity.findViewById(R.id.premierList);
                textView = (TextView) activity.findViewById(R.id.premierText);
                break;
            case "ligue_1":
                listView = (ListView) activity.findViewById(R.id.ligue1List);
                textView = (TextView) activity.findViewById(R.id.ligue1Text);
                break;
            case "primera_division":
                listView = (ListView) activity.findViewById(R.id.primeraList);
                textView = (TextView) activity.findViewById(R.id.primeraText);
                break;
            case "seria_a":
                listView = (ListView) activity.findViewById(R.id.seriaAList);
                textView = (TextView) activity.findViewById(R.id.seriaAText);
                break;
            case "bundesliga":
                listView = (ListView) activity.findViewById(R.id.bundesList);
                textView = (TextView) activity.findViewById(R.id.bundesText);
                break;
        }
        if (matches.size() > 0) {
            displayResults(true, listView, textView);
        } else {
            displayResults(false, listView, textView);
            return false;
        }
        MatchLiveScoreListAdapter arrayAdapter = new MatchLiveScoreListAdapter(activity, R.layout.list_match_live_score_view, matches);
        listView.setAdapter(arrayAdapter);
        ComponentsHelper.setDynamicHeight(listView);
        return true;
    }

    private void displayResults(Boolean show, ListView listView, TextView textView) {
        if (show) {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }
}
