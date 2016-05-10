package rs.tickettracker.fragments.tabs;

import android.content.Intent;
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
import rs.tickettracker.adapters.TabFragmentAdapter;
import rs.tickettracker.listeners.AddTicketAction;


public class MainTabFragment extends Fragment {


    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private FragmentManager fragmentManager;
    TabFragmentAdapter tabMenager;

    public MainTabFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.add_ticket_fab);
        fab.setOnClickListener(new AddTicketAction(((AppCompatActivity) getActivity())));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f != null)
                f.onActivityResult(requestCode, resultCode, data);
        }
    }
}
