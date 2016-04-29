package rs.tickettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import rs.tickettracker.R;
import rs.tickettracker.fragments.AboutFragment;
import rs.tickettracker.fragments.AddTicketFragment;
import rs.tickettracker.fragments.MainTabFragment;
import rs.tickettracker.helpers.SyncHelper;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ActionBar actionBar;
    CharSequence title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        title = getTitle();
        navigationView.setNavigationItemSelectedListener(new NavigationDrawerClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowHomeEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, new MainTabFragment()).commit();
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync_menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            return SyncHelper.syncLiveScore();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            if (fragmentManager.getBackStackEntryCount() == 1) {
                setActionBarTitle(getResources().getString(R.string.tickets));
            } else {
                FragmentManager.BackStackEntry lastBackstackValue = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 2);
                setActionBarTitle(lastBackstackValue.getName());
            }
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private class NavigationDrawerClickListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            drawerLayout.closeDrawers();
            FragmentTransaction newFragmentTransaction = fragmentManager.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.drawer_home:
                    if (!isCurrentFragment(getResources().getString(R.string.tickets))) {
                        newFragmentTransaction.addToBackStack(getResources().getString(R.string.tickets))
                                .replace(R.id.containerView, new MainTabFragment()).commit();
                        setActionBarTitle(getResources().getString(R.string.tickets));
                    }
                    break;
                case R.id.drawer_add_ticket:
                    if (!isCurrentFragment(getResources().getString(R.string.add_new_ticket))) {
                        newFragmentTransaction.addToBackStack(getResources().getString(R.string.add_new_ticket))
                                .replace(R.id.containerView, new AddTicketFragment()).commit();
                        //                    newFragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                        setActionBarTitle(getResources().getString(R.string.add_new_ticket));
                    }
                    break;
                case R.id.drawer_sync:
                    SyncHelper.syncLiveScore();
                    break;
                case R.id.drawer_settings:
                    Intent preference = new Intent(getApplicationContext(), TTPreferenceActivity.class);
                    startActivity(preference);
                    break;
                case R.id.drawer_about:
                    if (!isCurrentFragment(getResources().getString(R.string.about))) {
                        newFragmentTransaction.addToBackStack(getResources().getString(R.string.about))
                                .replace(R.id.containerView, new AboutFragment()).commit();
                        setActionBarTitle(getResources().getString(R.string.about));
                    }
                    break;
                case R.id.drawer_exit:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                    break;
            }
            return false;
        }
    }

    private void setActionBarTitle(String currentTitle) {
        if (actionBar != null) {
            actionBar.setTitle(currentTitle);
        }
    }

    private boolean isCurrentFragment(String current) {
        int numOfEntry = fragmentManager.getBackStackEntryCount();
        if (numOfEntry <= 0) {
            if (current.equals(getResources().getString(R.string.tickets))) {
                return true;
            }
            return false;
        }
        FragmentManager.BackStackEntry lastBackstackValue = fragmentManager.getBackStackEntryAt(numOfEntry - 1);
        if (lastBackstackValue != null) {
            if (current.equals(lastBackstackValue.getName())) {
                return true;
            }
        }
        return false;
    }
}
