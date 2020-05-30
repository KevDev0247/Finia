package protect.FinanceLord;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.TimeUtils.CalendarDialog;
import protect.FinanceLord.TimeUtils.TimeProcessor;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;
import protect.FinanceLord.TransactionUtils.TransactionInputUtils;
import protect.FinanceLord.TransactionUtils.TransactionInsertUtils;

public class TransactionEditActivity extends AppCompatActivity {

    private Date currentTime;
    private TransactionInsertUtils insertUtils;
    private TransactionInputUtils inputUtils = new TransactionInputUtils();

    private static String TAG = "TransactionEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        ImageButton returnButton = findViewById(R.id.transaction_edit_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String fragmentTag = getIntent().getExtras().getString(getString(R.string.transaction_fragment_key));
        if (fragmentTag.equals(getString(R.string.view_revenues_fragment_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_revenues, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            retrieveDataFromDatabase(fragmentTag);

        } else if (fragmentTag.equals(getString(R.string.view_expenses_fragment_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_expenses, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            retrieveDataFromDatabase(fragmentTag);
        }
    }

    private void retrieveDataFromDatabase(final String fragmentTag) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(TransactionEditActivity.this);
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                final List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fragmentTag.equals(getString(R.string.view_revenues_fragment_key))) {
                            initializeRevenueSection(budgetsTypes);

                        } else if (fragmentTag.equals(getString(R.string.view_expenses_fragment_key))) {
                            initializeExpenseSection(budgetsTypes);
                        }
                    }
                });
            }
        });
    }

    private void initializeRevenueSection(List<BudgetsType> budgetsTypes) {
        inputUtils.nameInputField = findViewById(R.id.revenue_name_field);
        inputUtils.valueInputField = findViewById(R.id.revenue_value_field);
        inputUtils.categoryInputField = findViewById(R.id.revenue_category_field);

        inputUtils.nameInput = findViewById(R.id.revenue_name_input);
        inputUtils.valueInput = findViewById(R.id.revenue_value_input);
        inputUtils.commentInput = findViewById(R.id.revenue_comments_input);
        inputUtils.categoryInput = findViewById(R.id.revenue_category_input);
        inputUtils.dateInput = findViewById(R.id.revenue_date_input);

        setUpCategoryAndDateInput(budgetsTypes);

        setUpSaveButton();

        loadDataToInputBoxes(budgetsTypes);

        setUpInputUtils(budgetsTypes, getString(R.string.revenues_section_key));
    }

    private void initializeExpenseSection(List<BudgetsType> budgetsTypes) {
        inputUtils.nameInputField = findViewById(R.id.expenses_name_field);
        inputUtils.valueInputField = findViewById(R.id.expenses_value_field);
        inputUtils.categoryInputField = findViewById(R.id.expenses_category_field);

        inputUtils.nameInput = findViewById(R.id.expenses_name_input);
        inputUtils.valueInput = findViewById(R.id.expenses_value_input);
        inputUtils.commentInput = findViewById(R.id.expenses_comments_input);
        inputUtils.categoryInput = findViewById(R.id.expenses_category_input);
        inputUtils.dateInput = findViewById(R.id.expenses_date_input);

        setUpCategoryAndDateInput(budgetsTypes);

        setUpSaveButton();

        loadDataToInputBoxes(budgetsTypes);

        setUpInputUtils(budgetsTypes, getString(R.string.expenses_section_key));
    }

    private void setUpCategoryAndDateInput(List<BudgetsType> budgetsTypes){
        List<String> typeNames = new ArrayList<>();
        for (BudgetsType budgetsType : budgetsTypes){
            typeNames.add(budgetsType.getBudgetsName());
        }

        inputUtils.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.transaction_categories_dropdown, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

        inputUtils.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(getSupportFragmentManager(), "DateTimePicker");
            }
        });
    }

    private void setUpSaveButton() {
        ImageButton saveButton = findViewById(R.id.transaction_edit_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUtils.insertOrUpdateData(false, true, getIntent().getExtras().getInt(getString(R.string.transaction_id_key)));
                insertUtils.addTextListener();
            }
        });
    }

    private void loadDataToInputBoxes(List<BudgetsType> budgetsTypes) {
        DecimalFormat decimalFormat = new DecimalFormat();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.CANADA);

        inputUtils.nameInput.setText(getIntent().getStringExtra(getString(R.string.transaction_name_key)));
        inputUtils.valueInput.setText(decimalFormat.format(getIntent().getExtras().getFloat(getString(R.string.transaction_value_key))).replace("-",""));
        inputUtils.dateInput.setText(dateFormat.format(getIntent().getExtras().getLong(getString(R.string.transaction_date_key))));

        try {
            currentTime = TimeProcessor.parseDateString(dateFormat.format(getIntent().getExtras().getLong(getString(R.string.transaction_date_key))), getString(R.string.date_format));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (getIntent().getStringExtra(getString(R.string.transaction_comments_key)) != null){
            inputUtils.commentInput.setText(getIntent().getStringExtra(getString(R.string.transaction_comments_key)));
        }

        for (BudgetsType budgetsType : budgetsTypes) {
            if (getIntent().getExtras().getInt(getString(R.string.transaction_category_key)) == budgetsType.getBudgetsCategoryId()) {
                inputUtils.categoryInput.setText(budgetsType.getBudgetsName());
            }
        }
    }

    private void setUpInputUtils(List<BudgetsType> budgetsTypes, String sectionTag) {
        ArrayList<BudgetTypesDataModel> dataModels = new ArrayList<>();
        if (budgetsTypes != null){
            for (BudgetsType budgetsType : budgetsTypes){
                BudgetTypesDataModel dataModel = new BudgetTypesDataModel(budgetsType.getBudgetsCategoryId(), budgetsType.getBudgetsName());
                dataModels.add(dataModel);
            }
        } else {
            dataModels = null;
        }

        insertUtils = new TransactionInsertUtils(this, currentTime, inputUtils, dataModels, sectionTag);
    }

    private CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            inputUtils.dateInput.setText(stringDate);
        }
    };
}