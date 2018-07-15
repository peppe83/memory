package com.vimo.memory.utils;

import android.content.Intent;

import com.vimo.memory.activityLogged;
import com.vimo.memory.activityLogin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FileUtils {
    public static String myPwdEncode = "giuseppegiuseppe";
    public static String encryptedFileNameUser = "latlng.mem";
    public static String encryptedFileNameAccount = "latitude.mem";
    public static String SEPARATOR = ":::";

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

    public static String buildRowAccount(String id, String idUser, String id_type, String link, String username, String password, String otp, String keysearch, boolean delete) {
        String row = id+SEPARATOR+idUser+SEPARATOR+id_type+SEPARATOR+link+SEPARATOR+username+SEPARATOR+password+SEPARATOR+otp+SEPARATOR+keysearch+SEPARATOR+delete;
        return row;
    }

    private static String getInRow(String row, int index) {
        if (row==null || row.equals("")) return null;

        String[] acc = row.split(FileUtils.SEPARATOR);
        if (acc.length>index) {
            return acc[index];
        }

        return null;
    }

    public static String getIdInRow(String row) {
        String id = getInRow(row, 0);
        return id;
    }

    public static String getIdUserInRow(String row) {
        String idUser = getInRow(row, 1);
        return idUser;
    }

    public static String getIdTypeInRow(String row) {
        String idType = getInRow(row, 2);
        return idType;
    }

    public static String getLinkInRow(String row) {
        String link = getInRow(row, 3);
        return link;
    }

    public static String getUserNameInRow(String row) {
        String username = getInRow(row, 4);
        return username;
    }

    public static String getPasswordInRow(String row) {
        String password = getInRow(row, 5);
        return password;
    }

    public static String getOtpInRow(String row) {
        String otp = getInRow(row, 6);
        return otp;
    }

    public static String getKeySearchInRow(String row) {
        String key = getInRow(row, 7);
        return key;
    }

    public static boolean getDeleteInRow(String row) {
        String delete = getInRow(row, 8);
        boolean del = Boolean.valueOf(delete);
        return del;
    }

}
