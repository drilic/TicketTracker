package rs.tickettracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.fragments.AddTicketFragment;
import rs.tickettracker.fragments.TicketDetailFragment;
import rs.tickettracker.fragments.tabs.ActiveTicketsFragment;
import rs.tickettracker.fragments.tabs.AllTicketsFragment;
import rs.tickettracker.fragments.tabs.LoseTicketsFragment;
import rs.tickettracker.fragments.tabs.WinTicketsFragment;
import rs.tickettracker.helpers.BackstackHelper;
import rs.tickettracker.helpers.StatusHelper;

/**
 * Created by gisko on 29-Apr-16.
 */
public class TicketDetailActivity extends AppCompatActivity {

    public Ticket currentTicket;
    TabFragmentAdapter tabMenager;
    Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_ticket_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("id");
        Ticket t = new Select().from(Ticket.class).where("_id= ?", id).executeSingle();
        currentTicket = t;
        fillData(t);

        Bundle bundle = new Bundle();
        bundle.putLong("ticket_id", id);
        TicketDetailFragment ticketDetailFragment = new TicketDetailFragment();
        ticketDetailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerView, ticketDetailFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu_items, menu);
        myMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            final Ticket t = currentTicket;
            builder.setMessage("Are you sure that u want to delete " + t.ticketName + "?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("retId", t.getId());
                    setResult(1000, returnIntent);
                    finish();
                }

            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent returnIntent = new Intent();
                    setResult(1001, returnIntent);
                    finish();
                }
            });

            android.app.AlertDialog alert = builder.create();
            alert.show();
            return true;
        } else if (item.getItemId() == R.id.edit_item) {
            BackstackHelper.FragmentTransaction(getSupportFragmentManager().beginTransaction(),
                    this.getResources().getString(R.string.add_new_ticket), new AddTicketFragment(currentTicket.getId(), myMenu));
            getSupportActionBar().setTitle(this.getResources().getString(R.string.edit_ticket));
            FrameLayout statusFrame = (FrameLayout) findViewById(R.id.statusPanel);
            for (int i = 0; i < myMenu.size(); i++)
                myMenu.getItem(i).setVisible(false);

            statusFrame.setVisibility(View.GONE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            fillData(currentTicket);
        }
        for (int i = 0; i < myMenu.size(); i++)
            myMenu.getItem(i).setVisible(true);
        super.onBackPressed();
    }

    private void fillData(Ticket t) {
        FrameLayout statusFrame = (FrameLayout) findViewById(R.id.statusPanel);
        statusFrame.setVisibility(View.VISIBLE);
        statusFrame.setBackgroundColor(getResources().getColor(StatusHelper.getStatusColor(t.status.status, getApplicationContext())));
        getSupportActionBar().setTitle(t.ticketName);
        TextView statusValue = (TextView) findViewById(R.id.detailStatus);
        statusValue.setText(t.status.status);
    }

}
