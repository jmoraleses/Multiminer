package Util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static Core.ScryptHelp.compactSize;

import Model.Transaction;
import Model.TransactionMined;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.util.encoders.Hex;

public class Util {


    //función hacer dos sha256 para obtener el block hash
    public static String blockHash(String input) {
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(hash);
    }

    public static String blockHashByte(byte[] input) {
        String hash = DigestUtils.sha256Hex(input);
        return DigestUtils.sha256Hex(hash);
    }

    //función de nombre toHex que convierte un número en 8 caracteres hexadecimales
    public static String numtoHex(int n) {
        return String.format("%08x", n);
    }

    //función que dado un string convierte el string a un número de 8 caracteres en hexadecimal
    public static String strToHex(String s) {
        return String.format("%08x", s.hashCode());
    }

    public static String timestampToHex(Instant ts) {
        return String.format("%08x", ts.getEpochSecond());
    }

    //función que convierte un string ascii a hexadecimal
    public static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    //función que devuelve la longitud de merkleroot + 1, en hexadecimal. pasandole un bloque.
    public static String merkleRootTXLen(String merkleroot){
        return compactSize(merkleroot.length() + 1);
    }

    //función para encontrar el tamaño del scriptSig
    public static String toHex(long n) {
        return String.format("%02x", n);
    }

    //función que calcula VarInt de scriptPubKey
    public static String scriptPubKeyVarInt(String scriptPubKey) {
        int size = scriptPubKey.length() / 2;
        if (size < 0xfd) {
            return String.format("%02x", size);
        } else if (size <= 0xffff) {
            return String.format("%02x%02x", 0xfd, size);
        } else {
            return String.format("%02x%02x%02x", 0xfe, size, size);
        }
    }

    //Convertir los satoshis en hexadecimal
    public static String satoshisToHex(double satoshis) {
        return String.format("%016x", (long) satoshis);
    }

    //dar la vuelta al hash de dos en dos
    public static String reverseHash(String hash) {
        StringBuilder sb = new StringBuilder();
        for (int i = hash.length() - 2; i >= 0; i -= 2) {
            sb.append(hash, i, i + 2);
        }
        return sb.toString();
    }

//    //convertir string a hexadecimal
//    public static String toHex(String s) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < s.length(); i++) {
//            sb.append(String.format("%02x", (int) s.charAt(i)));
//        }
//        return sb.toString();
//    }

    //Convertir hexadecimal a long
    public static long hexToLong(String hex) {
        return Long.parseLong(hex, 16);
    }


    //contar cantidad de ceros delante de hash
    public static int countLeadingZeros(String hash) {
        int i = 0;
        while (hash.charAt(i) == '0') {
            i++;
        }
        return i;
    }

    //function for found the difficulty of the hash
    public static String getDifficulty(String hash) {
        String difficulty = "";
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '0') {
                difficulty += "0";
            } else {
                break;
            }
        }
        return difficulty;
    }

    //función hexStringToByteArray
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    //función RIPEMD160
    public static String ripemd160(String input) {
        String data = org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(data.getBytes(StandardCharsets.US_ASCII), 0, data.getBytes(StandardCharsets.US_ASCII).length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);
        //bytes[] to string
        StringBuilder sb = new StringBuilder();
        for (byte b : out) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    //Convertir string con transacciones a lista de TransactionMined
    public static List<TransactionMined> transactionToList(JSONArray transactions) throws JSONException {
        List<TransactionMined> list = new ArrayList<>();
        for (int i = 0; i < transactions.length() && i < 1000 ; i++) {
            JSONObject jsonObjectTransaction = transactions.getJSONObject(i);
            String data = jsonObjectTransaction.getString("data");
            String txid = jsonObjectTransaction.getString("txid");
            String weight = jsonObjectTransaction.getString("weight");
            String hash = jsonObjectTransaction.getString("hash");
            String fee = jsonObjectTransaction.getString("fee");
            TransactionMined transactionMined = new TransactionMined(data, txid, weight, hash, fee, i + 1);
            list.add(transactionMined);
        }
        return list;
    }


}
