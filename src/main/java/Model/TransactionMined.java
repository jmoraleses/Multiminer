package Model;

import Util.Util;

public class TransactionMined {

    public String txid;
    public String data;
    public String weight;
    public String hash;

    public String id;
    public String fee;
    private final String address = Util.ripemd160("03779c54c2c8aa4deb4f606953204f4c3b734ac51d30cc1152a98ebb603b010a1b");

    public String scryptPubKey = "76a914" + address + "88ac";//P2PKH
    //public String lockingScript = Sha256Help.calculateBitcoinAddress(address.getBytes(StandardCharsets.UTF_8));
    public String sizeScryptPubKey = Util.scriptPubKeyVarInt(scryptPubKey);
    // public String sizeLockingScript = Util.numtoHex(lockingScript.length());

    public TransactionMined(String data, String txid, String weight, String hash, String fee, int count) {
        this.data = data;
        this.txid = txid;
        this.weight = weight;
        this.hash = hash;

        this.id = Util.toHex(count);
        this.fee = Util.reverseHash(Util.satoshisToHex(Double.parseDouble(fee)));
    }

    public String toString(){
        String output = "TransactionMined{" +
                "id='" + id + '\'' +
                "txid='" + txid + '\'' +
                ", data='" + data + '\'' +
                ", weight='" + weight + '\'' +
                ", hash='" + hash + '\'' +
                ", fee='" + fee + '\'' +
                '}';
        return output;
    }

    public String toString2(){
        String output = "TransactionMined{" +
                "id='" + id + '\'' +
                "txid='" + txid + '\'' +
                ", fee='" + fee + '\'' +
                ", sizeScryptPubKey='" + sizeScryptPubKey + '\'' +
                ", scryptPubKey='" + scryptPubKey + '\'' +
                '}';
        return output;
    }

    public String showTransactionMined(){
        String output = "" +
                id +
                fee +
                sizeScryptPubKey +
                scryptPubKey;
        return output;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScryptPubKey() {
        return scryptPubKey;
    }

    public void setScryptPubKey(String scryptPubKey) {
        this.scryptPubKey = scryptPubKey;
    }

    public String getSizeScryptPubKey() {
        return sizeScryptPubKey;
    }

    public void setSizeScryptPubKey(String sizeScryptPubKey) {
        this.sizeScryptPubKey = sizeScryptPubKey;
    }
}
