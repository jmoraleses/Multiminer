import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Searching {

    public static int nonce = 0;
//    public static int nonceMAX = 4294967295; //FFFFFFFF
    public static int nonceMAX = 999999999;
    public static int min = 0;
    public static int max = 100;

    public static int numThreads = Runtime.getRuntime().availableProcessors();
    public static int ini = 0;
    public static int middle = (int)(nonceMAX / numThreads);
    public static int fin = middle;

    //Búsqueda del nonce
    public static void searchNonce(Block block, String target, int ini, int fin) {
        //String nonce = "";
        //System.out.println("Buscando entre "+ini+" y "+fin);
        int num = 0;
        String blockhash = Util.blockHash(block.show() + String.valueOf(0));

        while (!blockhash.startsWith(target) && num < fin) {
            //Crear números aleatorios sin posibilidad de repetirse
            //nonce = charToHex();

            //calcular blockhash
            num++;
            blockhash = Util.blockHash(block.show() + Util.numtoHex(num));
        }
        //nonce = String.valueOf(num);
        if (num > 0) nonce = num;
        //System.out.println("Nonce: " + String.valueOf(nonce));
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

    //función que utiliza todos los núcleos del procesador para ejecutar un proceso
    public static void parallelProcess(Block block, String target) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ini += middle;
                fin = ini + middle;
                searchNonce(block, target, ini, fin);
            }
        };
        for (int i = 0; i < numThreads; i++) {
            new Thread(r).start();
        }
    }




}
