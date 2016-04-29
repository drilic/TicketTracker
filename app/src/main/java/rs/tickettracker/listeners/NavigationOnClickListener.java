package rs.tickettracker.listeners;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import rs.tickettracker.R;
import rs.tickettracker.activities.TTPreferenceActivity;
import rs.tickettracker.fragments.AboutFragment;
import rs.tickettracker.fragments.AddTicketFragment;
import rs.tickettracker.fragments.MainTabFragment;
import rs.tickettracker.helpers.SyncHelper;

/**
 * Created by gisko on 29-Apr-16.
 */
public class NavigationOnClickListener implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FragmentManager fragmentManager;
    AppCompatActivity activity;

    public NavigationOnClickListener(DrawerLayout drawerLayout,AppCompatActivity activity){
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
                if (!isCurrentFragment(activity.getResources().getString(R.string.tickets))) {
                    newFragmentTransaction.addToBackStack(activity.getResources().getString(R.string.tickets))
                            .replace(R.id.containerView, new MainTabFragment()).commit();
                    setActionBarTitle(activity.getResources().getString(R.string.tickets));
                }
                break;
            case R.id.drawer_add_ticket:
                if (!isCurrentFragment(activity.getResources().getString(R.string.add_new_ticket))) {
                    newFragmentTransaction.addToBackStack(activity.getResources().getString(R.string.add_new_ticket))
                            .replace(R.id.containerView, new AddTicketFragment()).commit();
                    //                    newFragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                    setActionBarTitle(activity.getResources().getString(R.string.add_new_ticket));
                }
                break;
            case R.id.drawer_sync:
                SyncHelper.syncLiveScore();
                break;
            case R.id.drawer_settings:
                Intent preference = new Intent(activity.getApplicationContext(), TTPreferenceActivity.class);
                activity.startActivity(preference);
                break;
            case R.id.drawer_about:
                if (!isCurrentFragment(activity.getResources().getString(R.string.about))) {
                    newFragmentTransaction.addToBackStack(activity.getResources().getString(R.string.about))
                            .replace(R.id.containerView, new AboutFragment()).commit();
                    setActionBarTitle(activity.getResources().getString(R.string.about));
                }
                break;
            case R.id.drawer_exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;
        }
        return false;
    }

    private void setActionBarTitle(String currentTitle) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(currentTitle);
        }
    }

    private boolean isCurrentFragment(String current) {
        int numOfEntry = fragmentManager.getBackStackEntryCount();
        if (numOfEntry <= 0) {
            if (current.equals(activity.getResources().getString(R.string.tickets))) {
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
