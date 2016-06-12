package rs.tickettracker.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.DatabaseHelper;
import com.activeandroid.query.Select;

import model.Status;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.helpers.GlobalConfig;
import rs.tickettracker.helpers.InitDatabaseHelper;

/**
 * Ticket Tracker Content provider is used to send data from this application to other aplication
 * that want to use this data. It is implemented only for tickets for now.
 */
public class TicketTrackerCP extends ContentProvider {

    public static final String TICKET_ID = "Ticket_ID";
    public static final String TICKET = "Ticket";

    public static final String TABLE_TICKET = "ticket";
    public static final String COLUMN_ID = "_id";

    public static final String AUTHORITIES = "rs.tickettracker";
    public static final String CONTENT_URI = "content://";


    private DatabaseHelper database;

    @Override
    public boolean onCreate() {
        ActiveAndroid.initialize(getContext());
        Configuration dbConfiguration = new Configuration.Builder(getContext()).create();
        database = new DatabaseHelper(dbConfiguration);

        Status s = new Select().from(Status.class).executeSingle();
        if (s == null) {
            InitDatabaseHelper.initDB();
            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);
            if (GlobalConfig.POPULATE_WITH_DUMMY_TICKETS) {
                InitDatabaseHelper.fillDummyTickets(GlobalConfig.NUMBER_OF_DUMMY_TICKETS);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (getType(uri)) {
            case TICKET_ID:
                queryBuilder.appendWhere(COLUMN_ID + "="
                        + uri.getLastPathSegment());
            case TICKET:
                queryBuilder.setTables(TABLE_TICKET);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        long id;
        try {
            id = Long.parseLong(uri.getLastPathSegment());
        } catch (Exception e) {
            id = -1;
        }

        if (id != -1) {
            return TICKET_ID;
        } else {
            return TICKET;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri retVal;
        Status s = new Select().from(Status.class).where("status = ?", "Active").executeSingle();
        switch (getType(uri)) {
            case TICKET:
                Ticket t = new Ticket();
                t.ticketName = values.getAsString("ticketName");
                t.possibleGain = values.getAsDouble("possibleGain");
                t.status = s;
                t.save();
                t = new Select().from(Ticket.class).orderBy("_id DESC").executeSingle();
                retVal = Uri.parse(CONTENT_URI + AUTHORITIES + "/" + TABLE_TICKET + t.getId());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (getType(uri)) {
            case TICKET:
                rowsDeleted = sqlDB.delete(TABLE_TICKET,
                        selection,
                        selectionArgs);
                break;
            case TICKET_ID:
                String idTicket = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(TABLE_TICKET,
                            COLUMN_ID + "=" + idTicket,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(TABLE_TICKET,
                            COLUMN_ID + "=" + idTicket
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (getType(uri)) {
            case TICKET:
                rowsUpdated = sqlDB.update(TABLE_TICKET,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TICKET_ID:
                String idTicket = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TABLE_TICKET,
                            values,
                            COLUMN_ID + "=" + idTicket,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TABLE_TICKET,
                            values,
                            COLUMN_ID + "=" + idTicket
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
