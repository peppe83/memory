package com.vimo.memory.utils;

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
}
