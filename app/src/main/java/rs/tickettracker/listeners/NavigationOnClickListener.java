package rs.tickettracker.listeners;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import rs.tickettracker.R;
import rs.tickettracker.activities.TTPreferenceActivity;
import rs.tickettracker.fragments.AboutFragment;
import rs.tickettracker.fragments.AddTicketFragment;
import rs.tickettracker.fragments.LiveScoreFragment;
import rs.tickettracker.fragments.tabs.MainTabFragment;
import rs.tickettracker.helpers.BackstackHelper;
import rs.tickettracker.helpers.GlobalConfig;
import rs.tickettracker.helpers.SyncHelper;
import rs.tickettracker.helpers.ContentProviderTestHelper;
import rs.tickettracker.sync.tasks.SyncTask;

/**
 * Action that is triggered when user open drawer menu, and click on some of their action.
 */
public class NavigationOnClickListener implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FragmentManager fragmentManager;
    AppCompatActivity activity;

    public NavigationOnClickListener(DrawerLayout drawerLayout, AppCompatActivity activity) {
        this.drawerLayout = drawerLayout;
        this.activity = activity;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawers();

        FragmentTransaction newFragmentTransaction = fragmentManager.beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                if (!BackstackHelper.isCurrentFragment(activity, activity.getResources().getString(R.string.tickets))) {
                    BackstackHelper.FragmentTransaction(newFragmentTransaction,
                            activity.getResources().getString(R.string.tickets), new MainTabFragment());
                    BackstackHelper.setActionBarTitle(activity, activity.getResources().getString(R.string.tickets));
                }
                break;
            case R.id.drawer_add_ticket:
                if (!BackstackHelper.isCurrentFragment(activity, activity.getResources().getString(R.string.add_new_ticket))) {
                    BackstackHelper.FragmentTransaction(newFragmentTransaction,
                            activity.getResources().getString(R.string.add_new_ticket), new AddTicketFragment());
                    BackstackHelper.setActionBarTitle(activity, activity.getResources().getString(R.string.add_new_ticket));
                }
                break;
            case R.id.drawer_live_score:
                if (!BackstackHelper.isCurrentFragment(activity, activity.getResources().getString(R.string.live_score))) {
                    BackstackHelper.FragmentTransaction(newFragmentTransaction,
                            activity.getResources().getString(R.string.live_score), new LiveScoreFragment());
                    BackstackHelper.setActionBarTitle(activity, activity.getResources().getString(R.string.live_score));
                }
                break;
            case R.id.drawer_sync:
                if (GlobalConfig.TEST_CONTENT_PROVIDER) {
                    if (GlobalConfig.TEST_CONTENT_PROVIDER_GET) {
                        ContentProviderTestHelper.getAllTickets(activity);
                    }
                    if (GlobalConfig.TEST_CONTENT_PROVIDER_INSERT) {
                        ContentProviderTestHelper.insertNewTicket(activity);
                    }
                } else {
                    if (SyncHelper.getConnectivityStatus(activity.getApplicationContext())) {
                        new SyncTask(activity, false, activity.getApplicationContext()).execute();
                    } else {
                        Toast.makeText(activity, "Check settings or net connection.", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.drawer_settings:
                Intent preference = new Intent(activity.getApplicationContext(), TTPreferenceActivity.class);
                activity.startActivity(preference);
                break;
            case R.id.drawer_about:
                if (!BackstackHelper.isCurrentFragment(activity, activity.getResources().getString(R.string.about))) {
                    BackstackHelper.FragmentTransaction(newFragmentTransaction,
                            activity.getResources().getString(R.string.about), new AboutFragment());
                    BackstackHelper.setActionBarTitle(activity, activity.getResources().getString(R.string.about));
                }
                break;
            case R.id.drawer_exit:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                builder.setTitle("Exit");
                builder.setMessage("Are you sure that u want to exit?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
        return false;
    }
}
