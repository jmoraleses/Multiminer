import java.io.IOException;


public class Hashcat {

    //find the number of process that system is able to execute
    private static int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();

    //write a function for execute to much subprocess in parallel

    private static void execute(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
    }

    //write function for executing in parallel process with external command in a shell
//    public static void process(String cmd) {
//        try {
//            java.lang.Process p = Runtime.getRuntime().exec(cmd);
//            p.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    //find library for execute hashcat in java
    public static void launch(String route_hashcat, String wordlist, String output_file, String hashes) throws IOException, InterruptedException {

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
            java.nio.file.Path path = java.nio.file.Paths.get(route_hashcat+hashes);
            java.nio.file.DirectoryStream<java.nio.file.Path> stream = java.nio.file.Files.newDirectoryStream(path);
            for (java.nio.file.Path file : stream) {
                total_files++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


//        String cmd = "C:/msys64/msys2.exe " + route_hashcat + "hashcat.exe -a 3 -m 21400 -o " + route_hashcat + output_file + " " + route_hashcat + "crackme/" + hashes + "_1" + ".txt --wordlist " + route_hashcat + "crackme/" + wordlist;
//        process(cmd);


        //create a array of string to store all the hashcat commands
        String cmd = "";
        for (int i = 1; i <= 3; i++) { //Change the number of process that you want to execute
            try {
//                if (i == 1) cmd = "C:/msys64/msys2.exe " + route_hashcat + "hashcat.exe  -a 3 -m 21400 --brain-server --session session_"+i+" --potfile-path "+ route_hashcat +"path_"+i+" -o " + route_hashcat + output_file + " " + route_hashcat + "crackme/" + hashes + "_" + i + ".txt --wordlist " + route_hashcat + "crackme/" + wordlist;
//                else cmd = "C:/msys64/msys2.exe " + route_hashcat + "hashcat.exe -a 3 -m 21400 --brain-client --session session_"+i+" --potfile-path "+ route_hashcat +"path_"+i+" -o " + route_hashcat + output_file + " " + route_hashcat + "crackme/" + hashes + "_" + i + ".txt --wordlist " + route_hashcat + "crackme/" + wordlist;
                cmd = "C:/msys64/msys2.exe " + route_hashcat + "hashcat.exe -a 3 -m 21400 --session session_"+i+" --potfile-path "+ route_hashcat +"path_"+i+" -o " + route_hashcat + output_file + " " + route_hashcat + "crackme/" + hashes + "_" + i + ".txt --wordlist " + route_hashcat + "crackme/" + wordlist;
                //Convert the string to command line arguments
                String[] args = cmd.split(" ");
                execute(args);
            } finally {
                System.out.println("Process " + i + " finished");
            }
        }



    }


}

