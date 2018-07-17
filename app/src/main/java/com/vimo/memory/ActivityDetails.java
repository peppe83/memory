package com.vimo.memory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vimo.memory.data.Account;
import com.vimo.memory.data.Constants;
import com.vimo.memory.utils.FileUtils;
import com.vimo.memory.utils.TextDrawable;

import java.io.File;

public class ActivityDetails extends AppCompatActivity {
    private Account account;
    private String user;
    private File fileAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if (b!=null && b.containsKey("session")) {
            user = b.getString("user");
            boolean session = b.getBoolean("session");
            String row = b.getString("account");
            account = Account.buildAccount(row);
            if (!session || account==null) {
                startActivity(new Intent(ActivityDetails.this, activityLogin.class));
                return;
            }
        } else {
            startActivity(new Intent(ActivityDetails.this, activityLogin.class));
            return;
        }

        fileAccount = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameAccount);
        setContentView(R.layout.activity_details);

        TextView txtDetailType = (TextView) findViewById(R.id.txt_detail_type);
        TextView txtDetailKey = (TextView) findViewById(R.id.txt_detail_key);
        TextView txtDetailUsername = (TextView) findViewById(R.id.txt_detail_username);
        TextView txtDetailPassword = (TextView) findViewById(R.id.txt_detail_password);
        TextView txtDetailOpt = (TextView) findViewById(R.id.txt_detail_opt);
        TextView txtDetailLink = (TextView) findViewById(R.id.txt_detail_link);

        txtDetailType.setText(txtDetailType.getText()+": "+account.getIdType());
        txtDetailKey.setText(txtDetailKey.getText()+": "+account.getTag());
        txtDetailUsername.setText(Html.fromHtml(txtDetailUsername.getText()+": <b>"+account.getUserName()+"</b>"));
        txtDetailPassword.setText(Html.fromHtml(txtDetailPassword.getText()+": <b>"+account.getPassword()+"</b>"));
        String link = account.getLink();
        String opt = account.getOtp();
        if (link.equals(account.DEFAULT_VALUE))
            link = "";
        if (opt.equals(account.DEFAULT_VALUE))
            opt = "";

        txtDetailOpt.setText(Html.fromHtml(txtDetailOpt.getText()+": "+opt));
        txtDetailLink.setText(Html.fromHtml(txtDetailLink.getText()+": <b>"+link+"</b>"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_detail, menu);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        TextDrawable faIconDelete = new TextDrawable(this);
        faIconDelete.setTextSize(TypedValue.COMPLEX_UNIT_PT, 15);
        faIconDelete.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        faIconDelete.setTextColor(Constants.COLOR_ICON_RED);
        faIconDelete.setTypeface(fontAwesomeFont);
        faIconDelete.setText(getResources().getText(R.string.font_awesome_android_delete));
        MenuItem menuItemDelete = menu.findItem(R.id.action_detail_delete);
        menuItemDelete.setIcon(faIconDelete);

        TextDrawable faIconBack = new TextDrawable(this);
        faIconBack.setTextSize(TypedValue.COMPLEX_UNIT_PT, 15);
        faIconBack.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        faIconBack.setTextColor(Constants.COLOR_ICON_GREEN);
        faIconBack.setTypeface(fontAwesomeFont);
        faIconBack.setText(getResources().getText(R.string.font_awesome_android_cancel));
        MenuItem menuItemBack = menu.findItem(R.id.action_detail_cancel);
        menuItemBack.setIcon(faIconBack);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_detail_cancel:
                backView();
                break;

            case R.id.action_detail_delete:
                delAccount();
            default:
                break;
        }
        return true;
    }

    public void backView() {
        Intent i = new Intent(ActivityDetails.this, activityLogged.class);
        i.putExtra("session", true);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void delAccount() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_dialog_delete_title_confirm)
                .setMessage(R.string.msg_dialog_delete_confirm_delete_account)
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        boolean del = FileUtils.deleteAccountByIdFile(fileAccount, account.getIdFile());
                        if (del) {
                            Toast.makeText(ActivityDetails.this, R.string.msg_dialog_delete_account_ok, Toast.LENGTH_SHORT).show();
                            backView();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetails.this);  //TODO
                            builder.setTitle("Attenzione!");
                            builder.setMessage("Impossibile eliminare l'account");
                            builder.show();
                        }
                    }})
                .setNegativeButton(R.string.no, null).show();
    }
}