package Controller;

import Core.ScryptHelp;
import Model.Block;
import Model.Transaction;
import Util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
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
        //String request = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"getblocktemplate\", \"params\": [{\"rules\": [\"segwit\"]}]}";
        String request = "{\"jsonrpc\": \"2.0\", \"id\":\"curltest\", \"method\": \"getblocktemplate\", \"params\": [] }";

        String response = "";
        Block blockMined = null;
        response = sendRequest(request);
//        System.out.println(response);

        if (!response.equals("")) {

            blockMined = Mining.mining(response);

            Transaction header = new Transaction();
            header.set(blockMined);

            header.setTransactionMineds(Util.transactionToList(blockMined.getTransactions()));
            String blockMinedString =  blockMined.showBlock() + header.showTransaction() ;
            System.out.println("Block mined: "+blockMinedString);

            String request2 = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"submitblock\", \"params\": [" + blockMinedString + "]}";
            System.out.println(request2);

//            String response2 = sendRequest(request2);
//            System.out.println(response2);


        }
    }

    public static String sendWork(String request) throws IOException{

        final String rpcuser ="doge013"; //RPC User name (set in config)
        final String rpcpassword ="dogepruebaX"; //RPC Pass (set in config)

        Authenticator.setDefault(new Authenticator() {//This sets the default authenticator, with the set username and password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (rpcuser, rpcpassword.toCharArray());
            }
        });
        URL url = new URL("http://127.0.0.1:9332");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        System.out.println(request);
        String rpcreturn = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"submitblock\", \"params\": [" +request+ "]}";//RPC call with the new nonced data
        System.out.println(rpcreturn);
        wr.write(rpcreturn);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        line = rd.readLine();
        rd.close();
        return line;
    }

    /**
     * Function for send request to bitcoin server
     *
     * @param requestBody
     * @return
     */
    public static String sendRequest(String requestBody) {
        String uri = "http://127.0.0.1:9332";
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


