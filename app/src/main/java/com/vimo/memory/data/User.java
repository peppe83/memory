package com.vimo.memory.data;

import android.content.Intent;
import android.widget.Toast;

import com.vimo.memory.R;
import com.vimo.memory.activityLogged;
import com.vimo.memory.activityLogin;
import com.vimo.memory.utils.FileUtils;

import java.util.StringTokenizer;

public class User {
    public static String DEFAULT_VALUE = "_";

    private String idUser;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private boolean enabled;
    private String description;
    private String dateCreation;
    private String role;

    public User(String idUser, String username, String password, String name, String surname, String email, String phone, boolean enabled, String description,
                String date_creation, String role) {
        super();
        this.idUser = buildValue(idUser);
        this.username = buildValue(username);
        this.password = buildValue(password);
        this.name = buildValue(name);
        this.surname = buildValue(surname);
        this.email = buildValue(email);
        this.phone = buildValue(phone);
        this.enabled = enabled;
        this.description = buildValue(description);
        this.dateCreation = buildValue(dateCreation);
        this.role = buildValue(role);
    }

    private String buildValue(String value) {
        if (value==null || value.equals("")) return DEFAULT_VALUE;

        return value;
    }

    public String buildRow() {
        String SEP = FileUtils.SEPARATOR;
        String row = username+SEP+password;
        return row;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static User buildUser(String row) {
        if (row==null || row.equals("")) return null;

        StringTokenizer st = new StringTokenizer(row, FileUtils.SEPARATOR);
        int cont = st.countTokens();
        if (st.countTokens()==2) {
            String[] str = new String[st.countTokens()];
            for (int i=0; i<str.length; i++) {
                str[i] = st.nextToken();
            }

            String username = str[0];
            String password = str[1];
            User acc = new User("", username, password, "", "", "", "", true, "", "", "ROLE_USER");

            return acc;
        }
        return null;
    }
}