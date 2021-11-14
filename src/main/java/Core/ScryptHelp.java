package Core;

import java.io.IOException;

import static Controller.Mining.blockhash;

/**
 * Dogecoin, Litecoin
 */
public class ScryptHelp {

    public  static void  incrementAtIndex(byte[] array, int index) {
        //Short method to increment the nonce
        if (array[index] == Byte.MAX_VALUE) {
            array[index] = 0;
            if(index > 0)
                incrementAtIndex(array, index - 1);
        }
        else {
            array[index]++;
        }
    }

    public static String prettyPrintByteArray(byte[] bites){
        //Method to convert a byte array to hex literal separated by bites.
        String str = "";
        int n = 0;
        for(byte bite:bites){
            n += 1;
            str = str + (Integer.toString( ( bite & 0xff ) + 0x100, 16 /* radix */ ).substring( 1 )) + " ";
            if((n%16) == 0) { str += "\n"; }
            else if((n%4) == 0) { str += " "; }
        }
        return str;
    }

    public static String printByteArray(byte[] bites){
        //Method to convert a byte array to hex literal
        String str = "";
        for(byte bite:bites){
            str = str + (Integer.toString( ( bite & 0xff ) + 0x100, 16 /* radix */ ).substring( 1 ));
        }
        return str;
    }

    public static byte[] endianSwitch(byte[] bytes) {
        //Method to switch the endianess of a byte array
        byte[] bytes2 = new byte[bytes.length];
        for(int i = 0; i < bytes.length;  i++){
            bytes2[i] = bytes[bytes.length-i-1];
        }
        return bytes2;
    }

    public static byte[] chunkEndianSwitch(byte[] bytes) {
        //Method to properly switch the endianness of the header -- numbers must be treated as 32 bit chunks. Thanks to ali1234 for this.
        byte[] bytes2 = new byte[bytes.length];
        for(int i = 0; i < bytes.length;  i+=4){
            bytes2[i] = bytes[i+3];
            bytes2[i+1] = bytes[i+2];
            bytes2[i+2] = bytes[i+1];
            bytes2[i+3] = bytes[i];
        }
        return bytes2;
    }

    //mostrar el bloque hash de dogecoin en hexadecimal
    public static String blockHash(String block) throws IOException {
        byte[] blockBytes = block.getBytes();
        byte[] outputBytes = org.spongycastle.crypto.generators.SCrypt.generate(blockBytes, new byte[16], 1024, 8, 1, 32);
        return org.spongycastle.util.encoders.Hex.toHexString(outputBytes);
    }

    //Función que comprueba si el bloque esta bien construido
    public static boolean checkBlock(String block) throws IOException {
        byte[] blockBytes = block.getBytes();
        byte[] outputBytes = org.spongycastle.crypto.generators.SCrypt.generate(blockBytes, new byte[16], 1024, 8, 1, 32);
        return org.spongycastle.util.encoders.Hex.toHexString(outputBytes).equals(blockhash);
    }

    //función que encripta la altura del bloque en dogecoin
    public static String encodeHeight(int height) {
        String output = "";
        output += Integer.toHexString(height);
        return output;
    }

    //función compactSize para transacciones
    public static String compactSize(int size) {
        String output = "";
        if (size < 253) {
            output += Integer.toHexString(size);
        } else if (size < 65536) {
            output += "fd" + Integer.toHexString(size);
        } else if (size < 4294967296L) {
            output += "fe" + Integer.toHexString(size);
        } else {
            output += "ff" + Long.toHexString(size);
        }
        return output;
    }

    //función hacer dos sha256 para obtener el block hash
    public static String blockHashOrTXID(String input) {
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(hash);
    }

}

