package Util;

import Model.Transaction;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

import static Core.ScryptHelper.compactSize;

public class Util {

    public static byte[] sha3(byte[] input){
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        return digestSHA3.digest(input);
//        return org.apache.commons.codec.digest.DigestUtils.sha384(input);
    }

    public static byte[] blake2AsU8a(byte[] data, int bitLength, byte[] key) {
        int byteLength = (int) Math.ceil(bitLength / 8F);
        Blake2bDigest blake2bkeyed = new Blake2bDigest(key, byteLength, null, null);
        blake2bkeyed.reset();
        blake2bkeyed.update(data, 0, data.length);
        byte[] keyedHash = new byte[64];
        int digestLength = blake2bkeyed.doFinal(keyedHash, 0);
        return ArrayUtils.subarray(keyedHash, 0, digestLength);
    }

    public static byte[] blake2AsU8a(byte[] data) {
        return blake2AsU8a(data, 256, null);
    }

    public static byte[] blake2AsU8a(byte[] data, int bitLength) {
        return blake2AsU8a(data, bitLength, null);
    }

//    public static String sha256(String input){
//        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
//    }
//
//    //función hacer dos hash256 para obtener el block hash
//    public static String hash256(String input) {
//        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
//        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(hash);
//    }

//    public static String blockHashByte(byte[] input) {
//        String hash = DigestUtils.sha256Hex(input);
//        return DigestUtils.sha256Hex(hash);
//    }

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
    public static String littleEndian(String hash) {
        StringBuilder sb = new StringBuilder();
        for (int i = hash.length() - 2; i >= 0; i -= 2) {
            sb.append(hash, i, i + 2);
        }
        return sb.toString();
    }

    //dar la vuelta al hash en bytes de dos en dos
    public static byte[] littleEndianByte(byte[] hash) {
        byte[] sb = new byte[hash.length];
        for (int i = hash.length - 2; i >= 0; i -= 2) {
            sb[i] = hash[i];
            sb[i + 1] = hash[i + 1];
        }
        return sb;
    }

    //swapendianness
    public static String swapEndianness(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = hex.length() - 2; i >= 0; i -= 2) {
            sb.append(hex, i, i + 2);
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
//    public static byte[] hexStringToByteArray(String s) {
//        int len = s.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
//                    + Character.digit(s.charAt(i+1), 16));
//        }
//        return data;
//    }

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

    //Convertir string con transacciones a lista de Transaction
    public static List<Transaction> transactionToList(JSONArray transactions) throws JSONException {
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < transactions.length() /*&& i < 1000 */; i++) {
            JSONObject jsonObjectTransaction = transactions.getJSONObject(i);
            String data = jsonObjectTransaction.getString("data");
            String txid = jsonObjectTransaction.getString("txid");
            String weight = jsonObjectTransaction.getString("weight");
            String hash = jsonObjectTransaction.getString("hash");
            String fee = jsonObjectTransaction.getString("fee");
            Transaction transactionMined = new Transaction(data, txid, weight, hash, fee, i + 2); // i+1
            list.add(transactionMined);
        }
        return list;
    }

    //comprobar merkleroot
//    public static boolean checkMerkleRoot(String merkleRoot, List<Transaction> list) {
//        List<String> listHashes = new ArrayList<>();
//        for (Transaction transaction : list) {
//            listHashes.add(transaction.getHash());
//        }
//        String hash = Mining.calculateMerkleRoot(listHashes);
//        return hash.equals(merkleRoot);
//    }

    //Función merkle root
    public static String calculateMerkleRoot(List<String> listHashes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listHashes.size(); i += 2) {
            if (i + 1 < listHashes.size()) {
                byte[] A = Util.swapEndianness(Util.hexStringToByteArray(listHashes.get(i)));
                byte[] B = Util.swapEndianness(Util.hexStringToByteArray(listHashes.get(i + 1)));
                byte[] AB = Arrays.copyOf(A, A.length + B.length);
                System.arraycopy(B, 0, AB, A.length, B.length);
                String result = Util.getHex(Util.swapEndianness(Util.SHA256(Util.SHA256(AB))));
//                byte[] result = concat(listHashes.get(i).getBytes(StandardCharsets.UTF_8), listHashes.get(i + 1).getBytes(StandardCharsets.UTF_8));
                list.add(result);
            } else {
                String result = Util.getHex(Util.swapEndianness(Util.SHA256(Util.SHA256(listHashes.get(i).getBytes(StandardCharsets.UTF_8)))));
                list.add(result);
            }
        }
        if (list.size() % 2 == 1) {
            return list.get(0);
        } else {
            return calculateMerkleRoot(list);
        }
    }


    public static byte[] concat(byte[]...arrays)
    {
        // Determine the length of the result array
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            totalLength += arrays[i].length;
        }
        // create the result array
        byte[] result = new byte[totalLength];
        // copy the source arrays into the result array
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }
        return result;
    }

    public static byte[] swapEndianness(byte[] hash) {
        byte[] result = new byte[hash.length];
        for (int i = 0; i < hash.length; i++) {
            result[i] = hash[hash.length-i-1];
        }
        return result;
    }
    public static byte[] SHA256(byte[] obytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(obytes);
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    private static final String    HEXES    = "0123456789abcdef";
    public static String getHex(byte[] raw) {
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }


}
