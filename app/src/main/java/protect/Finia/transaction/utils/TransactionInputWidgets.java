package protect.Finia.transaction.utils;

import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * The class of all the input widgets used when adding or editing transactions.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/05/22
 */
public class TransactionInputWidgets {
    public TextInputLayout nameInputField;
    public TextInputLayout valueInputField;
    public TextInputLayout categoryInputField;

    public TextInputEditText dateInput;
    public TextInputEditText nameInput;
    public TextInputEditText commentInput;
    public TextInputEditText valueInput;
    public AutoCompleteTextView categoryInput;

    public RelativeLayout deleteButton;
}
