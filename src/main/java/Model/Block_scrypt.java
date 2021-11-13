package Model;

import Core.Scrypt;

public class Block_scrypt {

    private String previousHash;
    private String data;
    private String nonce;
    private String timestamp;
    private String merkleRoot;
    private String difficulty;
    private String hash;
    private String version;
    private String bits; //target
    public String transactions;
    public String height;

    public String fee;
    public String blockHash; //hash válido?


    public String getBits() { return bits; }

    public void setBits(String bits) { this.bits = bits; }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public void addHeader(Transaction_scrypt header) {
        this.transactions = Scrypt.TransactiontoJSON(header) + this.transactions;
    }

    public String getTransactions() { return transactions; }

    public void setTransactions(String transactions) { this.transactions = transactions; }

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFee() { return fee; }

    public void setFee(String fee) { this.fee = fee; }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
