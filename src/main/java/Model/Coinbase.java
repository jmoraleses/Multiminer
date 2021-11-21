package Model;

import Util.Util;

import java.util.List;

public class Coinbase {

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

    public List<Transaction> transactions;
    private String byteCoinbase;

    public void set(Block block) throws Exception {
        Miner miner = new Miner();
        version = Util.numtoHex(1);
        inputCount = Util.toHex(1); //entrada de transaccion
        txid = "0000000000000000000000000000000000000000000000000000000000000000";
        vout = "ffffffff";
        heightLength = "03";
        height = Util.reverseHash(Util.toHex(Integer.parseInt(block.getHeight())));
        scriptSig = heightLength + height + Util.hexString(Miner.phrase); //script de desbloqueo
        scriptSigSize = Util.toHex(scriptSig.length()/2);
        sequence = "ffffffff"; //ffffffff //00000000
        outputCount = Util.toHex(1); // Util.toHex(this.transactions.size()); //salida de transaccion
        value = String.valueOf(block.getFee()); //no debe exceder las recompensas
        scriptPubKey = miner.getScriptPubKey(); //P2PKH
        scriptPubKeySize = Util.toHex(scriptPubKey.length()/2);
        locktime = Util.numtoHex(0);

    }


    //concatenar valores de transacción coinbase
    public String showTransaction() {
        String output = Util.toHex(this.transactions.size());
        //String output = "01";

        output += Util.reverseHash(this.getVersion()); //version
        output += this.getInputCount(); // input count //num_inputs
        output += Util.reverseHash(this.getTxid()); // 000... //prev_hash
        output += Util.reverseHash(this.getVout()); //previous output // prev_hash_index
        output += this.getScriptSigSize();//
        output += this.getScriptSig();
        output += Util.reverseHash(this.getSequence());
        output += this.getOutputCount(); //01: se aplica a todas las salidas
        output += Util.reverseHash(this.getValue()); //fee in satoshis
        output += this.getScriptPubKeySize();//
        output += this.getScriptPubKey();

//        output += Util.numtoHex(transactions.size());
//        for(int i = 0; i < transactions.size(); i++) {
//            output += transactions.get(i).showTransactionMined();
//        }

        output += this.getLocktime(); //locktime
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

    public void addTransactionMineds(Transaction transactionMineds) {
        this.transactions.add(transactionMineds);
    }

    public List<Transaction> getTransactionMineds() {
        return transactions;
    }

    public void setTransactionMineds(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
