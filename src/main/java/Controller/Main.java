package Controller;

import Model.Block;
import Model.Transaction;
import Util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
        Block blockMined = null;
        String response = "";
        while(blockMined == null){
            long startTime = System.currentTimeMillis();
            response = sendRequest(request);
            blockMined = Mining.mining(response, startTime);
        }
        if (!response.equals("")) {
            if (blockMined.getNonce() != null){

                Transaction header = new Transaction();
                header.set(blockMined);
                header.setTransactionMineds(Util.transactionToList(blockMined.getTransactions()));

                //Calculamos el merkleroot para todas las transacciones, incluida la coinbase
                List<String> hashes = new ArrayList<>();
                hashes.add(blockMined.getBlockhash());
                for(int i=0; i < header.getTransactionMineds().size(); i++){
                    hashes.add(header.getTransactionMineds().get(i).getHash());
                }
                blockMined.setMerkleRoot(Mining.lastHashMerkleRoot(hashes));

                String blockMinedString =  blockMined.showBlock() + header.showTransaction() ;

                String request2 = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"submitblock\", \"params\": [" + blockMinedString + "]}";
                System.out.println(request2);

                String response2 = sendRequest(request2);
                System.out.println(response2);
            }
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


