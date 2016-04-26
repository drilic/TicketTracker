package rs.tickettracker.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import fr.ganfra.materialspinner.MaterialSpinner;
import rs.tickettracker.R;

/**
 * Created by gisko on 26-Apr-16.
 */
public class AddMatchDialog extends DialogFragment {

    public AddMatchDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //TODO: Implement OK button
                            }
                        }
                )
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

        Dialog d = dialogBuilder.create();
        d.setCanceledOnTouchOutside(false);
        return d;
    }


    public View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_match, container); // inflate here
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] leagues = {"Premier", "La primera", "Seria A", "Bundes Liga"};
        ArrayAdapter<String> adapterLeague = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, leagues);
        adapterLeague.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinerLeague = (MaterialSpinner) view.findViewById(R.id.league);
        spinerLeague.setAdapter(adapterLeague);
        spinerLeague.setSelection(1);

        String[] date = {"-3", "-2", "-1", "Today", "+1", "+2", "+3"};
        ArrayAdapter<String> adapterDate = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, date);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinnerDate = (MaterialSpinner) view.findViewById(R.id.date);
        spinnerDate.setAdapter(adapterDate);
        spinnerDate.setSelection(4);

        String[] matches = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        ArrayAdapter<String> adapterMatches = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, matches);
        adapterMatches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinnerMatches = (MaterialSpinner) view.findViewById(R.id.match);
        spinnerMatches.setAdapter(adapterMatches);

        String[] bet = {"1", "X", "2"};
        ArrayAdapter<String> adapterBet = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, bet);
        adapterBet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinnerBet = (MaterialSpinner) view.findViewById(R.id.bet);
        spinnerBet.setAdapter(adapterBet);
        spinnerBet.setSelection(2);
    }

}
