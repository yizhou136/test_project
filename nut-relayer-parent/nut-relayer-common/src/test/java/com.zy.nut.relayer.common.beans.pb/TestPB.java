package com.zy.nut.relayer.common.beans.pb;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author by zy.
 */
public class TestPB {

    @Test
    public void testBaseMsg(){
        Basemsg.BaseMsgPB.Builder baseMsgBuilder = Basemsg.BaseMsgPB.newBuilder();
        Basemsg.BaseMsgPB baseMsgPB = baseMsgBuilder.setMid(10004).setCtime(System.currentTimeMillis())
                .setLctime(System.currentTimeMillis()).setMsg("hello").build();
        Roommsg.RoomMsgPB.Builder roomMsgBuilder = Roommsg.RoomMsgPB.newBuilder();
        Roommsg.RoomMsgPB roomMsgPB = roomMsgBuilder.setBaseMsg(baseMsgPB)
                .setFuid(1001)
                .setRid(23).build();


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CodedOutputStream codedOutputStream = CodedOutputStream.newInstance(byteArrayOutputStream);
        try {
            roomMsgPB.writeTo(codedOutputStream);
            codedOutputStream.flush();
            byte bytes[] = byteArrayOutputStream.toByteArray();
            //Stream.of(bytes).forEach(System.out::println);
            for (byte by:bytes)
                System.out.println(by);

            Roommsg.RoomMsgPB roomMsgPB1 = Roommsg.RoomMsgPB.parseFrom(bytes);
            System.out.println(roomMsgPB1);
            Descriptors.FileDescriptor fileDescriptor = Roommsg.getDescriptor();

            List<Descriptors.Descriptor> list = fileDescriptor.getMessageTypes();

            fileDescriptor = Basemsg.getDescriptor();

            list = fileDescriptor.getMessageTypes();
            list.forEach((des)->{
                Descriptors.FieldDescriptor fieldDescriptor = des.findFieldByName("msg");
                System.out.println(fieldDescriptor.getIndex());
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(baseMsg.);
    }
}
