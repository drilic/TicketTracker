package rs.tickettracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import rs.tickettracker.R;
import rs.tickettracker.helpers.MailHelper;
import rs.tickettracker.helpers.SyncHelper;

/**
 * This fragment contains data about creator of Application. Beside that, it contains mechanism
 * for contacting and send reports about application.
 */
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
                    if (SyncHelper.getConnectivityStatus(getContext())) {
                        startActivity(Intent.createChooser(MailHelper.sendEmail(), getActivity().getResources().getString(R.string.send_mail)));
                    } else {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.check_settings_or_net_conn), Toast.LENGTH_SHORT).show();
                    }
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_email_clients), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
