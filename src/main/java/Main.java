import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {


        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("prueba", "prueba".toCharArray());
            }
        });
        String uri = "http://127.0.0.1:9997";
        //String requestBody = "{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"getbalance\"}";
        String requestBody =   "{\"jsonrpc\":\"2.0\",\"id\": 0, \"method\": \"getblocktemplate\", \"params\": [{\"capabilities\": [\"coinbasetxn\", \"workid\", \"coinbase/append\"]}]}";
        String contentType = "application/json";
        HttpURLConnection connection = null;

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(requestBody.getBytes().length));
            connection.setUseCaches(true);
            connection.setDoInput(true);
            OutputStream out = connection.getOutputStream();
            out.write(requestBody.getBytes());
            out.flush();
            out.close();
        } catch (IOException ioE) {
            connection.disconnect();
            ioE.printStackTrace();
        }







        //BtcBlockTemplate  template = new BtcBlockTemplate();

        /*
        BitcoinJSONRPCClient client = new BitcoinJSONRPCClient();
        BitcoindRpcClient.BlockChainInfo bci = client.getBlockChainInfo();
        System.out.println("Tipo: "+bci.chain());
        System.out.println("Difficulty: "+bci.difficulty());
        System.out.println("progreso: "+bci.verificationProgress());
        System.out.println();
        System.out.println("getMiningInfo: "+client.getMiningInfo());
        System.out.println("getBestBlockHash: "+client.getBestBlockHash());
        System.out.println("getChainInfo: "+client.getBlockChainInfo());
        System.out.println("getBlockHash: "+client.getBlockHash(0));
        System.out.println(client.getBlockChainInfo());
        */
        //{"id": 0, "method": "getblocktemplate", "params": [{"capabilities": ["coinbasetxn", "workid", "coinbase/append"]}]}



        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                System.out.println(response);
            } else {
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
