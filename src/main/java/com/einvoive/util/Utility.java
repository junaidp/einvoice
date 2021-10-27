package com.einvoive.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utility {

    private static String hexIP = null;
    private static final java.security.SecureRandom SEEDER = new java.security.SecureRandom();

    public static byte[] getSHA(String text) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encrypt(String text) throws NoSuchAlgorithmException {
        byte[] hash = getSHA(text);
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public static final String generate(Object o)
    {
        StringBuffer tmpBuffer = new StringBuffer(16);
        if (hexIP == null)
        {
            java.net.InetAddress localInetAddress = null;
            try
            {
                // get the inet address
                localInetAddress = java.net.InetAddress.getLocalHost();
                byte serverIP[] = localInetAddress.getAddress();
                hexIP = hexFormat(getInt(serverIP), 8);
            }
            catch (java.net.UnknownHostException uhe)
            {
                uhe.printStackTrace();
                hexIP="";
            }
        }
        String hashcode = hexFormat(System.identityHashCode(o), 8);
        tmpBuffer.append(hexIP);
        tmpBuffer.append(hashcode);
        long timeNow = System.currentTimeMillis();
        int timeLow = (int) timeNow & 0xFFFFFFFF;
        int node = SEEDER.nextInt();
        StringBuffer guid = new StringBuffer(32);
        guid.append(hexFormat(timeLow, 8));
        guid.append(tmpBuffer.toString());
        guid.append(hexFormat(node, 8));
        return guid.toString();
    }
    private static int getInt(byte bytes[])
    {
        int i = 0;
        int j = 24;
        for (int k = 0; j >= 0; k++)
        {
            int l = bytes[k] & 0xff;
            i += l << j;
            j -= 8;
        }
        return i;
    }
    private static String hexFormat(int i, int j)
    {
        String s = Integer.toHexString(i);
        return padHex(s, j) + s;
    }
    private static String padHex(String s, int i)
    {
        StringBuffer tmpBuffer = new StringBuffer();
        if (s.length() < i)
        {
            for (int j = 0; j < i - s.length(); j++)
            {
                tmpBuffer.append('0');
            }
        }
        return tmpBuffer.toString();
    }
}
