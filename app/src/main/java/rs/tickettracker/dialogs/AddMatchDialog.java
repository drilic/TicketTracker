package rs.tickettracker.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import model.Bet;
import model.League;
import model.Match;
import rs.tickettracker.R;
import rs.tickettracker.sync.tasks.GetMatchesTask;
import rs.tickettracker.helpers.ComponentsHelper;
import rs.tickettracker.listeners.interfaces.GetMatchFromDialogListener;

/**
 * Created by gisko on 26-Apr-16.
 */
public class AddMatchDialog extends DialogFragment {

    Match currentMatch = null;
    Bet currentBet = null;
    Fragment fragment;

    public AddMatchDialog(Fragment fragment) {
        // Required empty public constructor
        this.fragment = fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (currentMatch != null) {
                            currentMatch.bet = currentBet;
                            GetMatchFromDialogListener getMatchFromDialogListener = (GetMatchFromDialogListener) fragment;
                            getMatchFromDialogListener.getMatchFromDialog(currentMatch);
                        }
                    }
                })
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        View view = onCreateDialogView(getActivity().getLayoutInflater(), null, null);
        onViewCreated(view, null);
        dialogBuilder.setView(view);

        Dialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_match, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<League> leaguesList = new Select().from(League.class).execute();
        final MaterialSpinner matchSpinner = ComponentsHelper.createSpinner(new ArrayList<Match>(), view, R.id.match, 0);
        new GetMatchesTask(getActivity(), view, matchSpinner, false).execute(leaguesList.get(0).leagueServisId, 1);

        matchSpinner.post(new Runnable() {
            public void run() {
                matchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == -1) {
                            currentMatch = null;
                            return;
                        } else {
                            currentMatch = (Match) parent.getSelectedItem();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        String[] date = getResources().getStringArray(R.array.date_list);
        final MaterialSpinner dateSpinner = ComponentsHelper.createSpinner(date, view, R.id.date, 4);
        final MaterialSpinner leagueSpinner = ComponentsHelper.createSpinner(leaguesList, view, R.id.league, 1);

        final int[] leagueFlag = {0};
        leagueSpinner.post(new Runnable() {
            public void run() {
                leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (leagueFlag[0] > 1) {
                            if (position == -1) return;
                            int dayPosition = dateSpinner.getSelectedItemPosition() - 1;
                            if (dayPosition == -1) dayPosition = 4;
                            int day = Integer.parseInt(getResources().getStringArray(R.array.date_values_list)[dayPosition]);
                            League currentLeague = (League) parent.getSelectedItem();
                            new GetMatchesTask(getActivity(), view, matchSpinner, true).execute(currentLeague.leagueServisId, day);
                        } else {
                            leagueFlag[0]++;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        final int[] dateFlag = {0};
        dateSpinner.post(new Runnable() {
            public void run() {
                dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (dateFlag[0] > 1) {
                            int day = 0;
                            if (position == -1) {
                                return;
                            } else {
                                day = Integer.parseInt(getResources().getStringArray(R.array.date_values_list)[position]);
                            }
                            int leaguePosition = leagueSpinner.getSelectedItemPosition() - 1;
                            if (leaguePosition == -1) return;
                            new GetMatchesTask(getActivity(), view, matchSpinner, true).execute(leaguesList.get(leaguePosition).leagueServisId, day);
                        } else {
                            dateFlag[0]++;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        final List<Bet> betList = new Select().from(Bet.class).execute();
        currentBet = betList.get(0);
        final MaterialSpinner betSpinner = ComponentsHelper.createSpinner(betList, view, R.id.bet, 2);
        betSpinner.post(new Runnable() {
            public void run() {
                betSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == -1) return;
                        else {
                            currentBet = (Bet) parent.getSelectedItem();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
    }
}
