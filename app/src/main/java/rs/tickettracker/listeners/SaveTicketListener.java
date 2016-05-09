package rs.tickettracker.listeners;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import model.Match;
import model.Status;
import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.MatchAddTicketListAdapter;
import rs.tickettracker.dialogs.AddMatchDialog;
import rs.tickettracker.helpers.BackstackHelper;

/**
 * Created by gisko on 29-Apr-16.
 */
public class SaveTicketListener implements View.OnClickListener {

    AppCompatActivity activity;
    MatchAddTicketListAdapter array;

    public SaveTicketListener(AppCompatActivity activity, MatchAddTicketListAdapter array){
        this.activity= activity;
        this.array=array;
    }

    @Override
    public void onClick(View v) {
        EditText ticketName = (EditText)activity.findViewById(R.id.add_ticket_name);
        EditText ticketGain = (EditText)activity.findViewById(R.id.add_ticket_gain);
        Status s = new Select().from(Status.class).where("status = ?", "Active").executeSingle();

        double gain = Double.parseDouble(ticketGain.getText().toString());
        Ticket t = new Ticket(ticketName.getText().toString(),s,gain);
        t.save();

        ActiveAndroid.beginTransaction();
        try {
            for (Match m: array.getAllMatches()) {
                m.ticket = t;
                m.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        BackstackHelper.isFragmentBackPressed(activity);
    }
}
