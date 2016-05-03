package rs.tickettracker.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.fragments.TicketDetailFragment;
import rs.tickettracker.helpers.StatusHelper;

/**
 * Created by gisko on 29-Apr-16.
 */
public class TicketDetailActivity extends AppCompatActivity {

    public Ticket currentTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_ticket_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("id");
        fillData(id);

        Bundle bundle = new Bundle();
        bundle.putLong("ticket_id", id);
        TicketDetailFragment ticketDetailFragment = new TicketDetailFragment();
        ticketDetailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerView, ticketDetailFragment).commit();
    }

    private void fillData(long id) {
        Ticket t = (Ticket) new Select().from(Ticket.class).where("_id= ?", id).execute().get(0);
        currentTicket=t;
        FrameLayout statusFrame = (FrameLayout) findViewById(R.id.statusPanel);
        statusFrame.setBackgroundColor(getResources().getColor(StatusHelper.getStatusColor(t.status.status,getApplicationContext())));
        getSupportActionBar().setTitle(t.ticketName);
        TextView statusValue = (TextView) findViewById(R.id.detailStatus);
        statusValue.setText(t.status.status);
    }

}
