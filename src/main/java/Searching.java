import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Searching {

//    public static int nonceMAX = 4294967295; //máximo conceptual de un int
    public static int nonceMAX = 999999999;
    public static int min = 0;
    public static int max = 100;

    //Búsqueda del nonce
    public static String searchNonce(Block block, String target) {
        String nonce = "";
        int num = 0;
        String blockhash = Util.blockHash(block.show() + String.valueOf(0));

        while (!blockhash.startsWith(target) && num < nonceMAX) {
            //Crear números aleaotorios sin posibilidad de repetirse
            //nonce = charToHex();

            //calcular blockhash
            num++;
            blockhash = Util.blockHash(block.show() + Util.numtoHex(num));
        }
        System.out.println("Nonce: " + nonce);
        return nonce;
    }


    public static String charToHex(){
        String nonce = "";
        String caracteresHex = "0123456789abcdef";

        //Crear números aleaotorios sin posibilidad de repetirse
        int randomNum1 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum2 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum3 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum4 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum5 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum6 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum7 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum8 = ThreadLocalRandom.current().nextInt(min, max + 1);

        nonce = String.valueOf(caracteresHex.charAt(randomNum1 % 16)) + String.valueOf(caracteresHex.charAt(randomNum2 % 16)) + String.valueOf(caracteresHex.charAt(randomNum3 % 16)) + String.valueOf(caracteresHex.charAt(randomNum4 % 16)) + String.valueOf(caracteresHex.charAt(randomNum5 % 16)) + String.valueOf(caracteresHex.charAt(randomNum6 % 16)) + String.valueOf(caracteresHex.charAt(randomNum7 % 16)) + String.valueOf(caracteresHex.charAt(randomNum8 % 16));
        return nonce;
    }

}
