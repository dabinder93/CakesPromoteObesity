package at.fhooe.mc.android.cakespromoteobesity.extra;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Spinner with Multiple Checkboxes for multiple selection of items
 */
public class MultiSelectionSpinner extends Spinner implements
        DialogInterface.OnMultiChoiceClickListener
{
    String[] allItems = null;
    String[] newItems = null;
    boolean[] mSelection = null;

    ArrayAdapter<String> simple_adapter;

    public MultiSelectionSpinner(Context context)
    {
        super(context);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;

            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        /*newItems = new String[allItems.length-1];
        for (int i = 0; i < newItems.length; i++) {
            newItems[i] = allItems[i+1];
        }*/
        builder.setMultiChoiceItems(allItems, mSelection, this);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {

            }
        });


        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        allItems = items;
        mSelection = new boolean[allItems.length];
        simple_adapter.clear();
        simple_adapter.add(allItems[0]);
        Arrays.fill(mSelection, false);
    }

    public void setItems(List<String> items) {
        allItems = items.toArray(new String[items.size()]);
        mSelection = new boolean[allItems.length];
        simple_adapter.clear();
        //simple_adapter.add(allItems[0]);
        simple_adapter.add("None selected ...");
        Arrays.fill(mSelection, false);
    }

    public void setSelection(String[] selection) {
        for (String cell : selection) {
            for (int j = 0; j < allItems.length; ++j) {
                if (allItems[j].equals(cell)) {
                    mSelection[j] = true;
                }
            }
        }
    }

    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < allItems.length; ++j) {
                if (allItems[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndicies) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (int index : selectedIndicies) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    /**
     * Get a List of Strings of the Selected Items
     * @return String List filled with the Selected Itemss Text of the Multiple Spinner
     */
    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<String>();
        for (int i = 0; i < allItems.length; ++i) {
            if (mSelection[i]) {
                selection.add(allItems[i]);
            }
        }
        return selection;
    }


    /**
     * Gets a List with selected Item Positions
     * @return Integer List with index of the selected Items in the Multiple Spinner
     */
    public List<Integer> getSelectedIndicies() {
        List<Integer> selection = new LinkedList<Integer>();
        for (int i = 0; i < allItems.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < allItems.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(allItems[i]);
            }
        }
        if (foundOne) return sb.toString();
        else return "None selected ...";
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < allItems.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(allItems[i]);
            }
        }
        return sb.toString();
    }



}
