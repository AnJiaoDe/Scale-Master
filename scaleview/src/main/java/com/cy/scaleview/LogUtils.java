package com.cy.scaleview;

import android.util.Log;

/**
 * Created by lenovo on 2017/8/20.
 */

public class LogUtils {
    public static  void log(String tag, Object content){
        Log.e(tag,"------------------------------------->>>>"+content.toString());
    }
    public static  void log(String tag){
        Log.e(tag,"---------------------------------------->>>>");
    }
    public static  void log(Object tag){
        Log.e(String.valueOf(tag),"--------------------------------------->>>>");
    }
}
