


public class Transaction {

    private String address = "tb1q5f2jdp006qp4q0qu8uq0ut0zh8lymwnvafy3rv";
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


    void set(Block block){
        version = block.getVersion();
        inputCount = "01";
        txid = "0000000000000000000000000000000000000000000000000000000000000000";
        vout = "ffffffff";
        scriptSig = Util.asciiToHex(phrase);
        scriptSigSize = calculateScriptSigSize(scriptSig);
        sequence = "ffffffff";

        outputCount = "01";

        value = "00000000"; //
        scriptPubKey = "76a914" + Util.toHex(address.length()/2) + address + "88ac"; //P2PKH
        scriptPubKeySize = Util.scriptPubKeyVarInt(scriptPubKey);

        locktime = "00000000";

    }




    //EXAMPLE
    //function that create transaction coinbase
//    public String createCoinbase(String address, String transactionId, String transactionOutput){
//        version = "01000000";
//        inputCount = "01";
//        txid = transactionId;
//        vout = "0000000000000000";
//        scriptSigSize = "FFFFFFFF";
//        scriptSig = "04" + address + "0000000000000000000000000000000000000000000000000000000000000000";
//        sequence = "FFFFFFFF";
//
//        outputCount = "01";
//        value = transactionOutput;
//        scriptPubKeySize = "19";
//        scriptPubKey = "76a914" + address + "88ac";
//
//        locktime = "00000000";
//
//        return version + inputCount + txid + vout + scriptSigSize + scriptSig + sequence + outputCount + value + scriptPubKeySize + scriptPubKey + locktime;
//    }

    //find how built a transaction coinbase



    //function calculate ScriptSigSize
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
