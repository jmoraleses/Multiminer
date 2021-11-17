package Model;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.params.KeyParameter;

import java.nio.charset.StandardCharsets;
import Util.Util;

import static Core.ScryptHelp.printByteArray;
import static com.google.common.primitives.Bytes.concat;

public class Miner {

    public static String seed = "seed3";
    public static String ScriptSig;
    public static String scriptPubKey;
    public static String sigHash;
    public static String signature;

    public static String address;
    public static String publicKey;
    public static String PubKeyHash;
    public static String privKey;

    public static final String phrase = "The tree of life 3000";


    public static String show(){
        String output = "" +
                "ScriptSig = " + ScriptSig + "\n" +
                "scriptPubKey = " + scriptPubKey + "\n" +
                "sigHash = " + sigHash + "\n" +
                "signature = " + signature + "\n" +
                "address = " + address + "\n" +
                "publicKey = " + publicKey + "\n" +
                "PubKeyHash = " + PubKeyHash + "\n" +
                "privKey = " + privKey + "\n";
        return output;
    }

    //generar P2PKH with ECKey
    public static void generatePublicAndSignature(String seed){
        byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8);
        byte[] pubkey = ECKey.fromPrivate(seedBytes).getPubKey();
        byte[] privkey = ECKey.fromPrivate(seedBytes).getPrivKeyBytes();
        byte[] pubkeyhash = ECKey.fromPrivate(seedBytes).getPubKeyHash();
        byte[] address_ = concat(pubkeyhash, new byte[]{0});

        publicKey = printByteArray(pubkey);
        privKey = printByteArray(privkey);
        PubKeyHash = printByteArray(pubkeyhash);
        address = printByteArray(address_);

//        System.out.println("PubKey: "+printByteArray(pubKey));
//        System.out.println("PubKeyHash: "+printByteArray(pubKeyHash));
//        System.out.println("PrivKey: "+printByteArray(privKey));
//        System.out.println("Address: "+Base58.encode(address));

        byte[] messageBytes = seed.getBytes(StandardCharsets.UTF_8); //firma
        byte[] privateKeyBytes = ECKey.fromPrivate(messageBytes).getPrivKeyBytes(); //private key
        ECKey key= new ECKey();
        KeyParameter keyparameter = new KeyParameter(privateKeyBytes);
        byte[] sighash = Sha256Hash.hash(messageBytes); //sighash = sha256(seed)
        ECKey.ECDSASignature signature_ = key.sign(Sha256Hash.wrap(sighash), keyparameter); //firma = (sighash + privatekey)
        String pubk = Util.hash160(printByteArray(privateKeyBytes)); //pubkey = hash160(privatekey)
        ScriptSig = printByteArray(signature_.encodeToDER()) + "01"; //script de desbloqueo
        scriptPubKey = pubk;
        sigHash = printByteArray(sighash);
        signature = printByteArray(signature_.encodeToDER());

//        System.out.println("*script de pila(firma): "+signature.r + " *script de canje(clave publica): " + signature.s);
//        System.out.println("*Sighash: "+printByteArray(sighash));
//        System.out.println("*signature: "+printByteArray(signature.encodeToDER()));
//        System.out.println("*pubkey: "+pubk);

    }



}

