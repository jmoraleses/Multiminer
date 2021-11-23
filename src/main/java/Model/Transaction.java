package Model;

import Util.Util;

public class Transaction {

    public String txid;
    public String data;
    public String weight;
    public String hash;

    public String id;
    public String fee;

    public String scryptPubKey = Miner.getScriptPubKey(); //P2PKH
    public String sizeScryptPubKey = Util.toHex(scryptPubKey.length()/2);

    public Transaction(String data, String txid, String weight, String hash, String fee, int count) {
        this.data = data;
        this.txid = txid;
        this.weight = weight;
        this.hash = hash;

        this.id = Util.toHex(count);
        this.fee = Util.littleEndian(Util.satoshisToHex(Double.parseDouble(fee)));
    }

    public String toString(){
        String output = "Transaction{" +
                "id='" + id + '\'' +
                "txid='" + txid + '\'' +
                ", data='" + data + '\'' +
                ", weight='" + weight + '\'' +
                ", hash='" + hash + '\'' +
                ", fee='" + fee + '\'' +
                '}';
        return output;
    }

    public String showTransactionMined(){
        String output = "" +
               // id +
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
