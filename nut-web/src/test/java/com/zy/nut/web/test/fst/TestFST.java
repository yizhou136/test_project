package com.zy.nut.web.test.fst;

import com.zy.nut.web.test.kryo.B;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTOutputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zhougb on 2017/3/2.
 */
public class TestFST {
    static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    public static void testNormal() throws IOException {
        String methodName = getCurrentMethodName();
        String file = String.format("%s.fst.bin", methodName);
        //FSTObjectOutput outputStream = conf.getObjectOutput(new FileOutputStream(file));
        B b = new B(12, "abc", 11);
        byte[] bytes = conf.asByteArray(b);
        OutputStream outputStream = new FileOutputStream(file);
        try {
            outputStream.write(bytes);
            outputStream.flush();
        }finally {
            outputStream.close();
        }
        //b = (B)conf.asObject(barray);
        //System.out.println(b.getC());
    }

    public static void main(String argv[]) throws IOException {
        testNormal();
    }

    public static String getCurrentMethodName(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        //Stream.of(stackTraceElements).forEach(System.out::println);
        if (stackTraceElements.length >= 3)
            return stackTraceElements[2].getMethodName();
        return "null";
    }
}