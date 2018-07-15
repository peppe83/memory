package com.vimo.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.vimo.memory.data.Account;
import com.vimo.memory.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class activityLogged extends AppCompatActivity {

    private AutoCompleteTextView mSearchView;
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
            }
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

        listViewResult = (ListView) findViewById(R.id.listViewResult);
        CustomAdapter customAdapter = new CustomAdapter();
        listViewResult.setAdapter(customAdapter);
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

                String msg = "trovato";
                String ho = "";
            } else {
                String msg = "non trovato";
                String ho = "";
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
            txtPassword.setText(acc.getPassword());


            return view;
        }
    }
}
