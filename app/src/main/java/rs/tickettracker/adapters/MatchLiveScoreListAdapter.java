package rs.tickettracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import model.Match;
import rs.tickettracker.R;

/**
 * Match adapter list for Matches placed on 'Live score' page. Used as placeholder for matches.
 */
public class MatchLiveScoreListAdapter extends ArrayAdapter<Match> {
    Context context;
    int layoutResourceId;
    List<Match> data = null;

    public MatchLiveScoreListAdapter(Context context, int layoutResourceId, List<Match> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MatchHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MatchHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.matchTeam);
            holder.txtDescription = (TextView) row.findViewById(R.id.description);
            holder.txtScores = (TextView) row.findViewById(R.id.scores);

            row.setTag(holder);
        } else {
            holder = (MatchHolder) row.getTag();
        }

        Match match = data.get(position);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        String gameStart = df.format(match.gameStart);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);

        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            holder.txtTitle.setText(match.homeTeam + " - " + match.awayTeam);
        } else {
            if (x > 30) {
                holder.txtTitle.setText(match.homeTeam + " - " + match.awayTeam);
            } else {
                holder.txtTitle.setText(Match.fixTeamName(match.homeTeam) + " - " + Match.fixTeamName(match.awayTeam));
            }
        }

        holder.txtDescription.setText(match.league.leagueName + ", " + gameStart);
        if (match.homeScore == -1) {
            holder.txtScores.setText("N : N");
        } else {
            holder.txtScores.setText(match.homeScore + " : " + match.awayScore);
        }
        return row;
    }

    static class MatchHolder {
        TextView txtTitle;
        TextView txtDescription;
        TextView txtScores;
    }
}
