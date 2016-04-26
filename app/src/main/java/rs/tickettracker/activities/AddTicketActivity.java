package rs.tickettracker.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import rs.tickettracker.R;

public class AddTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton addMatchFab = (FloatingActionButton) findViewById(R.id.add_match_fab);
        addMatchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CLICK", "Click test");
            }
        });
        addMatchFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Add new match on ticket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

        FloatingActionButton saveTicketFab = (FloatingActionButton) findViewById(R.id.save_ticket_fab);
        saveTicketFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Save current ticket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

    }


}
