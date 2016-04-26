package rs.tickettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import rs.tickettracker.R;
import rs.tickettracker.fragments.AboutFragment;
import rs.tickettracker.fragments.MainTabFragment;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mTitle = getTitle();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.drawer_home) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new MainTabFragment()).commit();
                }
                if (menuItem.getItemId() == R.id.drawer_add_ticket) {
                    Intent addTicketScreen = new Intent(getApplicationContext(), AddTicketActivity.class);
                    startActivity(addTicketScreen);
                }
                if (menuItem.getItemId() == R.id.drawer_sync) {
                    //TODO: Call sync method
                    Log.i(MainActivity.class.getSimpleName(), "Call sync method");
                }
                if (menuItem.getItemId() == R.id.drawer_settings) {
                    Intent preference = new Intent(getApplicationContext(), TTPreferenceActivity.class);
                    startActivity(preference);
                }

                if (menuItem.getItemId() == R.id.drawer_about) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new AboutFragment()).commit();
                    getSupportActionBar().setTitle("About");
                }
                if (menuItem.getItemId() == R.id.drawer_exit) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }

                return false;
            }


        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new MainTabFragment()).commit();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync_menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.action_sync){
            //TODO: Call sync method
            Log.i(MainActivity.class.getSimpleName(), "Call sync method");
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}