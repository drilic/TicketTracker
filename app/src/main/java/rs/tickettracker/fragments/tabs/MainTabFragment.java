package rs.tickettracker.fragments.tabs;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.tickettracker.R;
import rs.tickettracker.activities.MainActivity;
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.fragments.interfaces.FragmentUpdateInterface;
import rs.tickettracker.listeners.AddTicketAction;


public class MainTabFragment extends Fragment implements FragmentUpdateInterface {


    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private FragmentManager fragmentManager;
    TabFragmentAdapter tabMenager;

    public MainTabFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        View view = inflater.inflate(R.layout.fragment_tab_main, null);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabMenager = new TabFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(tabMenager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (MainActivity.NEED_SYNC) {
                    for (Fragment f : getChildFragmentManager().getFragments()) {
                        if (f != null && f instanceof FragmentUpdateInterface)
                            ((FragmentUpdateInterface) f).reloadTicketAdapter();
                    }
                    MainActivity.NEED_SYNC = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (savedInstanceState != null) {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int currentItem = savedInstanceState.getInt("currentIndex");
                    viewPager.setCurrentItem(currentItem);
                }
            }, 1);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.add_ticket_fab);
        fab.setOnClickListener(new AddTicketAction(((AppCompatActivity) getActivity())));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getChildFragmentManager() == null)
            return;
        if(getChildFragmentManager().getFragments()==null)
            return;
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f != null)
                f.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void reloadTicketAdapter() {
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f != null && f instanceof FragmentUpdateInterface)
                ((FragmentUpdateInterface) f).reloadTicketAdapter();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewPager != null) {
            int currentIndex = viewPager.getCurrentItem();
            outState.putInt("currentIndex", currentIndex);
        }
    }
}
