package com.example.sundforluft.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;

public class HelpRanklisteFragment extends Fragment implements View.OnClickListener {

    Button back, forward;

    ImageView imageView,  help_container;

    int n;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_help, container, false);

        back = view.findViewById(R.id.back);
        forward = view.findViewById(R.id.forward);
        imageView = view.findViewById(R.id.imageView);
        help_container = view.findViewById(R.id.help_container);
        back.setOnClickListener(this);
        forward.setOnClickListener(this);

        imageView.setImageResource(R.drawable.ic_help_1_place);
        help_container.setImageResource(R.drawable.favorit_help_1);
        n = 1;


        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == back && n != 1){
            n--;
        } else if (v == forward && n != 7){
            n++;
        } else {
            if(n <= 1)n = 7;
            if(n >= 7)n = 1;
        }
    }
}
