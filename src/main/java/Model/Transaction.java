package Model;

import Util.Util;

public class Transaction {

    private String address = "03779c54c2c8aa4deb4f606953204f4c3b734ac51d30cc1152a98ebb603b010a1b"; //bitcoin
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

    //another
    private String height;
    private String heightLength;


    public void set(Block block){
        version = block.getVersion();
        inputCount = "01";
        txid = "0000000000000000000000000000000000000000000000000000000000000000";  //blockhash?
        vout = "ffffffff";
        height = block.getHeight();
        heightLength = "03";
        scriptSig = heightLength + height + Util.asciiToHex(phrase);
        scriptSigSize = calculateScriptSigSize(scriptSig);
        sequence = "ffffffff";

        outputCount = "01";
        value = String.valueOf(block.getFee()); //no debe exceder las recompensas
        scriptPubKey = "76a914" + Util.ripemd160(address) + "88ac"; //P2PKH
        scriptPubKeySize = Util.scriptPubKeyVarInt(scriptPubKey);

        locktime = "00000000";
    }

    //concatenar valores de transacción coinbase
    public String showTransaction() {
        //String heightHexLength = "03";
        String output = "";
        output += Util.reverseHash(this.getVersion());
        output += this.getInputCount();
        output += Util.reverseHash(this.getTxid());
        output += Util.reverseHash(this.getVout());
        output += this.getScriptSigSize();
        output += this.getScriptSig();
        output += Util.reverseHash(this.getSequence());
        output += this.getOutputCount();
        output += this.getValue();
        output += this.getScriptPubKeySize();
        output += this.getScriptPubKey();
        output += Util.reverseHash(this.getLocktime());
        return output;
    }

    //generar toString para transaction
    public String transactiontoJSON(){
        String output = "";
        output += "{";
        output += "\"Version\":\"" + this.getVersion() + "\",";
        output += "\"InputCount\":\"" + this.getInputCount() + "\",";
        output += "\"Txid\":\"" + this.getTxid() + "\",";
        output += "\"Vout\":\"" + this.getVout() + "\",";
        output += "\"Height\":\"" + this.getHeight() + "\",";
        output += "\"HeightLen\":\"" + this.getHeightLength() + "\",";
        output += "\"ScriptSigSize\":\"" + this.getScriptSigSize() + "\",";
        output += "\"ScriptSig\":\"" + this.getScriptSig() + "\",";
        output += "\"Sequence\":\"" + this.getSequence() + "\",";
        output += "\"Output Count\":\"" + this.getOutputCount() + "\",";
        output += "\"Value\":\"" + this.getValue() + "\",";
        output += "\"ScriptPubKeySize\":\"" + this.getScriptPubKeySize() + "\",";
        output += "\"ScriptPubKey\":\"" + this.getScriptPubKey() + "\",";
        output += "\"Locktime\":\"" + this.getLocktime() + "\"";
        output += "}";
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

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeightLength() {
        return heightLength;
    }

    public void setHeightLength(String heightLength) {
        this.heightLength = heightLength;
    }
}
