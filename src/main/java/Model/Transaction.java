package Model;

import Util.Util;

public class Transaction {

    private String address = "bc1qt0m5pypcp0zz9d39mkhp9mfuhzeaw7jttn54sy";
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


    public void set(Block block){
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

    //generar toString para transaction
    public String toJSON(){
        String output = "";
        output += "{";
        output += "\"Version\":\"" + version + "\",";
        output += "\"Inputs\":\"" + inputCount + "\",";
        output += "\"Txid\":\"" + txid + "\",";
        output += "\"Vout\":\"" + vout + "\",";
        output += "\"ScriptSigSize\":\"" + scriptSigSize + "\",";
        output += "\"ScriptSig\":\"" + scriptSig + "\",";
        output += "\"Sequence\":\"" + sequence + "\",";
        output += "\"Outputs\":\"" + outputCount + "\",";
        output += "\"Value\":\"" + value + "\",";
        output += "\"ScriptPubKeySize\":\"" + scriptPubKeySize + "\",";
        output += "\"ScriptPubKey\":\"" + scriptPubKey + "\",";
        output += "\"Locktime\":\"" + locktime + "\"";
        output += "}";
        return output;
    }

    //concatenar valores de transacción coinbase
    public String show() {
        String output = "";
        output += version;
        output += inputCount;
        output += txid;
        output += vout;
        output += scriptSigSize;
        output += scriptSig;
        output += sequence;
        output += outputCount;
        output += value;
        output += scriptPubKeySize;
        output += scriptPubKey;
        output += locktime;
        return output;
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
