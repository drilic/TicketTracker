package rs.tickettracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Ticket;
import rs.tickettracker.R;
import rs.tickettracker.helpers.StatusHelper;

/**
 * Created by gisko on 03-May-16.
 */
public class TicketListAdapter extends ArrayAdapter<Ticket> {
    Context context;
    int layoutResourceId;
    List<Ticket> data = null;

    public TicketListAdapter(Context context, int layoutResourceId, List<Ticket> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TicketHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TicketHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.item_icon);
            holder.txtTitle = (TextView) row.findViewById(R.id.name);
            holder.txtGain = (TextView) row.findViewById(R.id.description);

            row.setTag(holder);
        } else {
            holder = (TicketHolder) row.getTag();
        }

        Ticket ticket = data.get(position);
        holder.txtTitle.setText(ticket.ticketName);
        holder.txtGain.setText("Possible gain: " + ticket.possibleGain);
        holder.imgIcon.setImageResource(StatusHelper.getStatusIconType(ticket.status.status, context));

        return row;
    }

    static class TicketHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtGain;
    }

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
