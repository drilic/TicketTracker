package rs.tickettracker.helpers;

import android.app.Activity;

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

    public static void initializeDB(Activity activity){
        String db_name=activity.getResources().getString(R.string.db_name);
        File dbFile = activity.getDatabasePath(db_name);
        if(dbFile.exists()){
            Configuration dbConfiguration = new Configuration.Builder(activity).setDatabaseName(db_name).create();
            ActiveAndroid.initialize(dbConfiguration);
        }else{
            Configuration dbConfiguration = new Configuration.Builder(activity).setDatabaseName(db_name).create();
            ActiveAndroid.initialize(dbConfiguration);
            populateDB();
        }
    }

    public static void populateDB(){
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

        League l = new League("Test league", 12223);
        l.save();

        List<Status> s = new Select().from(Status.class).where("status = ?", "Active").execute();
        Ticket t = new Ticket("Singlirao Arsenal", s.get(0), 55.5);
        t.save();

        List<League> leag = new Select().from(League.class).where("leagueName = ?", "Test league").execute();
        List<Bet> myBet = new Select().from(Bet.class).where("betName = ?", "1").execute();
        List<Ticket> myTick = new Select().from(Ticket.class).where("ticketName = ?", "Singlirao Arsenal").execute();
        Match m = new Match("Arsenal", "Lester",new Date(),0,3,s.get(0),223323,leag.get(0),myTick.get(0),myBet.get(0));
        m.save();

    }
}
