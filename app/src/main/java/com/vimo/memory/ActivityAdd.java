package com.vimo.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityAdd extends AppCompatActivity {

    private TextView txtViewTag;

    //https://code.tutsplus.com/tutorials/android-essentials-creating-simple-user-forms--mobile-1758

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        txtViewTag = (TextView) findViewById(R.id.txt_tag);
    }

    private void add() {
        // Reset errors.
        // Store values at the time of the login attempt.
        String tag = txtViewTag.getText().toString();
        if (tag.equals("")) {
            txtViewTag.setError(getString(R.string.error_field_required));
            txtViewTag.requestFocus();
        } else {
            txtViewTag.setError(null);
            //TODO - faccio inserimento

            //se non ci sono errori ritorno indietro
            startActivity(new Intent(ActivityAdd.this, activityLogged.class));
        }
    }
}
