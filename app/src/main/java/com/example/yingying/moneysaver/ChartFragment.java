package com.example.yingying.moneysaver;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    private TextView mChartIncome, mChartExpense;
    private PieChart mPieChart;

    //Firebase
    private DatabaseReference mIncomeDatabase, mExpenseDatabase;

    double totalIncome = 0.00, totalExpense = 0.00;
    private int percentageIncome = 0, percentageExpense = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("Income");
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("Expense");

        mChartIncome = (TextView) view.findViewById(R.id.chart_income);
        mChartExpense = (TextView) view.findViewById(R.id.chart_expense);
        mPieChart = (PieChart) view.findViewById(R.id.piechart);

        //Calculate total income
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mysnapshot : dataSnapshot.getChildren()){
                    Saving temp = mysnapshot.getValue(Saving.class);

                    totalIncome += temp.getAmount();
                    //Calculate percentage of total income and total expense
                    percentageIncome = (int) (totalIncome / (totalIncome + totalExpense) * 100);
                    percentageExpense = (int) (totalExpense / (totalIncome + totalExpense) * 100);
                    setData();
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
                    percentageIncome = (int) (totalIncome / (totalIncome + totalExpense) * 100);
                    percentageExpense = (int) (totalExpense / (totalIncome + totalExpense) * 100);
                    setData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setData(){
        //Set the percentage of income and expense
        mChartIncome.setText(Integer.toString(percentageIncome));
        mChartExpense.setText(Integer.toString(percentageExpense));

        // Set the data and color to the pie chart
        mPieChart.addPieSlice(new PieModel("Income", Integer.parseInt(mChartIncome.getText().toString()),
                Color.parseColor("#66BB6A")));
        mPieChart.addPieSlice(new PieModel("Expense", Integer.parseInt(mChartExpense.getText().toString()),
                Color.parseColor("#EF5350")));

        //To animate the pie chart
        mPieChart.startAnimation();
    }

}
