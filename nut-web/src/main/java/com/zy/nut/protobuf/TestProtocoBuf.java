package com.zy.nut.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by Administrator on 2016/2/25.
 */
public class TestProtocoBuf {

    public static void main(String argc[]){
        MyClass.getDescriptor();

        Descriptors.FieldDescriptor descriptor = MyClass.msgInfo.Builder.getDescriptor().findFieldByName("Url");

        MyClass.msgInfo.Builder builder = MyClass.msgInfo.newBuilder();
        builder.setID(2);
        builder.setGoodID(1);
        builder.setUrl("http://xxx.jpg");
        builder.setGuid("a");
        builder.setType("t");
        builder.setOrder(0);



        MyClass.msgInfo msgInfo = builder.build();
        byte[]  aa = msgInfo.toByteArray();
        printHexString(aa);
        System.out.println(msgInfo.toByteArray());

        try {
            MyClass.msgInfo msgInfo1 = MyClass.msgInfo.parseFrom(aa);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public static void printHexString( byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.println(hex.toUpperCase() );
        }

    }
}
