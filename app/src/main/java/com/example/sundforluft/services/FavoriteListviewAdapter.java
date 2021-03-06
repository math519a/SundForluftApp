package com.example.sundforluft.services;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;
import com.example.sundforluft.models.FavoritListviewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<FavoritListviewModel> items;
    private final Fragment fragment;
    private final LayoutInflater inflater;

    private HashMap<String, FavoritListviewModel> textToModel = new HashMap<>();

    public enum ClickListenerType {
        School,
        Close
    }

    public interface ClickListener {
        void onClick(FavoritListviewModel model, ClickListenerType type);
    }
    private ClickListener clickListener;
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button)v;

        if (clickListener == null) return;

        if (btn.getText().length() > 0) { //  school
            FavoritListviewModel model = textToModel.get(btn.getText());
            clickListener.onClick(model, ClickListenerType.School);
        } else { // delete
            FavoritListviewModel model = items.get(btn.getId());
            clickListener.onClick(model, ClickListenerType.Close);
        }
    }

    public void deleteSchoolByModel(FavoritListviewModel model) {
        int position = getPosition(model);
        if (position >= 0) {
            deleteSchool(position);
            notifyDataSetChanged();
        }
    }

    public String getName(FavoritListviewModel model){
        return items.get(getPosition(model)).getName();
    }


    public FavoriteListviewAdapter(Fragment fragment) {
        items = new ArrayList<>();
        this.fragment = fragment;
        inflater = LayoutInflater.from(fragment.getActivity());
    }

    public void addSchool(FavoritListviewModel favoritListviewModel) {
        items.add(favoritListviewModel);
    }

    private void deleteSchool(int position) {
        items.remove(position);
    }

    private int getPosition(FavoritListviewModel inputModel) {
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

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Create new object for activity listener
        convertView = inflater.inflate(R.layout.activity_favorite_listview, null);

        // Item to be displayed
        FavoritListviewModel model = items.get(position);

        // Change item properties to model values.
        Button button = convertView.findViewById(R.id.button);
        String schoolModelText = model.getName() + "\n" + model.getAirQualityString();
        button.setText(schoolModelText);
        textToModel.put(schoolModelText, model);
        button.setOnClickListener(this);

        // Trigger delete on click
        Button closeButton = convertView.findViewById(R.id.removeButton);
        closeButton.setId(position);
        closeButton.setOnClickListener(this);

        // Get quality from model
        switch (model.getAirQuality()) {
            case 1:
                Drawable redCircle = ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.ic_listview_circle_red, null);
                convertView.findViewById(R.id.cicle).setBackground(redCircle);
                break;

            case 2:
                Drawable orangeCircle = ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.ic_listview_circle_orange, null);
                convertView.findViewById(R.id.cicle).setBackground(orangeCircle);
                break;

            case 3:
            case 4:
                Drawable greenCircle = ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.ic_listview_circle_green, null);
                convertView.findViewById(R.id.cicle).setBackground(greenCircle);
                break;
        }

        // return view for current row
        return convertView;
    }

}