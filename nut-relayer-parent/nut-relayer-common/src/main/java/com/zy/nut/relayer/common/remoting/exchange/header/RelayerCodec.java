package com.zy.nut.relayer.common.remoting.exchange.header;

import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;
import com.zy.nut.relayer.common.serialization.ObjectInput;
import com.zy.nut.relayer.common.serialization.ObjectOutput;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/6.
 */
public class RelayerCodec extends HeaderExchangeCodec implements Codec{

    @Override
    protected void encodeTransfredData(Channel channel, ObjectOutput out, TransfredData transfredData) throws IOException {
        out.writeUTF(transfredData.getGroup());
        out.writeUTF(transfredData.getFid());
        out.writeUTF(transfredData.getTid());

        if(transfredData.getData() != null)
            out.writeObject(transfredData.getData());
    }

    @Override
    protected void decodeTransfredData(Channel channel, ObjectInput in, TransfredData transfredData) throws IOException {
        transfredData.setGroup(in.readUTF());
        transfredData.setFid(in.readUTF());
        transfredData.setTid(in.readUTF());

        try {
            transfredData.setData(in.readObject());
        } catch (ClassNotFoundException e) {
            throw new IOException("Read object failed.", e);
        }
    }
}