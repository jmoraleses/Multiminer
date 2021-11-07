


public class JsonCreate {

    //fill block of mined and send to network bitcoin for RPC connection
    public static String createBlock(String hash, String previousHash, String data, String timestamp, String nonce, String miner) {
        String block = "{" +
                "\"hash\":\"" + hash + "\"," +
                "\"previousHash\":\"" + previousHash + "\"," +
                "\"data\":\"" + data + "\"," +
                "\"timestamp\":\"" + timestamp + "\"," +
                "\"nonce\":\"" + nonce + "\"," +
                "\"miner\":\"" + miner + "\"" +
                "}";
        return block;
    }

    //after call getblocktemplate in network bitcoin I need save result in array
    public static String createBlock(String hash, String previousHash, String data, String timestamp, String nonce, String miner, String version, String bits, String difficulty) {
        String block = "{" +
                "\"hash\":\"" + hash + "\"," +
                "\"previousHash\":\"" + previousHash + "\"," +
                "\"data\":\"" + data + "\"," +
                "\"timestamp\":\"" + timestamp + "\"," +
                "\"nonce\":\"" + nonce + "\"," +
                "\"miner\":\"" + miner + "\"," +
                "\"version\":\"" + version + "\"," +
                "\"bits\":\"" + bits + "\"," +
                "\"difficulty\":\"" + difficulty + "\"" +
                "}";
        return block;
    }




}
