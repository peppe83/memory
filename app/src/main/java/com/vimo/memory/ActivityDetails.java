package com.vimo.memory;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vimo.memory.data.Account;

public class ActivityDetails extends AppCompatActivity {
    private Account account;
    private String user;

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

        setContentView(R.layout.activity_details);

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setTypeface(fontAwesomeFont);
        /*int color = Color.parseColor("#FFFFFF");
        btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));*/
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delAccount();
            }
        });

        Button btnCancelDetail = (Button) findViewById(R.id.btn_cancel_detail);
        btnCancelDetail.setTypeface(fontAwesomeFont);
        /*int color = Color.parseColor("#FFFFFF");
        btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));*/
        btnCancelDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backView();
            }
        });


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

    public void backView() {
        Intent i = new Intent(ActivityDetails.this, activityLogged.class);
        i.putExtra("session", true);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void delAccount() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //TODO
        builder.setTitle("Attenzione!");
        builder.setMessage("Elimino account");
        builder.show();
    }
}
