package com.vimo.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vimo.memory.utils.FileUtils;

import java.io.File;

public class ActivityAdd extends AppCompatActivity {

    private TextView txtViewTag;
    private String user;
    //https://code.tutsplus.com/tutorials/android-essentials-creating-simple-user-forms--mobile-1758

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if (b!=null && b.containsKey("session")) {
            boolean session = b.getBoolean("session");
            user = b.getString("user");
            if (!session) {
                startActivity(new Intent(ActivityAdd.this, activityLogin.class));
            }
        }

        setContentView(R.layout.activity_add);
        txtViewTag = (TextView) findViewById(R.id.txt_tag);

        Button btnAdd = (Button) findViewById(R.id.btnAddAcount);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    public void add() {
        // Reset errors.
        // Store values at the time of the login attempt.
        String tag = txtViewTag.getText().toString();
        if (tag.equals("")) {
            txtViewTag.setError(getString(R.string.error_field_required));
            txtViewTag.requestFocus();
        } else {
            txtViewTag.setError(null);
            //TODO - faccio inserimento

            String id = "";
            String id_type = "";
            String link = "";
            String username = "";
            String password = "";
            String otp= "";
            String keyword = "hotmail";
            boolean delete = false;
            File file = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameAccount);
            String row = FileUtils.buildRowAccount(id,user,id_type, link,username,password,otp,keyword,delete);
            FileUtils.saveFile(file, row);
            //se non ci sono errori ritorno indietro
            startActivity(new Intent(ActivityAdd.this, activityLogged.class));
        }
    }
}
