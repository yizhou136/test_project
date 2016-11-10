package com.zy.nut.relayer.common.remoting.exchange.header;

import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.exchange.*;
import com.zy.nut.relayer.common.serialize.ObjectInput;
import com.zy.nut.relayer.common.serialize.ObjectOutput;


import java.io.IOException;

/**
 * Created by Administrator on 2016/11/6.
 */
public class RelayerCodec extends HeaderExchangeCodec implements Codec{

    @Override
    protected byte encodeTransfredData(Channel channel, ObjectOutput out, Object message) throws IOException {
        byte type = 0;
        if (message instanceof RelayerPingPong){
            type = PINGPONG_FLAG_TYPE;
            RelayerPingPong relayerPingPong = (RelayerPingPong)message;
            out.writeBytes(relayerPingPong.getClientBitTable());
            out.writeShort(relayerPingPong.getPerformance());
        }else if (message instanceof RelayerLoginLogout){
            if (((RelayerLoginLogout)message).isLogin())
                type = LOGIN_FLAG_TYPE;
            else
                type = LOGOUT_FLAG_TYPE;
            RelayerLoginLogout relayerLoginLogout = (RelayerLoginLogout)message;
            out.writeLong(relayerLoginLogout.getUid());
        }else if (message instanceof RelayerElecting){
            type = ELECTING_FLAG_TYPE;
            RelayerElecting relayerElecting = (RelayerElecting)message;
            out.writeUTF(relayerElecting.getServerName());
            out.writeInt(relayerElecting.getGuessedConnections());
            out.writeShort(relayerElecting.getPerformance());
        }else if (message instanceof TransfredData){
            type = TRANSFORM_FLAG_TYPE;
            TransfredData transfredData = (TransfredData) message;
            out.writeUTF(transfredData.getGroup());
            out.writeUTF(transfredData.getFid());
            out.writeUTF(transfredData.getTid());
            if(transfredData.getData() != null)
                out.writeObject(transfredData.getData());
        }else if (message instanceof RelayerRegistering){
            RelayerRegistering relayerRegistering = (RelayerRegistering)message;
            out.writeByte(relayerRegistering.getType());
            out.writeLong(relayerRegistering.getRoutingkey());
            if (relayerRegistering.getType() == RelayerRegistering.RelayerRegisteringType.FANOUT_TYPE.getType())
                type = REGISTERING_FANOUT_FLAG_TYPE;
            else
                type = REGISTERING_DIRECT_FLAG_TYPE;
        }
        return type;
    }

    @Override
    protected Object decodeTransfredData(Channel channel, ObjectInput in, byte type) throws IOException {
        Object obj = null;
        if (type == LOGIN_FLAG_TYPE){
            RelayerLoginLogout relayerLoginLogout = new RelayerLoginLogout();
            relayerLoginLogout.setUid(in.readLong());
            obj = relayerLoginLogout;
        }else if (type == LOGIN_FLAG_TYPE || type == LOGOUT_FLAG_TYPE){
            RelayerLoginLogout relayerLoginLogout = new RelayerLoginLogout();
            if (type == LOGIN_FLAG_TYPE)
                relayerLoginLogout.setLogin(true);
            relayerLoginLogout.setUid(in.readLong());
            obj = relayerLoginLogout;
        }else if (type == ELECTING_FLAG_TYPE){
            RelayerElecting relayerElecting = new RelayerElecting();
            relayerElecting.setServerName(in.readUTF());
            relayerElecting.setGuessedConnections(in.readInt());
            relayerElecting.setPerformance(in.readShort());
            obj = relayerElecting;
        }else if (type == TRANSFORM_FLAG_TYPE){
            TransfredData transfredData = new TransfredData();
            transfredData.setGroup(in.readUTF());
            transfredData.setFid(in.readUTF());
            transfredData.setTid(in.readUTF());
            try {
                transfredData.setData(in.readObject());
            } catch (ClassNotFoundException e) {
                throw new IOException("Read object failed.", e);
            }
            obj = transfredData;
        }else if (type == REGISTERING_DIRECT_FLAG_TYPE ||
                type == REGISTERING_FANOUT_FLAG_TYPE){
            RelayerRegistering relayerRegistering = new RelayerRegistering();
            if (type == REGISTERING_FANOUT_FLAG_TYPE)
                relayerRegistering.setType(RelayerRegistering.RelayerRegisteringType.FANOUT_TYPE.getType());
            else
                relayerRegistering.setType(RelayerRegistering.RelayerRegisteringType.DIRECTER_TYPE.getType());

            relayerRegistering.setType(in.readByte());
            relayerRegistering.setRoutingkey(in.readLong());
            obj = relayerRegistering;
        }
        return obj;
    }
}