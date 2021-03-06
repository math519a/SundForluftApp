package com.example.sundforluft.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.R;

import java.util.ArrayList;
import java.util.Objects;

public class RemoveSchoolActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    Spinner dropdown;
    Button button;
    ArrayList<String> items;
    SchoolModel[] schools;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_remove_school);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.removeSchool);

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        dropdown = findViewById(R.id.dropdown);

        schoolLoader();

        dropdown.setOnItemSelectedListener(this);
    }

    void schoolLoader(){

        // Display de skoler der bliver hentet i dropdown menuen
        schools = DataAccessLayer.getInstance().getSchools();
        items = new ArrayList<>();


        for (SchoolModel model : schools) {
            if (model.Id != 0) {
                items.add(model.Name);
            }
        }

        adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_layout_2);
        dropdown.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        String schoolName = (String)dropdown.getSelectedItem();
        SchoolModel foundModel = DataAccessLayer.getInstance().getSchoolByName(schoolName);
        DataAccessLayer.getInstance().removeSchool(foundModel);
        DataAccessLayer.getInstance().waitForLoad();
        Toast.makeText(getApplicationContext(),
                R.string.schoolRemoved + "" + dropdown.getSelectedItem() + R.string.schoolAddedRemoved,
                Toast.LENGTH_SHORT).show();
        schoolLoader();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
