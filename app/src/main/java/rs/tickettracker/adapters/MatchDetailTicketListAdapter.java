package rs.tickettracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Match;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.helpers.StatusHelper;

/**
 * Created by gisko on 03-May-16.
 */
public class MatchDetailTicketListAdapter extends ArrayAdapter<Match> {
    Context context;
    int layoutResourceId;
    List<Match> data = null;

    public MatchDetailTicketListAdapter(Context context, int layoutResourceId, List<Match> objects) {
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
            holder.imgIcon = (ImageView) row.findViewById(R.id.item_icon);
            holder.txtTitle = (TextView) row.findViewById(R.id.matchTeam);
            holder.txtDescription = (TextView) row.findViewById(R.id.description);
            holder.txtScores = (TextView) row.findViewById(R.id.scores);

            row.setTag(holder);
        } else {
            holder = (MatchHolder) row.getTag();
        }

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        Match match = data.get(position);
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

        holder.txtDescription.setText(match.league.leagueName + ", " + "Bet: " + match.bet.betName);
        holder.imgIcon.setImageResource(StatusHelper.getStatusIconType(match.status.status, context));
        if (match.homeScore == -1) {
            holder.txtScores.setText("N : N");
        } else {
            holder.txtScores.setText(match.homeScore + " : " + match.awayScore);
        }

        return row;
    }

    static class MatchHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDescription;
        TextView txtScores;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
}
