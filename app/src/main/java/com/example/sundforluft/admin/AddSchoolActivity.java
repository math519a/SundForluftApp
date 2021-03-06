package com.example.sundforluft.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.UserModel;
import com.example.sundforluft.R;

import java.util.Objects;

import carbon.view.View;

public class AddSchoolActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button add;
    EditText username, password, school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_school);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.addSchool);

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        add = findViewById(R.id.button);
        add.setOnClickListener(this);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        school = findViewById(R.id.schoolEditText);
    }

    @Override
    public void onClick(android.view.View v) {
        if (v == add){
            if (validateInput(username.getText().toString()) && !password.getText().toString().equals("") && validateInput(school.getText().toString())) {
                DataAccessLayer.getInstance().addSchool(
                        school.getText().toString(),
                        new UserModel(username.getText().toString(), password.getText().toString())
                );
                DataAccessLayer.getInstance().waitForLoad();

                Toast.makeText(getApplicationContext(),
                        R.string.schoolAdded + school.getText().toString() + R.string.schoolAddedRemoved,
                        Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
                school.setText("");
            }
        }
    }

    private boolean validateInput(String input) {
        if (input.equals("")) return false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (!Character.isAlphabetic(c) && !Character.isDigit(c) &&
                    Character.toLowerCase(c) != 'æ' &&
                    Character.toLowerCase(c) != 'ø' &&
                    Character.toLowerCase(c) != 'å' &&
                    Character.toLowerCase(c) != ' ') {
                return false;
            }

        }
        return true;
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
