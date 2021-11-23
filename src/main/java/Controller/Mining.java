package Controller;

import Core.Scrypt.Converter;
import Core.ScryptHelp;
import Model.Block;
import Model.MerkleTree;
import Util.Util;
import com.google.common.primitives.Bytes;
import com.lambdaworks.crypto.SCrypt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static Core.ScryptHelp.printByteArray;
import static java.time.LocalDateTime.now;

public class Mining {

    public static String target;
    public static double fee_transactions;
    public static double fee_for_mine = 10000;
    public static String fee_total;

    //public static int numThreads = Runtime.getRuntime().availableProcessors();


    public static Block mining(String response, long startTime) throws JSONException, IOException, InterruptedException, GeneralSecurityException {
        List<Object> list = extractInfoFromJson(response);
        if(response != null && !response.equals("")) {
            String nonce = Util.numtoHex(0); //esto hay que cambiarlo por la llamada al método personalizado de minería
            Block block = createBlock((String) list.get(0), (JSONArray) list.get(1), (String) list.get(2), (String) list.get(3), (String) list.get(4), (String) nonce); //String previousHash, String transactions, String bits, String nonce
            //if (block.getMerkleRoot() != "" && block.getMerkleRoot() != null) {
                //Buscamos el nonce
                //List<String> nonceHash = doSha256(Converter.fromHexString(block.showBlockWithoutNonce()), Util.getDifficulty(block.getTarget()), startTime);
                List<String> nonceHash = doScrypt(Converter.fromHexString(block.showBlockWithoutNonce()), block.getTarget(), startTime);

                if (nonceHash != null) {
                    block.setNonce(nonceHash.get(0));
                    block.setBlockhash(nonceHash.get(1));
                    return block;
                }
                return null;
           //}
        }
        return null;

    }

    //crear un bloque de minado correcto según el BIP22
    public static Block createBlock(String previousHash, JSONArray transactions, String bits, String height, String target, String nonce) throws JSONException, IOException {
        Block block = new Block();
        block.setPreviousHash(previousHash);
        block.setNonce(nonce);
        block.setTimestamp(String.valueOf(System.currentTimeMillis()));
        block.setVersion(Util.numtoHex(1));
        block.setBits(bits);
        String zeros = "";
        for(int i=0; i < Util.countLeadingZeros(target); i++){
            zeros += "0";
        }
        block.setDifficulty(zeros);
        block.setTransactions(transactions);
        block.setMerkleRoot(extractMerkleRoot(transactions));
        block.setFee(fee_total);
        block.setHeight(height);
        block.setTarget(target);
        return block;
    }

