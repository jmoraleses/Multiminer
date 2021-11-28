/*
 * JOCL - Java bindings for OpenCL
 * 
 * Copyright 2009 Marco Hutter - http://www.jocl.org/
 */
package Core;

import static org.jocl.CL.*;

import Core.Sha256.Sha256;
import Util.Util;
import org.jocl.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * GPUhash
 *
 * @author Zhuowen Fang
 */
public class Sha256Helper {

    /**
     * The source code of the OpenCL program to execute
     */
    private static int[] n = new int[]{1, 5, 10, 50, 100, 200, 500, 1000, 2000, 3500, 5000, 8000, 10000, 12000,
        15000, 18000, 20000, 35000, 50000, 80000, 100000, 150000, 200000, 350000, 500000};
    private static int ite = 5;


    public static String sha256(String input) throws IOException {
        Sha256 sha256 = new Sha256();
        sha256.setData(input);
        sha256.init(2048, input.length());
        return sha256.crypt(input.length());

    }


}
