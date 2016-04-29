package rs.tickettracker.listeners;

import android.content.DialogInterface;
import android.util.Log;

/**
 * Created by gisko on 29-Apr-16.
 */
public class AddMatchListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        addMatchOnTicket();
    }

    private void addMatchOnTicket() {
        //TODO: Add match on ticket method
        Log.i(AddMatchListener.class.getSimpleName(), "Add match on ticket");
    }
}
