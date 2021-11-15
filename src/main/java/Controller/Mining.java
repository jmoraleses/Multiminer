package Controller;

import Core.Scrypt.Converter;
import Core.ScryptHelp;
import Model.Block;
import Util.Util;
import com.lambdaworks.crypto.SCrypt;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import com.google.common.primitives.Bytes;

import static Core.ScryptHelp.printByteArray;

public class Mining {

    public static String target;
    public static double fee_transactions;
    public static double fee_for_mine = 10000;
    public static String fee_total;

    public static String blockhash = "";

    public static int numThreads = Runtime.getRuntime().availableProcessors();

    

    public static Block mining(String response) throws JSONException, IOException, InterruptedException, GeneralSecurityException {
        List<Object> list = extractInfoFromJson(response);
        String nonce = Util.numtoHex(0); //esto hay que cambiarlo por la llamada al método personalizado de minería
        Block block = createBlock((String)list.get(0), (JSONArray)list.get(1), (String)list.get(2), (String)list.get(3), (String)list.get(4), (String)nonce); //String previousHash, String transactions, String bits, String nonce

        //buscamos el verdadero nounce
        nonce = doSha256(Converter.fromHexString(block.showBlock()), Util.getDifficulty(block.getTarget()));
        //nonce = doScrypt(Converter.fromHexString(block.showBlock()), Util.getDifficulty(block.getTarget()));

        //if (nonce == null) return null;

        block.setNonce(nonce);
        block.setBlockHash(blockhash);
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
        //block.setHash(mineBlock(block, nonce));
        block.setTransactions(transactions);
        block.setMerkleRoot(extractMerkleRoot(transactions));
        block.setFee(fee_total);
        //block.setBlockHash(Scrypt.scrypt(Scrypt.showBlock(block)));
        block.setBlockHash(blockhash);
        block.setHeight(height);
        block.setTarget(target);
        return block;
    }

    //calcular el merkleroot a partir  de una array de transacciones
    public static String extractMerkleRoot(JSONArray transactions) throws JSONException {
        fee_transactions = 0;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < transactions.length(); i++) {
            JSONObject jsonObjectTransaction = transactions.getJSONObject(i);
            list.add(jsonObjectTransaction.getString("hash"));
            fee_transactions += Long.parseLong(jsonObjectTransaction.getString("fee"));
        }
        fee_total = Util.satoshisToHex((fee_for_mine*100000000) + fee_transactions);
        if (list.size() == 0) return "";
        else return calculateMerkleRoot(list);
    }

    //calcute merkle root from a list of transactions
    public static String calculateMerkleRoot(List<String> data){
        String merkleRoot = "";
        if (data.size() == 1){
            merkleRoot = data.get(0);
        }else {
            List<String> newData = new ArrayList<>();
            for (int i = 0; i < data.size() -1; i = i + 2){
                String str = data.get(i) + data.get(i + 1);
                newData.add(org.apache.commons.codec.digest.DigestUtils.sha256Hex(str));
            }
            merkleRoot = calculateMerkleRoot(newData);
        }
        return merkleRoot;
    }

    //Búsqueda de nonce para algoritmo Scrypt
    public static String doScrypt(byte[] databyte, String target) throws GeneralSecurityException {
        System.out.println("Buscando para Scrypt");
        //Initialize the nonce
        byte[] nonce = new byte[4];
        nonce[0] = databyte[76] ;
        nonce[1] = databyte[77] ;
        nonce[2] = databyte[78] ;
        nonce[3] = databyte[79] ;
        boolean found = false;

        //Loop over and increment nonce
        while(!found){
            //Set the bytes of the data to the nonce
            databyte[76] = nonce[0];
            databyte[77] = nonce[1];
            databyte[78] = nonce[2];
            databyte[79] = nonce[3];

            byte[] scrypted = (SCrypt.scryptJ(databyte,databyte, 1024, 1, 1, 32));//Scrypt the data with proper params
            //System.out.println(printByteArray(nonce)+": "+printByteArray(scrypted));

            if (printByteArray(scrypted).startsWith(target)) {  //!
                System.out.println(printByteArray(nonce)+": "+printByteArray(scrypted));
                blockhash = printByteArray(scrypted);
                return printByteArray(nonce);
            }
            else{
                ScryptHelp.incrementAtIndex(nonce, nonce.length-1); //Otherwise increment the nonce
            }
            //System.out.println(printByteArray(nonce));
        }
        return null;
    }


    //Búsqueda de nonce para algoritmo Scrypt
    public static String doSha256(byte[] databyte, String target) throws GeneralSecurityException {
        System.out.println("Buscando para Sha256");
        //Initialize the nonce
        byte[] nonce = new byte[4];
//        nonce[0] = databyte[76] ;
//        nonce[1] = databyte[77] ;
//        nonce[2] = databyte[78] ;
//        nonce[3] = databyte[79] ;

        byte[] nonceMAX = new byte[4];
        nonceMAX[0] = (byte)255;
        nonceMAX[1] = (byte)255;
        nonceMAX[2] = (byte)255;
        nonceMAX[3] = (byte)255;
        //System.out.println("nonceMAX: "+printByteArray(nonceMAX));

        nonce[0] = (byte)128;
        boolean found = false;
        //Loop over and increment nonce
        while(nonce[0] < nonceMAX[0]){
            //Set the bytes of the data to the nonce
//            databyte[76] = nonce[0];
//            databyte[77] = nonce[1];
//            databyte[78] = nonce[2];
//            databyte[79] = nonce[3];


            byte[] hash = Bytes.concat(databyte, nonce);
            String scrypted = Util.blockHashByte(hash);
            //System.out.println(printByteArray(nonce)+": "+scrypted+" - "+target);

            if (scrypted.startsWith(target)) {  //!
                System.out.println(printByteArray(nonce)+": "+scrypted);
                blockhash = scrypted;
                return printByteArray(nonce);
            }
            else{
                ScryptHelp.incrementAtIndex(nonce, nonce.length-1); //Otherwise increment the nonce
            }
            //System.out.println(printByteArray(nonce));
        }
        return printByteArray("00000000".getBytes(StandardCharsets.UTF_8));
    }


    public static String charToHex(){
        String nonce = "";
        String caracteresHex = "0123456789abcdef";
        int min = 0;
        int max = 0;
        //Crear números aleaotorios sin posibilidad de repetirse
        int randomNum1 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum2 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum3 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum4 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum5 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum6 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum7 = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomNum8 = ThreadLocalRandom.current().nextInt(min, max + 1);

        nonce = String.valueOf(caracteresHex.charAt(randomNum1 % 16)) + String.valueOf(caracteresHex.charAt(randomNum2 % 16)) + String.valueOf(caracteresHex.charAt(randomNum3 % 16)) + String.valueOf(caracteresHex.charAt(randomNum4 % 16)) + String.valueOf(caracteresHex.charAt(randomNum5 % 16)) + String.valueOf(caracteresHex.charAt(randomNum6 % 16)) + String.valueOf(caracteresHex.charAt(randomNum7 % 16)) + String.valueOf(caracteresHex.charAt(randomNum8 % 16));
        return nonce;
    }


}
