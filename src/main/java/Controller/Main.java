package Controller;

import Model.Block;
import Model.Transaction;
import Util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * (SHA256) Bitcoin
 */
public class Main {

    public static int total_process = 10;
    public static int total_process_parallel = 2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {

        System.out.println("##############################");
        System.out.println("## Welcome to Breaking Hash ##");
        System.out.println("##############################");


        //json for to get template for mining in bitcoin 0.22.0
        String request = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"getblocktemplate\", \"params\": [{\"rules\": [\"segwit\"]}]}";

        //Use the function sendRequest to get gettemplate in bitcoin
        String response = sendRequest(request);
//        System.out.println(response);

        if (!response.equals("")) {
            //Create new block mined

            Block blockMined = Mining.mining(response);
            //System.out.println(blockMined);
            //System.out.println(blockMined.show());

            //Create header for transactions
            Transaction header = new Transaction();
            header.set(blockMined);


            //System.out.println(blockMinedString);
            //System.out.println(blockMined.getTransactionsTwoOnly());
//            System.out.println(header.transactiontoJSON());

            System.out.println(blockMined.toString());

            header.setTransactionMineds(Util.transactionToList(blockMined.getTransactions()));
            String blockMinedString =  blockMined.showBlock() + blockMined.getNonce() + header.showTransaction() ;

            //System.out.println(header.transactiontoJSON());


            //json for to get submitblock for mining in bitcoin 0.22.0
            String request2 = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"submitblock\", \"params\": [" + blockMinedString + "]}";
            System.out.println(request2);

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
            System.out.println("Conexión rechazada.");
            //e.printStackTrace();
        }
        return response;
    }

}


