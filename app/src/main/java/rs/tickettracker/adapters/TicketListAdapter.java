package rs.tickettracker.adapters;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.helpers.StatusHelper;
import rs.tickettracker.listeners.DeleteTicketAction;
import rs.tickettracker.listeners.EditTicketAction;

/**
 * Ticket list adapter placed on each fragment from 'Home' page. Used as placeholder for all matches.
 */
public class TicketListAdapter extends ArrayAdapter<Ticket> {
    int layoutResourceId;
    Activity activity;
    List<Ticket> data = null;
    public static ArrayList<Long> selectedIds = new ArrayList<Long>();
    TabFragmentAdapter tabManager;

    public TicketListAdapter(Activity activity, int layoutResourceId, List<Ticket> objects, TabFragmentAdapter tabManager) {
        super(activity.getApplicationContext(), layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.data = objects;
        this.activity = activity;
        this.tabManager = tabManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TicketHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TicketHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.item_icon);
            holder.txtTitle = (TextView) row.findViewById(R.id.name);
            holder.txtGain = (TextView) row.findViewById(R.id.description);
            holder.btnDelete = (Button) row.findViewById(R.id.deleteTicketButton);
            holder.btnEdit = (Button) row.findViewById(R.id.editTicketButton);
            row.setTag(holder);
        } else {
            holder = (TicketHolder) row.getTag();
        }

        Ticket ticket = data.get(position);

        if (!selectedIds.contains(ticket.getId())) {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            if (ticket.status.status.equals("Active")) {
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnEdit.setOnClickListener(new EditTicketAction((AppCompatActivity) activity, ticket.getId()));
            }
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new DeleteTicketAction(activity, ticket.getId(), tabManager, holder.btnEdit, holder.btnDelete));
        }
        holder.txtTitle.setText(ticket.ticketName);
        holder.txtGain.setText("Possible gain: " + ticket.possibleGain);
        holder.imgIcon.setImageResource(StatusHelper.getStatusIconType(ticket.status.status, activity.getApplicationContext()));
        return row;
    }

    static class TicketHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtGain;
        Button btnDelete;
        Button btnEdit;
    }

    /**
     * Remove ticket from Ticket list by ticket id.
     * @param id - Id of ticket that we want to remove from list.
     */
    public void removeById(long id) {
        for (Ticket t : new ArrayList<Ticket>(data)) {
            if (t.getId() == id) {
                data.remove(t);
                break;
            }
        }
    }

    /**
     * Check if ticket is already placed in list of tickets.
     * @param id - Id of ticket that we want to check
     * @return True if ticket is already at list.
     */
    public boolean contains(long id) {
        for (Ticket t : data) {
            if (t.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
}
