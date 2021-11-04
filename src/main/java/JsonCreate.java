

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

}
