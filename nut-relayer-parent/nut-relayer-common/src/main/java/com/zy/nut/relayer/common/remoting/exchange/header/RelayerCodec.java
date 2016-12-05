package com.zy.nut.relayer.common.remoting.exchange.header;

import com.zy.nut.relayer.common.beans.DialogMsg;
import com.zy.nut.relayer.common.beans.Room;
import com.zy.nut.relayer.common.beans.RoomMsg;
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
        }else if (message instanceof RelayerLogin){
            type = LOGIN_FLAG_TYPE;
            RelayerLogin relayerLogin = (RelayerLogin)message;
            out.writeLong(relayerLogin.getUid());
            out.writeByte(relayerLogin.getPid());
            out.writeUTF(relayerLogin.getUserName());
            out.writeUTF(relayerLogin.getPassword());
        }else if (message instanceof RelayerLogout){
            type = LOGOUT_FLAG_TYPE;
            RelayerLogout relayerLogout = (RelayerLogout)message;
            out.writeLong(relayerLogout.getUid());
        }else if (message instanceof RelayerElecting){
            type = ELECTING_FLAG_TYPE;
            RelayerElecting relayerElecting = (RelayerElecting)message;
            out.writeUTF(relayerElecting.getServerName());
            out.writeInt(relayerElecting.getGuessedConnections());
            out.writeShort(relayerElecting.getPerformance());
        }else if (message instanceof RelayerRegisteringUnRegistering){
            RelayerRegisteringUnRegistering relayerRegistering = (RelayerRegisteringUnRegistering)message;
            out.writeByte(relayerRegistering.getRegisterType());
            out.writeByte(relayerRegistering.getProject());
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
            out.writeByte(transfredData.getProject());
            out.writeByte(transfredData.getMatchType());
            out.writeUTF(transfredData.getMatchConditiones());
            if (transfredData.getData() != null)
                out.writeObject(transfredData.getData());
        }else if (message instanceof DialogMsg) {
            type = DIALOG_MSG_FLAG_TYPE;
            DialogMsg dialogMsg = (DialogMsg)message;
            out.writeLong(dialogMsg.getFuid());
            out.writeLong(dialogMsg.getTuid());
            out.writeLong(dialogMsg.getLctime());
            out.writeLong(dialogMsg.getCtime());
            out.writeUTF(dialogMsg.getMsg());
        }else if (message instanceof RoomMsg) {
            type = ROOM_MSG_FLAG_TYPE;
            RoomMsg roomMsg = (RoomMsg)message;
            out.writeLong(roomMsg.getFuid());
            out.writeLong(roomMsg.getRid());
            out.writeLong(roomMsg.getLctime());
            out.writeLong(roomMsg.getCtime());
            out.writeUTF(roomMsg.getMsg());
        }
        return type;
    }

    @Override
    protected Object decodeTransfredData(Channel channel, ObjectInput in, byte type) throws IOException {
        Object obj = null;
        if (type == LOGIN_FLAG_TYPE){
            RelayerLogin relayerLogin = new RelayerLogin();
            relayerLogin.setUid(in.readLong());
            relayerLogin.setPid(in.readByte());
            relayerLogin.setUserName(in.readUTF());
            relayerLogin.setPassword(in.readUTF());
            obj = relayerLogin;
        }else if (type == LOGOUT_FLAG_TYPE){
            RelayerLogout relayerLogout = new RelayerLogout();
            relayerLogout.setUid(in.readLong());
            obj = relayerLogout;
        }else if (type == ELECTING_FLAG_TYPE){
            RelayerElecting relayerElecting = new RelayerElecting();
            relayerElecting.setServerName(in.readUTF());
            relayerElecting.setGuessedConnections(in.readInt());
            relayerElecting.setPerformance(in.readShort());
            obj = relayerElecting;
        }else if (type == TRANSFORM_FLAG_TYPE){
            TransformData transfredData = new TransformData();
            transfredData.setProject(in.readByte());
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
            relayerRegistering.setProject(in.readByte());
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
        }else if (type == DIALOG_MSG_FLAG_TYPE){
            DialogMsg dialogMsg = new DialogMsg();
            dialogMsg.setFuid(in.readLong());
            dialogMsg.setTuid(in.readLong());
            dialogMsg.setLctime(in.readLong());
            dialogMsg.setCtime(in.readLong());
            dialogMsg.setMsg(in.readUTF());
            obj = dialogMsg;
        }else if (type == ROOM_MSG_FLAG_TYPE){
            RoomMsg roomMsg = new RoomMsg();
            roomMsg.setFuid(in.readLong());
            roomMsg.setRid(in.readLong());
            roomMsg.setLctime(in.readLong());
            roomMsg.setCtime(in.readLong());
            roomMsg.setMsg(in.readUTF());
            obj = roomMsg;
        }

        return obj;
    }
}