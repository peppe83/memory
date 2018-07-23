package com.vimo.memory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vimo.memory.data.User;
import com.vimo.memory.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class activityLogin extends AppCompatActivity  {

    /*String encryptedFileNameU = "latlng.mem";
    String encryptedFileNameA = "latitude.mem";
    String myPwdEncode = "giuseppegiuseppe";*/

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        ImageButton mSignInButton = (ImageButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        /*Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });*/

        //Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        //mSignInButton.setTypeface(fontAwesomeFont);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) { //&& !isPasswordValid(password)
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        */

        if (cancel) return;

        File file = new File(this.getFilesDir().getPath() + File.separator + FileUtils.encryptedFileNameUser);
        boolean simulateFirstAccess = false;//simulo primo accesso
        if (simulateFirstAccess && file.exists()) {
            file.delete();
            boolean exist = file.exists();
            System.out.print(exist);
        }
        User userDB = checkUserDb(this, email, password);   //TODO
        String content = FileUtils.decodeFile(file);
        if (content==null) {
            //non ho il file presente - lo creo
            if (isOnline()) {
                //se sono online cerco account remoto
                boolean existUserInDB = false;
                //faccio query su db per vedere se l'utente esiste
                existUserInDB = true; //TODO
                //scrivo su file prima riga nomeutente:::password
                if (existUserInDB) {
                    try {
                        file.createNewFile();
                        FileUtils.saveFile(file, email + FileUtils.SEPARATOR + password);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //vado view successa
                    //startActivity(new Intent(activityLogin.this, activityLogged.class));
                    Intent i = new Intent(activityLogin.this, activityLogged.class);
                    i.putExtra("session", true);
                    i.putExtra("user", email);
                    startActivity(i);
                } else {
                    Toast.makeText(this, R.string.msg_no_user_registred , Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, R.string.msg_no_connection , Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            String result = FileUtils.decodeFile(file);
            //estraggo prima riga
            String[] acc = result.split(FileUtils.SEPARATOR);
            if (acc.length==2 && acc[0].equals(email) && acc[1].equals(password)) {
                Intent i = new Intent(activityLogin.this, activityLogged.class);
                i.putExtra("session", true);
                i.putExtra("user", email);
                startActivity(i);
            } else {
                //return;   //TODO mostrare un errore
                Toast.makeText(this, R.string.msg_user_no_registred , Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public String  performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            } else {
                response="";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    protected User checkUserDb(Context myContext, String uname, String pwd) {
        try {
            String urlLogin = "http://localhost/dev_memory_php/memory/users/loginUser.php";
            //String urlLogin = "http://localhost/dev_memory_php/memory/users/loginUser.php?username="+name+"&password="+pwd;
            HashMap<String,String> hasmMap = new HashMap<>();
            hasmMap.put("username", uname);
            hasmMap.put("password", pwd);
            String response = performPostCall(urlLogin, hasmMap);
            System.out.println(response);
            JSONObject obj = new JSONObject(response);
            if (obj.has("message")) {
                Toast.makeText(myContext, obj.getString("message") , Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jArrRecords = obj.getJSONArray("records");
                if (jArrRecords.length()==1) {
                    JSONObject jObjUser = jArrRecords.getJSONObject(0);
                    if (jObjUser.has("enabled")) {
                        String app = jObjUser.getString("enabled");
                        boolean appB = jObjUser.getBoolean("enabled");
                        String c = "";
                    }
                    String enabled = (jObjUser.has("enabled")) ? jObjUser.getString("enabled") : "";

                    String idUser = (jObjUser.has("id_user")) ? jObjUser.getString("id_user") : "";
                    String username = (jObjUser.has("username")) ? jObjUser.getString("username") : "";
                    String password = (jObjUser.has("password")) ? jObjUser.getString("password") : "";
                    String name = (jObjUser.has("name")) ? jObjUser.getString("name") : "";
                    String surname = (jObjUser.has("surname")) ? jObjUser.getString("surname") : "";
                    String email = (jObjUser.has("email")) ? jObjUser.getString("email") : "";
                    String phone = (jObjUser.has("phone")) ? jObjUser.getString("phone") : "";

                    String description = (jObjUser.has("description")) ? jObjUser.getString("description") : "";
                    String date_creation = (jObjUser.has("date_creation")) ? jObjUser.getString("date_creation") : "";
                    String role = (jObjUser.has("role")) ? jObjUser.getString("role") : "";

                    User userObjDB = new User(idUser, username, password, name, surname, email, phone, true, description, date_creation, role);
                    return userObjDB;
                }

               // {"records":[{"id_user":"3","username":"we","password":"we","name":"we","surname":"we","email":"we","phone":"we","enabled":"1","description":"","date_creation":"2018-07-17 14:17:14","role":"ROLE_ADMIN"}]}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}