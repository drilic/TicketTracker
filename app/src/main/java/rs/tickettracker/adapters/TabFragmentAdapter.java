package rs.tickettracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rs.tickettracker.fragments.tabs.ActiveTicketsFragment;
import rs.tickettracker.fragments.tabs.AllTicketsFragment;
import rs.tickettracker.fragments.tabs.LoseTicketsFragment;
import rs.tickettracker.fragments.tabs.WinTicketsFragment;

/**
 * Created by gisko on 25-Apr-16.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private int int_items = 4;
    public List<Fragment> tabFragments = new ArrayList<Fragment>();

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllTicketsFragment allTicketsFragment = new AllTicketsFragment(this);
                tabFragments.add(allTicketsFragment);
                return allTicketsFragment;
            case 1:
                ActiveTicketsFragment activeTicketsFragment = new ActiveTicketsFragment(this);
                tabFragments.add(activeTicketsFragment);
                return activeTicketsFragment;
            case 2:
                WinTicketsFragment winTicketsFragment = new WinTicketsFragment(this);
                tabFragments.add(winTicketsFragment);
                return winTicketsFragment;
            case 3:
                LoseTicketsFragment loseTicketsFragment = new LoseTicketsFragment(this);
                tabFragments.add(loseTicketsFragment);
                return loseTicketsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {

        return int_items;
    }

    public Fragment getFragmentByPosition(int position) {
        return tabFragments.get(position);
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
