package Model;

import Util.Util;
import org.bitcoinj.core.Base58;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

import static Core.ScryptHelp.printByteArray;

public class Miner {

    public static String seed = "seed42";
    public static String address;
    public static String privateKey;
    public static String scriptSig;
    public static String signature;
    //public static String sigHash;

    public static String publicKey = "03e7efaf51e64595f6db2980b49843da2e46fbec3e41f79ac8ef3bcf5308be02c1"; //example public key: testnet
    public static String pubKeyHash = Util.hash160(publicKey);
    public static String scriptPubKey = "76a914" + pubKeyHash + "88ac";

    public static final String phrase = "3000 /birds/ sing in the /tree/ of /life/, they say /pio/ /pio/ /pio/...";


    public static String show(){
        String output = "" +
                "ScriptSig = " + scriptSig + "\n" +
                "scriptPubKey = " + scriptPubKey + "\n" +
                //"sigHash = " + sigHash + "\n" +
                "signature = " + signature + "\n" +
                "address = " + address + "\n" +
                "publicKey = " + publicKey + "\n" +
                "PubKeyHash = " + pubKeyHash + "\n" +
                "privKey = " + privateKey + "\n";
        return output;
    }

    public static void generate(String seed) throws Exception {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");

        //KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        random.setSeed(seed.getBytes(StandardCharsets.UTF_8));

        //keypair from seed
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        keyGen.initialize(ecSpec, random);

        //key pair
        KeyPair kp = keyGen.generateKeyPair();
        PublicKey pub = kp.getPublic(); //publickey
        PrivateKey pvt = kp.getPrivate(); //privatekey
        //System.out.println("pub: "+printByteArray(pub.getEncoded()));
        System.out.println("private key: "+printByteArray(pvt.getEncoded()));
        //System.out.println("public key: "+printByteArray(pub.getEncoded()));
        privateKey = printByteArray(pvt.getEncoded());

        //clave privada: la que almacenan las wallets
        ECPrivateKey epvt = (ECPrivateKey) pvt;
        String sepvt = adjustTo64(epvt.getS().toString(16)).toLowerCase();
        //System.out.println("s[" + sepvt.length() + "]: " + sepvt); //clave privada

        //clave publica
        ECPublicKey epub = (ECPublicKey)pub;
        ECPoint pt = epub.getW();
        String sx = adjustTo64(pt.getAffineX().toString(16)).toLowerCase();
        String sy = adjustTo64(pt.getAffineY().toString(16)).toLowerCase();
        //extract s low value
        String s = sx + sy;
        //System.out.println("r: " + s); //clave publica
        String bcPub = "04" + sx + sy;
        //x y sy y clave publica
        //System.out.println("(r)x[" + sx.length() + "]: " + sx);
        //System.out.println("(s)y[" + sy.length() + "]: " + sy);
        System.out.println("bcPub: " + bcPub);
        //comprimir clave publica
        String bcPubCompress = null;
        if ( pt.getAffineY().bitLength() % 2 == 0){
            bcPubCompress = "02" + sx;
        }else{
            bcPubCompress = "03" + sx;
        }
        System.out.println("pubkey comprimida: " + bcPubCompress );
        publicKey = bcPubCompress; //clave pública

        //Hash160 de la clave pública: bcPub
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] s1 = sha.digest(bcPub.getBytes("UTF-8"));
        System.out.println("  sha: " + printByteArray(s1).toUpperCase());
        MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
        byte[] r1 = rmd.digest(s1);
        System.out.println("r1: "+printByteArray(r1)); //pubkeyhash?

        //agregamos byte al comienzo 0x00
        byte[] r2 = new byte[r1.length + 1];
        r2[0] = 0;
        for (int i = 0 ; i < r1.length ; i++) r2[i+1] = r1[i];
        System.out.println("  rmd: " + printByteArray(r2).toUpperCase());

        pubKeyHash = Util.hash160(publicKey);
        scriptPubKey = "76a914" + pubKeyHash + "88ac"; //scriptpubkey
        System.out.println("scriptPubKey: "+scriptPubKey);

        //sha256 dos veces
        byte[] s2 = sha.digest(r2);
        //System.out.println("  sha: " + printByteArray(s2).toUpperCase());
        byte[] s3 = sha.digest(s2);
        //System.out.println("  sha: " + printByteArray(s3).toUpperCase());

        byte[] a1 = new byte[25];
        for (int i = 0 ; i < r2.length ; i++) a1[i] = r2[i];
        for (int i = 0 ; i < 5 ; i++) a1[20 + i] = s3[i];
        //address = Base58.encode(a1);
        System.out.println("  adr: " + Base58.encode(a1)); //address
        address = Base58.encode(a1);

        //create signature from privatekey
        Signature signature_ = Signature.getInstance("SHA256withECDSA", "BC");
        signature_.initSign(pvt);
        signature_.update(bcPub.getBytes("UTF-8"));
        byte[] signatureBytes = signature_.sign();
        //System.out.println("signature: " + printByteArray(signatureBytes).toLowerCase());
        signature = printByteArray(signatureBytes); //signature
        System.out.println("signature: " + printByteArray(signatureBytes));
        //signature = "30440220" + sx  + "0220" + printByteArray(r1) + "01"; //scriptSig
        scriptSig = "47" + signature + "21" + publicKey;
        System.out.println("scriptSig: "+scriptSig);

    }

    //función adjustTo64
    public static String adjustTo64(String hex) {
        String hex64 = hex;
        if (hex64.length() % 2 != 0) {
            hex64 = "0" + hex64;
        }
        return hex64;
    }

    public static String getSeed() {
        return seed;
    }

    public static void setSeed(String seed) {
        Miner.seed = seed;
    }

    public static String getScriptSig() {
        return scriptSig;
    }

    public static void setScriptSig(String scriptSig) {
        Miner.scriptSig = scriptSig;
    }

    public static String getScriptPubKey() {
        return scriptPubKey;
    }

    public static void setScriptPubKey(String scriptPubKey) {
        Miner.scriptPubKey = scriptPubKey;
    }

    public static String getSignature() {
        return signature.toString();
    }

    public static void setSignature(String signature) {
        Miner.signature = signature;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Miner.address = address;
    }

}

