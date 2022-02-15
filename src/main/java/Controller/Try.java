package Controller;

import Core.ScryptHelper;
import Util.Util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static Core.ScryptHelper.printByteArray;

public class Try {


    public static byte[] SHA256(byte[] obytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(obytes);
    }

    public static byte[] MD5(byte[] obytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        return digest.digest(obytes);
    }

    public static void main(String[] args) throws Throwable {

        byte[] nonce = new byte[4];
        byte[] nonceMAX = new byte[4];
        nonceMAX[0] = (byte)255;
        nonceMAX[1] = (byte)255;
        nonceMAX[2] = (byte)255;
        nonceMAX[3] = (byte)255;
        nonce[0] = (byte)18;
        nonce[1] = (byte)235;
        nonce[2] = (byte)198;
        nonce[3] = (byte)0;

        byte[] base = "010000009500c43a25c624520b5100adf82cb9f9da72fd2447a496bc600b0000000000006cd862370395dedf1da2841ccda0fc489e3039de5f1ccddef0e834991a65600ea6c8cb4db3936a1ae31439".getBytes(StandardCharsets.UTF_8);
        byte[] base_md5 = "dddddddddddddddddddd000000000000".getBytes(StandardCharsets.UTF_8);
        BigInteger base_big = new BigInteger(printByteArray(base_md5), 16);

        while (nonce[0] != nonceMAX[0]) {
            byte[] target = Util.concat(base, nonce);

            byte[] hashmd5 = MD5(MD5(target));
            byte[] noncehash = SHA256(SHA256(target));
            byte[] hash = SHA256(SHA256(noncehash));


            if (new BigInteger(printByteArray(hashmd5), 16).compareTo(base_big) <= 0) {
                if (printByteArray(hash).endsWith("00000")) {
                    System.out.println("sha256(hash): " + printByteArray(hash) + " sha256(noncehash): " + printByteArray(noncehash) + " md5: " + printByteArray(hashmd5) + " nonce: " + Arrays.toString(nonce));
                }
            }
            ScryptHelper.incrementAtIndex(nonce, nonce.length-1);
        }

    }
}
