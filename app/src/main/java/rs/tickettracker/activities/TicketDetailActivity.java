package rs.tickettracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.fragments.AddTicketFragment;
import rs.tickettracker.fragments.ErrorFragment;
import rs.tickettracker.fragments.TicketDetailFragment;
import rs.tickettracker.helpers.BackstackHelper;
import rs.tickettracker.helpers.GlobalStaticValuesHelper;
import rs.tickettracker.helpers.StatusHelper;

/**
 * Ticket detatil activity shows details of ticket. When user click on ticket from list of ticket,
 * he can see details about it (all matches on ticket, bets...).
 */
public class TicketDetailActivity extends AppCompatActivity {

    public Ticket currentTicket;
    Menu myMenu;
    public static boolean showMenu = true;
    public long notification_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_ticket_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("id");
        notification_click = extras.getLong("notification_click", GlobalStaticValuesHelper.DETAIL_ACTIVITY_REGULAR);
        Ticket t = null;
        try {
            t = Ticket.load(Ticket.class, id);
        } catch (Exception e) {
            ActiveAndroid.initialize(this);
            try {
                t = Ticket.load(Ticket.class, id);
            } catch (Exception ex) {
                t = null;
            }
        }
        if (t == null) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(this.getResources().getString(R.string.error));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerView, new ErrorFragment()).commit();
        } else {
            currentTicket = t;
            fillData(t);

            Bundle bundle = new Bundle();
            bundle.putLong("ticket_id", id);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (savedInstanceState != null) {
                Fragment lastFragment = BackstackHelper.getLastFragment(this);
                if (fragmentTransaction != null) {
                    if (lastFragment != null) {
                        fragmentTransaction
                                .replace(R.id.containerView, lastFragment, lastFragment.getTag())
                                .commit();
                        if (lastFragment.getTag().equals(this.getResources().getString(R.string.add_new_ticket))) {
                            BackstackHelper.setActionBarTitle(this, getResources().getString(R.string.edit_ticket));
                            showMenu = false;
                        }
                    }
                }

            } else {
                TicketDetailFragment ticketDetailFragment = new TicketDetailFragment();
                ticketDetailFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.containerView, ticketDetailFragment, "ticketDetail").commit();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BackstackHelper.getLastFragment(this) == null)
            showMenu = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (notification_click == GlobalStaticValuesHelper.DETAIL_ACTIVITY_REGULAR) {
            if (currentTicket != null) {
                getMenuInflater().inflate(R.menu.detail_menu_items, menu);
                myMenu = menu;
                if (showMenu) {
                    myMenu.findItem(R.id.delete_item).setVisible(true);
                    if (!currentTicket.status.status.equals("Active"))
                        myMenu.findItem(R.id.edit_item).setVisible(false);
                } else {
                    for (int i = 0; i < myMenu.size(); i++)
                        myMenu.getItem(i).setVisible(false);
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (currentTicket != null) {
            if (item.getItemId() == R.id.delete_item) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle(this.getResources().getString(R.string.confirm));
                final Ticket t = currentTicket;
                builder.setMessage(this.getResources().getString(R.string.are_you_sure_to_delete) + " " + t.ticketName + "?");
                builder.setPositiveButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("retId", t.getId());
                        setResult(GlobalStaticValuesHelper.DETAIL_ACTIVITY_DELETE_TICKET, returnIntent);
                        finish();
                    }

                });
                builder.setNegativeButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
                return true;
            } else if (item.getItemId() == R.id.edit_item) {
                showMenu = false;
                BackstackHelper.FragmentTransaction(getSupportFragmentManager().beginTransaction(),
                        this.getResources().getString(R.string.add_new_ticket), new AddTicketFragment(currentTicket.getId(), myMenu));
                getSupportActionBar().setTitle(this.getResources().getString(R.string.edit_ticket));
                FrameLayout statusFrame = (FrameLayout) findViewById(R.id.statusPanel);
                for (int i = 0; i < myMenu.size(); i++)
                    myMenu.getItem(i).setVisible(false);

                statusFrame.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (currentTicket != null) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                fillData(currentTicket);
            }
            if (notification_click == GlobalStaticValuesHelper.DETAIL_ACTIVITY_REGULAR) {
                if (!currentTicket.status.status.equals("Active"))
                    myMenu.findItem(R.id.edit_item).setVisible(false);
                else
                    myMenu.findItem(R.id.edit_item).setVisible(true);

                myMenu.findItem(R.id.delete_item).setVisible(true);
            }
            showMenu = true;
        }
        super.onBackPressed();
    }

    /**
     * Read data from ticket and populate all fields on page.
     *
     * @param t - Ticket for reading.
     */
    private void fillData(Ticket t) {
        FrameLayout statusFrame = (FrameLayout) findViewById(R.id.statusPanel);
        statusFrame.setVisibility(View.VISIBLE);
        statusFrame.setBackgroundColor(getResources().getColor(StatusHelper.getStatusColor(t.status.status, getApplicationContext())));
        getSupportActionBar().setTitle(t.ticketName);
        TextView statusValue = (TextView) findViewById(R.id.detailStatus);
        statusValue.setText(t.status.status);
    }


}
