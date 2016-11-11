package com.zy.nut.relayer.common.remoting.exchange;

import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.utils.HashAlgorithms;
import com.zy.nut.relayer.common.utils.StringUtils;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhougb on 2016/11/11.
 */
public class DownStreamClient {
    private Channel channel;
    //private Byte    type;
    private Map<Byte,BitSet> routingKeyBitTableMap;
    private int     routingKeyBitTableSize;
    private short   sizeBitCnt;
    private int     func;

    public DownStreamClient(Channel channel){
        this.channel = channel;
        routingKeyBitTableMap = new HashMap<Byte, BitSet>();
        routingKeyBitTableSize =  (int)Math.pow(2,20);
        func = 8;
        //sizeBitCnt = 4;
        sizeBitCnt = 0;
    }

    public Channel getChannel() {
        return channel;
    }

    public BitSet getBitSetByType(Byte type){
        BitSet bitSet = routingKeyBitTableMap.get(type);
        if (bitSet == null){
            bitSet = new BitSet(routingKeyBitTableSize);
            routingKeyBitTableMap.put(type, bitSet);
        }
        return bitSet;
    }

    public void setBitSetByType(Byte type, BitSet bitSet){
        if (!routingKeyBitTableMap.containsKey(type)){
            routingKeyBitTableMap.put(type, bitSet);
        }
    }

    public void unregister(String matchConditiones){

    }

    public boolean hasContains(Byte type, String matchCondition){
        if (StringUtils.isEmpty(matchCondition))
            return false;

        BitSet bitSet = getBitSetByType(type);
        for (int fi=0; fi<func; fi++) {
            int idx = hash(matchCondition, fi);
            if (!bitSet.get(idx))
                return false;
        }
        return true;
    }

    public void register(Byte type, String matchConditiones){
        if (StringUtils.isEmpty(matchConditiones))
            return;
        BitSet bitSet = getBitSetByType(type);
        String[] ids = matchConditiones.split(",");
        for (String id : ids){
            for (int fi=0; fi<func; fi++){
                int idx = hash(id, fi);
                bitSet.set(idx);
                /*int fromidx = hash(id, idx);
                int toidx = fromidx + sizeBitCnt;
                BitSet cntBits = bitSet.get(fromidx, toidx);
                byte count = cntBitsToInt(cntBits);
                count++;
                bitSet.*/
            }
        }
    }

    public byte cntBitsToInt(BitSet bitSet){
        byte[] bytes = bitSet.toByteArray();
        return (byte) (bytes[0] & (byte) 0x0F);
    }

    public int hash(String key, int idx){
        int ret = 0;
        switch (idx){
            case 0:
                ret = HashAlgorithms.APHash(key);
                break;
            case 1:
                ret = HashAlgorithms.oneByOneHash(key);
                break;
            case 2:
                ret = HashAlgorithms.bernstein(key);
                break;
            case 3:
                ret = HashAlgorithms.FNVHash(key.getBytes());
                break;
            case 4:
                ret = HashAlgorithms.FNVHash1(key.getBytes());
                break;
            case 5:
                ret = HashAlgorithms.RSHash(key);
                break;
            case 6:
                ret = HashAlgorithms.JSHash(key);
                break;
            case 7:
                ret = HashAlgorithms.PJWHash(key);
                break;
            case 8:
                ret = HashAlgorithms.ELFHash(key);
                break;
            case 9:
                ret = HashAlgorithms.BKDRHash(key);
                break;
            case 10:
                ret = HashAlgorithms.SDBMHash(key);
                break;
            case 11:
                ret = HashAlgorithms.DJBHash(key);
                break;
            case 12:
                ret = HashAlgorithms.DEKHash(key);
                break;
            case 13:
                ret = HashAlgorithms.APHash(key);
                break;
        }
        ret = Math.abs(ret);
        return ret % (routingKeyBitTableSize-sizeBitCnt);
    }

    public static void main(String argv[]){
        DownStreamClient downStreamClient = new DownStreamClient(null);
        Random random = new Random();
        Byte type = (byte) 1;
        for (int i=0; i< 100000; i++) {
            String routingKey = String.valueOf(random.nextLong());
            downStreamClient.register(type, routingKey);
            boolean ret = downStreamClient.hasContains(type, routingKey);
            if (!ret) throw new RuntimeException("xxxxxxxxx");
            System.out.println("the routingkey:" + routingKey + " hasContains:"
                    + ret);
        }
    }
}
