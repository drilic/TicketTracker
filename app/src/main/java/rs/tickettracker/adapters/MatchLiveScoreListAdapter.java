package rs.tickettracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import model.Match;
import rs.tickettracker.R;
import rs.tickettracker.helpers.StatusHelper;

/**
 * Created by gisko on 03-May-16.
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
        DateFormat df = new SimpleDateFormat("dd/MM HH:mm");
        String gameStart = df.format(match.gameStart);

        holder.txtTitle.setText(fixTeamName(match.homeTeam) + " - " + fixTeamName(match.awayTeam));
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


    private static String fixTeamName(String teamName) {
        if (teamName.length() > 15) {
            String trimmedTeamName = teamName.substring(0, 15);
            while (trimmedTeamName.charAt(trimmedTeamName.length() - 1) == ' ') {
                trimmedTeamName = trimmedTeamName.substring(0, trimmedTeamName.length() - 1);
            }
            return trimmedTeamName + "...";
        }
        return teamName;
    }
}
