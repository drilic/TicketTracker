package rs.tickettracker.sync.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import model.Match;
import rs.tickettracker.R;
import rs.tickettracker.helpers.LiveScoreApiHelper;

/**
 * Used as async task to get all matches for desired league on chosen day.
 */
public class GetMatchesTask extends AsyncTask<Object, Void, List<Match>> {

    Activity activity = null;
    ProgressDialog dialog = null;
    View view = null;
    MaterialSpinner spinner = null;
    int selectedMatch = -1;

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
            dialog.setMessage(activity.getResources().getString(R.string.loading_matches));
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
        if (selectedMatch != -1)
            spinner.setSelection(selectedMatch);
    }

    @Override
    protected List<Match> doInBackground(Object... params) {
        long leagueId = (long) params[0];
        int day = (int) params[1];
        try {
            selectedMatch = (int) params[2];
        } catch (Exception e) {
            selectedMatch = -1;
        }
        return LiveScoreApiHelper.findAllMatchesForLeague(day, leagueId);
    }

}
