package Controller;

import Util.Util;
import Core.Scrypt;
import Model.Block_scrypt;
import Model.Transaction_scrypt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * (Scrypt) Dogecoin, Litecoin
 */
public class Main_scrypt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {

        System.out.println("##############################");
        System.out.println("## Welcome to Breaking Hash ##");
        System.out.println("##############################");

        //json for to get template for mining in bitcoin 0.22.0
//        String request = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"getblocktemplate\", \"params\": [{\"rules\": [\"segwit\"]}]}";
        String request = "{\"jsonrpc\": \"2.0\", \"id\":\"curltest\", \"method\": \"getblocktemplate\", \"params\": [] }";

        //Use the function sendRequest to get gettemplate in bitcoin
        //String response = sendRequest(request);
        //System.out.println(response);



        String response = "";
        Block_scrypt blockMined = null;
        //while (blockMined == null) {
        response = sendRequest(request);
        System.out.println(response); //
        //Create new block mined
        blockMined = Mining_scrypt.operation(response);
        System.out.println("block mined: "+blockMined);
        //}

        if (!response.equals("")) {
            //System.out.println(blockMined.show());

            //Create header for transactions
            Transaction_scrypt header_coinbase = new Transaction_scrypt();
            header_coinbase.set(blockMined);

            //Prepare to send block mined to the network
            String blockMinedString = Scrypt.showBlock(blockMined) + Util.merkleRootTXLen(blockMined.getMerkleRoot()) +  Scrypt.showTransaction(header_coinbase) ; //+  blockMined.getTransactions();
            System.out.println(blockMinedString);


            //json for to get submitblock for mining in Scrypt
            String request2 = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"submitblock\", \"params\": [" + blockMinedString + "]}";

            String response2 = sendRequest(request2);
            System.out.println(response2);


        }
    }


    /**
     * Function for send request to bitcoin server
     *
     * @param requestBody
     * @return
     */
    public static String sendRequest(String requestBody) {
        String uri = "http://127.0.0.1:18332";
        String contentType = "application/json";
        //user and password for bitcoin server
        String user = "doge013";
        String password = "dogepruebaX";
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
            System.out.println("Conexión rechazada.");
            //e.printStackTrace();
        }
        return response;
    }

}


