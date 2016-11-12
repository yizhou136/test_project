package com.zy.nut.relayer.common.remoting.exchange;

/**
 * Created by zhougb on 2016/11/10.
 */
public class RelayerRegisteringUnRegistering extends TransformData{
    public static enum RelayerRegisteringType{
        NORMAL_REG_CLIENT((byte) 0),
        NORMAL_UNREG_CLIENT((byte) 1),
        SERVER_REG_CLIENT((byte) 2),
        SERVER_UNREG_CLIENT((byte) 3);

        byte type;
        RelayerRegisteringType(byte t){
            this.type = t;
        }
        public byte getType() {
            return type;
        }
    }

    private byte registerType;

    public byte getRegisterType() {
        return registerType;
    }

    public void setRegisterType(byte registerType) {
        this.registerType = registerType;
    }

    public boolean isRegAction(){
        return registerType == RelayerRegisteringType.NORMAL_REG_CLIENT.getType() ||
                registerType == RelayerRegisteringType.SERVER_REG_CLIENT.getType();

    }

    //public boolean is

    public boolean isServerClient(){
        return registerType == RelayerRegisteringType.SERVER_REG_CLIENT.getType() ||
                registerType == RelayerRegisteringType.SERVER_UNREG_CLIENT.getType();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" registerType:").append(getRegisterType());
        return sb.toString();
    }
}