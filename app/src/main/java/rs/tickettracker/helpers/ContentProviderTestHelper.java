package rs.tickettracker.helpers;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.activeandroid.Configuration;
import com.activeandroid.DatabaseHelper;
import com.activeandroid.query.Select;

import java.util.List;

import model.Status;
import model.Ticket;
import rs.tickettracker.providers.TicketTrackerCP;

/**
 * Created by gisko on 03-Jun-16.
 */
public class ContentProviderTestHelper {

    public static void getAllTickets(Activity activity) {
        String[] allColumns = {TicketTrackerCP.COLUMN_ID,
                "ticketName"};

        Uri contentUri = Uri.parse(TicketTrackerCP.CONTENT_URI+ TicketTrackerCP.AUTHORITIES + "/" + TicketTrackerCP.TABLE_TICKET);
        Cursor c = activity.getContentResolver().query(
                contentUri,
                new String[]{"ticketName"},
                null,
                null,
                "ticketName");

        if (c.moveToFirst()) {
            do {
                Log.i("***", c.getString(c.getColumnIndex("ticketName")));
            } while (c.moveToNext());
        }
    }

    public static void insertNewTicket(Activity activity) {
        Configuration dbConfiguration = new Configuration.Builder(activity).create();
        DatabaseHelper database = new DatabaseHelper(dbConfiguration);
        Status s = new Select().from(Status.class).where("status = ?", "Active").executeSingle();
        SQLiteDatabase db = database.getWritableDatabase();
        {
            ContentValues entry = new ContentValues();
            entry.put("ticketName", "Finale");
            entry.put("possibleGain", 1000d);
            entry.put("status", s.getId());
            List<Ticket> tickets = Ticket.getAll();
            entry.put("_id", tickets.size()+1);
            Uri contentUri = Uri.parse("content://" + TicketTrackerCP.AUTHORITIES + "/" + TicketTrackerCP.TABLE_TICKET);
            activity.getContentResolver().insert(contentUri, entry);
        }
        db.close();
    }
}
