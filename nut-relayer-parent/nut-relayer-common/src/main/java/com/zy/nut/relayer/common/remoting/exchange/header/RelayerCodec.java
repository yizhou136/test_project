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
        }else if (message instanceof RelayerRegisteringUnRegistering){
            RelayerRegisteringUnRegistering relayerRegistering = (RelayerRegisteringUnRegistering)message;
            out.writeByte(relayerRegistering.getRegisterType());
            out.writeUTF(relayerRegistering.getProject());
            out.writeByte(relayerRegistering.getMatchType());
            out.writeUTF(relayerRegistering.getMatchConditiones());

            /*if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.NORMAL_REG_CLIENT.getType()) {
            }else if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.NORMAL_UNREG_CLIENT.getType()) {
            }else if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.SERVER_REG_CLIENT.getType()) {
            }else if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.SERVER_UNREG_CLIENT.getType()) {
            }*/
            type = REGISTERING_ORUNREG_FLAG_TYPE;
        }else if (message instanceof TransformData) {
            type = TRANSFORM_FLAG_TYPE;
            TransformData transfredData = (TransformData) message;
            out.writeUTF(transfredData.getProject());
            out.writeByte(transfredData.getMatchType());
            out.writeUTF(transfredData.getMatchConditiones());
            if (transfredData.getData() != null)
                out.writeObject(transfredData.getData());
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
            TransformData transfredData = new TransformData();
            transfredData.setProject(in.readUTF());
            transfredData.setMatchType(in.readByte());
            transfredData.setMatchConditiones(in.readUTF());
            try {
                transfredData.setData(in.readObject());
            } catch (ClassNotFoundException e) {
                throw new IOException("Read object failed.", e);
            }
            obj = transfredData;
        }else if (type == REGISTERING_ORUNREG_FLAG_TYPE){
            RelayerRegisteringUnRegistering relayerRegistering = new RelayerRegisteringUnRegistering();
            relayerRegistering.setRegisterType(in.readByte());
            relayerRegistering.setProject(in.readUTF());
            relayerRegistering.setMatchType(in.readByte());
            relayerRegistering.setMatchConditiones(in.readUTF());
            /*if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.NORMAL_REG_CLIENT.getType()) {
            }else if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.NORMAL_UNREG_CLIENT.getType()) {
            }else if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.SERVER_REG_CLIENT.getType()) {
            }else if (relayerRegistering.getRegisterType() ==
                    RelayerRegisteringUnRegistering.RelayerRegisteringType.SERVER_UNREG_CLIENT.getType()) {
            }*/
            obj = relayerRegistering;
        }

        return obj;
    }
}