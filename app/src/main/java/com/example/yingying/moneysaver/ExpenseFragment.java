package com.example.yingying.moneysaver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
public class ExpenseFragment extends Fragment {

    //Recyclerview
    private RecyclerView mExpenseRecyclerView;
    //Firebase
    private DatabaseReference mExpenseDatabase;

    private TextView mExpenseTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("Expense");

        mExpenseTotal = (TextView) view.findViewById(R.id.expense_expense_amount);

        mExpenseRecyclerView = (RecyclerView) view.findViewById(R.id.expense_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mExpenseRecyclerView.setHasFixedSize(true);
        mExpenseRecyclerView.setLayoutManager(layoutManager);

        //Calculate total expense
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double total = 0;

                for (DataSnapshot mysnapshot : dataSnapshot.getChildren()){
                    Saving temp = mysnapshot.getValue(Saving.class);

                    total += temp.getAmount();
                    String tempAmount = String.valueOf(total);

                    mExpenseTotal.setText(tempAmount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Saving, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Saving, MyViewHolder>
                (Saving.class, R.layout.expense_recycler_data, MyViewHolder.class, mExpenseDatabase) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Saving model, int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setCategory(model.getCategory());
                viewHolder.setCategoryIcon(model.getCategory());
                viewHolder.setAmount(model.getAmount());

                final String id = model.getId();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean updateIncome = false;
                        Intent updateData = UpdateActivity.newIntent(getActivity(), updateIncome, id);
                        startActivity(updateData);
                    }
                });
            }
        };

        mExpenseRecyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ArrayList<Category> arrCategory;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setCategory(String category){
            TextView mCategory = mView.findViewById(R.id.expense_recycler_category);
            mCategory.setText(category);
        }

        private void setCategoryIcon(String category){
            ImageView mCategoryIcon = mView.findViewById(R.id.expense_recycler_categoryicon);
            initCategoryList();

            for (Category temp: arrCategory){
                if (temp.getname().equals(category)){
                    mCategoryIcon.setImageResource(temp.getIconResourceId());
                }
            }

        }

        private void setDate(String date){
            TextView mDate = mView.findViewById(R.id.expense_recycler_date);
            mDate.setText(date);
        }

        private void setAmount(double amount){
            TextView mAmount = mView.findViewById(R.id.expense_recycler_amount);
            String temp = String.valueOf(amount);
            mAmount.setText(temp);
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
    }
}
