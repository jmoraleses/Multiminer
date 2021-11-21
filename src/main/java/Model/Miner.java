package Model;

import org.bitcoinj.core.Base58;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import Util.Util;

import static Core.ScryptHelp.printByteArray;

public class Miner {

    public static String seed = "seed3";
    public static String scriptSig;
    public static String scriptPubKey;
//    public static String sigHash;
    public static String signature;

    public static String address = getPubKeyHash("ne1Cqt1kz64w3hk8iGJq9t7JcGihcEDYqv");
    public static String publicKey;
    public static String pubKeyHash;
    public static String privKey;

    public static final String phrase = "3000 /birds/ sing in the /tree/ of /life/, they say /pio/ /pio/ /pio/...";


    public static String show(){
        String output = "" +
                "ScriptSig = " + scriptSig + "\n" +
                "scriptPubKey = " + scriptPubKey + "\n" +
//                "sigHash = " + sigHash + "\n" +
                "signature = " + signature + "\n" +
                "address = " + address + "\n" +
                "publicKey = " + publicKey + "\n" +
                "PubKeyHash = " + pubKeyHash + "\n" +
                "privKey = " + privKey + "\n";
        return output;
    }

    public Miner() throws Exception {
        generate(seed);
    }

//    //calculate txid of the coinbase transaction
//    public static String calculateCoinbaseTxid(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
//        return Base58.encode(hash);
//    }
//
//    public static void generate1(String seed) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException, UnsupportedEncodingException, InvalidAlgorithmParameterException, SignatureException {
//        //calcular la private key desde seed
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
//        //KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
//        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
//        random.setSeed(seed.getBytes(StandardCharsets.UTF_8));
//        //keypair from seed
//        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
//        keyGen.initialize(ecSpec, random);
//        //key pair
//        KeyPair kp = keyGen.generateKeyPair();
//        PublicKey pub = kp.getPublic(); //publickey
//        PrivateKey pvt = kp.getPrivate(); //privatekey
//
//        privKey = printByteArray(pvt.getEncoded());
//
//
//        //calcular public key from private key
//        //comprimir clave publica
//        ECPublicKey epub = (ECPublicKey)pub;
//        ECPoint pt = epub.getW();
//        String sx = adjustTo64(pt.getAffineX().toString(16)).toLowerCase();
//        String sy = adjustTo64(pt.getAffineY().toString(16)).toLowerCase();
//        String bcPubCompress = null;
//        if ( pt.getAffineY().bitLength() % 2 == 0){
//            bcPubCompress = "02" + sx;
//        }else{
//            bcPubCompress = "03" + sx;
//        }
//        publicKey = bcPubCompress;
//        System.out.println("publicKey: "+publicKey);
//
//        //calcular el hash160 desde la public key
//        pubKeyHash = calculatePubKeyHash(publicKey);
//        System.out.println("pubKeyHash: "+pubKeyHash);
//
//        //calcular signature desde la private key
//        signature = calculateSignature(privKey);
//        System.out.println("signature: "+signature);
//
//        //calcular la address desde el hash160
//        address = calculateAddress(pubKeyHash);
//        System.out.println("address: "+address);
//
////        scriptPubKey = calculateScriptPubKey(pubKeyHash);
//
//        scriptPubKey = "76a914" + pubKeyHash + "88ac"; //scriptpubkey
//        System.out.println("scriptPubKey: "+scriptPubKey);
//
//        //calcular scriptSig
//        scriptSig = "47" + signature + "01" + publicKey;
//        System.out.println("scriptSig: "+scriptSig);
//
////        System.out.println(verifyScriptPubKey(pubKeyHash));
////        System.out.println(verifySignature(publicKey, signature, scriptSig));
//
//    }
//
//
//
//    //verificar scriptpubkey
//    public static boolean verifyScriptPubKey(String scriptPubKey) throws UnsupportedEncodingException {
//        String pubKeyHash = scriptPubKey.substring(8, 24);
//        String address = calculateAddress(pubKeyHash);
//        return address.equals(scriptPubKey.substring(2, 42));
//    }
//
//    //calculate scriptPubKeyHash from scriptPubKey
//    public static String calculateScriptPubKeyHash(String scriptPubKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hash = md.digest(scriptPubKey.getBytes(StandardCharsets.UTF_8));
//        return Base58.encode(hash);
//    }
//
//    //función calcular scriptSigHash
//    public static String calculateScriptSigHash(String scriptSig) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hash = md.digest(scriptSig.getBytes(StandardCharsets.UTF_8));
//        return Base58.encode(hash);
//    }
//
//    //Función calcular signature
//    public static String calculateSignature(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException, SignatureException {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        //private key
//        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(hexToByteArray(privateKey));
//        KeyFactory keyFactory = KeyFactory.getInstance("EC");
//        PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);
//
//        //data
//        byte[] dataBytes = seed.getBytes(StandardCharsets.UTF_8);
//
//        //signature
//        Signature sig = Signature.getInstance("SHA256withECDSA");
//        sig.initSign(privKey);
//        sig.update(dataBytes);
//        return printByteArray(sig.sign());
//    }
//
//    //Fución que dado la public key calcula el hash160 formateado
//    public static String calculatePubKeyHash(String publicKey) {
//        MessageDigest digest = null;
//        try {
//            digest = MessageDigest.getInstance("SHA-256");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        byte[] hash = digest.digest(publicKey.getBytes());
//        return printByteArray(hash);
//    }
//
//
//    //firmar el hash160
//    private static byte[] sign(byte[] hash, String privKey) {
//        try {
//            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
//            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base58.decode(privKey)));
//            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
//            ecdsaSign.initSign(privateKey);
//            ecdsaSign.update(hash);
//            return ecdsaSign.sign();
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    //Función convertir hex a byte array
//    public static byte[] hexToByteArray(String hex) {
//        if (hex.length() % 2 == 1) {
//            throw new IllegalArgumentException();
//        }
//        byte[] bytes = new byte[hex.length() / 2];
//        for (int i = 0; i < hex.length(); i += 2) {
//            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
//            bytes[(i / 2)] = value;
//        }
//        return bytes;
//    }
//
//    //Función calcular scriptPubKey
//    public static String calculateScriptPubKey(String pubKeyHash) {
//        String scriptPubKey = "76a914" + pubKeyHash + "88ac";
//        return scriptPubKey;
//    }
//
//
//    //convertir base58 to Hex
//    public static String base58ToHex(String base58) {
//        byte[] decode = Base58.decode(base58);
//        return printByteArray(decode);
//    }
//
//    //convertir hexadecimal a base58
//    public static String hexToBase58(String hex) {
//        return Base58.encode(hexToByteArray(hex));
//    }
//
//
//    //Función que dado el hash160 calcula la address
//    public static String calculateAddress(String pubKeyHash) {
//        String address = "";
//        String version = "00";
//        String checksum = calculateChecksum(version + pubKeyHash);
//        address = version + pubKeyHash + checksum;
//        return address;
//    }
//
//    private static String calculateChecksum(String s) {
//        byte[] hash = new byte[32];
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            hash = digest.digest(s.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return printByteArray(hash);
//    }

    //****************************************************************************************************************

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
//        System.out.println("pub: "+printByteArray(pub.getEncoded()));
//        System.out.println("private key: "+printByteArray(pvt.getEncoded()));
//        System.out.println("public key: "+printByteArray(pub.getEncoded()));
        privKey = printByteArray(pvt.getEncoded());
        publicKey = printByteArray(pub.getEncoded());;

        //clave privada: la que almacenan las wallets
        ECPrivateKey epvt = (ECPrivateKey) pvt;
        String sepvt = adjustTo64(epvt.getS().toString(16)).toLowerCase();
//        System.out.println("s[" + sepvt.length() + "]: " + sepvt); //clave privada


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
//        System.out.println("(r)x[" + sx.length() + "]: " + sx);
//        System.out.println("(s)y[" + sy.length() + "]: " + sy);
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

        //create signature from privatekey
        Signature signature_ = Signature.getInstance("SHA256withECDSA", "BC");
        signature_.initSign(pvt);
        signature_.update(bcPub.getBytes("UTF-8"));
        byte[] signatureBytes = signature_.sign();
//        System.out.println("signature: " + printByteArray(signatureBytes).toLowerCase());
        signature = printByteArray(signatureBytes); //signature
        System.out.println("signature: " + printByteArray(signatureBytes));
//        signature = "47" + printByteArray(signatureBytes) + "01" ;
    //    signature = "30440220" + sx  + "0220" + printByteArray(r1) + "01"; //scriptSig
//        scriptSig = "47" + printByteArray(signatureBytes) + "01" + "02" + bcPub; //scriptSig
//        scriptSig = "47" + signature +  Util.toHex(bcPubCompress.length()) + bcPubCompress;
//        scriptSig = "47" + signature  + bcPubCompress;
        scriptSig = "47" + signature + "21" + publicKey;
        System.out.println("scriptSig: "+scriptSig);

        System.out.println(getPubKeyHash("ne1Cqt1kz64w3hk8iGJq9t7JcGihcEDYqv"));
    }

