import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.List;

public class Main {

    private static JsonCreate jsonCreate;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {

        System.out.println("##############################");
        System.out.println("## Welcome to Breaking Hash ##");
        System.out.println("##############################");

        //Ask for introduce by keyboard the next variables: dir_hashcat, hash, wordlist and output_file
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String route_hashcat = "C:/Users/Javier/Documents/hashcat-6.2.4"; //Introduce the route of hashcat

        //if route_hashcat is equal to "" then return to ask for route_hashcat""
        while (route_hashcat.equals("")) {
            System.out.println("Please introduce route of hashcat (necessary):");
            route_hashcat = br.readLine();
        }
        //if directory of route_hashcat doesn't exist, the program show error
        if (!(new java.io.File(route_hashcat).exists())) {
            System.out.println("Error directory not exist, the program will close.");
            //close de program
            System.exit(0);
        } else {
            //if directory exist create a new directory "crackme"
            String route_crackme = route_hashcat + "/crackme";
            new java.io.File(route_crackme).mkdir();
            //Show message and put in pause the program until the user press enter
//            System.out.println("Please introduce name file for wordlist:");
//            String wordlist = br.readLine();
//            System.out.println("Please introduce name file for output file:");
//            String output_file = br.readLine();
//            System.out.println("Please introduce name file with the hashes:");
//            String hashes = br.readLine();

            String wordlist = "";
            String output_file = "";
            String hashes = "";


            //call to function hashcat in "Hashcat" class
            //Hashcat.launch(route_hashcat, wordlist, output_file, hashes);

        }

        //pause the program until push enter
//        System.out.println("Press enter to continue...");
//        System.in.read();

        //variable "request" with json for getbalance in bitcoin
        //String request = "{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"getbalance\"}";

        //json for to get template for mining in bitcoin 0.22.0
        String request = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"getblocktemplate\", \"params\": [{\"rules\": [\"segwit\"]}]}";
        //String request2= "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"getblocktemplate\", \"params\": [{\"rules\": [\"segwit\"]}]}";

        //Use the function sendRequest to get gettemplate in bitcoin
        String response = sendRequest(request);
        System.out.println(response);

        //Create new block mined
        Block blockMined = Mining.operation(response);
        System.out.println(blockMined);
        System.out.println(blockMined.show());
        System.out.println(blockMined.getBlockHash());





        //Create transaction
        Transaction header = new Transaction();
        header.set(blockMined);

        //Add header to transactions of the block mined
        //blockMined.addHeader(header);

        //set merkleRoot complete
//        String str = header + blockMined.getMerkleRoot();
//        blockMined.setMerkleRoot(org.apache.commons.codec.digest.DigestUtils.sha256Hex(str));


        //Prepare to send block mined to the network
//        String blockMinedString = Mining.blockMinedtoJSON(blockMined);
        String blockMinedString = blockMined.show() + header.show();
        System.out.println(blockMinedString);



        //json for to get submitblock for mining in bitcoin 0.22.0
        String request2 = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"submitblock\", \"params\": [" + blockMinedString + "]}";


        String response2 = sendRequest(request2);
        System.out.println(response2);



    }


    /**
     * Function for send request to bitcoin server
     *
     * @param requestBody
     * @return
     */
    public static String sendRequest(String requestBody) {
//        Authenticator.setDefault(new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("prueba", "prueba".toCharArray());
//            }
//        });
        String uri = "http://127.0.0.1:18332";
        String contentType = "application/json";
        //user and password for bitcoin server
        String user = "prueba";
        String password = "prueba";
        String response = "";
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept", contentType);
            connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((user + ":" + password).getBytes()));
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(requestBody);
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();
            } else {
                System.out.println("Error: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

//        HttpURLConnection connection = null;

//        try {
//            URL url = new URL(uri);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", contentType);
//            connection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));
//            connection.setUseCaches(false);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setConnectTimeout(5000);
//            connection.setReadTimeout(5000);
//            connection.connect();
//            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
//            writer.writeBytes(requestBody);
//            writer.flush();
//            writer.close();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//            return response.toString();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }



//    //send block mined to the network with submitblock
//    public static String sendBlockMined(String blockMined) {
//
//        String response = "";
//        try {
//            URL url = new URL("http://localhost:9997/");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(blockMined);
//            wr.flush();
//            wr.close();
//            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response1 = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response1.append(inputLine);
//            }
//            in.close();
//            response = response1.toString();
//            System.out.println(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return response;
//    }

}


