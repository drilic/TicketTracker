package rs.tickettracker.helpers;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by gisko on 26-Apr-16.
 */
public class MailHelper {

    public static Intent sendEmail() {
        Log.i("Send email", "");
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
