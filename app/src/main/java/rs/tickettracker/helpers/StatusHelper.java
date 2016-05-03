package rs.tickettracker.helpers;

import android.content.Context;
import android.content.res.TypedArray;

import rs.tickettracker.R;

/**
 * Created by gisko on 03-May-16.
 */
public class StatusHelper {

    public static int getStatusIconType(String status, Context context) {
        TypedArray statusImages = context.getResources().obtainTypedArray(R.array.status_img);
        int imageId = -1;
        switch (status) {
            case "Active":
                imageId = statusImages.getResourceId(0, -1);
                break;
            case "Win":
                imageId = statusImages.getResourceId(1, -1);
                break;
            case "Lose":
                imageId = statusImages.getResourceId(2, -1);
                break;
        }
        return imageId;
    }

    public static int getStatusColor(String status, Context context) {
        int statusColor = -1;
        TypedArray statusColors = context.getResources().obtainTypedArray(R.array.status_color);
        switch (status) {
            case "Active":
                statusColor = statusColors.getResourceId(0, -1);
                break;
            case "Lose":
                statusColor = statusColors.getResourceId(1, -1);
                break;
            case "Win":
                statusColor = statusColors.getResourceId(2, -1);
                break;
        }
        return statusColor;
    }

}
