package rs.tickettracker.helpers;

import android.app.Activity;
import android.preference.PreferenceManager;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;

import java.io.File;
import java.util.Date;
import java.util.List;

import model.Bet;
import model.League;
import model.Match;
import model.Status;
import model.Ticket;
import rs.tickettracker.R;

/**
 * Created by gisko on 02-May-16.
 */
public class DatabaseHelper {

    public static void initializeDB(Activity activity) {
        String db_name = activity.getResources().getString(R.string.db_name);
        File dbFile = activity.getDatabasePath(db_name);
        if (dbFile.exists()) {
            Configuration dbConfiguration = new Configuration.Builder(activity).setDatabaseName(db_name).create();
            ActiveAndroid.initialize(dbConfiguration);
        } else {
            PreferenceManager.setDefaultValues(activity, R.xml.preferences, false);
            Configuration dbConfiguration = new Configuration.Builder(activity).setDatabaseName(db_name).create();
            ActiveAndroid.initialize(dbConfiguration);
            populateDB();
        }
    }

    public static void populateDB() {
        Bet bet = new Bet("1");
        bet.save();
        bet = new Bet("X");
        bet.save();
        bet = new Bet("2");
        bet.save();

        Status status = new Status("Active");
        status.save();
        status = new Status("Win");
        status.save();
        status = new Status("Lose");
        status.save();


        League league = new League("Bundesliga", 394);
        league.save();

        league = new League("Ligue 1", 396);
        league.save();

        league = new League("Premier League", 398);
        league.save();

        league = new League("Primera Division", 399);
        league.save();

        league = new League("Seria A", 401);
        league.save();

    }
}
