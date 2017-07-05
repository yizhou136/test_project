package com.zy.nut.common.beans;

/**
 * Created by zhougb on 2016/12/5.
 */
public class Response {
    public static enum ResponseCode {
        SUCCESSED((byte) 0),
        FAILURED((byte)1),
        RETRY((byte)2);

        byte code;
        ResponseCode(byte code){
            this.code = code;
        }

        public byte getCode() {
            return code;
        }

        public void setCode(byte code) {
            this.code = code;
        }
    }
    public static final Response SUCCESSED_RES = new Response(ResponseCode.SUCCESSED.getCode(), "successed");
    public static final Response FAILURED_RES = new Response(ResponseCode.FAILURED.getCode(), "failured");


    private byte errCode;
    private String msg;

    public Response(){}

    public Response(byte errCode, String msg){
        this.errCode = errCode;
        this.msg = msg;
    }

    public byte getErrCode() {
        return errCode;
    }

    public void setErrCode(byte errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
