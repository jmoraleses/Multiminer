package Model;

import Core.Base58;
import Util.Util;
import org.bitcoinj.core.ECKey;

import java.nio.charset.StandardCharsets;

import static Core.ScryptHelp.printByteArray;

public class Miner {


    public static final String seed = "seed3";
    public static final String publicKey = generatePublicKey(seed);
    public static final String privateKey = generatePrivateKey(seed);
    public static final String address = generateAddress(privateKey);
    public static final String phrase = "The tree of life 3000";


    //generar P2PKH aleatoriamente con spongycastle
    public static String generatePrivateKey(String seed){
        byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8);
        byte[] privKey = ECKey.fromPrivate(seedBytes).getPrivKeyBytes();
        return printByteArray(privKey);
    }

    //generar P2PKH aleatoriamente con spongycastle
    public static String generatePublicKey(String seed){
        byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8);
        byte[] pubKey = ECKey.fromPrivate(seedBytes).getPubKeyHash();
        return printByteArray(pubKey);
    }

    //generar address from private key
    public static String generateAddress(String privateKey){
        byte[] privateKeyBytes = Util.hexStringToByteArray(privateKey);
        byte[] pubKey = ECKey.fromPrivate(privateKeyBytes).getPubKeyHash();
        return Base58.encode(pubKey);
    }

}

