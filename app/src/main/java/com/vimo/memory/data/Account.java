package com.vimo.memory.data;

import com.vimo.memory.utils.FileUtils;

import java.util.StringTokenizer;

public class Account {
    public static String DEFAULT_VALUE = "_";

    private String idFile;
    private String idDb;
    private String idUser;
    private String idType;
    private String link;
    private String userName;
    private String password;
    private String otp;
    private String tag;
    private String delete;

    public Account(String idFile, String idDb, String idUser, String idType, String link, String userName, String password, String otp, String tag, String delete) {
        super();
        this.idDb = buildValue(idDb);
        this.idFile = buildValue(idFile);
        this.idUser = buildValue(idUser);
        this.idType = buildValue(idType);
        this.link = buildValue(link);
        this.userName = buildValue(userName);
        this.password = buildValue(password);
        this.otp = buildValue(otp);
        this.tag = buildValue(tag);
        this.delete = buildValue(delete);
    }

    private String buildValue(String value) {
        if (value==null || value.equals("")) return DEFAULT_VALUE;

        return value;
    }
    public String getIdDb() {
        return idDb;
    }

    public void setIdDb(String idDb) {
        this.idDb = idDb;
    }

    public String getIdFile() {
        return idFile;
    }

    public void setIdFile(String idFile) {
        this.idFile = idFile;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String tag) {
        this.tag = link;
    }

    public String buildRow() {
        String SEP = FileUtils.SEPARATOR;
        String row = idFile+SEP+idDb+SEP+idUser+SEP+idType+SEP+link+SEP+userName+SEP+password+SEP+otp+SEP+tag+SEP+delete;
        return row;
    }

    public static Account buildAccount(String row) {
        if (row==null || row.equals("")) return null;

        StringTokenizer st = new StringTokenizer(row, FileUtils.SEPARATOR);
        int cont = st.countTokens();
        if (st.countTokens()==10) {
            String[] str = new String[st.countTokens()];
            for (int i=0; i<str.length; i++) {
                str[i] = st.nextToken();
            }

            String idFile = str[0];
            String idDb = str[1];
            String idUser = str[2];
            String idType = str[3];
            String link = str[4];
            String userName = str[5];
            String password = str[6];
            String otp = str[7];
            String tag = str[8];
            String delete = str[9];
            Account acc = new Account(idFile, idDb, idUser, idType, link, userName, password, otp, tag, delete);
            return acc;
        }
        return null;
    }
}