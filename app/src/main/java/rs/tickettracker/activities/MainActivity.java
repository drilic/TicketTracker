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
import android.provider.SyncStateContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.query.Select;

import model.Status;
import rs.tickettracker.R;
import rs.tickettracker.helpers.SyncHelper;
import rs.tickettracker.sync.SyncReceiver;
import rs.tickettracker.sync.SyncService;
import rs.tickettracker.fragments.tabs.MainTabFragment;
import rs.tickettracker.helpers.BackstackHelper;
import rs.tickettracker.listeners.NavigationOnClickListener;
import rs.tickettracker.sync.tasks.SyncTask;

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
    public static String SYNC_DATA = "SYNC_DATA";
    public static boolean NEED_SYNC = false;

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
        fragmentTransaction.replace(R.id.containerView, new MainTabFragment(), "initTagName").commit();
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationOnClickListener(drawerLayout, this));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean allowSync = sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_sync), false);
        if (allowSync) {
            setUpReceiver();
        }

    }

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
            registerBroadcastReceiver();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean allowSync = sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_sync), false);
        if (!allowSync) {
            if (pendingIntent != null) {
                manager.cancel(pendingIntent);
                pendingIntent.cancel();
                if (sync != null)
                    unregisterBroadcastReceiver();
            }
        } else {
            boolean allowNotification = sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_notification), false);
            if (!allowNotification) {
                if (sync != null)
                    unregisterBroadcastReceiver();
            }
        }
    }

    private boolean registerBroadcastReceiver() {
        try {
            registerReceiver(sync, filter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean unregisterBroadcastReceiver() {
        try {
            unregisterReceiver(sync);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync_menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            if (SyncHelper.getConnectivityStatus(getApplicationContext())) {
                new SyncTask(this, false, getApplicationContext()).execute();
            } else {
                Toast.makeText(MainActivity.this, "Check settings or net connection.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!BackstackHelper.isFragmentBackPressed(this)) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
