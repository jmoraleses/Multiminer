package Model;

import Util.Util;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

import static Core.ScryptHelp.printByteArray;

public class Miner {

    public static String seed = "seed3";
    public static String scriptSig;
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
                "ScriptSig = " + scriptSig + "\n" +
                "scriptPubKey = " + scriptPubKey + "\n" +
                "sigHash = " + sigHash + "\n" +
                "signature = " + signature + "\n" +
                "address = " + address + "\n" +
                "publicKey = " + publicKey + "\n" +
                "PubKeyHash = " + PubKeyHash + "\n" +
                "privKey = " + privKey + "\n";
        return output;
    }


    public static void generate3(String seed) {
        scriptPubKey = "03779c54c2c8aa4deb4f606953204f4c3b734ac51d30cc1152a98ebb603b010a1b";
        scriptSig = "";
    }

//    public static void generate2(String nonce){
//        //firmar ECDSA con clave privada y nonce
//
//
//        ECKey key= new ECKey();
//        KeyParameter keyparameter = new KeyParameter(key.getPrivKeyBytes());
//
//        byte[] sighash = Sha256Hash.hash(nonce.getBytes(StandardCharsets.UTF_8)); //sighash = sha256(seed)
//        ECKey.ECDSASignature signature = key.sign(Sha256Hash.wrap(sighash), keyparameter); //firma = (sighash + privatekey)
//
//        System.out.println("signature (firma): " + printByteArray(signature.encodeToDER()));
//        System.out.println("signature (r): " + printByteArray(signature.r.toByteArray()));
//        System.out.println("signature (s): " + printByteArray(signature.s.toByteArray()));
//
//        String pubkey = "04" + printByteArray(key.getPubKey());
//        System.out.println("publicKey (clave pública): " + pubkey);
//        scriptSig = printByteArray(signature.encodeToDER()) + pubkey;
//
//        String pubKeyHash2 = "0014" + printByteArray(key.getPubKeyHash());
//        System.out.println("pubKeyHash (hash de la clave pública): " + pubKeyHash2);
//        scriptPubKey = pubKeyHash2;
//        //scriptPubKey = "03779c54c2c8aa4deb4f606953204f4c3b734ac51d30cc1152a98ebb603b010a1b";
//
//        System.out.println("scriptPubKey: "+scriptPubKey);
//        System.out.println("scriptSig: "+scriptSig);
//
//    }


    public static void generate(String seed) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {

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
        System.out.println("pub: "+pub);
        System.out.println("private key: "+printByteArray(pvt.getEncoded()));

        //clave privada: la que almacenan las wallets
        ECPrivateKey epvt = (ECPrivateKey)pvt;
        String sepvt = adjustTo64(epvt.getS().toString(16)).toLowerCase();
        System.out.println("s[" + sepvt.length() + "]: " + sepvt); //clave privada

        //clave publica
        ECPublicKey epub = (ECPublicKey)pub;
        ECPoint pt = epub.getW();
        String sx = adjustTo64(pt.getAffineX().toString(16)).toLowerCase();
        String sy = adjustTo64(pt.getAffineY().toString(16)).toLowerCase();
        String bcPub = "04" + sx + sy;
        System.out.println("bcPub: " + bcPub);
        //comprimir clave publica
        String bcPubCompress = null;
        if ( pt.getAffineY().bitLength() % 2 == 0){
            bcPubCompress = "02" + sx;
        }else{
            bcPubCompress = "03" + sx;
        }
        System.out.println("pubkey comprimida: " + "02" + sx );
        scriptPubKey = bcPubCompress; //clave pública

        //Hash160
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] s1 = sha.digest(bcPub.getBytes("UTF-8"));
//        System.out.println("  sha: " + printByteArray(s1).toUpperCase());

        MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
        byte[] r1 = rmd.digest(s1);

        //agregamos byte al comienzo 0x00
        byte[] r2 = new byte[r1.length + 1];
        r2[0] = 0;
        for (int i = 0 ; i < r1.length ; i++) r2[i+1] = r1[i];
//        System.out.println("  rmd: " + printByteArray(r2).toUpperCase());

        //sha256 dos veces
        byte[] s2 = sha.digest(r2);
//        System.out.println("  sha: " + printByteArray(s2).toUpperCase());
        byte[] s3 = sha.digest(s2);
//        System.out.println("  sha: " + printByteArray(s3).toUpperCase());

        byte[] a1 = new byte[25];
        for (int i = 0 ; i < r2.length ; i++) a1[i] = r2[i];
        for (int i = 0 ; i < 5 ; i++) a1[20 + i] = s3[i];

        System.out.println("  adr: " + Base58.encode(a1));

        //create signature from privatekey
        Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
        signature.initSign(pvt);
        signature.update(bcPub.getBytes("UTF-8"));
        byte[] signatureBytes = signature.sign();
        System.out.println("signature: " + printByteArray(signatureBytes).toLowerCase());
        setSignature(printByteArray(signatureBytes)); //signature
        scriptSig = "47" + signature + "01" + bcPubCompress;

    }

    static private String adjustTo64(String s) {
        switch(s.length()) {
            case 62: return "00" + s;
            case 63: return "0" + s;
            case 64: return s;
            default:
                throw new IllegalArgumentException("not a valid key: " + s);
        }
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
        return signature;
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

    public static String getPubKeyHash() {
        return PubKeyHash;
    }

    public static void setPubKeyHash(String pubKeyHash) {
        PubKeyHash = pubKeyHash;
    }
}

