package com.vimo.memory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vimo.memory.data.Account;
import com.vimo.memory.utils.FileUtils;
import com.vimo.memory.utils.TextDrawable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class activityLogged extends AppCompatActivity {

    private EditText mSearchView;
    private String user;
    File fileAccount;
    ArrayList<Account> listResult;
    ListView listViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b!=null && b.containsKey("session")) {
            boolean session = b.getBoolean("session");
            user = b.getString("user");
            if (!session) {
                startActivity(new Intent(activityLogged.this, activityLogin.class));
                return;
            }
        } else {
            startActivity(new Intent(activityLogged.this, activityLogin.class));
            return;
        }

        listResult = new ArrayList<Account>();
        fileAccount = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameAccount);
        if (!fileAccount.exists()) {
            try {
                fileAccount.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setContentView(R.layout.activity_logged);

        mSearchView = (EditText) findViewById(R.id.txt_tag);

        /*ImageButton btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(activityLogged.this, ActivityAdd.class));
                Intent i = new Intent(activityLogged.this, ActivityAdd.class);
                i.putExtra("session", true);
                i.putExtra("user", user);
                startActivity(i);
            }
        });*/

        /*Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setTypeface(fontAwesomeFont);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(activityLogged.this, ActivityAdd.class));
                Intent i = new Intent(activityLogged.this, ActivityAdd.class);
                i.putExtra("session", true);
                i.putExtra("user", user);
                startActivity(i);
            }
        });*/

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setTypeface(fontAwesomeFont);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        listViewResult = (ListView) findViewById(R.id.listViewResult);
        CustomAdapter customAdapter = new CustomAdapter();
        listViewResult.setAdapter(customAdapter);

        listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Account acc = listResult.get(position);
                Intent i = new Intent(activityLogged.this, ActivityDetails.class);
                i.putExtra("session", true);
                i.putExtra("user", user);
                i.putExtra("account", acc.buildRow());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        TextDrawable faIcon = new TextDrawable(this);
        faIcon.setTextSize(TypedValue.COMPLEX_UNIT_PT, 15);
        faIcon.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        int color = Color.parseColor("#2cb789");
        faIcon.setTextColor(color);
        //btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));*/
        faIcon.setTypeface(fontAwesomeFont);
        faIcon.setText(getResources().getText(R.string.font_awesome_android_add));

        MenuItem menuItem = menu.findItem(R.id.action_add_account);
        menuItem.setIcon(faIcon);
        //menuItem.setOnMenuItemClickListener()
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_add_account:
                Intent i = new Intent(activityLogged.this, ActivityAdd.class);
                i.putExtra("session", true);
                i.putExtra("user", user);
                startActivity(i);
                break;

            case R.id.action_logout:
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
            default:
                break;
        }
        return true;
    }

    private void search() {
        // Reset errors.
        // Store values at the time of the login attempt.
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String tag = mSearchView.getText().toString();
        if (tag.equals("")) {
            mSearchView.setError(getString(R.string.error_field_required));
            mSearchView.requestFocus();
        } else {
            mSearchView.setError(null);
            //TODO - faccio la ricerca
            String content = FileUtils.decodeFile(fileAccount);
            List<Account> listAccount = FileUtils.getKeySearchInFile(content, tag);
            if (listAccount.size()>0) {
                listResult = new ArrayList<Account>();
                for (int i=0; i<listAccount.size(); i++) {
                    Account account = listAccount.get(i);
                    listResult.add(account);
                }

                listViewResult = (ListView) findViewById(R.id.listViewResult);
                CustomAdapter customAdapter = new CustomAdapter();
                listViewResult.setAdapter(customAdapter);
            } else {
                Toast.makeText(this, R.string.msg_no_contact_found , Toast.LENGTH_SHORT).show();
            }

        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listResult.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_list_view, null);

            TextView txtKey = (TextView) view.findViewById(R.id.txt_result_tag);
            TextView txtUsername = (TextView) view.findViewById(R.id.txt_result_username);
            TextView txtPassword = (TextView) view.findViewById(R.id.txt_result_password);
            Account acc = listResult.get(i);
            txtKey.setText(acc.getTag());
            txtUsername.setText(acc.getUserName());
            //txtPassword.setText(acc.getPassword());
            txtPassword.setText("");

            return view;
        }
    }
}
