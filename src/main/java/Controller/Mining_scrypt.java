package Controller;

import Core.Scrypt;
import Model.Block_scrypt;
import Util.Util;
import Util.Searching;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mining_scrypt {

    public static String target;
    public static double fee_transactions;
    public static double fee_for_mine = 10000;
    public static String fee_total;



    public static Block_scrypt operation(String response) throws JSONException, IOException, InterruptedException, GeneralSecurityException {
        List<String> list = extractInfoFromJson(response);
        //Creamos un bloque con nounce y blockhash erróneos
        String nonce = Util.numtoHex(0); //esto hay que cambiarlo por la llamada al método personalizado de minería
        Block_scrypt block = createBlock(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), nonce); //String previousHash, String transactions, String bits, String nonce
        //System.out.println(block);

        //buscamos el verdadero nounce
        nonce = Scrypt.SearchingNonce(block, block.getTarget());

        //if (nonce == null) return null;

        block.setNonce(nonce);
        block.setBlockHash(Scrypt.blockhash);
        //System.out.println("Nonce: "+block.getNonce()); //
        //System.out.println("Blockhash: "+block.getBlockHash()); //
        return block;
    }



    //Extraer de un json en formato string las siguientes variables String previousHash, String data, String nonce
    public static List<String> extractInfoFromJson(String response) {
        String previousHash = "";
        String bits = "";
        String transactions = "";
        String height = "";
        String target = "";
        try {
            if (!Objects.equals(response, "")) {
                JSONObject jsonObject = new JSONObject(response);

                String result = jsonObject.getString("result");
                JSONObject jsonResult = new JSONObject(result);

                target = jsonResult.getString("target");
                bits = jsonResult.getString("bits");

                previousHash = jsonResult.getString("previousblockhash");
                transactions = jsonResult.getString("transactions");

                //extraer altura del bloque
                height = jsonResult.getString("height");
                target = jsonResult.getString("target");
                Scrypt.noncerange = jsonResult.getString("noncerange");

                List<String> list = new ArrayList<>();
                list.add(previousHash);
                list.add(transactions);
                list.add(bits);
                list.add(height);
                list.add(target);
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //crear un bloque de minado correcto según el BIP22
    public static Block_scrypt createBlock(String previousHash, String transactions, String bits, String height, String target, String nonce) throws JSONException, IOException {
        Block_scrypt block = new Block_scrypt();
        block.setPreviousHash(previousHash);
        block.setNonce(nonce);
        block.setTimestamp(String.valueOf(System.currentTimeMillis()));
        block.setVersion(Util.numtoHex(1));
        block.setBits(bits);
        block.setDifficulty(Util.getDifficulty(previousHash));
        block.setHash(mineBlock(block, nonce));
        block.setTransactions(transactions);
        block.setMerkleRoot(extractMerkleRoot(transactions));
        block.setFee(fee_total);
//        block.setBlockHash(Scrypt.scrypt(Scrypt.showBlock(block)));
        block.setBlockHash(Scrypt.blockhash);
        block.setHeight(height);
        block.setTarget(target);
        return block;
    }

    //crear la función mineBlock(block, nonce)
    public static String mineBlock(Block_scrypt block, String nonce) {
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
            fee_transactions += Long.parseLong(jsonObjectTransaction.getString("fee"));
        }
        fee_total = Util.satoshisToHex((fee_for_mine*100000000) + fee_transactions);
        if (list.size() == 0) return "";
        else return calculateMerkleRoot(list);
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

}
