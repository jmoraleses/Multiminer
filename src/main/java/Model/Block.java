package Model;

import Util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Block {
    //crear todos los atributos de un bloque de bitcoin

    private String previousHash;
    private String data;
    private String nonce;
    private String timestamp;
    private String merkleRoot;
    private String difficulty;
    private String blockhash;
    private String version;
    private String bits; //target is the same as difficulty
    private String target; //target
    public JSONArray transactions;
//    public List<TransactionMined> transactionsMined;
    public String height;

    public String fee;
//    public String blockHash; //hash válido?


    public String showBlock() { //Header

        String output = "";
        output += Util.reverseHash(this.getVersion());
        output += Util.reverseHash(this.getPreviousHash());
        output += Util.reverseHash(this.getMerkleRoot());

        //timestamp = "1636560593731";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDtm = Instant.ofEpochSecond(Long.parseLong(this.getTimestamp())/1000)
                .atZone(java.time.ZoneOffset.UTC)
                .format(formatter);
        Instant time = Timestamp.valueOf(formattedDtm).toLocalDateTime().toInstant(java.time.ZoneOffset.UTC);
        output += Util.reverseHash(Util.timestampToHex(time)); //timestamp

        output += Util.reverseHash(this.getBits());
//        output += Util.reverseHash(this.getNonce()); /////////

        return output;
    }

    //función toString de un bloque de bitcoin
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDtm = Instant.ofEpochSecond(Long.parseLong(this.getTimestamp())/1000)
                .atZone(java.time.ZoneOffset.UTC)
                .format(formatter);
        Instant time = Timestamp.valueOf(formattedDtm).toLocalDateTime().toInstant(java.time.ZoneOffset.UTC);
        String timestampX = Util.reverseHash(Util.timestampToHex(time)); //timestamp

        return "Model.Block{" +
                "previousHash='" + this.getPreviousHash() + '\'' +
                ",nonce='" + this.getNonce() + '\'' +
                ",time='" + timestampX + '\'' +
                ",merkleRoot='" + this.getMerkleRoot() + '\'' +
                ",difficulty='" + this.getDifficulty() + '\'' +
                ",hash='" + this.getBlockhash() + '\'' +
                ",version='" + this.getVersion() + '\'' +
                ",bits='" + this.getBits() + '\'' +
                ",Height='" + this.getHeight() + '\'' +
                ",Target='" + this.getTarget() + '\'' +
                ",transactions='" + this.getTransactionsSerialized() + '\'' +
                '}';
    }

    //función para cada elemento de transactions mostrarlo
    public String getTransactionsSerialized() {
        StringBuilder transactionsSerialized = new StringBuilder();
        transactionsSerialized.append(this.transactions.length());
        for (int i = 0; i < this.transactions.length(); i++) {
            try {
                JSONObject jsonObjectTransaction = this.transactions.getJSONObject(i);
                transactionsSerialized.append(jsonObjectTransaction.getString("data"));
                transactionsSerialized.append(jsonObjectTransaction.getString("fee"));
                transactionsSerialized.append(jsonObjectTransaction.getString("txid"));
                transactionsSerialized.append(jsonObjectTransaction.getString("weight"));
                transactionsSerialized.append(jsonObjectTransaction.getString("hash"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(transactionsSerialized);
        return transactionsSerialized.toString();
    }

    //mostrar toString de las dos primeras transacciones
    public String getTransactionsTwoOnly() {
        String transactionsSerialized = "";
        for (int i = 0; i < 2; i++) {
            try {
                transactionsSerialized += "{";
                transactionsSerialized += "\"data\": \"" + this.transactions.getJSONObject(i).getString("data")+"\"";
                transactionsSerialized += "\"fee\": \"" + this.transactions.getJSONObject(i).getString("fee")+"\"";
                transactionsSerialized += "\"txid\": \"" + this.transactions.getJSONObject(i).getString("txid")+"\"";
                transactionsSerialized += "\"weight\": \"" + this.transactions.getJSONObject(i).getString("weight")+"\"";
                transactionsSerialized += "\"hash\": \"" + this.transactions.getJSONObject(i).getString("hash")+"\"";
                transactionsSerialized += "}";

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(transactionsSerialized);
        return "["+transactionsSerialized+"]";
    }

    public String getBits() { return bits; }

    public void setBits(String bits) { this.bits = bits; }


    public void addHeader(Transaction header) throws JSONException {
        this.transactions = transactions.put(0, header);
        //Scrypt_do.TransactiontoJSON(header) + this.transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFee() { return fee; }

    public void setFee(String fee) { this.fee = fee; }

    public void setTransactions(JSONArray transactions) {
        this.transactions = transactions;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public JSONArray getTransactions() {
        return transactions;
    }
}
