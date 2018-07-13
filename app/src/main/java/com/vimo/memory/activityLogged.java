package com.vimo.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

public class activityLogged extends AppCompatActivity {

    private AutoCompleteTextView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        mSearchView = (AutoCompleteTextView) findViewById(R.id.txt_tag);

        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activityLogged.this, ActivityAdd.class));
            }
        });
    }

    private void search() {
        // Reset errors.
        // Store values at the time of the login attempt.
        String tag = mSearchView.getText().toString();
        if (tag.equals("")) {
            mSearchView.setError(getString(R.string.error_field_required));
            mSearchView.requestFocus();
        } else {
            mSearchView.setError(null);
            //TODO - faccio la ricerca
        }
    }
}
