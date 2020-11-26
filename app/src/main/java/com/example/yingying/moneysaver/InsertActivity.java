package com.example.yingying.moneysaver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InsertActivity extends AppCompatActivity {

    private static final String EXTRA_INSERT_INCOME = "com.example.yingying.moneysaver.insert_income";

    public static Intent newIntent(Context packageContext, boolean insertIncome){
        Intent i = new Intent(packageContext, InsertActivity.class);
        i.putExtra(EXTRA_INSERT_INCOME, insertIncome);
        return i;
    }

    private boolean mInsertIncome;
    String id = "", category = "", title = "", date = "", remarks = "";
    double amount = 0.0;

    private Spinner mCategory;
    private TextInputEditText mTitle;
    private TextInputEditText mAmount;
    private TextView mDate;
    private TextInputEditText mRemarks;
    private Button mSave, mCancel;

    private ArrayList<Category> arrCategory;
    private CategoryAdapter adpCategory;

    //Firebase
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        mInsertIncome = getIntent().getBooleanExtra(EXTRA_INSERT_INCOME, false);

        mCategory = (Spinner) findViewById(R.id.insert_category);
        mTitle = (TextInputEditText) findViewById(R.id.insert_title);
        mAmount = (TextInputEditText) findViewById(R.id.insert_amount);
        mDate = (TextView) findViewById(R.id.insert_date);
        mRemarks = (TextInputEditText) findViewById(R.id.insert_remarks);
        mSave = (Button) findViewById(R.id.insert_btnsave);
        mCancel = (Button) findViewById(R.id.insert_btncancel);

        //Launch the DatePickerFragment to prompt user choose a date
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertDatePickerFragment df = new InsertDatePickerFragment();
                df.show(getSupportFragmentManager(),"Pick a date");
            }
        });

        //Spinner connect to adapter
        initCategoryList();
        adpCategory = new CategoryAdapter(this, arrCategory);
        mCategory.setAdapter(adpCategory);
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category clicked = (Category) parent.getSelectedItem();
                category = clicked.getname();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("Income");
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("Expense");

        //Save the inserted data
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = mIncomeDatabase.push().getKey();
                title = mTitle.getText().toString().trim();
                date = mDate.getText().toString().trim();
                amount = Double.parseDouble(mAmount.getText().toString().trim());
                remarks = mRemarks.getText().toString().trim();

                Saving saving = new Saving(id, category, title, amount, date, remarks);

                if (mInsertIncome){
                    mIncomeDatabase.child(id).setValue(saving);
                    Toast.makeText(InsertActivity.this, "Income data saved successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    mExpenseDatabase.child(id).setValue(saving);
                    Toast.makeText(InsertActivity.this, "Expense data saved successfully", Toast.LENGTH_SHORT).show();
                }
                InsertActivity.super.onBackPressed();
            }
        });

        //Cancel the data insert and back to previous activity
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertActivity.super.onBackPressed();
            }
        });
    }

    private void initCategoryList(){
        arrCategory = new ArrayList<>();
        arrCategory.add(new Category("Food and Beverage", R.drawable.ic_food_beverage));
        arrCategory.add(new Category("Clothing", R.drawable.ic_clothing));
        arrCategory.add(new Category("Sport", R.drawable.ic_sport));
        arrCategory.add(new Category("Fuel", R.drawable.ic_fuel));
        arrCategory.add(new Category("Travel", R.drawable.ic_travel));
        arrCategory.add(new Category("Pet", R.drawable.ic_pet));
        arrCategory.add(new Category("Baby", R.drawable.ic_baby));
        arrCategory.add(new Category("Health", R.drawable.ic_health));
        arrCategory.add(new Category("Education", R.drawable.ic_education));
        arrCategory.add(new Category("Entertainment", R.drawable.ic_entertainment));
        arrCategory.add(new Category("Gifts", R.drawable.ic_gift));
        arrCategory.add(new Category("Transport", R.drawable.ic_transport));
        arrCategory.add(new Category("Shopping", R.drawable.ic_shopping));
        arrCategory.add(new Category("Bill", R.drawable.ic_bill));
        arrCategory.add(new Category("Salary", R.drawable.ic_salary));
        arrCategory.add(new Category("Awards", R.drawable.ic_award));
        arrCategory.add(new Category("Rental", R.drawable.ic_rental));
        arrCategory.add(new Category("Refund", R.drawable.ic_refund));
        arrCategory.add(new Category("Investment", R.drawable.ic_investment));
        arrCategory.add(new Category("Others", R.drawable.ic_others));
    }

    public void setDate(int year, int month, int day){
        String newDate = day + "/" + month + "/" + year;
        //Set the selected date into textview
        mDate.setText(newDate);
    }
}
