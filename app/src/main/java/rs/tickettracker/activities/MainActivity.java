package rs.tickettracker.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import rs.tickettracker.R;
import rs.tickettracker.helpers.SyncHelper;
import rs.tickettracker.sync.SyncReceiver;
import rs.tickettracker.sync.SyncService;
import rs.tickettracker.fragments.tabs.MainTabFragment;
import rs.tickettracker.helpers.BackstackHelper;
import rs.tickettracker.listeners.NavigationOnClickListener;
import rs.tickettracker.sync.tasks.SyncTask;

/**
 * Main Activity of application. Contains all fragments used in application.
 */
public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ActionBar actionBar;
    CharSequence title;
    SharedPreferences sharedPreferences;
    AlarmManager manager;
    SyncReceiver sync;
    PendingIntent pendingIntent;
    Intent alarmIntent;
    IntentFilter filter;
    MenuItem syncMenu;
    boolean allowSync = false;
    public static String SYNC_DATA = "TicketTracker_SYNC_DATA";
    public static boolean NEED_SYNC = false;
    private static boolean REGISTER_RECEIVER = true;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        title = getTitle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowHomeEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (savedInstanceState != null) {
            Fragment lastFragment = BackstackHelper.getLastFragment(this);
            if (fragmentTransaction != null) {
                if (lastFragment != null) {
                    fragmentTransaction
                            .replace(R.id.containerView, lastFragment, lastFragment.getTag())
                            .commit();
                    BackstackHelper.setActionBarTitle(this, lastFragment.getTag());
                }
            }

        } else {
            fragmentTransaction.replace(R.id.containerView, new MainTabFragment(), "initTagName").commit();
        }

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationOnClickListener(drawerLayout, this));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        allowSync = sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_sync), false);
        if (allowSync) {
            setUpReceiver();
        }
    }

    /**
     * Set up receiver for notification.
     */
    private void setUpReceiver() {
        sync = new SyncReceiver();
        alarmIntent = new Intent(this, SyncService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SyncHelper.getConnectivityStatus(getApplicationContext())) {
            setUpReceiver();
            int syncIntervalFromSettings = Integer.parseInt(sharedPreferences.getString("pref_sync_list_interval", "5"));
            int interval = SyncHelper.calculateTimeTillNextSync(syncIntervalFromSettings);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            filter = new IntentFilter();
            filter.addAction(SYNC_DATA);
            if (!REGISTER_RECEIVER) {
                registerBroadcastReceiver();
            }
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        allowSync = sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_sync), false);
        if (!allowSync) {
            if (syncMenu != null) {
                syncMenu.setIcon(R.drawable.sync_black);
                syncMenu.setEnabled(false);
            }
            if (pendingIntent != null) {
                manager.cancel(pendingIntent);
                pendingIntent.cancel();
                if (sync != null)
                    unregisterBroadcastReceiver();
            }
        } else {
            if (syncMenu != null) {
                syncMenu.setIcon(R.drawable.sync_white_32);
                syncMenu.setEnabled(true);
            }

            boolean allowNotification = sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_notification), false);
            if (!allowNotification) {
                if (sync != null)
                    unregisterBroadcastReceiver();
            }
        }
    }

    /**
     * Register broadcast receiver.
     * @return True if receiver was register successful.
     */
    private boolean registerBroadcastReceiver() {
        try {
            registerReceiver(sync, filter);
            REGISTER_RECEIVER = true;
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Unregister broadcast receiver.
     * @return True if receiver was unregistered successful.
     */
    private boolean unregisterBroadcastReceiver() {
        try {
            unregisterReceiver(sync);
            REGISTER_RECEIVER = false;
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync_menu_item, menu);
        syncMenu = menu.findItem(R.id.action_sync);
        if (syncMenu != null) {
            if (!allowSync) {
                syncMenu.setIcon(R.drawable.sync_black);
                syncMenu.setEnabled(false);
            } else {
                syncMenu.setIcon(R.drawable.sync_white_32);
                syncMenu.setEnabled(true);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            if (SyncHelper.getConnectivityStatus(getApplicationContext())) {
                new SyncTask(this, false, getApplicationContext()).execute();
            } else {
                Toast.makeText(MainActivity.this, this.getResources().getString(R.string.check_settings_or_net_conn), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
                return;
            }
        }
        if (!BackstackHelper.isFragmentBackPressed(this)) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle(this.getResources().getString(R.string.exit));
                builder.setMessage(this.getResources().getString(R.string.are_you_sure_to_exit));
                builder.setPositiveButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                builder.setNegativeButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (f != null)
                f.onActivityResult(requestCode, resultCode, data);
        }

    }
}
