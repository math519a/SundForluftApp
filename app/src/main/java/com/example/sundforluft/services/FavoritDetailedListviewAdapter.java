package com.example.sundforluft.services;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;
import com.example.sundforluft.models.FavoritDetailedListviewModel;

import java.util.ArrayList;

public class FavoritDetailedListviewAdapter extends BaseAdapter {

    private ArrayList<FavoritDetailedListviewModel> items;
    private final Fragment fragment;
    private final LayoutInflater inflater;

    public FavoritDetailedListviewAdapter(Fragment fragment) {
        items = new ArrayList<>();
        this.fragment = fragment;
        inflater = LayoutInflater.from(fragment.getActivity());
    }

    public void addSchool(FavoritDetailedListviewModel favoritDetailedListviewModel) {
        items.add(favoritDetailedListviewModel);
    }

    public int getPosition(FavoritDetailedListviewModel inputModel) {
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
        convertView = inflater.inflate(R.layout.activity_favorit_detailed_listview, null);

        // Item to be displayed
        final FavoritDetailedListviewModel model = items.get(position);

        // Change item properties to model values.
        Button button = convertView.findViewById(R.id.button);
        String schoolModelText = model.getName();
        button.setText(schoolModelText);
        TextView text = convertView.findViewById(R.id.text);
        String modelText = model.getProcent();
        text.setText(modelText);

        // Get quality from model
        /*switch (model.getAirQuality()) {
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
        }*/

        // return view for current row
        return convertView;
    }
}
