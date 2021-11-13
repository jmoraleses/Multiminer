package Core;

import java.io.IOException;

/**
 * Dogecoin, Litecoin
 */
public class Scrypt {

    //función que encripta en Core.Scrypt el contenido de un string
    public static String scrypt(String input) throws IOException {
        byte[] salt = new byte[16];
        byte[] inputBytes = input.getBytes();
        byte[] outputBytes = org.spongycastle.crypto.generators.SCrypt.generate(inputBytes, salt, 1024, 8, 1, 32);
        return org.spongycastle.util.encoders.Base64.toBase64String(outputBytes);
    }

    //Función que calcula el nonce del bloque de dogecoin, dados el input y la dificultad con scrytp
    public static String nonce(String input, String difficulty) throws IOException {
        String nonce = "0";
        while (true) {
            String hash = scrypt(input + nonce);
            if (hash.startsWith(difficulty)) {
                return nonce;
            }
            nonce = String.valueOf(Integer.parseInt(nonce) + 1);
        }
    }

    //Función que crea el header en Core.Scrypt de dogecoin
    public static byte[] createHeader(byte[] header, byte[] merkleRoot, byte[] nonce, byte[] time, byte[] bits, byte[] version) {
        byte[] headerBytes = new byte[80];
        System.arraycopy(header, 0, headerBytes, 0, header.length);
        System.arraycopy(merkleRoot, 0, headerBytes, 32, merkleRoot.length);
        System.arraycopy(nonce, 0, headerBytes, 52, nonce.length);
        System.arraycopy(time, 0, headerBytes, 56, time.length);
        System.arraycopy(bits, 0, headerBytes, 60, bits.length);
        System.arraycopy(version, 0, headerBytes, 64, version.length);
        return headerBytes;
    }

    //mostrar el bloque hash de dogecoin en hexadecimal
    public static String blockHash(String block) throws IOException {
        byte[] blockBytes = block.getBytes();
        byte[] outputBytes = org.spongycastle.crypto.generators.SCrypt.generate(blockBytes, new byte[16], 1024, 8, 1, 32);
        return org.spongycastle.util.encoders.Hex.toHexString(outputBytes);
    }

}

