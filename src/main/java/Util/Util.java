package Util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class Util {

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

    //función de nombre toHex, convierte un String de tipo ascii en un String de tipo hexadecimal
    public static String asciiToHex(String ascii) {
        byte[] bytes = ascii.getBytes(StandardCharsets.US_ASCII);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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

}
