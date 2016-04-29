package rs.tickettracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import rs.tickettracker.fragments.ActiveTicketsFragment;
import rs.tickettracker.fragments.AllTicketsFragment;
import rs.tickettracker.fragments.LoseTicketsFragment;
import rs.tickettracker.fragments.WinTicketsFragment;

/**
 * Created by gisko on 25-Apr-16.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private int int_items = 4;

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllTicketsFragment();
            case 1:
                return new ActiveTicketsFragment();
            case 2:
                return new WinTicketsFragment();
            case 3:
                return new LoseTicketsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {

        return int_items;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "ALL";
            case 1:
                return "ACTIVE";
            case 2:
                return "WIN";
            case 3:
                return "LOSE";
        }
        return null;
    }

}
