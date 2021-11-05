


public class JsonCreate {

//    public JsonCreate(String response) {
//        //crear json a partir del string, separando por comas los objetos
//        String[] blocks = response.split(",");
//        //crear array de objetos
//        Block[] block = new Block[blocks.length];
//        //rellenar array de objetos
//        for (int i = 0; i < blocks.length; i++) {
//            block[i] = new Block();
//            //separar por :
//            String[] block_data = blocks[i].split(":");
//            //rellenar objeto
//            block[i].setHash(block_data[0]);
//            block[i].setPreviousHash(block_data[1]);
//            block[i].setData(block_data[2]);
//            block[i].setTimestamp(block_data[3]);
//            block[i].setNonce(block_data[4]);
//            block[i].setMiner(block_data[5]);
//            block[i].setVersion(block_data[6]);
//            block[i].setBits(block_data[7]);
//            block[i].setDifficulty(block_data[8]);
//        }
//        //mostrar resultado
//        for (int i = 0; i < blocks.length; i++) {
//            System.out.println(
//                    "Hash: " + block[i].getHash() + "\n" +
//                    "Previous Hash: " + block[i].getPreviousHash() + "\n" +
//                    "Data: " + block[i].getData() + "\n" +
//                    "Timestamp: " + block[i].getTimestamp() + "\n" +
//                    "Nonce: " + block[i].getNonce() + "\n" +
//                    "Miner: " + block[i].getMiner() + "\n" +
//                    "Version: " + block[i].getVersion() + "\n" +
//                    "Bits: " + block[i].getBits() + "\n" +
//                    "Difficulty: " + block[i].getDifficulty() + "\n");
//        }
//    }

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
