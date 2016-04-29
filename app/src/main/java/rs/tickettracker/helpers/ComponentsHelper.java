package rs.tickettracker.helpers;

import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import fr.ganfra.materialspinner.MaterialSpinner;
import rs.tickettracker.R;

/**
 * Created by gisko on 29-Apr-16.
 */
public class ComponentsHelper {
    public static MaterialSpinner createSpinner(String[] valueList, View view, int componentId, int selected){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, valueList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spiner = (MaterialSpinner) view.findViewById(componentId);
        spiner.setAdapter(adapter);
        spiner.setSelection(selected);
        return spiner;
    }
}
