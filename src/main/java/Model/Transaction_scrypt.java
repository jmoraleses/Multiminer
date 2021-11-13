package Model;

import Util.Util;

public class Transaction_scrypt {

    private String address = "niwMwv2fWek3HV5n4SLzEySZhxmUTAWgEs";
    private String phrase = "The Tree of Life";
    private String version;

    //inputs
    private String inputCount;

    private String txid;
    private String vout;
    private String scriptSigSize;
    private String scriptSig;
    private String sequence;

    //outputs
    private String outputCount;

    private String value;
    private String scriptPubKeySize;
    private String scriptPubKey;

    private String locktime;


    public void set(Block_scrypt block){
        version = block.getVersion();
        inputCount = "01";
        txid = "0000000000000000000000000000000000000000000000000000000000000000";
        vout = "ffffffff";
        scriptSig = Util.asciiToHex(phrase);
        scriptSigSize = calculateScriptSigSize(scriptSig);
        sequence = "ffffffff";

        outputCount = "01";
        value = String.valueOf(block.getFee()); //no debe exceder las recompensas
        scriptPubKey = "76a914" + Util.toHex(address.length()/2) + address + "88ac"; //P2PKH
        scriptPubKeySize = Util.scriptPubKeyVarInt(scriptPubKey);

        locktime = "00000000";
    }


    public String calculateScriptSigSize(String scriptSig){
        return Util.toHex(scriptSig.length()/2);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInputCount() {
        return inputCount;
    }

    public void setInputCount(String inputCount) {
        this.inputCount = inputCount;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getVout() {
        return vout;
    }

    public void setVout(String vout) {
        this.vout = vout;
    }

    public String getScriptSigSize() {
        return scriptSigSize;
    }

    public void setScriptSigSize(String scriptSigSize) {
        this.scriptSigSize = scriptSigSize;
    }

    public String getScriptSig() {
        return scriptSig;
    }

    public void setScriptSig(String scriptSig) {
        this.scriptSig = scriptSig;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getOutputCount() {
        return outputCount;
    }

    public void setOutputCount(String outputCount) {
        this.outputCount = outputCount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getScriptPubKeySize() {
        return scriptPubKeySize;
    }

    public void setScriptPubKeySize(String scriptPubKeySize) {
        this.scriptPubKeySize = scriptPubKeySize;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public String getLocktime() {
        return locktime;
    }

    public void setLocktime(String locktime) {
        this.locktime = locktime;
    }
}
