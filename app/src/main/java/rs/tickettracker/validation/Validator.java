package rs.tickettracker.validation;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import rs.tickettracker.R;

/**
 * Validate text fields that need to be entered. Checking isRequired, MaxLength, isDecimal, etc...
 */
public class Validator implements TextWatcher {
    private EditText inputField;
    private View view;
    private TextInputLayout ticketName, posibleGain;

    public Validator(View view, EditText inputField) {
        this.inputField = inputField;
        this.view = view;
        this.ticketName = (TextInputLayout) view.findViewById(R.id.input_ticket_name);
        this.posibleGain = (TextInputLayout) view.findViewById(R.id.input_posible_gain);
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * On each text entered validate max length.
     * @param editable - field that need to be checked.
     */
    public void afterTextChanged(Editable editable) {
        if (validateRequired(inputField.getText().toString())) {
            switch (inputField.getId()) {
                case R.id.add_ticket_name:
                    validateMaxLength(inputField.getText().toString());
                    break;
            }
        }
    }

    /**
     * Validate if current value is empty.
     * @param value - value to be checked
     * @return True if value is NOT empty.
     */
    private boolean validateRequired(String value) {
        if (value.trim().isEmpty()) {
            switch (inputField.getId()) {
                case R.id.add_ticket_name:
                    ticketName.setError(view.getResources().getString(R.string.error_msg_required));
                    ticketName.requestFocus();
                    break;
                case R.id.add_ticket_gain:
                    posibleGain.setError(view.getResources().getString(R.string.error_msg_required));
                    posibleGain.requestFocus();
                    break;
            }
            return false;
        } else {
            switch (inputField.getId()) {
                case R.id.add_ticket_name:
                    ticketName.setErrorEnabled(false);
                    break;
                case R.id.add_ticket_gain:
                    posibleGain.setErrorEnabled(false);
                    break;
            }
        }
        return true;
    }

    /**
     * Validate max length of value.
     * @param value - value to be checked
     * @return True if length of value is lower than 30.
     */
    private boolean validateMaxLength(String value) {
        if (value.length() > 30) {
            ticketName.setError(view.getResources().getString(R.string.error_msg_max_length));
            ticketName.requestFocus();
            return false;
        } else {
            ticketName.setErrorEnabled(false);
        }
        return true;
    }
}
