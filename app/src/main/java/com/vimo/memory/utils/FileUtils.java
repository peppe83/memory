package com.vimo.memory.utils;

import android.content.Intent;

import com.vimo.memory.activityLogged;
import com.vimo.memory.activityLogin;
import com.vimo.memory.data.Account;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FileUtils {
    public static String myPwdEncode = "giuseppegiuseppe";
    public static String encryptedFileNameUser = "latlng.mem";
    public static String encryptedFileNameAccount = "latitude.mem";
    public static String SEPARATOR = ":::";
    public static String NEW_LINE = "\r\n";

    public static void saveFile(File file, String stringToSave) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            SecretKeySpec yourKey = new SecretKeySpec(myPwdEncode.getBytes(), "AES");
            byte[] filesBytes = encodeFile(yourKey, stringToSave.getBytes());
            bos.write(filesBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addRow(File file, String row) {
        try {
            String content = decodeFile(file);
            if (content!= null && !content.equals("")) {
                content = content + NEW_LINE + row;
            } else {
                content = row;
            }

            saveFile(file, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decodeFile(File file) {
        if (file==null || !file.exists()) return null;
        try {
            SecretKeySpec yourKey = new SecretKeySpec(myPwdEncode.getBytes(), "AES");
            byte[] decodedData = decodeFile(yourKey, readFile(file));
            String str = new String(decodedData);
            System.out.println("DECODED FILE CONTENTS : " + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeFile(SecretKey yourKey, byte[] fileData) throws Exception {
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length, "AES/ECB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(fileData);
        return encrypted;
    }

    public static byte[] readFile(File file) {
        byte[] contents = null;
        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static byte[] decodeFile(SecretKey yourKey, byte[] fileData) throws Exception {
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length, "AES/ECB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] decrypted = cipher.doFinal(fileData);
        return decrypted;
    }

    /*public static String buildRowAccount(String id, String idUser, String id_type, String link, String username, String password, String otp, String keysearch, boolean delete) {
        String row = id+SEPARATOR+idUser+SEPARATOR+id_type+SEPARATOR+link+SEPARATOR+username+SEPARATOR+password+SEPARATOR+otp+SEPARATOR+keysearch+SEPARATOR+delete;
        return row;
    }*/

    private static String getInRow(String row, int index) {
        if (row==null || row.equals("")) return null;

        String[] acc = row.split(FileUtils.SEPARATOR);
        if (acc.length>index) {
            return acc[index];
        }

        return null;
    }

    public static List<Account> getKeySearchInFile(String contentFile, String tag) {
        ArrayList<Account> listAccount = new ArrayList<Account>();
        StringTokenizer st = new StringTokenizer(contentFile, NEW_LINE);
        for (; st.hasMoreTokens();) {
            String row = st.nextToken();
            Account acc = Account.buildAccount(row);
            if (acc!=null && (acc.getTag().toLowerCase().contains(tag.toLowerCase()) || tag.toLowerCase().equals("*"))) {
                listAccount.add(acc);
            }
        }
        return listAccount;
    }

    public static boolean getDeleteInRow(String row) {
        String delete = getInRow(row, 8);
        boolean del = Boolean.valueOf(delete);
        return del;
    }

    public static int getNextIdFile(String contentFile) {
        ArrayList<Account> listAccount = new ArrayList<Account>();
        StringTokenizer st = new StringTokenizer(contentFile, NEW_LINE);
        int nextId = 0;
        Account acc = null;
        for (; st.hasMoreTokens();) {
            String row = st.nextToken();
            acc = Account.buildAccount(row);
        }

        if (acc!=null) {
            nextId = Integer.parseInt(acc.getIdFile())+1;
        }
        return nextId;
    }
}
