

public class Block {
    //crear todos los atributos de un bloque de bitcoin

//    private String creator = "bc1qc7f3azswx3ezyfgsqxtsp0447yalhz98eq7ygf";
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
    public double fee;

    @Override
    public String toString() {
        return "Block{" +
//                "creator='" + creator + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", data='" + data + '\'' +
                ", nonce='" + nonce + '\'' +
                ", time='" + timestamp + '\'' +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", hash='" + hash + '\'' +
                ", version='" + version + '\'' +
                ", bits='" + bits + '\'' +
                '}';
    }

    public String getBits() { return bits; }

    public void setBits(String bits) { this.bits = bits; }

//    public String getCreator() {
//        return creator;
//    }
//
//    public void setCreator(String creator) {
//        this.creator = creator;
//    }


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

    public double getFee() { return fee; }

    public void setFee(double fee) { this.fee = fee; }
}
