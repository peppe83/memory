package com.vimo.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.vimo.memory.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class activityLogged extends AppCompatActivity {

    private AutoCompleteTextView mSearchView;
    private String user;
    File fileAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b!=null && b.containsKey("session")) {
            boolean session = b.getBoolean("session");
            user = b.getString("user");
            if (!session) {
                startActivity(new Intent(activityLogged.this, activityLogin.class));
            }
        }
        fileAccount = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameAccount);
        if (!fileAccount.exists()) {
            try {
                fileAccount.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                //startActivity(new Intent(activityLogged.this, ActivityAdd.class));
                Intent i = new Intent(activityLogged.this, ActivityAdd.class);
                i.putExtra("session", true);
                i.putExtra("user", user);
                startActivity(i);
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
            String content = FileUtils.decodeFile(fileAccount);
            String key = FileUtils.getKeySearchInRow(content);
            if (key.toLowerCase().contains(tag.toLowerCase())) {
                String msg = "trovato";
                String ho = "";
            } else {
                String msg = "non trovato";
                String ho = "";
            }

        }
    }
}
