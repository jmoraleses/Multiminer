public class Searching {

    public static int nonce = 0;
    public static int nonceMAX = 1500000000;

    //Búsqueda del nonce
    public static int search(Block block, String target) {
        String blockhash = Util.blockHash(block.show()+Util.numtoHex(nonce));
        while (!blockhash.startsWith(target) && nonce < nonceMAX) {
            nonce += 1;
            blockhash = Util.blockHash(block.show()+Util.numtoHex(nonce));
        }
        System.out.println("Nonce: " + nonce);
        return nonce;
    }

}
