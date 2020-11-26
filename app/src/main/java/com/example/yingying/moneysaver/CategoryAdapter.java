package com.example.yingying.moneysaver;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Context context, ArrayList<Category> categoryList){
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_row, parent, false);
        }

        ImageView iconImage = convertView.findViewById(R.id.icon_image);
        TextView name = convertView.findViewById(R.id.category_text);

        Category currentItem = getItem(position);

        if(currentItem != null){
            iconImage.setImageResource(currentItem.getIconResourceId());
            name.setText(currentItem.getname());
        }

        return convertView;
    }
}
