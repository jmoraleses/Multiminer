/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.Sha256;

import Core.Sha256Helper;
import Util.Util;
import org.jocl.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.jocl.CL.*;

/**
 * Call SHA256 kernel using JOCL
 * @author Zhuowen Fang
 */
public class Sha256 implements Runnable {

    private static final int SHA256_PLAINTEXT_LENGTH = 64; // 64 Bytes = 512 bits per block
    private static final int SHA256_BINARY_SIZE = 32;
    private static final int SHA256_RESULT_SIZE = 8; // 8 ints = 32 Bytes = 256 bits
    private static final int UINT_SIZE = 4;

    private char[] data;
    private int lth = 0;

    // The platform, device type and device number that will be used
    private static final int platformIndex = 0;
    private static final long deviceType = CL_DEVICE_TYPE_GPU;
    private static final int deviceIndex = 0;

    private static int kpc = 4;
    private String kernelCode = ""; // Kernel source code
    private long kernelLen; // Kernel length
    private cl_command_queue commandQueue;
    private char[] dataArray;
    private cl_mem dataMem, dataInfo, messageDigest;
    private cl_program program_;
    private cl_kernel kernel;
    private long[] global_work_size;
    private long[] local_work_size;
    private int[] datai = new int[3];
    private int[] result;
    private int[] device_info = new int[3];
    private String resultado;

    public static long nonce_max = 100000000000L;

    private static final long MEGABYTE = 1024L;
    private cl_context context;
    private cl_program program;

    private String input;
    private String target;
    private long startTime;
    private long nonce_ini;


    public Sha256(String input, String target, long startTime, long nonce_ini) throws IOException, InterruptedException {
        this.input = input;
        this.target = target;
        this.startTime = startTime;
        this.nonce_ini = nonce_ini;
    }


//    public void setData(String input) throws IOException {
//        lth = input.length();
//        data = input.toCharArray();
//    }


    public void init(int userKpc, int n, String input) throws IOException {

        lth = input.length();
        dataArray = input.toCharArray();

        // Perhaps for input length..?
        kpc = input.length();

//        dataArray = new char[data.length];
//        dataArray = new char[SHA256_PLAINTEXT_LENGTH * kpc];

        //System.out.println("File length: " + lth);
//        System.out.println(dataArray);
        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        //clGetDeviceInfo(device,CL_DEVICE_ADDRESS_BITS,12,Pointer.to(device_info),null);
        //System.out.println("Sizes: " + device_info[0]);

        // Create a context for the selected device

        context = clCreateContext( //cl_context
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue, with profiling info enabled
//        long properties = 0;
//        properties |= CL_QUEUE_PROFILING_ENABLE;
//        commandQueue = clCreateCommandQueue(
//                context, devices[0], properties, null);

        long properties = 0;
        properties |= CL_QUEUE_PROFILING_ENABLE;
        cl_queue_properties queueProperties = new cl_queue_properties();
        queueProperties.addProperty(CL_QUEUE_PROPERTIES, properties);
        commandQueue = clCreateCommandQueueWithProperties(context, devices[0], queueProperties, null);
//

        dataMem = new cl_mem();
        dataInfo = new cl_mem();
        messageDigest = new cl_mem();
        dataMem = clCreateBuffer(context, CL_MEM_READ_ONLY, (long) SHA256_PLAINTEXT_LENGTH * kpc,
                null, null);
        dataInfo = clCreateBuffer(context, CL_MEM_READ_ONLY, UINT_SIZE * 3, null, null);
        messageDigest = clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long) Sizeof.cl_uint * SHA256_RESULT_SIZE * n, null, null);

        // Load kernel code
        load();

//        kernel.setArgs(dataInfo, dataMem, messageDigest);

