package com.example.sundforluft.services;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

import com.example.sundforluft.R;
import com.example.sundforluft.models.SchoolModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SchoolBadgeAdapter extends BaseAdapter {
    private ArrayList<SchoolModel> items;
    private final Context context;
    private final LayoutInflater inflater;

    public SchoolBadgeAdapter(Context context) {
        items = new ArrayList<>();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addSchool(SchoolModel schoolModel) {
        items.add(schoolModel);
    }

    public void deleteSchool(int position) {
        //TODO: Delete from favorites (locally saved)
        items.remove(position);
    }

    public int getPosition(SchoolModel inputModel) {
        for (int i = 0; i < items.size(); i++) {
            if (inputModel.getName().equals(items.get(i).getName())) { return i; }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Create new object for activity listener
        convertView = inflater.inflate(R.layout.activity_listview, null);

        // Item to be displayed
        final SchoolModel model = items.get(position);

        // Change item properties to model values.
        Button button = convertView.findViewById(R.id.button);
        String schoolModelText = model.getName() + "\n" + model.getAirQualityString();
        button.setText(schoolModelText);

        // Trigger delete on click
        Button closeButton = convertView.findViewById(R.id.removeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int position = getPosition(model);

                if (position >= 0) {
                    deleteSchool(position);
                    notifyDataSetChanged();
                }
            }
        });

        // Get quality from model
        switch (model.getAirQuality()) {
            case 1:
                Drawable redCircle = ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_red, null);
                convertView.findViewById(R.id.cicle).setBackground(redCircle);
                break;

            case 2:
                Drawable orangeCircle = ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_orange, null);
                convertView.findViewById(R.id.cicle).setBackground(orangeCircle);
                break;

            case 3:
            case 4:
                Drawable greenCircle = ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_green, null);
                convertView.findViewById(R.id.cicle).setBackground(greenCircle);
                break;
        }

        // return view for current row
        return convertView;
    }

}
