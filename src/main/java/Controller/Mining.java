package Controller;

import Core.Sha256.Sha256;
import Core.Sha256Helper;
import Model.Block;
import Util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Mining {

    public static double fee_transactions;
    public static double fee_for_mine = Main.fee_for_mine;
    public static String fee_total;

    public static Block mining(String response, long startTime, String algorithm) throws JSONException, IOException, GeneralSecurityException, InterruptedException {
        //System.out.println("Comienza:");
        List<Object> list = extractInfoFromJson(response);
        if(response != null && !response.equals("")) {
            String nonce = Util.numtoHex(0);
            Block block = createBlock((String) list.get(0), (JSONArray) list.get(1), (String) list.get(2), (String) list.get(3), (String) list.get(4), (String) nonce); //String previousHash, String transactions, String bits, String nonce
            if (block.getMerkleRoot() != "" && block.getMerkleRoot() != null) {
                System.out.println("Minando...");
                //Buscamos el nonce
                Sha256Helper sha256helper = new Sha256Helper(block.showBlockWithoutNonce(), block.getTarget(), startTime);
                List<String> nonceHash = sha256helper.startWait();
//                List<String> nonceHash = Algorithm.POW(Converter.fromHexString(block.showBlockWithoutNonce()), block.getTarget(), startTime, algorithm);
                if (nonceHash != null && nonceHash.get(1) != "0000000000000000000000000000000000000000000000000000000000000000") {
                    block.setNonce(nonceHash.get(0));
                    block.setBlockhash(nonceHash.get(1));
                    return block;
                }
                return null;
            }else{
                System.out.println("Transacciones no encontradas...");
                TimeUnit.SECONDS.sleep(60);
            }
        }
        return null;

    }


    //crear un bloque de minado correcto según el BIP22
    public static Block createBlock(String previousHash, JSONArray transactions, String bits, String height, String target, String nonce) throws JSONException, IOException, NoSuchAlgorithmException {
        Block block = new Block();
        block.setPreviousHash(previousHash);
        block.setNonce(nonce);
        block.setTimestamp(String.valueOf(System.currentTimeMillis()));
        block.setVersion(Util.numtoHex(1));
        block.setBits(bits);
        String zeros = "";
        for(int i=0; i < Util.countLeadingZeros(target); i++){
            zeros += "0";
        }
        block.setDifficulty(zeros);
        block.setTransactions(transactions);
        block.setMerkleRoot(extractMerkleRoot(transactions));
        block.setFee(fee_total);
        block.setHeight(height);
        block.setTarget(target);
        return block;
    }

    //Extraer de un json en formato string las siguientes variables String previousHash, String data, String nonce
    public static List<Object> extractInfoFromJson(String response) {
        String previousHash = "";
        String bits = "";
        String height = "";
        String target = "";
        try {
            if (!Objects.equals(response, "")) {
                JSONObject jsonObject = new JSONObject(response);
                String result = jsonObject.getString("result");
                JSONObject jsonResult = new JSONObject(result);
                bits = jsonResult.getString("bits");
                previousHash = jsonResult.getString("previousblockhash");
                JSONArray transactions = jsonResult.getJSONArray("transactions");
                height = jsonResult.getString("height");
                target = jsonResult.getString("target");
                //Scrypt.noncerange = jsonResult.getString("noncerange");
                List<Object> list = new ArrayList<>();
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

    //calcular el merkleroot a partir  de una array de transacciones
    public static String extractMerkleRoot(JSONArray transactions) throws JSONException, NoSuchAlgorithmException {
        fee_transactions = 0;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < transactions.length(); i++) {
            JSONObject jsonObjectTransaction = transactions.getJSONObject(i);
            list.add(jsonObjectTransaction.getString("txid"));
            fee_transactions += Long.parseLong(jsonObjectTransaction.getString("fee"));
        }
        fee_total = Util.satoshisToHex((fee_for_mine*100000000) + fee_transactions);
        if (list.size() == 0) return null;
        return (Util.calculateMerkleRoot(list)); //
    }


}
