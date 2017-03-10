package com.zy.nut.common.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.cluster.Router;
import com.alibaba.dubbo.rpc.cluster.RouterFactory;

/**
 * Created by zhougb on 2017/3/10.
 */
public class MatchRouterFactory implements RouterFactory{
    @Override
    public Router getRouter(URL url) {
        return null;
    }


    public static void main(String argvs[]){
        boolean [] is = isLengthPicture3(17027, 6000);
        System.out.println(is[0]+" "+ is[1]);
    }

    public final static boolean[] isLengthPicture3(float width, float height) {
        boolean resu[] = new boolean[2];
        boolean isVertical = (height > width);
        // TODO bug:15046  java.lang.ArithmeticException: divide by zero
        if(height==0 || width == 0){
            return resu;
        }
        float ratio = isVertical ? height / width : width / height;
        if (ratio >= 2.5F) {// 高宽比例太悬殊  按巨图处理
            if (isVertical) {
                if (height > 1080) {
                    resu[0] = true;
                }
            } else {
                if (width > 1080) {
                    resu[0] = true;
                }
            }
        }
        resu[1] = isVertical;
        return resu;
    }

}
