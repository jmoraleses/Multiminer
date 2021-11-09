

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

    public String fee;


//    //función toString de un bloque de bitcoin
//    public String toString() {
//        return "Block{" +
//                "previousHash='" + previousHash + '\'' +
//                ",data='" + data + '\'' +
//                ",nonce='" + nonce + '\'' +
//                ",time='" + timestamp + '\'' +
//                ",merkleRoot='" + merkleRoot + '\'' +
//                ",difficulty='" + difficulty + '\'' +
//                ",hash='" + hash + '\'' +
//                ",version='" + version + '\'' +
//                ",bits='" + bits + '\'' +
//                ",transactions='" + transactions + '\'' +
//                '}';
//    }
//
//
//    //crear toString de block
//    public String show() {
//        String output = "";
//        output += previousHash;
//        output += data;
//        output += nonce;
//        output += timestamp;
//        output += merkleRoot;
//        output += difficulty;
//        output += hash;
//        output += version;
//        output += bits;
//        output += transactions;
//        return output;
//    }


    public String getBits() { return bits; }

    public void setBits(String bits) { this.bits = bits; }

//    public String getCreator() {
//        return creator;
//    }
//
//    public void setCreator(String creator) {
//        this.creator = creator;
//    }

    public void addHeader(Transaction header) {
        this.transactions = header.toJSON() + this.transactions;
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
}
