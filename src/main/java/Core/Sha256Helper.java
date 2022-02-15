package Core;

import Core.Sha256.Sha256;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sha256Helper extends Thread {

    public static String thehash;
    public static String thenonce;

    List<Thread> hilos = new ArrayList<>();
    private int numThreads = 100;
    public static int hashesSecond = 0;


    public Sha256Helper(String block, String target, long startTime) throws IOException, InterruptedException {

        for (int i = 0; i < numThreads; i++) {
            Runnable r1 = (Runnable) new Sha256(block, target, startTime, 1000000000L*i);
            hilos.add(new Thread(r1));
//            System.out.println("Hilo " + i + " creado");
        }
    }

    //función para comprobar si algún hilo ha finalizado y en tal caso recoger el resultado y cerrar los demás hilos
    public List<String> startWait() throws InterruptedException {
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread hilo : hilos) {
            hilo.start();
        }
        //comprobar si algún hilo a terminado
        while (true) {
            for (Thread hilo : hilos) {
                if (hilo.isInterrupted()) {
                    List<String> list = new ArrayList<String>();
                    list.add(thenonce);
                    list.add(thehash);
                    //cerrar los demás hilos
                    for (Thread hilo1 : hilos) {
                        hilo1.interrupt();
                    }
                    return list;
                }
            }
            Thread.sleep(1000);
            System.out.println(hashesSecond+" hashes/s");
            hashesSecond = 0;

        }
    }


}
