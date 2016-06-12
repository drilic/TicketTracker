package rs.tickettracker.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.activeandroid.query.Select;

import model.Match;
import model.Status;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.activities.MainActivity;

/**
 * This helper class is used for sync methods.
 */
public class SyncHelper {

    /**
     * Update ticket with new downloaded results.
     * @param t - current ticket that need to be updated.
     * @param showNotification - boolean variable that chose does notification need to be shown.
     * @param context - current context.
     */
    public static void updateTicket(Ticket t, boolean showNotification, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Status lose = new Select().from(Status.class).where("status = ?", "Lose").executeSingle();
        Status win = new Select().from(Status.class).where("status = ?", "Win").executeSingle();
        boolean showGoalsNotification = false;
        boolean allowNotification = false;
        if (showNotification) {
            allowNotification = sharedPreferences.getBoolean(context.getResources().getString(R.string.pref_notification), false);
            if (allowNotification) {
                String notificationType = sharedPreferences.getString("pref_notification_list_type", "all");
                if (notificationType.equals("all")) {
                    showGoalsNotification = true;
                }
            }
        }
        if (t.status.status.equals("Active")) {
            for (Match m : t.matches()) {
                LiveScoreApiHelper.getMatchUpdate(t.getId(), m.matchServisId, m.getId(), showGoalsNotification, context);
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
                if (showNotification) {
                    allowNotification = sharedPreferences.getBoolean(context.getResources().getString(R.string.pref_notification), false);
                    if (allowNotification) {
                        Intent ints = new Intent(MainActivity.SYNC_DATA);
                        ints.putExtra("ticketId", t.getId());
                        ints.putExtra("MESSAGE_TEXT", "You lose ticket " + t.ticketName + ".");
                        context.sendBroadcast(ints);
                    }
                }
            } else {
                t.status = win;
                t.save();
                if (showNotification) {
                    allowNotification = sharedPreferences.getBoolean(context.getResources().getString(R.string.pref_notification), false);
                    if (allowNotification) {
                        Intent ints = new Intent(MainActivity.SYNC_DATA);
                        ints.putExtra("ticketId", t.getId());
                        ints.putExtra("MESSAGE_TEXT", "You won " + t.possibleGain + " on ticket " + t.ticketName + "!!!");
                        context.sendBroadcast(ints);
                    }
                }
            }
        }
    }

    /**
     * Checking result for current match.
     * @param m - current match.
     * @return True if match result is agree with played bet.
     */
    public static boolean checkResults(Match m) {
        switch (m.bet.betName) {
            case "1":
                return isHomeWin(m);
            case "X":
                return isDraw(m);
            case "2":
                return isAwayWin(m);
        }
        return false;
    }

    /**
     * Check if match is drawn.
     * @param m - current match
     * @return True if match is drawn.
     */
    public static boolean isDraw(Match m) {
        if (m.homeScore == m.awayScore)
            return true;
        return false;
    }

    /**
     * Check if match is won by home team.
     * @param m - current match
     * @return True if match is won by home team.
     */
    public static boolean isHomeWin(Match m) {
        if (m.homeScore > m.awayScore)
            return true;
        return false;
    }

    /**
     * Check if match is won by away team.
     * @param m - current match
     * @return True if match is won by away team.
     */
    public static boolean isAwayWin(Match m) {
        if (m.homeScore < m.awayScore)
            return true;
        return false;
    }

    /**
     * Check status of ticket. If all matches is won, ticket is won.
     * @param t - current ticket.
     * @return status of all matches.
     */
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

    /**
     * Helper class for status of each ticket.
     */
    static class StatusHelper {
        boolean notFinished = false;
        boolean lose = false;
        boolean win = false;
    }

    /**
     * Get conectivity status from mobile phone and check it with settings which user is allowed.
     * @param context - current context
     * @return True if connection is established well.
     */
    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean allowSync = sharedPreferences.getBoolean(context.getResources().getString(R.string.pref_sync), false);
        String syncConnectionType = sharedPreferences.getString("pref_sync_list_type", "both");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (allowSync) {
            switch (syncConnectionType) {
                case "both":
                    if (activeNetwork != null) {
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                            return true;
                    }
                    break;
                case "wifi":
                    if (activeNetwork != null) {
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                            return true;
                    }
                    break;
            }
        }

        return false;
    }

    /**
     * Calculate time untill next sync.
     * @param minutes - Minutes on each will be triggered Sync service.
     * @return Value of minutes in miliseconds.
     */
    public static int calculateTimeTillNextSync(int minutes) {
        return 1000 * 60 * minutes;
    }
}
