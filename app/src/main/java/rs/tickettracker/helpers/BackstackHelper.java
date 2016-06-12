package rs.tickettracker.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;

import rs.tickettracker.R;

/**
 * This helper class is used for Backstack methods for fragment transition.
 */
public class BackstackHelper {

    /**
     * Check if current fragment is fragment that is pressed.
     * @param activity - current activity
     * @param current - name of current fragment
     * @return True if name of pressed fragment is last on backstack.
     */
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

    /**
     * Do fragment transition and set animations between two fragments.
     * @param newTransaction - current transaction
     * @param name - name of new fragment that's used to set on top of backstack.
     * @param nextFragment - Next fragment
     */
    public static void FragmentTransaction(FragmentTransaction newTransaction, String name, Fragment nextFragment) {
        newTransaction.addToBackStack(name)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.containerView, nextFragment, name)
                .commit();
    }

    /**
     * Setting title of current fragment to top of current activity.
     * @param activity - current activity
     * @param currentTitle - title that need to be set.
     */
    public static void setActionBarTitle(AppCompatActivity activity, String currentTitle) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(currentTitle);
        }
    }

    /**
     * Check if back button pressed on telephone and set appropriate title of activity.
     * @param activity - current activity
     * @return True if back button is pressed.
     */
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

    /**
     * Get last fragment from backstack.
     * @param activity - current fragment.
     * @return last fragment from backstack.
     */
    public static Fragment getLastFragment(AppCompatActivity activity) {
        int backstackCount = activity.getSupportFragmentManager().getBackStackEntryCount();
        if (backstackCount == 0)
            return null;
        FragmentManager.BackStackEntry lastBackstackValue = activity.getSupportFragmentManager().getBackStackEntryAt(backstackCount - 1);
        String str = lastBackstackValue.getName();
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(str);
        return fragment;
    }
}
