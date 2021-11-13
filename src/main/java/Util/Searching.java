package Util;

import Util.Util;
import Model.Block;

import java.util.concurrent.ThreadLocalRandom;

public class Searching {

    public static String nonce;

    public static int min = 0;
    public static int max = 100;
    public static int numThreads = Runtime.getRuntime().availableProcessors();
    public static int ini =  500000000;
    public static int fin = 1000000000;
//    public static int nonceMAX = 4294967295; //FFFFFFFF

    //Búsqueda del nonce
    public static void searchNonce(Block block, String target) {
        System.out.println("Buscando entre "+ini+" y "+fin);
        int num = ini;
        String blockhash = Util.blockHash(block.show() + String.valueOf(0));

        while (!blockhash.startsWith(target) && num < fin) {
            //Crear números aleatorios sin posibilidad de repetirse
            //nonce = charToHex();

            //calcular blockhash
            num++;
            blockhash = Util.blockHash(block.show() + Util.numtoHex(num));
        }
        if (blockhash.startsWith(target)){
            nonce = Util.numtoHex(num);
            System.out.println("Nonce: " + String.valueOf(nonce));
        }

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
