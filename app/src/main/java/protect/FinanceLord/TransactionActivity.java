package protect.FinanceLord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.Database.TransactionsDao;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;
import protect.FinanceLord.TransactionViewingUtils.CategoryLabelsAdapter;
import protect.FinanceLord.TransactionViewingUtils.ViewPagerAdapter;
import protect.FinanceLord.TransactionViewingUtils.View_TransactionsFragment;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        retrieveDataFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();

        retrieveDataFromDatabase();
    }

    private void retrieveDataFromDatabase() {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(TransactionActivity.this);
                TransactionsDao transactionsDao = database.transactionsDao();
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                final List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();
                final List<Transactions> transactions = transactionsDao.queryAllTransaction();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshCategoryViewList(budgetsTypes);
                        resetView(budgetsTypes, transactions);
                    }
                });
            }
        });
    }

    private void resetView(List<BudgetsType> budgetsTypes, List<Transactions> transactions) {

        ImageButton returnButton = findViewById(R.id.transaction_return_button);
        ImageButton addButton = findViewById(R.id.add_transaction_button);
        TabLayout tablayout = findViewById(R.id.transaction_tab_layout);
        final ViewPager viewPager = findViewById(R.id.transaction_view_pager);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<BudgetTypesDataModel> dataModels = new ArrayList<>();
        if (budgetsTypes != null){
            for (BudgetsType budgetsType : budgetsTypes){
                BudgetTypesDataModel dataModel = new BudgetTypesDataModel(budgetsType.getBudgetsCategoryId(), budgetsType.getBudgetsName());
                dataModels.add(dataModel);
            }
        } else {
            dataModels = null;
        }

        ArrayList<Fragment> fragments = new ArrayList<>();
        View_TransactionsFragment revenuesFragment = new View_TransactionsFragment(transactions, dataModels, getString(R.string.view_revenues_fragment_key));
        View_TransactionsFragment expensesFragment = new View_TransactionsFragment(transactions, dataModels, getString(R.string.view_expenses_fragment_key));
        fragments.add(revenuesFragment);
        fragments.add(expensesFragment);

        ViewPagerAdapter sectionsPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tablayout.setupWithViewPager(viewPager);

        final ArrayList<BudgetTypesDataModel> finalDataModels = dataModels;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(getString(R.string.budget_categories_key), finalDataModels);
                intent.setClass(TransactionActivity.this, TransactionAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreshCategoryViewList(List<BudgetsType> budgetsTypes) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoryLabelsList = findViewById(R.id.transaction_label_list);
        categoryLabelsList.setLayoutManager(layoutManager);
        CategoryLabelsAdapter categoryLabelsAdapter = new CategoryLabelsAdapter(this, budgetsTypes);
        categoryLabelsList.setAdapter(categoryLabelsAdapter);
    }
}
