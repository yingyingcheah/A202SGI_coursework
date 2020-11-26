package com.example.yingying.moneysaver;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    //Floating button
    private FloatingActionButton mAdd;
    private FloatingActionButton mAddIncome;
    private FloatingActionButton mAddExpense;

    //Floating button text
    private TextView mIncomeText;
    private TextView mExpenseText;

    private boolean isOpen = false;

    //Animation for add button
    private Animation fadeOpen, fadeClose;

    //Recycler view to display income and expense list
    private RecyclerView mIncomeRecycler, mExpenseRecycler;

    //Firebase
    private DatabaseReference mIncomeDatabase, mExpenseDatabase;

    private TextView mIncomeTotal, mExpenseTotal, mBalanceTotal;

    private double totalIncome = 0.00, totalExpense = 0.00, totalBalance = 0.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("Income");
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("Expense");

        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);

        mIncomeTotal = (TextView) view.findViewById(R.id.dashboard_income_total);
        mExpenseTotal = (TextView) view.findViewById(R.id.dashboard_expense_total);
        mBalanceTotal = (TextView) view.findViewById(R.id.dashboard_balance);

        mAdd = (FloatingActionButton) view.findViewById(R.id.dashboard_btnAdd);
        mAddIncome = (FloatingActionButton) view.findViewById(R.id.dashboard_btnincome);
        mAddExpense = (FloatingActionButton) view.findViewById(R.id.dashboard_btnexpense);

        mIncomeText = (TextView) view.findViewById(R.id.dashboard_income_buttontext);
        mExpenseText = (TextView) view.findViewById(R.id.dashboard_expense_buttontext);

        mIncomeRecycler = (RecyclerView) view.findViewById(R.id.dashboard_income_recyclerview);
        mExpenseRecycler = (RecyclerView) view.findViewById(R.id.dashboard_expense_recyclerview);

        //Animation connect
        fadeOpen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDate();

                if (isOpen){
                    mAddIncome.startAnimation(fadeClose);
                    mAddExpense.startAnimation(fadeClose);
                    mAddIncome.setClickable(false);
                    mAddExpense.setClickable(false);

                    mIncomeText.startAnimation(fadeClose);
                    mExpenseText.startAnimation(fadeClose);
                    mIncomeText.setClickable(false);
                    mExpenseText.setClickable(false);
                    isOpen = false;
                }
                else{
                    mAddIncome.startAnimation(fadeOpen);
                    mAddExpense.startAnimation(fadeOpen);
                    mAddIncome.setClickable(true);
                    mAddExpense.setClickable(true);

                    mIncomeText.startAnimation(fadeOpen);
                    mExpenseText.startAnimation(fadeOpen);
                    mIncomeText.setClickable(true);
                    mExpenseText.setClickable(true);
                    isOpen = true;
                }
            }
        });

        //Calculate total income
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mysnapshot : dataSnapshot.getChildren()){
                    Saving temp = mysnapshot.getValue(Saving.class);

                    totalIncome += temp.getAmount();
                    String tempAmount = String.valueOf(totalIncome);

                    mIncomeTotal.setText(tempAmount);
                    totalBalance = totalIncome - totalExpense;

                    String tempBalance = String.valueOf(totalBalance);
                    mBalanceTotal.setText(tempBalance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Calculate total expense
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mysnapshot : dataSnapshot.getChildren()){
                    Saving temp = mysnapshot.getValue(Saving.class);

                    totalExpense += temp.getAmount();
                    String tempAmount = String.valueOf(totalExpense);

                    mExpenseTotal.setText(tempAmount);
                    totalBalance = totalIncome - totalExpense;

                    String tempBalance = String.valueOf(totalBalance);
                    mBalanceTotal.setText(tempBalance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Recyclerview display income list
        LinearLayoutManager incomeLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        incomeLayoutManager.setStackFromEnd(true);
        incomeLayoutManager.setReverseLayout(true);
        mIncomeRecycler.setHasFixedSize(true);
        mIncomeRecycler.setLayoutManager(incomeLayoutManager);

        //Recyclerview display expense list
        LinearLayoutManager expenseLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        expenseLayoutManager.setStackFromEnd(true);
        expenseLayoutManager.setReverseLayout(true);
        mExpenseRecycler.setHasFixedSize(true);
        mExpenseRecycler.setLayoutManager(expenseLayoutManager);

        return view;
    }

    private void insertDate(){
        mAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean insertIncome = true;
                Intent addData = InsertActivity.newIntent(getActivity(), insertIncome);
                startActivity(addData);
            }
        });

        mAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean insertIncome = false;
                Intent addData = InsertActivity.newIntent(getActivity(), insertIncome);
                startActivity(addData);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Saving, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Saving, IncomeViewHolder>
                (Saving.class, R.layout.dashboard_income, DashboardFragment.IncomeViewHolder.class, mIncomeDatabase) {
            @Override
            protected void populateViewHolder(IncomeViewHolder viewHolder, Saving model, int position) {
                viewHolder.setIncomeCategoryIcon(model.getCategory());
                viewHolder.setIncomeCategory(model.getCategory());
                viewHolder.setIncomeAmount(model.getAmount());
                viewHolder.setIncomeDate(model.getDate());
            }
        };
        mIncomeRecycler.setAdapter(incomeAdapter);

        FirebaseRecyclerAdapter<Saving, ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Saving, ExpenseViewHolder>
                (Saving.class, R.layout.dashboard_expense, DashboardFragment.ExpenseViewHolder.class, mExpenseDatabase) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder viewHolder, Saving model, int position) {
                viewHolder.setExpenseCategoryIcon(model.getCategory());
                viewHolder.setExpenseCategory(model.getCategory());
                viewHolder.setExpenseAmount(model.getAmount());
                viewHolder.setExpenseDate(model.getDate());
            }
        };
        mExpenseRecycler.setAdapter(expenseAdapter);
    }

    //For income list
    public static class IncomeViewHolder extends RecyclerView.ViewHolder {

        View mIncomeView;
        ArrayList<Category> arrCategory;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            mIncomeView = itemView;
        }

        public void setIncomeCategoryIcon(String category){
            ImageView mCategoryIcon = mIncomeView.findViewById(R.id.dashboard_income_categoryicon);
            initExpenseCategoryList();

            for (Category temp: arrCategory){
                if (temp.getname().equals(category)){
                    mCategoryIcon.setImageResource(temp.getIconResourceId());
                }
            }
        }

        public void setIncomeCategory(String category){
            TextView mCategory = mIncomeView.findViewById(R.id.dashboard_income_category);
            mCategory.setText(category);
        }

        public void setIncomeAmount(double amount){
            TextView mAmount = mIncomeView.findViewById(R.id.dashboard_income_amount);
            String temp = String.valueOf(amount);
            mAmount.setText(temp);
        }

        public void setIncomeDate(String date){
            TextView mDate = mIncomeView.findViewById(R.id.dashboard_income_date);
            mDate.setText(date);
        }

        public void initExpenseCategoryList(){
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
    }

    //For expense list
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        View mExpenseView;
        ArrayList<Category> arrCategory;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            mExpenseView = itemView;
        }

        public void setExpenseCategoryIcon(String category){
            ImageView mCategoryIcon = mExpenseView.findViewById(R.id.dashboard_expense_categoryicon);
            initExpenseCategoryList();

            for (Category temp: arrCategory){
                if (temp.getname().equals(category)){
                    mCategoryIcon.setImageResource(temp.getIconResourceId());
                }
            }
        }

        public void setExpenseCategory(String category){
            TextView mCategory = mExpenseView.findViewById(R.id.dashboard_expense_category);
            mCategory.setText(category);
        }

        public void setExpenseAmount(double amount){
            TextView mAmount = mExpenseView.findViewById(R.id.dashboard_expense_amount);
            String temp = String.valueOf(amount);
            mAmount.setText(temp);
        }

        public void setExpenseDate(String date){
            TextView mDate = mExpenseView.findViewById(R.id.dashboard_expense_date);
            mDate.setText(date);
        }

        public void initExpenseCategoryList(){
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
    }
}
