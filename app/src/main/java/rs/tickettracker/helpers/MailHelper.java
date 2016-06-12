package rs.tickettracker.helpers;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * This helper class is used for sending mail to author using client apps from user telephone.
 */
public class MailHelper {

    /**
     * Sending mail to author of application.
     */
    public static Intent sendEmail() {
        String[] TO = {"ticket.tracker.sit@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        return emailIntent;
    }
}
