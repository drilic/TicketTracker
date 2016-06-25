package rs.tickettracker.fragments.interfaces;

import model.Ticket;

/**
 * Interface that is used as custom Observer for updating all tab fragments on 'Home' page.
 */
public interface FragmentUpdateInterface {

    /**
     * Method that implement all tab fragments and it reload all tickets from array adapters.
     */
    void reloadTicketAdapter();

    void updateAdapter(Ticket t);
}
