package Core;

import Model.Block_scrypt;
import Model.Transaction_scrypt;
import Util.Util;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.google.gson.Gson;
import com.lambdaworks.crypto.SCrypt;

/**
 * Dogecoin, Litecoin
 */
public class Scrypt {

    public static String noncerange;
    public static String blockhash = "";


    //Función que calcula el nonce del bloque de dogecoin, dados el input y la dificultad con scrypt. Pasando como parámtetros el bloque y la dificultad.
    public static String SearchingNonce(Block_scrypt block, String target) throws IOException, GeneralSecurityException {
        byte[] nonce = doScrypt(Scrypt.showBlock(block).getBytes(StandardCharsets.UTF_8), target.getBytes(StandardCharsets.UTF_8));
        return Arrays.toString(nonce);
    }

    //#############################

    public static byte[] doScrypt(byte[] databyte, byte[] target) throws GeneralSecurityException {
        //Initialize the nonce
        byte[] nonce = new byte[4];
        nonce[0] = databyte[76] ;
        nonce[1] = databyte[77] ;
        nonce[2] = databyte[78] ;
        nonce[3] = databyte[79] ;
        boolean found = false;
        //Loop over and increment nonce
        while(!found){
            //Set the bytes of the data to the nonce
            databyte[76] = nonce[0];
            databyte[77] = nonce[1];
            databyte[78] = nonce[2];
            databyte[79] = nonce[3];

            byte[] scrypted = (SCrypt.scryptJ(databyte,databyte, 1024, 1, 1, 32));//Scrypt the data with proper params

            BigInteger bigScrypt = new BigInteger(printByteArray(endianSwitch(scrypted)), 16); //Create a bigInteger to compare against the target
            BigInteger bigTarget = new BigInteger(printByteArray(target),16);//Create a bigInteger from the target
            if(bigScrypt.compareTo(bigTarget) == -1){
                System.out.println(printByteArray(scrypted));
                blockhash = Arrays.toString(scrypted);
                return nonce;//Compare the two bigIntegers, return the data with nonce if smaller
            }

            else incrementAtIndex(nonce, nonce.length-1); //Otherwise increment the nonce

        }
        return nonce;
    }


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


    //###################


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





    //función blockhash en dogecoin


    public static String showBlock(Block_scrypt block) {
        String output = "";
        output += Util.reverseHash(block.getVersion());
        output += Util.reverseHash(block.getPreviousHash());
        output += Util.reverseHash(block.getMerkleRoot());

        //timestamp = "1636560593731";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDtm = Instant.ofEpochSecond(Long.parseLong(block.getTimestamp())/1000)
                .atZone(java.time.ZoneOffset.UTC)
                .format(formatter);
        Instant time = Timestamp.valueOf(formattedDtm).toLocalDateTime().toInstant(java.time.ZoneOffset.UTC);
        output += Util.reverseHash(Util.timestampToHex(time)); //timestamp

        output += Util.reverseHash(block.getBits());
        output += Util.reverseHash(block.getNonce()); //// no habilitar, pues es utilizado en la búsqueda del nonce
        return output;
    }


    //generar toString para transaction
    public static String TransactiontoJSON(Transaction_scrypt transaction){
        String output = "";
        output += "{";
        output += "\"Version\":\"" + transaction.getVersion() + "\",";
        output += "\"Inputs\":\"" + transaction.getInputCount() + "\",";
        output += "\"Txid\":\"" + transaction.getTxid() + "\",";
        output += "\"Vout\":\"" + transaction.getVout() + "\",";
        output += "\"ScriptSigSize\":\"" + transaction.getScriptSigSize() + "\",";
        output += "\"ScriptSig\":\"" + transaction.getScriptSig() + "\",";
        output += "\"Sequence\":\"" + transaction.getSequence() + "\",";
        output += "\"Outputs\":\"" + transaction.getOutputCount() + "\",";
        output += "\"Value\":\"" + transaction.getValue() + "\",";
        output += "\"ScriptPubKeySize\":\"" + transaction.getScriptPubKeySize() + "\",";
        output += "\"ScriptPubKey\":\"" + transaction.getScriptPubKey() + "\",";
        output += "\"Locktime\":\"" + transaction.getLocktime() + "\"";
        output += "}";
        return output;
    }

    //función que encripta la altura del bloque en dogecoin
    public static String encodeHeight(int height) {
        String output = "";
        output += Integer.toHexString(height);
        return output;
    }


    //concatenar valores de transacción coinbase
    public static String showTransaction(Transaction_scrypt transaction) {
        //String heightHexLength = "03";
        String output = "";
        output += transaction.getVersion();
        output += transaction.getInputCount();
        output += transaction.getTxid();
        output += transaction.getVout();
//        output += heightHexLength; //
//        output += transaction.getHeight(); //
        output += transaction.getScriptSigSize();
        output += transaction.getScriptSig();
        output += transaction.getSequence();
        output += transaction.getOutputCount();
        output += transaction.getValue();
        output += transaction.getScriptPubKeySize();
        output += transaction.getScriptPubKey();
        output += transaction.getLocktime();
        return output;
    }



}

