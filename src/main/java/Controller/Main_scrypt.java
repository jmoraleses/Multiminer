package Controller;

import Core.Scrypt.Converter;
import Model.Block;
import Model.Transaction;
import Util.Util;
import Core.ScryptHelp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static Core.ScryptHelp.*;

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
        System.out.println(response);

        //Create new block mined
        blockMined = Mining.mining(response);

        System.out.println("block mined: "+blockMined);
        System.out.println("block mined: "+ (blockMined.showBlock()));

        if (!response.equals("")) {

            blockMined = Mining.mining(response);

            Transaction header_coinbase = new Transaction();
            header_coinbase.set(blockMined);

            //###
//
//            //This chunk pulls apart the data so they can be endian switched (see the scrypt proof of work page on the wiki)
//            String version = blockMined.getVersion(); //data.substring(0, 8);
//            String prevhash = blockMined.getPreviousHash(); //data.substring(8, 72);
//            String merkle = blockMined.getMerkleRoot(); //data.substring(72, 136 );
//            //String timestamp = blockMined.getTimestamp(); //data.substring(136, 144);
//            String bits = blockMined.getBits(); //data.substring(144, 152);
//            String nonce = blockMined.getNonce(); //data.substring(152,160);
//
//            //This chunk creates endian switched byte arrays from the data
//            byte[] versionbit = chunkEndianSwitch(Converter.fromHexString(version));
//            byte[] prevhashbit = chunkEndianSwitch(Converter.fromHexString(prevhash));
//            byte[] merklebit = chunkEndianSwitch(Converter.fromHexString(merkle));
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String formattedDtm = Instant.ofEpochSecond(Long.parseLong(String.valueOf(System.currentTimeMillis()/1000))).atZone(java.time.ZoneOffset.UTC).format(formatter);
//            Instant time = Timestamp.valueOf(formattedDtm).toLocalDateTime().toInstant(java.time.ZoneOffset.UTC);
//            String timestamp = Util.timestampToHex(time); //timestamp whitout reverse
//
//            byte[] timestampbit = chunkEndianSwitch(Converter.fromHexString(timestamp));
//            byte[] bitsbit = chunkEndianSwitch(Converter.fromHexString(bits));
//            byte[] noncebit = chunkEndianSwitch(Converter.fromHexString(nonce));
//
//            //This chunk of code reassembles the data into a single byre array
//            byte[] databyte = new byte[80];
//            System.arraycopy(versionbit, 0, databyte, 0, versionbit.length);
//            System.arraycopy(prevhashbit, 0, databyte, 4, prevhashbit.length);
//            System.arraycopy(merklebit, 0, databyte, 36, merklebit.length);
//            System.arraycopy(timestampbit, 0, databyte, 68, timestampbit.length);
//            System.arraycopy(bitsbit, 0, databyte, 72, bitsbit.length);
//            System.arraycopy(noncebit, 0, databyte, 76, noncebit.length);
//
//            //Converts the target string to a byte array for easier comparison
//            byte[] targetbyte = Converter.fromHexString(blockMined.getTarget());
//            targetbyte = endianSwitch(targetbyte);
//
//
//            nonce = Mining.doScrypt(databyte, blockMined.getDifficulty());//Calls sCrypt with the proper parameters, and returns the correct data
//            System.out.println("Nonce: " + nonce);
//
//            blockMined.setNonce(Util.reverseHash(nonce));
//            blockMined.setBlockHash(Util.reverseHash(Mining.blockhash));

            //###

            //Prepare to send block mined to the network
            String blockMinedString = (header_coinbase.showTransaction() + Util.merkleRootTXLen(blockMined.getMerkleRoot()) + blockMined.showBlock() ); //+  blockMined.getTransactions();
            System.out.println("mined: "+blockMinedString);
            //System.out.println("Transactions serialized (only txid's): " + blockMined.getTransactionsSerialized());

            String request2 = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"submitblock\", \"params\": [" + blockMinedString + "]}";
            System.out.println(request2);
            System.out.println(ScryptHelp.checkBlock((blockMined.showBlock())+ header_coinbase.showTransaction() )); //+ blockMined.getTransactionsSerialized() ));
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


