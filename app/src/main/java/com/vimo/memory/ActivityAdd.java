package com.vimo.memory;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.vimo.memory.data.Account;
import com.vimo.memory.utils.FileUtils;

import java.io.File;

public class ActivityAdd extends AppCompatActivity {

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
                return;
            }
        } else {
            startActivity(new Intent(ActivityAdd.this, activityLogin.class));
            return;
        }

        setContentView(R.layout.activity_add);

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setTypeface(fontAwesomeFont);
        /*int color = Color.parseColor("#FFFFFF");
        btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));*/
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setTypeface(fontAwesomeFont);
        /*int color = Color.parseColor("#FFFFFF");
        btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));*/
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backView();
            }
        });


        /*Button btnAdd = (Button) findViewById(R.id.btnAddAcount);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });*/
    }

    public void backView() {
        Intent i = new Intent(ActivityAdd.this, activityLogged.class);
        i.putExtra("session", true);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void addAccount() {
        // Reset errors.
        // Store values at the time of the login attempt.
        Spinner  spinnerViewType = (Spinner) findViewById(R.id.cmb_type);
        TextView txtViewKey = (TextView) findViewById(R.id.txt_key);
        TextView  txtViewUsername = (TextView) findViewById(R.id.txt_username);
        TextView  txtViewPassword = (TextView) findViewById(R.id.txt_password);
        TextView  txtViewPassword2 = (TextView) findViewById(R.id.txt_confirm_password);
        TextView  txtViewOpt = (TextView) findViewById(R.id.txt_opt);
        TextView  txtViewLink = (TextView) findViewById(R.id.txt_link);

        String key = txtViewKey.getText().toString();
        if (key.equals("")) {
            txtViewKey.setError(getString(R.string.error_field_required));
            txtViewKey.requestFocus();
        } else {
            txtViewKey.setError(null);
            //TODO - faccio inserimento
            String idDb="";
            String idFile="";
            String idUser="";
            String type = (String)spinnerViewType.getSelectedItem(); //TODO
            /*Resources res = getResources();
            String[] types = res.getStringArray(R.array.typelist);
            for (int i=0; i<types.length; i++) {
                if (types[i].equals(type)) {

                }
            }*/
            String idType = type;
            String link = txtViewLink.getText().toString();
            String userName = txtViewUsername.getText().toString();
            String password = txtViewPassword.getText().toString();
            String password2 = txtViewPassword2.getText().toString();
            String otp = txtViewOpt.getText().toString();
            String tagToSave=key;
            String delete = "false";

            if (!password.equals(password2)) {
                txtViewPassword2.setError(getString(R.string.error_field_pwd_no_match));
                txtViewPassword2.requestFocus();
                return;
            }


            Account acc = new Account(idDb, idFile, idUser, idType, link, userName, password, otp, tagToSave, delete);

            File file = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameAccount);
            //String row = FileUtils.buildRowAccount(id,user,id_type, link,username,password,otp,keyword,delete);
            String row = acc.buildRow();
            //FileUtils.saveFile(file, row);
            FileUtils.addRow(file, row);
            //se non ci sono errori ritorno indietro
            startActivity(new Intent(ActivityAdd.this, activityLogged.class));
        }
    }
}
