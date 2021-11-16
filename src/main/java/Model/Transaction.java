package Model;

import Util.Util;

import java.util.List;

public class Transaction {


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

    public List<TransactionMined> transactionMineds;
    private String byteCoinbase;

    public void set(Block block){

        version = block.getVersion();

        inputCount = "01"; //entrada de transaccion
        txid = "0000000000000000000000000000000000000000000000000000000000000000";
        vout = "ffffffff";

        height = Util.toHex(Integer.parseInt(block.getHeight()));
        //String phraseHex = height + Util.asciiToHex(Miner.phrase);
        heightLength = "03";
        //heightLength = Util.numtoHex(phraseHex.length());

        scriptSig = heightLength + height + Util.asciiToHex(Miner.phrase);
        scriptSigSize = Util.toHex(scriptSig.length());

        sequence = "ffffffff"; //FFFFFFFF

        outputCount = "01"; //salida de transaccion
        value = String.valueOf(block.getFee()); //no debe exceder las recompensas

        scriptPubKey = "76a914" + Miner.address + "88ac"; //P2PKH
        scriptPubKeySize = Util.toHex(scriptPubKey.length()); //Util.scriptPubKeyVarInt(scriptPubKey);

        locktime = "00000000";

    }


    //concatenar valores de transacción coinbase
    public String showTransaction() {
        String output = "";

        output += Util.reverseHash(this.getVersion()); //version

        output += this.getInputCount(); // input count
        output += Util.reverseHash(this.getTxid()); // 000...
        output += Util.reverseHash(this.getVout()); //previous output

        output += this.getScriptSigSize();
        output += this.getScriptSig();
        output += Util.reverseHash(this.getSequence());

        output += this.getOutputCount(); //01
        output += this.getValue(); //fee in satoshis
        output += this.getScriptPubKeySize();
        output += this.getScriptPubKey();
        //output += Util.reverseHash(this.getLocktime());

//        for(int i = 0; i < transactionMineds.size(); i++) {
//            output += transactionMineds.get(i).showTransactionMined();
//        }

        output += Util.reverseHash(this.getLocktime()); //locktime

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


//    public String calculateScriptSigSize(String scriptSig){
//        return Util.toHex(scriptSig.length()/2);
//    }


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

    public void addTransactionMineds(TransactionMined transactionMineds) {
        this.transactionMineds.add(transactionMineds);
    }

    public List<TransactionMined> getTransactionMineds() {
        return transactionMineds;
    }

    public void setTransactionMineds(List<TransactionMined> transactionMineds) {
        this.transactionMineds = transactionMineds;
    }
}
