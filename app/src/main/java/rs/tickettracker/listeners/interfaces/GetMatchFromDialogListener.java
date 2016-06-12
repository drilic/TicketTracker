package rs.tickettracker.listeners.interfaces;

import model.Match;

/**
 * This interface is used as custom observer/listener to send ticket from modal dialog to
 * 'Add ticket' page.
 */
public interface GetMatchFromDialogListener {

    /**
     * Send match from modal dialog to parent fragment.
     * @param match - Match that need to be sent.
     */
    void getMatchFromDialog(Match match);
}
