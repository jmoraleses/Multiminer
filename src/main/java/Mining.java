import net.minidev.json.JSONObject;

public class Mining {

    //Function Miner, takes in a json object and return a block mined
    public static String miner(JSONObject jsonObject) {
        //Processing the json object for mining bitcoins
        String hash = jsonObject.get("hash").toString();
        String previousHash = jsonObject.get("previousHash").toString();
        String data = jsonObject.get("data").toString();
        String timestamp = jsonObject.get("timestamp").toString();
        String nonce = jsonObject.get("nonce").toString();
        String difficulty = jsonObject.get("difficulty").toString();
        String block = jsonObject.get("block").toString();
        String miner = jsonObject.get("miner").toString();
        String signature = jsonObject.get("signature").toString();

        //Mining the block
        String newHash = hash;
        while (!newHash.substring(0, difficulty.length()).equals(difficulty)) {
            newHash = hash(previousHash, data, timestamp, nonce, difficulty, block, miner, signature);
            nonce = String.valueOf(Integer.parseInt(nonce) + 1);
        }
        return newHash;
    }

//    //Function hash, takes in a block and return the hash of the block
//    public static String hash(String previousHash, String data, String timestamp, String nonce, String difficulty, String block, String miner, String signature) {
//        return StringUtil.applySha256(previousHash + data + timestamp + nonce + difficulty + block + miner + signature);
//    }
}

}