    //Extraer de un json en formato string las siguientes variables String previousHash, String data, String nonce
    public static List<Object> extractInfoFromJson(String response) {
        String previousHash = "";
        String bits = "";
        String height = "";
        String target = "";
        try {
            if (!Objects.equals(response, "")) {
                JSONObject jsonObject = new JSONObject(response);
                String result = jsonObject.getString("result");
                JSONObject jsonResult = new JSONObject(result);
                bits = jsonResult.getString("bits");
                previousHash = jsonResult.getString("previousblockhash");
                JSONArray transactions = jsonResult.getJSONArray("transactions");
                height = jsonResult.getString("height");
                target = jsonResult.getString("target");
                //Scrypt.noncerange = jsonResult.getString("noncerange");
                List<Object> list = new ArrayList<>();
                list.add(previousHash);
                list.add(transactions);
                list.add(bits);
                list.add(height);
                list.add(target);
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //calcular el merkleroot a partir  de una array de transacciones
    public static String extractMerkleRoot(JSONArray transactions) throws JSONException {
        fee_transactions = 0;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < transactions.length(); i++) {
            JSONObject jsonObjectTransaction = transactions.getJSONObject(i);
            list.add(Util.littleEndian(jsonObjectTransaction.getString("txid")));
            fee_transactions += Long.parseLong(jsonObjectTransaction.getString("fee"));
        }
        fee_total = Util.satoshisToHex((fee_for_mine*100000000) + fee_transactions);
        if (list.size() == 0) return "";
        //else {
//            MerkleTree merkle = new MerkleTree(list);
//            merkle.merkle_tree();
//            return Util.littleEndian(merkle.getMerkleRoot()); //
            return (Util.calculateMerkleRoot(list)); //
        //}
    }


    public static String lastHashMerkleRoot(String merkleroot, String blockhash){
        String str = merkleroot + blockhash;
        return Util.littleEndian(Util.hash256(str));
    }

    //Búsqueda de nonce para algoritmo Scrypt
    public static List<String> doScrypt(byte[] databyte, String target, long startTime) throws GeneralSecurityException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String difficulty = Util.getDifficulty(target);
        System.out.println("Buscando para Scrypt " + dtf.format(now()) );
        List<String> lista = new ArrayList<>();
        //Initialize the nonce
        byte[] nonce = new byte[4];
        byte[] nonceMAX = new byte[4];
        nonceMAX[0] = (byte)255;
        nonceMAX[1] = (byte)255;
        nonceMAX[2] = (byte)255;
        nonceMAX[3] = (byte)255;
        nonce[0] = (byte)26; //empieza en la mitad de todos los nonce permitidos: 128
        //boolean found = false;

        //Loop over and increment nonce
//        while(nonce[0] != nonceMAX[0] && (System.currentTimeMillis() - startTime < 60*1000)){ //1 minute in dogecoin
        while(nonce[0] != nonceMAX[0]){ //1 minute in dogecoin
            byte[] hash = Bytes.concat(databyte, Util.littleEndianByte(nonce));
            String scrypted = printByteArray(SCrypt.scryptJ(hash, hash, 1024, 1, 1, 32));
            //System.out.println(printByteArray(nonce)+": "+scrypted + " target: " + target);

//            if ((scrypted.endsWith(difficulty)) && (scrypted.compareTo(target)<0 || scrypted.compareTo(target)==0) ) {
//          if ((scrypted.endsWith(difficulty)) && (new BigInteger(Util.littleEndian(scrypted), 16)).compareTo(new BigInteger(target, 16))<0 ) {  //! // || scrypted.endsWith(difficulty)
            if ((scrypted.endsWith(difficulty)) && (scrypted.compareTo(target)<0) ) {  //! // || scrypted.endsWith(difficulty)


                System.out.println(printByteArray(nonce)+": "+scrypted);
                lista.add(printByteArray(nonce));
                lista.add(scrypted);
                return lista;
            }
            else{
                ScryptHelp.incrementAtIndex(nonce, nonce.length-1); //Otherwise increment the nonce
            }
            //System.out.println(printByteArray(nonce));
        }
        return null;
    }

    //Búsqueda de nonce para algoritmo SHA256
    public static List<String> doSha256(byte[] databyte, String target, long startTime) throws GeneralSecurityException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String difficulty = Util.getDifficulty(target);
        System.out.println("Buscando para Sha256" + dtf.format(now()));
        List<String> lista = new ArrayList<>();
        //Initialize the nonce
        byte[] nonce = new byte[4];
        byte[] nonceMAX = new byte[4];
        nonceMAX[0] = (byte)255;
        nonceMAX[1] = (byte)255;
        nonceMAX[2] = (byte)255;
        nonceMAX[3] = (byte)255;
        nonce[0] = (byte)24; //empieza en la mitad de todos los nonce permitidos: 128
        //boolean found = false;

        //Loop over and increment nonce
        while(nonce[0] != nonceMAX[0] && (System.currentTimeMillis() - startTime < 600*1000)){ //10 minutes
            byte[] hash = Bytes.concat(databyte, Util.littleEndianByte(nonce));
            String scrypted = Util.sha256(Arrays.toString(Util.littleEndianByte(hash)));

            System.out.println(printByteArray(nonce)+": "+scrypted + " target: " + target);
            if ((scrypted.startsWith(difficulty)) && (scrypted.compareTo(target)<0 || scrypted.compareTo(target)==0) ) {  //!
                System.out.println(printByteArray(nonce)+": "+scrypted);
                lista.add(printByteArray(nonce));
                lista.add(scrypted);
                return lista;
            }
            else{
                ScryptHelp.incrementAtIndex(nonce, nonce.length-1); //Otherwise increment the nonce
            }
            //System.out.println(printByteArray(nonce));
        }
        return null;
    }

    public static List<String> POW (byte[] databyte, String target, long startTime) throws GeneralSecurityException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String difficulty = Util.getDifficulty(target);
        System.out.println("POW" + dtf.format(now()));
        List<String> lista = new ArrayList<>();
        int target_count = Util.getDifficulty(target).length();

        for (int nonce = 0; (System.currentTimeMillis() - startTime < 55*1000); nonce++) {  // 55 seconds
            String scrypted = Bytes.concat(databyte, Util.littleEndianByte(Util.numtoHex(nonce).getBytes(StandardCharsets.UTF_8))).toString();
            //scrypted = Util.blockHashByte(scrypted.getBytes(StandardCharsets.UTF_8));
            //scrypted = printByteArray(SCrypt.scryptJ(scrypted.getBytes(StandardCharsets.UTF_8), scrypted.getBytes(StandardCharsets.UTF_8), 1024, 1, 1, 32));
            if (scrypted.startsWith(difficulty)) {
                System.out.println(Util.numtoHex(nonce)+": "+scrypted + "" + target);
                lista.add(Util.numtoHex(nonce));
                lista.add(scrypted);
                return lista;
            }
        }
        return null;
    }

}
