package Core;

import Model.Block_scrypt;
import Model.Transaction_scrypt;
import Util.Util;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

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

    //Función que calcula el nonce del bloque de dogecoin, dados el input y la dificultad con scrypt. Pasando como parámtetros el bloque y la dificultad.
    public static String nonce(Block_scrypt block, String difficulty) throws IOException {
        int nonce = 0;
        while (true) {
            String hash = scrypt(Scrypt.showBlock(block) + Util.numtoHex(nonce));
            if (hash.startsWith(difficulty)) {
                return String.valueOf(Util.numtoHex(nonce));
            }
            nonce += 1;
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



    //función toString de un bloque de bitcoin
    public String toString(Block_scrypt block) {
        return "Model.Block{" +
                "previousHash='" + block.getPreviousHash() + '\'' +
                ",nonce='" + block.getNonce() + '\'' +
                ",time='" + block.getTimestamp() + '\'' +
                ",merkleRoot='" + block.getMerkleRoot() + '\'' +
                ",difficulty='" + block.getDifficulty() + '\'' +
                ",hash='" + block.getHash() + '\'' +
                ",version='" + block.getVersion() + '\'' +
                ",bits='" + block.getBits() + '\'' +
                ",transactions='" + block.getTransactions() + '\'' +
                '}';
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
        output += Util.reverseHash(block.getNonce()); ////
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
        String heightHexLength = "03";
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

