import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mining {

    public static String target;
    public static double fee_transactions;
    public static double fee_for_mine = 6.25;
    public static String fee_total;

    //comprueba que crea un hash valido para minar
//    public static String checkHash(String hash, String difficulty) {
//        String hashToCheck = hash.substring(0, difficulty.length());
//        if (hashToCheck.equals(difficulty)) {
//            return hash;
//        } else {
//            return "";
//        }
//    }



    //function for found the difficulty of the hash
    public static String getDifficulty(String hash) {
        String difficulty = "";
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '0') {
                difficulty += "0";
            } else {
                break;
            }
        }
        return difficulty;
    }


    public static Block operation(String response) throws JSONException {
        List<String> list = extractInfoFromJson(response);
        //Encontrar nonce

        String nonce = Util.numtoHex(10); //esto hay que cambiarlo por la llamada al método personalizado de minería


        return createBlock(list.get(0), list.get(1), list.get(2), nonce); //String previousHash, String transactions, String bits, String nonce
    }


    //Extraer de un json en formato string las siguientes variables String previousHash, String data, String nonce
    public static List<String> extractInfoFromJson(String response) {
        String previousHash = "";
        String bits = "";
        String transactions = "";
        try {
            JSONObject jsonObject = new JSONObject(response);

            //extraer el datos de JSONObject response
            String result = jsonObject.getString("result");
            JSONObject jsonResult = new JSONObject(result);

            target = jsonResult.getString("target");
            bits = jsonResult.getString("bits");

            previousHash = jsonResult.getString("previousblockhash");
            transactions = jsonResult.getString("transactions");

            List<String> list = new ArrayList<>();
            list.add(previousHash);
            list.add(transactions);
            list.add(bits);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //crear un bloque de minado correcto según el BIP22
    public static Block createBlock(String previousHash, String transactions, String bits, String nonce) throws JSONException {
        Block block = new Block();
        block.setPreviousHash(previousHash);
        //block.setData(data.toString());
        block.setNonce(nonce);
        block.setTimestamp(String.valueOf(System.currentTimeMillis()));
        block.setVersion(Util.numtoHex(1));
        block.setBits(bits);
        block.setDifficulty(getDifficulty(previousHash));
        block.setHash(mineBlock(block, nonce));
        //de momento concatenamos todas las transacciones
        block.setTransactions(transactions);
        block.setMerkleRoot(extractMerkleRoot(transactions));
        block.setFee(fee_total);
        return block;
    }


    //crear la función mineBlock(block, nonce)
    public static String mineBlock(Block block, String nonce) {
        String theblock = (block.getPreviousHash() + block.getData() + block.getDifficulty() + block.getTimestamp() + block.getVersion() + block.getMerkleRoot() + nonce);
        //Encriptar theblock con librería SHA-256
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(theblock);
        return hash;
    }


    //calcular el merkleroot a partir  de una array de transacciones
    public static String extractMerkleRoot(String transactions) throws JSONException {
        JSONArray jsonTransactions = new JSONArray(transactions);
        fee_transactions = 0;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonTransactions.length(); i++) {
            JSONObject jsonObjectTransaction = jsonTransactions.getJSONObject(i);
            list.add(jsonObjectTransaction.getString("hash"));
            fee_transactions += Integer.parseInt(jsonObjectTransaction.getString("fee"));
        }
        fee_total = Util.satoshisToHex((fee_for_mine*100000000) + fee_transactions);

        return calculateMerkleRoot(list);
    }


    //calcute merkle root from a list of transactions
    public static String calculateMerkleRoot(List<String> data){
        String merkleRoot = "";
        if (data.size() == 1){
            merkleRoot = data.get(0);
        }else {
            List<String> newData = new ArrayList<>();
            for (int i = 0; i < data.size() -1; i = i + 2){
                String str = data.get(i) + data.get(i + 1);
                newData.add(org.apache.commons.codec.digest.DigestUtils.sha256Hex(str));
            }
            merkleRoot = calculateMerkleRoot(newData);
        }
        return merkleRoot;
    }


    //find structure of block mined for send to network with the address of the creator
    //header = reversebytes(field(version, 4)) + reversebytes(prevblock) + reversebytes(merkleroot) + reversebytes(field(time, 4)) + reversebytes(bits)
    public static String blockMinedtoJSON(Block block) throws JSONException {
        String blockMined = "";
        blockMined += "{";
        blockMined += "\"version\":\"" + block.getVersion() + "\",";
        blockMined += "\"previousblockhash\":\"" + block.getPreviousHash() + "\",";

        blockMined += "\"merkleroot\":\"" + block.getMerkleRoot() + "\",";
        blockMined += "\"time\":\"" + block.getTimestamp() + "\",";
        blockMined += "\"bits\":\"" + block.getBits() + "\",";
        blockMined += "\"nonce\":\"" + block.getNonce() + "\",";

//        String transactions = block.getTransactions();
//        transactions = transactions.replace("[", "");
//        transactions = transactions.replace("]", "");

        blockMined += "\"transactions\":[" + block.getTransactions() + "]";

        blockMined += "}";
        return blockMined;
    }

    //find structure of submitblock for send to network with coinbasetxn
    public static String createSubmitBlock(Block block) throws JSONException {
        String submitBlock = "";
        submitBlock += "{";
        submitBlock += "\"jsonrpc\":\"1.0\",";
        submitBlock += "\"id\":\"curltext\",";
        submitBlock += "\"method\":\"submitblock\",";
        submitBlock += "\"params\":[\"" + blockMinedtoJSON(block) + "\"]";
        submitBlock += "}";
        return submitBlock;
    }

    //submitblock












}
