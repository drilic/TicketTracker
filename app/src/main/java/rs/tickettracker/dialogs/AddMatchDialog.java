package rs.tickettracker.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.tickettracker.R;
import rs.tickettracker.helpers.ComponentsHelper;

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
                                addMatchOnTicket();
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

        Dialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void addMatchOnTicket(){
        //TODO: Add match on ticket method
        Log.i(AddMatchDialog.class.getSimpleName(), "Add match on ticket");
    }

    public View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_match, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] leagues = {"Premier", "La primera", "Seria A", "Bundes Liga"};
        ComponentsHelper.createSpiner(leagues, view, R.id.league, 1);

        String[] date = {"-3", "-2", "-1", "Today", "+1", "+2", "+3"};
        ComponentsHelper.createSpiner(date, view, R.id.date, 4);

        String[] matches = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        ComponentsHelper.createSpiner(matches, view, R.id.match, 0);

        String[] bet = {"1", "X", "2"};
        ComponentsHelper.createSpiner(bet, view, R.id.bet, 2);
    }

}
