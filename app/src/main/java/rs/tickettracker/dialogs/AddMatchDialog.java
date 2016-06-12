package rs.tickettracker.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

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
 * Dialog for adding new matches on Ticket.
 */
public class AddMatchDialog extends DialogFragment {

    Match currentMatch = null;
    Bet currentBet = null;
    MaterialSpinner dateSpinner;
    MaterialSpinner matchSpinner;
    MaterialSpinner leagueSpinner;
    MaterialSpinner betSpinner;
    int selectedDate = 0;
    int selectedLeague = 0;
    int selectedMatch = 0;
    int selectedBet = 0;
    static boolean STATE_CHANGED = false;
    final int[] dateFlag = {0};

    public AddMatchDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            selectedMatch = savedInstanceState.getInt("matchSpinner");
            selectedBet = savedInstanceState.getInt("betSpinner");
            selectedLeague = savedInstanceState.getInt("leagueSpinner");
            selectedDate = savedInstanceState.getInt("dateSpinner");
            STATE_CHANGED = true;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (currentMatch != null) {
                            currentMatch.bet = currentBet;
                            GetMatchFromDialogListener getMatchFromDialogListener = (GetMatchFromDialogListener) getTargetFragment();
                            getMatchFromDialogListener.getMatchFromDialog(currentMatch);
                        } else {
                            Toast.makeText(getActivity(), "Invalid match parameters.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                STATE_CHANGED = false;
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

    /**
     * Returns view part of dialog.
     */
    public View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_match, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<League> leaguesList = new Select().from(League.class).execute();
        final List<Bet> betList = new Select().from(Bet.class).execute();
        String[] date = getResources().getStringArray(R.array.date_list);
        if (STATE_CHANGED) {
            dateSpinner = ComponentsHelper.createSpinner(date, view, R.id.date, selectedDate, true);
            int day = 5;
            if (selectedDate != 0)
                day = Integer.parseInt(getResources().getStringArray(R.array.date_values_list)[selectedDate - 1]);
            else
                selectedMatch = -1;

            leagueSpinner = ComponentsHelper.createSpinner(leaguesList, view, R.id.league, selectedLeague, true);
            long leagueServisId = -1;
            if (selectedLeague != 0)
                leagueServisId = leaguesList.get(selectedLeague - 1).leagueServisId;

            betSpinner = ComponentsHelper.createSpinner(betList, view, R.id.bet, selectedBet, true);

            matchSpinner = ComponentsHelper.createSpinner(new ArrayList<Match>(), view, R.id.match, 0, true);
            if (leagueServisId != -1) {
                dateFlag[0] = 1;
                new GetMatchesTask(getActivity(), view, matchSpinner, true).execute(leagueServisId, day, selectedMatch);
            }
        } else {
            matchSpinner = ComponentsHelper.createSpinner(new ArrayList<Match>(), view, R.id.match, 0, true);
            dateSpinner = ComponentsHelper.createSpinner(date, view, R.id.date, 5, true);
            leagueSpinner = ComponentsHelper.createSpinner(leaguesList, view, R.id.league, 1, true);
            betSpinner = ComponentsHelper.createSpinner(betList, view, R.id.bet, 2, true);
            new GetMatchesTask(getActivity(), view, matchSpinner, false).execute(leaguesList.get(0).leagueServisId, 1);
        }


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

        final int[] leagueFlag = {0};
        leagueSpinner.post(new Runnable() {
            public void run() {
                leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (leagueFlag[0] > 1) {
                            if (position == -1) return;
                            int dayPosition = dateSpinner.getSelectedItemPosition() - 1;
                            if (dayPosition == -1) dayPosition = 5;
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

        currentBet = betList.get(0);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dateSpinner != null) {
            int selected = dateSpinner.getSelectedItemPosition();
            outState.putInt("dateSpinner", selected);
        }
        if (leagueSpinner != null) {
            int selected = leagueSpinner.getSelectedItemPosition();
            outState.putInt("leagueSpinner", selected);
        }
        if (matchSpinner != null) {
            int selected = matchSpinner.getSelectedItemPosition();
            outState.putInt("matchSpinner", selected);
        }
        if (betSpinner != null) {
            int selected = betSpinner.getSelectedItemPosition();
            outState.putInt("betSpinner", selected);
        }
    }
}
