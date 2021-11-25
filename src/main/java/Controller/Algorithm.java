package Controller;

import Core.Scrypt.Converter;
import Core.ScryptHelp;
import Util.Util;
import com.google.common.primitives.Bytes;
import com.lambdaworks.crypto.SCrypt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static Core.ScryptHelp.printByteArray;
import static java.time.LocalDateTime.now;

public class Algorithm {


    public static List<String> POW (byte[] databyte, String target, long startTime, String algorithm) throws GeneralSecurityException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String difficulty = Util.getDifficulty(target);
        BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - difficulty.length()));
        System.out.println("POW " + dtf.format(now()));
        List<String> lista = new ArrayList<>();
        int target_count = Util.getDifficulty(target).length();//Initialize the nonce
        byte[] nonce = new byte[4];
        byte[] nonceMAX = new byte[4];
        nonceMAX[0] = (byte)255;
        nonceMAX[1] = (byte)255;
        nonceMAX[2] = (byte)255;
        nonceMAX[3] = (byte)255;
        nonce[0] = (byte)26; //empieza en la mitad de todos los nonce permitidos: 128
        int timewait = 0;
        switch (algorithm){
            case "sha256":
                timewait = 600;
                break;
            case "equihash":
                timewait = 150;
                break;
            case "scrypt":
                timewait = 60;
                break;
            case "sha3":
                timewait = 60;
                break;
        }
        while (nonce[0] != nonceMAX[0] && (System.currentTimeMillis() - startTime < timewait*1000)) {  // 1 minute
            String scrypted = null;

            switch (algorithm){
                case "sha256":
                    scrypted = Bytes.concat(databyte, Util.littleEndianByte(nonce)).toString();
                    scrypted = Util.blockHashByte(scrypted.getBytes(StandardCharsets.UTF_8));
                    break;
                case "equihash":
                    byte[] hash = Bytes.concat(databyte, Util.littleEndianByte(nonce));
                    scrypted = Util.blake2AsU8a(hash).toString();
                    break;
                case "scrypt":
                    scrypted = Bytes.concat(databyte, Util.littleEndianByte(nonce)).toString();
                    scrypted = printByteArray(SCrypt.scryptJ(scrypted.getBytes(StandardCharsets.UTF_8), scrypted.getBytes(StandardCharsets.UTF_8), 1024, 1, 1, 32));
                    break;
                case "sha3":
                    scrypted = Bytes.concat(databyte, Util.littleEndianByte(nonce)).toString();
                    scrypted = Util.sha3(scrypted.getBytes(StandardCharsets.UTF_8)).toString();
                    break;
            }
            //System.out.println(printByteArray(nonce)+": "+scrypted + " target: " + target);

            if (scrypted.startsWith(difficulty)){ //!
                if (new BigInteger(scrypted, 16).compareTo(targetValue) <= 0) {
                    System.out.println("Found nonce: " + printByteArray(Util.littleEndianByte(nonce)) + " with hash: " + scrypted);
                    lista.add(printByteArray(nonce));
                    lista.add(scrypted);
                    return lista;
                }
            }
            else{
                ScryptHelp.incrementAtIndex(nonce, nonce.length-1);
            }
        }
        return null;
    }



}