        // Create the program from the source code
        program = clCreateProgramWithSource(context,
                1, new String[]{kernelCode}, null, null);
        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        kernel = clCreateKernel(program, "sha256Kernel", null);
        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(dataInfo));
        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(dataMem));
        clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(messageDigest));

        // Set the work-item dimensions
        global_work_size = new long[]{n};
        local_work_size = new long[]{1}; //1

        result = new int[8 * n];
    }

    public String crypt(int n) {

        datai[0] = SHA256_PLAINTEXT_LENGTH;
        datai[1] = (int) global_work_size[0];
        datai[2] = lth;

        clEnqueueWriteBuffer(commandQueue, dataInfo, CL_TRUE, 0,
                UINT_SIZE * 3, Pointer.to(datai), 0, null, null);

        clEnqueueWriteBuffer(commandQueue, dataMem, CL_TRUE, 0,
                (long) SHA256_PLAINTEXT_LENGTH * kpc, Pointer.to(dataArray), 0, null, null);


        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate initial memory usage
        long startMem = runtime.totalMemory() - runtime.freeMemory();
//        System.out.println("Memory>  Total: " + runtime.totalMemory() + "  Free: " + runtime.freeMemory() + "  Used: " + startMem);
        // Get start time
        long startTime = System.nanoTime();

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(commandQueue, messageDigest, CL_TRUE, 0,
                (long) Sizeof.cl_uint * SHA256_RESULT_SIZE * n, Pointer.to(result), 0, null, null);

//        long stopTime = System.nanoTime();

        // Release kernel, program, and memory objects
        clReleaseMemObject(dataInfo);
        clReleaseMemObject(dataMem);
        clReleaseMemObject(messageDigest);
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);


        //StringBuilder builder = new StringBuilder();
//        System.out.print("Hash result: ");
        String hashResult = "";
        for (int i = 0; i < SHA256_RESULT_SIZE; i++) {
//            builder.append(String.format("%08x", result[i]));
            hashResult += String.format("%08x", result[i]);
//            System.out.printf("%08x", result[i]);
        }
//        System.out.println();


        // Get stop time
//        System.out.println("Runtime: " + (stopTime - startTime) + " nanosecond");
//
//        // Calculate final memory usage
//        long endMem = runtime.totalMemory() - runtime.freeMemory();
//        System.out.println("Memory>  Total: " + runtime.totalMemory() + "  Free: " + runtime.freeMemory() + "  Used: " + endMem);
//
//        System.out.println("Memory usage in bytes: " + (endMem - startMem));
//        long memMB = bytesToMegabytes(endMem - startMem);
//        System.out.println("Memory usage in megabytes: " + memMB);
//        double h = n * 1000000000.0 / (stopTime-startTime);
//        System.out.println(h + " Hashes/s");

//        Result rslt = new Result();
//        rslt.setResult(n, stopTime - startTime, endMem - startMem, memMB, h);
//        return builder.toString();
        return hashResult;
    }


    public void load() throws IOException {
        // Read kernel
        BufferedReader br = new BufferedReader(new FileReader("src/main/java/Core/Sha256/Sha256.cl")); //src/gpuhash/kernel/Sha256.cl
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (true) {
            line = br.readLine();
            if (line == null) {
                break;
            }
            sb.append(line).append("\n");
        }
        kernelCode = sb.toString();
        kernelLen = kernelCode.length();
    }

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }


    @Override
    public void run() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        System.out.println("POW " + dtf.format(now()));
        String difficulty = Util.getDifficulty(target);
//        String targetValue = String.valueOf(BigInteger.valueOf(1).shiftLeft((256 - difficulty.length())));
//        int target_count = Util.getDifficulty(target).length();//Initialize the nonce
        int nonce = (int) nonce_ini;
//        byte[] nonceMAX = new byte[4];
//        nonceMAX[0] = (byte)255;
//        nonce[0] = (byte)128; //empieza en la mitad de todos los nonce permitidos: 128
        int timewait = 600;
        String scrypted = null;
        byte[] hash = null;
        String hashing = "";

        while ((nonce - nonce_ini) < nonce_max && (System.currentTimeMillis() - startTime < timewait*1000)) {  // 10 minutes
            byte[] nonce_byte = Util.intToByteArray(nonce);
            hashing = input + Util.getHex(Util.littleEndianByte(nonce_byte));

            try {
                init(hashing.length(), 64, hashing);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            init(2048, 64, hashing);
//            sha256.setData(hashing);
            scrypted = crypt(64);
//            System.out.println(Util.getHex(Util.littleEndianByte(nonce_byte))+": "+scrypted + " target: " + target + " difficulty: " + difficulty);
            Sha256Helper.hashesSecond++;
            if (scrypted.endsWith(difficulty)){
                System.out.println("Found nonce: " + Util.getHex(Util.littleEndianByte(nonce_byte)) + " with hash: " + scrypted);
                Sha256Helper.thenonce = Util.getHex(Util.littleEndianByte(nonce_byte));
                Sha256Helper.thehash = scrypted;
                break;
            }
            else{
                nonce++;
//                ScryptHelper.incrementAtIndex(nonce, nonce.length - 1);

            }
        }

    }


}
