package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;


public class Hashcat {

    private static final Map<Thread, Runnable> map = new HashMap<>();
    private static final int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors(); //find the number of process that system is able to execute


    private static void execute(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
    }

    public static Runnable ejecutar(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    execute(args);
                } catch (IOException | InterruptedException e) {
                    System.out.println("Error: hashcat ha sido detenido");
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        //establecer datos
        map.put(thread, runnable);
        //iniciar thread
        thread.start();
        //Obtener datos
        return map.get(thread);
    }


    //write function for executing in parallel process with external command in a shell
    public static void process(String[] args) {
        try {
            java.lang.Process p = Runtime.getRuntime().exec(args);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    //find library for execute hashcat in java
    public static String launch(String route_hashcat, String wordlist, String output_file, String hashes, int total_process, int total_process_parallel) throws IOException, InterruptedException {

        //if all before variables is empty , assign the default values
        if (route_hashcat.equals("")) route_hashcat = "C:/Users/Javier/Documents/hashcat-6.2.4";
        if (hashes.equals("")) hashes = "crackme";
        if (wordlist.equals("")) wordlist = "wordlist.txt";
        if (output_file.equals("")) output_file = "output_file.txt";
        //to change "\" to "/" in all before variables
        route_hashcat = route_hashcat.replace("\\", "/");
        if (route_hashcat.charAt(route_hashcat.length() - 1) != '/') route_hashcat += '/';
        hashes = hashes.replace("\\", "/");
        wordlist = wordlist.replace("\\", "/");
        output_file = output_file.replace("\\", "/");

        //count total the files inside the folder route_hashcat
        int total_files = -1;
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(route_hashcat + hashes);
            java.nio.file.DirectoryStream<java.nio.file.Path> stream = java.nio.file.Files.newDirectoryStream(path);
            for (java.nio.file.Path file : stream) {
                total_files++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //create a array of string to store all the hashcat commands
        String cmd = "";
        for (int x = 2; x <= total_process; x += 2) { //Change the number of process that you want to execute
            for (int i = x-2; i < x + total_process_parallel; i++) { //Change the number of process that you want to execute

                while (!new File(route_hashcat + "crackme/" + output_file).exists()) {

                    cmd = "C:/msys64/msys2.exe " + route_hashcat + "hashcat.exe -a 3 -m 21400 --session session_" + i + " --potfile-path " + route_hashcat + "path_" + i + " -o " + route_hashcat + "crackme/" + output_file + " " + route_hashcat + "crackme/" + hashes + "_" + i + ".txt --wordlist " + route_hashcat + "crackme/" + wordlist;
                    System.out.println(cmd);
                    //Convert the string to command line arguments
                    String[] args = cmd.split(" ");

                    //Ejecutar el comando hasta que se detenga el proceso
                    process(args);
                    //ejecutar(args);

                    //si existe le archivo output_file
                    if (new File(route_hashcat + "crackme/" + output_file).exists()) {
                        //recuperar contenido de del archivo output_file
                        String content = new String(Files.readAllBytes(Paths.get(route_hashcat + "crackme/" + output_file)));
                        System.out.println("contenido del output file:");
                        System.out.println(content);
                        return content;
                    } else {
                        System.out.println("no existe el archivo output_file");
                        //esperar un minuto
                        Thread.sleep(5000);
                    }
                }

            }
        }

        return null;
    }
}