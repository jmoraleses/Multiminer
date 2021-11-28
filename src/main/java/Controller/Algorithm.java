package Controller;

import Core.ScryptHelper;
import Core.Sha256Helper;
import Util.Util;
import com.google.common.primitives.Bytes;
import com.lambdaworks.crypto.SCrypt;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static Core.ScryptHelper.printByteArray;
import static java.time.LocalDateTime.now;

public class Algorithm {


    public static List<String> POW (byte[] databyte, String target, long startTime, String algorithm) throws GeneralSecurityException, IOException {
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
        nonce[0] = (byte)128; //empieza en la mitad de todos los nonce permitidos: 128
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
            byte[] hash = Bytes.concat(databyte, Util.littleEndianByte(nonce));
            switch (algorithm){
                case "sha256":
                    scrypted = Sha256Helper.sha256(Sha256Helper.sha256(hash.toString())).toString();
//                    scrypted = Util.SHA256(Util.SHA256(hash)).toString();
                    break;
                case "equihash":
                    scrypted = Util.blake2AsU8a(hash).toString();
                    break;
                case "scrypt":
                    scrypted = printByteArray(SCrypt.scryptJ(hash, hash, 1024, 1, 1, 32));
                    break;
                case "sha3":
                    scrypted = Util.sha3(hash).toString();
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
                ScryptHelper.incrementAtIndex(nonce, nonce.length-1);
            }
        }
        return null;
    }



}
