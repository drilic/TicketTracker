package rs.tickettracker.helpers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * This helper class is used as factory for components.
 */
public class ComponentsHelper {

    /**
     * Create new material spinner from static list.
     */
    public static MaterialSpinner createSpinner(String[] valueList, View view, int componentId, int selected, boolean animate) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, valueList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spiner = (MaterialSpinner) view.findViewById(componentId);
        spiner.setAdapter(adapter);
        if (animate) {
            spiner.setSelection(selected);
        } else {
            spiner.setSelection(selected, animate);
        }
        return spiner;
    }

    /**
     * Create new material spinner from List of dinamic objects.
     */
    public static MaterialSpinner createSpinner(List<?> valueList, View view, int componentId, int selected, boolean animate) {
        ArrayAdapter<?> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, valueList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spiner = (MaterialSpinner) view.findViewById(componentId);
        spiner.setAdapter(adapter);
        if (animate) {
            spiner.setSelection(selected);
        } else {
            spiner.setSelection(selected, animate);
        }
        return spiner;
    }

    /**
     * Set dynamic height for list views depends of size of elements in list.
     * @param mListView - list that need to be resized.
     */
    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }
}
