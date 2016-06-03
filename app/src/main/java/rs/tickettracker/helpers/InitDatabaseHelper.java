package rs.tickettracker.helpers;

import android.app.Activity;
import android.content.Context;
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
public class InitDatabaseHelper {

    public static void initDB() {
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

        league = new League("Champions League", 405);
        league.save();

        league = new League("EURO 2016", 424);
        league.save();

    }

    public static void fillDummyTickets(int numOfTickets){
        ActiveAndroid.beginTransaction();
        Status s = new Select().from(Status.class).where("status = ?", "Active").executeSingle();
        try {
            for (int i = 0; i < numOfTickets; i++) {
                Ticket t = new Ticket();
                t.ticketName = "Dummy " + i;
                t.status = s;
                t.possibleGain = 1000d;
                t.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
