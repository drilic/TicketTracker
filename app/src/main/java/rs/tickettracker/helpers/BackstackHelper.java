package rs.tickettracker.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;

import rs.tickettracker.R;
import rs.tickettracker.fragments.AddTicketFragment;

/**
 * Created by gisko on 29-Apr-16.
 */
public class BackstackHelper {

    public static boolean isCurrentFragment(AppCompatActivity activity, String current) {
        int numOfEntry = activity.getSupportFragmentManager().getBackStackEntryCount();
        if (numOfEntry <= 0) {
            if (current.equals(activity.getResources().getString(R.string.tickets))) {
                return true;
            }
            return false;
        }
        FragmentManager.BackStackEntry lastBackstackValue = activity.getSupportFragmentManager().getBackStackEntryAt(numOfEntry - 1);
        if (lastBackstackValue != null) {
            if (current.equals(lastBackstackValue.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void FragmentTransaction(FragmentTransaction newTransaction, String name, Fragment nextFragment) {
        newTransaction.addToBackStack(name)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.containerView, nextFragment)
                .commit();
    }

    public static void setActionBarTitle(AppCompatActivity activity, String currentTitle) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(currentTitle);
        }
    }

    public static boolean isFragmentBackPressed(AppCompatActivity activity) {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (activity.getSupportFragmentManager().getBackStackEntryCount() == 1) {
                setActionBarTitle(activity, activity.getResources().getString(R.string.tickets));
            } else {
                FragmentManager.BackStackEntry lastBackstackValue = activity.getSupportFragmentManager().getBackStackEntryAt(activity.getSupportFragmentManager().getBackStackEntryCount() - 2);
                setActionBarTitle(activity, lastBackstackValue.getName());
            }
            activity.getSupportFragmentManager().popBackStack();
            return true;
        } else {
            return false;
        }
    }
}
