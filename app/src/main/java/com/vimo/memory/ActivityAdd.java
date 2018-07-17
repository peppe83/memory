package com.vimo.memory;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.vimo.memory.data.Account;
import com.vimo.memory.data.Constants;
import com.vimo.memory.utils.FileUtils;
import com.vimo.memory.utils.TextDrawable;

import java.io.File;
import java.util.List;

public class ActivityAdd extends AppCompatActivity {
    private int nextIdFile = 0;
    private File fileAccount;
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

        /*Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setTypeface(fontAwesomeFont);
        //int color = Color.parseColor("#FFFFFF");
        //btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setTypeface(fontAwesomeFont);
        //int color = Color.parseColor("#FFFFFF");
        //btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backView();
            }
        });*/


        /*Button btnAdd = (Button) findViewById(R.id.btnAddAcount);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });*/

        //recupero l'ultimo id e lo incremento di uno
        fileAccount = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameAccount);
        nextIdFile = FileUtils.getNextIdFile(fileAccount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_add_account, menu);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        TextDrawable faIconCancel = new TextDrawable(this);
        faIconCancel.setTextSize(TypedValue.COMPLEX_UNIT_PT, 15);
        faIconCancel.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        faIconCancel.setTextColor(Constants.COLOR_ICON_GREEN);
        faIconCancel.setTypeface(fontAwesomeFont);
        faIconCancel.setText(getResources().getText(R.string.font_awesome_android_cancel));
        MenuItem menuItemCancel = menu.findItem(R.id.action_add_cancel);
        menuItemCancel.setIcon(faIconCancel);

        TextDrawable faIconSave = new TextDrawable(this);
        faIconSave.setTextSize(TypedValue.COMPLEX_UNIT_PT, 15);
        faIconSave.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        faIconSave.setTextColor(Constants.COLOR_ICON_GREEN);
        faIconSave.setTypeface(fontAwesomeFont);
        faIconSave.setText(getResources().getText(R.string.font_awesome_android_save));
        MenuItem menuItemSave = menu.findItem(R.id.action_add_save);
        menuItemSave.setIcon(faIconSave);
        //menuItem.setOnMenuItemClickListener()
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_add_save:
                addAccount();
                break;
            case R.id.action_add_cancel:
                backView();
                break;
            default:
                break;
        }
        return true;
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
        TextView  txtViewOpt2 = (TextView) findViewById(R.id.txt_cofirm_opt);
        TextView  txtViewLink = (TextView) findViewById(R.id.txt_link);

        String key = txtViewKey.getText().toString();
        if (key.equals("")) {
            txtViewKey.setError(getString(R.string.error_field_required));
            txtViewKey.requestFocus();
        } else {
            txtViewKey.setError(null);
            String idDb="";
            String idFile = ""+nextIdFile;
            String idUser = user;
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
            String otp2 = txtViewOpt2.getText().toString();
            String tagToSave=key;
            String delete = "false";

            if (userName.equals("")) {
                txtViewPassword2.setError(getString(R.string.error_field_required));
                txtViewPassword2.requestFocus();
                return;
            }

            if (password.equals("")) {
                txtViewPassword.setError(getString(R.string.error_field_required));
                txtViewPassword.requestFocus();
                return;
            }

            if (!password.equals(password2)) {
                txtViewPassword2.setError(getString(R.string.error_field_pwd_no_match));
                txtViewPassword2.requestFocus();
                return;
            }

            if (!otp.equals("") && !otp.equals(otp2)) {
                txtViewOpt2.setError(getString(R.string.error_field_otp_no_match));
                txtViewOpt2.requestFocus();
                return;
            }

            Account acc = new Account(idFile, idDb, idUser, idType, link, userName, password, otp, tagToSave, delete);
            File file = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameAccount);
            //String row = FileUtils.buildRowAccount(id,user,id_type, link,username,password,otp,keyword,delete);
            String row = acc.buildRow();
            //FileUtils.saveFile(file, row);
            FileUtils.addRow(file, row);
            //se non ci sono errori ritorno indietro
            Intent i = new Intent(ActivityAdd.this, activityLogged.class);
            i.putExtra("session", true);
            i.putExtra("user", user);
            startActivity(i);
        }
    }
}
