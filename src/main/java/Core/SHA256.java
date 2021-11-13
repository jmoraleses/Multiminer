package Core;

import Model.Block_scrypt;
import Model.Block_sha256;
import Model.Transaction_scrypt;
import Model.Transaction_sha256;
import Util.Util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Bitcoin
 */
public class SHA256 {

    //función hacer dos sha256 para obtener el block hash
    public static String blockHash(String input) {
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(hash);
    }


    //función toString de un bloque de bitcoin
    public String toString(Block_sha256 block) {
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


    public static String showBlock(Block_sha256 block) {
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
        output += Util.reverseHash(block.getNonce());
        return output;
    }



    //generar toString para transaction
    public static String TransactiontoJSON(Transaction_sha256 transaction){
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

    //concatenar valores de transacción coinbase
    public static String showTransaction(Transaction_sha256 transaction) {
        String output = "";
        output += transaction.getVersion();
        output += transaction.getInputCount();
        output += transaction.getTxid();
        output += transaction.getVout();
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
