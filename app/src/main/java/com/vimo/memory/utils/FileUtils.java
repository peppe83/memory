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

    public static List<Account> getKeySearchInFile(File fileAccount, String tag) {
        String content = FileUtils.decodeFile(fileAccount);
        ArrayList<Account> listAccount = new ArrayList<Account>();
        StringTokenizer st = new StringTokenizer(content, NEW_LINE);
        for (; st.hasMoreTokens();) {
            String row = st.nextToken();
            Account acc = Account.buildAccount(row);
            if (acc!=null && (acc.getTag().toLowerCase().contains(tag.toLowerCase()) || tag.toLowerCase().equals("*"))) {
                listAccount.add(acc);
            }
        }
        return listAccount;
    }

    public static int getNextIdFile(File fileAccount) {
        String content = FileUtils.decodeFile(fileAccount);
        ArrayList<Account> listAccount = new ArrayList<Account>();
        StringTokenizer st = new StringTokenizer(content, NEW_LINE);
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

    public static boolean deleteAccountByIdFile(File fileAccount, String idFile) {
        boolean del = false;
        String content = FileUtils.decodeFile(fileAccount);
        String rowToSave = "";
        ArrayList<Account> listAccount = new ArrayList<Account>();
        StringTokenizer st = new StringTokenizer(content, NEW_LINE);
        for (; st.hasMoreTokens();) {
            String row = st.nextToken();
            Account acc = Account.buildAccount(row);
            if (acc!=null && !acc.getIdFile().toLowerCase().equals(idFile.toLowerCase())) {
                listAccount.add(acc);
            } else {
                del = true;
            }
        }

        for (int i=0; i<listAccount.size(); i++) {
            Account acc = listAccount.get(i);
            rowToSave += acc.buildRow();

            if (i!=listAccount.size()-1) {
                rowToSave += NEW_LINE;
            }
        }

        if (del) {
            saveFile(fileAccount, rowToSave);
        }

        return del;
    }


        /*private static String getInRow(String row, int index) {
        if (row==null || row.equals("")) return null;

        String[] acc = row.split(FileUtils.SEPARATOR);
        if (acc.length>index) {
            return acc[index];
        }

        return null;
    }*/

        /*public static boolean getDeleteInRow(String row) {
        String delete = getInRow(row, 8);
        boolean del = Boolean.valueOf(delete);
        return del;
    }*/

}
