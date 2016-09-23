package cn.xcom.helper.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class StringJoint {

    public static String arrayJointchar(List<String> stringArrayList,String separator){
       String upString="";
        for (int i = 0; i < stringArrayList.size(); i++) {

            upString = stringArrayList.get(i) + separator+upString ;
        }
        upString = upString.substring(0, upString.length() -1);
        return  upString;
    }

}
