package Model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import Util.Util;

public class MerkleTree {


    String merkleRoot;

    List<String> txLst;


    public MerkleTree(List<String> txList) {
        this.txLst = txList;
        merkleRoot = "";
    }


    private List<String> getNodeHashList(List<String> tempTxList) {
        List<String> newTxList = new ArrayList<String>();
        int index = 0;
        while (index < tempTxList.size()) {
            // left
            String left = tempTxList.get(index);
            index++;
            // right
            String right = "";
            if (index != tempTxList.size()) {
                right = tempTxList.get(index);
            }
            String sha2HexValue = Util.sha256(left + right);
            sha2HexValue = Util.sha256(sha2HexValue);
            newTxList.add(sha2HexValue);
            index++;
        }
        return newTxList;
    }


    public void merkle_tree() {
        List<String> tempTxList = new ArrayList<String>();
        for (int i = 0; i < this.txLst.size(); i++) {
            tempTxList.add(this.txLst.get(i));
        }
        List<String> newTxList = getNodeHashList(tempTxList);

        while (newTxList.size() != 1) {
            newTxList = getNodeHashList(newTxList);
        }
        this.merkleRoot = newTxList.get(0);
    }


    public String getMerkleRoot() {
        return this.merkleRoot;
    }
}