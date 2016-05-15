package rs.tickettracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import rs.tickettracker.R;
import rs.tickettracker.helpers.MailHelper;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button sendMail = (Button) getView().findViewById(R.id.sendEmail);

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(Intent.createChooser(MailHelper.sendEmail(), "Send mail..."));
                    Log.i("Loggin MAIL", "Finished sending email...");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
