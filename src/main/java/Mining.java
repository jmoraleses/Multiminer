import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mining {



    //comprueba que crea un hash valido para minar
    public static String checkHash(String hash, String difficulty) {
        String hashToCheck = hash.substring(0, difficulty.length());
        if (hashToCheck.equals(difficulty)) {
            return hash;
        } else {
            return "";
        }
    }

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


    //Use btc4j-core library for mining with the following parameters: jsonObjectResponse and the nonce
//    public static String mineBlock(String jsonObjectResponse, String nonce) {
//        String hash = "";
//        try {
//            JSONObject jsonObject = new JSONObject(jsonObjectResponse);
//            String previousHash = jsonObject.getString("previous_hash");
//            String difficulty = getDifficulty(previousHash);
//            String data = jsonObject.getString("data");
//            String timestamp = jsonObject.getString("timestamp");
//            String version = jsonObject.getString("version");
//            String merkleRoot = jsonObject.getString("merkle_root");
//            String nonceString = nonce;
//
//            //Use btc4j-core library for mining with the following parameters: hash and the nonce
//            //encrypt in sha256 the nonce and the another data
//
//            String theblock = (previousHash + data + difficulty + timestamp + version + merkleRoot + nonceString);
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            hash = md.digest(theblock.getBytes()).toString();
//
//        } catch (JSONException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return hash;
//    }
//



//    //Use library bitcoin-rpc-client for mining with the following parameters: the block and the nonce
//    public static String mineBlock(Block block, String nonce) {
//        String hash = "";
//        try {
//            String theblock = (block.getPreviousHash() + block.getData() + block.getDifficulty() + block.getTimestamp() + block.getVersion() + block.getMerkleRoot() + nonce);
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            hash = md.digest(theblock.getBytes()).toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return hash;
//    }



    public static Block operation(String response){
        List<String> list = extractInfoFromJson(response);
        //Encontrar nonce

        String nonce = "";

        return createBlock(list.get(0), list.get(1), nonce);
    }



    //Extraer de un json en formato string las siguientes variables String previousHash, String data, String nonce
    public static List<String> extractInfoFromJson(String response) {
        String previousHash = "";
        String data = "";
        //String nonce = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            //extraer el datos de JSONObject response
            String result = jsonObject.getString("result");
            JSONObject jsonResult = new JSONObject(result);
            previousHash = jsonResult.getString("previousblockhash");
            data = jsonResult.getString("transactions");
            //nonce = jsonObject.getString("nonce");
            List<String> list = new ArrayList<>();
            list.add(previousHash);
            list.add(data);
            //list.add(nonce);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //calcular el merkleroot
    public static String calculateMerkleRoot(String[] transactions) {
        String merkleRoot = "";
        int i = 0;
        while (i < transactions.length) {
            if (i + 1 < transactions.length) {
                merkleRoot += transactions[i] + transactions[i + 1];
                i += 2;
            } else {
                merkleRoot += transactions[i];
                i++;
            }
        }
        return merkleRoot;
    }


    //crear un bloque de minado correcto según el BIP22
    public static Block createBlock(String previousHash, String data, String nonce) {
        Block block = new Block();
        block.setPreviousHash(previousHash);
        block.setData(data);
        block.setNonce(nonce);
        block.setTimestamp(String.valueOf(System.currentTimeMillis()));
        block.setVersion("1");
        block.setMerkleRoot(calculateMerkleRoot(new String[] { data }));
        block.setDifficulty(getDifficulty(previousHash));
        block.setHash(mineBlock(block, nonce));
        return block;
    }

    //crear la función mineBlock(block, nonce)
    public static String mineBlock(Block block, String nonce) {
        String theblock = (block.getPreviousHash() + block.getData() + block.getDifficulty() + block.getTimestamp() + block.getVersion() + block.getMerkleRoot() + nonce);
        //Encriptar theblock con librería SHA-256
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(theblock);
        return hash;
    }

}
