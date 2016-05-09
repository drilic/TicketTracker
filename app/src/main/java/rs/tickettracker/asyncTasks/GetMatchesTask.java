package rs.tickettracker.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;
import model.Match;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchLiveScoreListAdapter;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.helpers.LiveScoreAPIHelper;

/**
 * Created by gisko on 04-May-16.
 */
public class GetMatchesTask extends AsyncTask<Object, Void, List<Match>> {

    Activity activity = null;
    ProgressDialog dialog = null;
    View view = null;
    MaterialSpinner spinner = null;

    public GetMatchesTask(Activity activity, View view, MaterialSpinner spinner, Boolean useDialog) {
        this.activity = activity;
        if (useDialog)
            this.dialog = new ProgressDialog(activity);
        this.view = view;
        this.spinner = spinner;
    }

    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.setMessage("Loading matches...");
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(List<Match> matches) {
        ArrayAdapter<Match> adapter = new ArrayAdapter<Match>(view.getContext(), android.R.layout.simple_spinner_item, matches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected List<Match> doInBackground(Object... params) {
        long leagueId = (long) params[0];
        int day = (int) params[1];
        return LiveScoreAPIHelper.findAllMatchesForLeague(day, leagueId);
    }

}
