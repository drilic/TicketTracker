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
import rs.tickettracker.helpers.LiveScoreApiHelper;
import rs.tickettracker.helpers.SyncHelper;

/**
 * Get all ticket from football API for chosen leagues (from settings) and for chosen day. Using
 * mobile data or WiFi network - depends from settings.
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
        dialog.setMessage(activity.getResources().getString(R.string.loading_matches));
        dialog.show();
        if (SyncHelper.getConnectivityStatus(activity.getApplicationContext())) {
            String[] leagues = activity.getResources().getStringArray(R.array.pref_leagues_values);
            for (String league : leagues) {
                setLayoutParameters(league, new ArrayList<Match>());
            }
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
        if (SyncHelper.getConnectivityStatus(activity.getApplicationContext())) {
            if (showMessage) {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_any_matches_for_league), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.check_settings_or_net_conn), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected HashMap<String, List<Match>> doInBackground(Object... params) {
        Set<String> selectedLeagues = (Set<String>) params[0];
        int day = (int) params[1];
        HashMap<String, List<Match>> retVal = new HashMap<String, List<Match>>();
        if (SyncHelper.getConnectivityStatus(activity.getApplicationContext())) {
            for (String s : selectedLeagues) {
                retVal.put(s, getMatches(s, day));
            }
        }
        return retVal;
    }

    /**
     * Get matches from Live score helper.
     *
     * @param leagueName - league name from settings
     * @param day        - day of the match
     * @return List of matches from current league and for chosen day.
     */
    private List<Match> getMatches(String leagueName, int day) {
        List<Match> matches = null;
        switch (leagueName) {
            case "premier_league":
                matches = LiveScoreApiHelper.findAllMatchesForLeague(day, 398);
                break;
            case "ligue_1":
                matches = LiveScoreApiHelper.findAllMatchesForLeague(day, 396);
                break;
            case "primera_division":
                matches = LiveScoreApiHelper.findAllMatchesForLeague(day, 399);
                break;
            case "seria_a":
                matches = LiveScoreApiHelper.findAllMatchesForLeague(day, 401);
                break;
            case "bundesliga":
                matches = LiveScoreApiHelper.findAllMatchesForLeague(day, 394);
                break;
            case "champions":
                matches = LiveScoreApiHelper.findAllMatchesForLeague(day, 405);
                break;
            case "euro_2016":
                matches = LiveScoreApiHelper.findAllMatchesForLeague(day, 424);
                break;

        }
        return matches;
    }

    /**
     * Settup layout for live score page. Fill listViews and display text for each leagues.
     *
     * @param leagueName - league name from settings
     * @param matches    - list of found matches for league
     * @return True if some matches exists.
     */
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
            case "champions":
                listView = (ListView) activity.findViewById(R.id.championsList);
                textView = (TextView) activity.findViewById(R.id.championsText);
                break;
            case "euro_2016":
                listView = (ListView) activity.findViewById(R.id.euroList);
                textView = (TextView) activity.findViewById(R.id.euroText);
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

    /**
     * Show/Hide all list views depends from found matches.
     *
     * @param show     - Show/Hide listView and textView
     * @param listView - to Show/Hide
     * @param textView - to Show/Hide
     */
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