    //función adjustTo64
    public static String adjustTo64(String hex) {
        String hex64 = hex;
        if (hex64.length() % 2 != 0) {
            hex64 = "0" + hex64;
        }
        return hex64;
    }



    //adress to pubkeyhash in dogecoin
    public static String getPubKeyHash(String address) {
        String pubKeyHash = null;
        try {
            byte[] addressBytes = Base58.decode(address);
            byte[] pubKeyHashBytes = new byte[21];
            for (int i = 0 ; i < 21 ; i++) pubKeyHashBytes[i] = addressBytes[i];
            pubKeyHash = printByteArray(pubKeyHashBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKeyHash;
    }


//    public static String adjustTo64(String printByteArray) {
//        byte[] bytes = new byte[64];
//        byte[] byteArray = printByteArray.getBytes();
//        for (int i = 0; i < byteArray.length; i++) {
//            bytes[i] = byteArray[i];
//        }
//        return printByteArray(bytes);
//    }

    //////////////////////////
////        KeyPairGenerator kg = KeyPairGenerator.getInstance ("EC");
//        keyGen.initialize (new ECGenParameterSpec ("secp256k1"));
//        ECParameterSpec p = ((ECPublicKey) keyGen.generateKeyPair().getPublic()).getParams();
//        System.out.println ("p=(dec)" + ((ECFieldFp) p.getCurve().getField()).getP() );
//        ECPoint G = p.getGenerator();
//        System.out.format ("Gx=(hex)%032x%n", G.getAffineX());
//        System.out.format ("Gy=(hex)%032x%n", G.getAffineY());
//        byte[] privatekey_enc = DatatypeConverter.parseHexBinary(printByteArray(pvt.getEncoded())); //clave privada encriptada
//        // note fixed prefix for PKCS8-EC-secp256k1 plus your private value
//        KeyFactory kf = KeyFactory.getInstance("EC");
//        PrivateKey k1 = kf.generatePrivate(new PKCS8EncodedKeySpec(privatekey_enc));
//        ECParameterSpec p2 = ((ECPrivateKey) k1).getParams();
//        System.out.println ("again p=(dec)" + ((ECFieldFp) p2.getCurve().getField()).getP() );
    //////////////////////////
    //calcular n  = x - y
//        BigInteger n = epub.getW().getAffineX().subtract(epub.getW().getAffineY());
//        System.out.println("n: "+n);
    //////////////////////////

//    static private String adjustTo64(String s) {
//        switch(s.length()) {
//            case 62: return "00" + s;
//            case 63: return "0" + s;
//            case 64: return s;
//            default:
//                throw new IllegalArgumentException("not a valid key: " + s);
//        }
//    }

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

