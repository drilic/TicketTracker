package rs.tickettracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.activeandroid.query.Select;

import model.Match;
import model.Status;
import model.Ticket;
import rs.tickettracker.R;

/**
 * Created by gisko on 08-May-16.
 */
public class SyncHelper {

    public static void updateTicket(Ticket t) {
        Status lose = new Select().from(Status.class).where("status = ?", "Lose").executeSingle();
        Status win = new Select().from(Status.class).where("status = ?", "Win").executeSingle();
        if (t.status.status.equals("Active")) {
            for (Match m : t.matches()) {
                LiveScoreAPIHelper.getMatchUpdate(m.matchServisId, m.getId());
            }
            for (Match m : t.matches()) {
                if (m.isFinished) {
                    boolean checkResult = checkResults(m);
                    if (checkResult) {
                        m.status = win;
                    } else {
                        m.status = lose;
                    }
                    m.save();
                }
            }
        }

        StatusHelper ticketStatus = checkTickets(t);
        if (!ticketStatus.notFinished) {
            if (ticketStatus.lose) {
                t.status = lose;
                t.save();
            } else {
                t.status = win;
                t.save();
            }
        }
    }

    public static boolean checkResults(Match m) {
        switch (m.bet.betName) {
            case "1":
                return isHomeWin(m);
            case "X":
                return isDraw(m);
            case "2":
                return isAwayWIn(m);
        }
        return false;
    }

    public static boolean isDraw(Match m) {
        if (m.homeScore == m.awayScore)
            return true;
        return false;
    }

    public static boolean isHomeWin(Match m) {
        if (m.homeScore > m.awayScore)
            return true;
        return false;
    }

    public static boolean isAwayWIn(Match m) {
        if (m.homeScore < m.awayScore)
            return true;
        return false;
    }

    public static StatusHelper checkTickets(Ticket t) {
        StatusHelper helper = new StatusHelper();
        for (Match m : t.matches()) {
            if (m.isFinished) {
                if (m.status.status.equals("Win")) {
                    helper.win = true;
                } else {
                    helper.lose = true;
                }
            } else {
                helper.notFinished = true;
                return helper;
            }
        }
        return helper;
    }

    static class StatusHelper {
        boolean notFinished = false;
        boolean lose = false;
        boolean win = false;
    }

    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean allowSync = sharedPreferences.getBoolean(context.getResources().getString(R.string.pref_sync), false);
        String syncConnectionType = sharedPreferences.getString("pref_sync_type", "Both");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        /*if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;*/
        return false;
    }

    public static int calculateTimeTillNextSync(int minutes) {
        return 1000 * 60 * minutes;
    }
}
