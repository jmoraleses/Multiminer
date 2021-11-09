import org.json.JSONArray;
import org.json.JSONException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Util {

    //función de nombre toHex que convierte un número en 8 caracteres hexadecimales
    public static String numtoHex(int n) {
        return String.format("%08x", n);
    }

    //función de nombre toHex, convierte un String de tipo ascii en un String de tipo hexadecimal
    public static String asciiToHex(String ascii) {
        byte[] bytes = ascii.getBytes(StandardCharsets.US_ASCII);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    //función para encontrar el tamaño del scriptSig
    public static String toHex(long n) {
        return String.format("%02x", n);
    }

    //función que calcula VarInt de scriptPubKey
    public static String scriptPubKeyVarInt(String scriptPubKey) {
        int size = scriptPubKey.length() / 2;
        if (size < 0xfd) {
            return String.format("%02x", size);
        } else if (size <= 0xffff) {
            return String.format("%02x%02x", 0xfd, size);
        } else {
            return String.format("%02x%02x%02x", 0xfe, size, size);
        }
    }

    //Convertir los satoshis en hexadecimal
    public static String satoshisToHex(double satoshis) {
        return String.format("%016x", (long) satoshis);
    }


//    public static String onlyhex(String data) throws JSONException {
//        JSONArray jsonTransactions = new JSONArray(data);
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < jsonTransactions.length(); i++) {
//            //concatenar hashes
//
//        }
//    }



}
