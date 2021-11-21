package Util;

import Model.TransactionMined;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static Core.ScryptHelp.compactSize;

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

    //función que dado un string encripta a hexadecimal
    public static String hexString(String input) {
        return String.format("%040x", new BigInteger(1, input.getBytes(StandardCharsets.UTF_8)));
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

    //función que decodifica un string hexadecimal a string ascii
    public static String hexToAscii(String hexValue)
    {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
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

    //funtion hash160 with RIPEMD160Digest
    public static String hash160(String h) {
        try {
            byte[] hash = h.getBytes(StandardCharsets.UTF_8);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(hash);

            RIPEMD160Digest digest160 = new RIPEMD160Digest();
            digest160.update(hashBytes, 0, hashBytes.length);
            byte[] hash160Bytes =  new byte[digest160.getDigestSize()];
            digest160.doFinal(hash160Bytes, 0);

            return String.format("%40x", new BigInteger(1, hash160Bytes));
            //return printByteArray(hash160Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }




    //función RIPEMD160
//    public static String hash160(String input) {
//        RIPEMD160Digest digest = new RIPEMD160Digest();
//        digest.update(input.getBytes(StandardCharsets.US_ASCII), 0, input.getBytes(StandardCharsets.US_ASCII).length);
//        String data = org.apache.commons.codec.digest.DigestUtils.sha256Hex(digest.toString());
//        byte[] out = new byte[20];
//        digest.doFinal(out, 0);
//        //bytes[] to string
//        StringBuilder sb = new StringBuilder();
//        for (byte b : out) {
//            sb.append(String.format("%02x", b));
//        }
//        String hash160 = sb.substring(2, sb.length() - 8);
//        return hash160;
//    }


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
