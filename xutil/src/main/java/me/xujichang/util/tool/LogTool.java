package me.xujichang.util.tool;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.google.common.base.Strings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import me.xujichang.util.BuildConfig;
import me.xujichang.util.thirdparty.AppUtils;

/**
 * 对日志作封装
 * 主要功能：
 * 1. 只在Debug模式下打印Log
 * 2. 默认TAG 是调用类的类名
 * 3. 可以被IDE识别并跳转
 * 4. Json 格式化
 * 5. XML格式化
 * 6. 文字太长换行
 * 7. 当前所在线程
 * <p>
 * 日志格式：
 * <p>
 * 线程信息
 * 调用类、方法信息
 * MSG
 * 错误栈
 *
 * @author xjc
 * Created by huangjun on 2016/10/14.
 */

public class LogTool {
    private static final String CLASS_NAME = LogTool.class.getName();

    private static boolean isDeBug = BuildConfig.DEBUG;

    //可选格式方式

    //错误信息

    public static void e(Throwable pThrowable) {
        e(getThrowableInfo(pThrowable));
    }

    public static void i(Throwable pThrowable) {
        i(getThrowableInfo(pThrowable));
    }

    public static void d(Throwable pThrowable) {
        d(getThrowableInfo(pThrowable));
    }

    public static void v(Throwable pThrowable) {
        v(getThrowableInfo(pThrowable));
    }

    public static void w(Throwable pThrowable) {
        w(getThrowableInfo(pThrowable));
    }

    public static void wtf(Throwable pThrowable) {
        wtf(getThrowableInfo(pThrowable));
    }


    //===========供使用===============

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        i(wrapperLog(tag, msg));
    }

    public static void i(String msg, LogFormat pFormat) {
        i(null, msg, pFormat);
    }

    public static void i(String tag, String msg, LogFormat pFormat) {
        i(wrapperLog(tag, msg, pFormat));
    }


    public static void v(String msg) {
        v(null, msg);
    }

    public static void v(String tag, String msg) {
        v(wrapperLog(tag, msg));
    }

    public static void v(String msg, LogFormat pFormat) {
        v(null, msg, pFormat);
    }

    public static void v(String tag, String msg, LogFormat pFormat) {
        v(wrapperLog(tag, msg, pFormat));
    }


    public static void d(String msg) {
        d(null, msg);
    }

    public static void d(String tag, String msg) {
        d(wrapperLog(tag, msg));
    }

    public static void d(String msg, LogFormat pFormat) {
        d(null, msg, pFormat);
    }

    public static void d(String tag, String msg, LogFormat pFormat) {
        d(wrapperLog(tag, msg, pFormat));
    }


    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(String tag, String msg) {
        e(wrapperLog(tag, msg));
    }

    public static void e(String msg, LogFormat pFormat) {
        e(null, msg, pFormat);
    }

    public static void e(String tag, String msg, LogFormat pFormat) {
        e(wrapperLog(tag, msg, pFormat));
    }


    public static void w(String msg) {
        w(null, msg);
    }

    public static void w(String tag, String msg) {
        w(wrapperLog(tag, msg));
    }

    public static void w(String msg, LogFormat pFormat) {
        w(null, msg, pFormat);
    }

    public static void w(String tag, String msg, LogFormat pFormat) {
        w(wrapperLog(tag, msg, pFormat));
    }


    public static void wtf(String msg) {
        wtf(null, msg);
    }

    public static void wtf(String tag, String msg) {
        wtf(tag, msg, null);
    }

    public static void wtf(String msg, LogFormat pFormat) {
        wtf(null, msg, pFormat);
    }

    public static void wtf(String tag, String msg, LogFormat pFormat) {
        wtf(wrapperLog(tag, msg, pFormat));
    }

    //第一层封装

    private static void i(LogWrapper pWrapper) {
        if (!isDeBug) {
            return;
        }
        Log.i(pWrapper.getTAG(), pWrapper.getMsg());
    }

    private static void v(LogWrapper pWrapper) {
        if (!isDeBug) {
            return;
        }
        Log.v(pWrapper.getTAG(), pWrapper.getMsg());

    }

    private static void d(LogWrapper pWrapper) {
        if (!isDeBug) {
            return;
        }
        Log.d(pWrapper.getTAG(), pWrapper.getMsg());

    }

    private static void e(LogWrapper pWrapper) {
        if (!isDeBug) {
            return;
        }
        Log.e(pWrapper.getTAG(), pWrapper.getMsg());

    }

    private static void w(LogWrapper pWrapper) {
        if (!isDeBug) {
            return;
        }
        Log.w(pWrapper.getTAG(), pWrapper.getMsg());

    }

    private static void wtf(LogWrapper pWrapper) {
        if (!isDeBug) {
            return;
        }
        Log.wtf(pWrapper.getTAG(), pWrapper.getMsg());
    }

    //==========================工具================================

    /**
     * 将错误信息，格式化输出
     *
     * @param pThrowable
     * @return
     */
    private static String getThrowableInfo(Throwable pThrowable) {

        if (pThrowable == null) {
            return "";
        }
        String rtn = pThrowable.getStackTrace().toString();
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            pThrowable.printStackTrace(printWriter);
            printWriter.flush();
            writer.flush();
            rtn = writer.toString();
            printWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        }
        return rtn;
    }

    private static LogWrapper wrapperLog(String pTag, String pMsg) {
        return wrapperLog(pTag, pMsg, LogFormat.DEFAULT);
    }

    private static LogWrapper wrapperLog(String pTag, String pMsg, LogFormat pFormat) {
        StackTraceElement lElement = getElement();
        if (Strings.isNullOrEmpty(pTag)) {
            pTag = convertClassName(lElement.getClassName());
        }
        LogWrapper lLogWrapper = new LogWrapper();
        lLogWrapper.setElement(lElement);
        lLogWrapper.setMsg(pMsg);
        lLogWrapper.setTAG(pTag);
        if (null != pFormat) {
            lLogWrapper.setFormat(pFormat);
        }
        return lLogWrapper;
    }

    /**
     * 仅取类名
     *
     * @param pClassName
     * @return
     */
    private static String convertClassName(String pClassName) {
        int index = pClassName.lastIndexOf(".");
        return pClassName.substring(index + 1, pClassName.length());
    }

    /**
     * 获取调用类的栈信息
     *
     * @return
     */
    private static StackTraceElement getElement() {
        String classInfo = null;
        boolean flag = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        //遍历用
        StackTraceElement lElement = null;
        //最后的结果
        StackTraceElement tagElement = null;
        //如果找不到合适的
        StackTraceElement bakElement = null;
        for (int lI = 0; lI < stackTrace.length; lI++) {
            lElement = stackTrace[lI];
            if (CLASS_NAME.equals(lElement.getClassName())) {
                flag = true;
                bakElement = lElement;
            } else {
                if (flag) {
                    tagElement = lElement;
                    break;
                }
            }
        }
        if (null == tagElement) {
            tagElement = bakElement;
        }
        return tagElement;
    }

    /**
     * LOG 包装，便于传递
     */
    private static final class LogWrapper {
        /**
         * 类 方法信息 格式
         * at void android.support.v7.app.AppCompatDelegateImpl.setContentView(int) (AppCompatDelegateImpl.java:466)
         */
        private static final String classFormat = "%s(%s:%s) %s";
        /**
         * TAG
         */
        private String TAG;
        /**
         * MSG
         */
        private String msg;
        /**
         * FORMAT
         */
        private LogFormat format = LogFormat.DEFAULT;
        private StackTraceElement mElement;

        public StackTraceElement getElement() {
            return mElement;
        }

        public void setElement(StackTraceElement pElement) {
            mElement = pElement;
        }

        public String getTAG() {
            return TAG;
        }

        public void setTAG(String pTAG) {
            TAG = pTAG;
        }

        public String getMsg() {
            return formatMsg();
        }

        private String formatMsg() {
            msg = beJson(msg);
//            msg = beXml(msg);
            return String.format(format.getFormat(), Thread.currentThread().getName(), getClassAndMethod(mElement), msg);
        }

        private String beXml(String pMsg) {
            return pMsg;
        }

        private String beJson(String pMsg) {
            if (Strings.isNullOrEmpty(pMsg)) {
                return pMsg;
            }
            String str = pMsg;
            try {
                if (pMsg.startsWith("{")) {
                    JSONObject lJSONObject = new JSONObject(msg);
                    str = lJSONObject.toString(4);
                } else if (pMsg.startsWith("[")) {
                    JSONArray lArray = new JSONArray(msg);
                    str = lArray.toString(4);
                }
            } catch (JSONException pE) {
                pE.printStackTrace();
            }
            return str;
        }


        private String getClassAndMethod(StackTraceElement pElement) {
            if (null == pElement) {
                return "";
            }
            return String.format(classFormat, pElement.getClassName(), pElement.getFileName(), String.valueOf(pElement.getLineNumber()), pElement.getMethodName());
        }

        public void setMsg(String pMsg) {
            msg = pMsg;
        }

        public LogFormat getFormat() {
            return format;
        }

        public void setFormat(LogFormat pFormat) {
            format = pFormat;
        }
    }

    public enum LogFormat {
        /**
         * 默认样式
         */
        DEFAULT(0),
        /**
         * 简单样式
         */
        SIMPLE(1),
        /**
         * 自定义样式
         */
        CUSTOM(2);

        LogFormat() {

        }

        LogFormat(String pStart, String pEnd, String pDivide, String pMsg_start) {

            start = pStart;
            end = pEnd;
            divide = pDivide;
            msg_start = pMsg_start;
        }

        LogFormat(int pType) {
            type = pType;
        }

        private static final String NEW_LINE = "\n";
        private static final String LOG_THREAD = "Thread:";
        private static final String LOG_CLASS_METHOD = "Class&Method:";
        private static final String LOG_MSG = "Info:";

        private int type = 0;
        private String start = "----------";
        private String end = "----------";
        private String divide = " - ";
        private String msg_start = "| ";
        private String msg_divide = "----------";
        private String formatStr;

        private String getStart() {
            return start;
        }

        private void setStart(String pStart) {
            start = pStart;
        }

        private String getEnd() {
            return end;
        }

        private void setEnd(String pEnd) {
            end = pEnd;
        }

        private String getDivide() {
            return divide;
        }

        private void setDivide(String pDivide) {
            divide = pDivide;
        }

        private String getMsg_start() {
            return msg_start;
        }

        private void setMsg_start(String pMsg_start) {
            msg_start = pMsg_start;
        }

        private void setFormatStr(String pFormatStr) {
            formatStr = pFormatStr;
        }

        LogFormat(String pFormatStr) {
            formatStr = pFormatStr;
        }

        private String getFormat() {
            if (type == DEFAULT.type) {
                return "|" + NEW_LINE
                        + start + NEW_LINE
                        + LOG_THREAD + NEW_LINE
                        + "%s" + NEW_LINE
                        + msg_divide + NEW_LINE
                        + LOG_CLASS_METHOD + NEW_LINE
                        + "%s" + NEW_LINE
                        + msg_divide + NEW_LINE
                        + LOG_MSG + NEW_LINE
                        + "%s" + NEW_LINE
                        + end;
            }
            if (type == SIMPLE.type) {
                return "|" + NEW_LINE
                        + "%s" + NEW_LINE
                        + "%s" + NEW_LINE
                        + "%s" + NEW_LINE;
            }
            if (type == CUSTOM.type) {
                return formatStr;
            }
            return "";
        }
    }

    /**
     * 设置是否是Debug
     *
     * @param pApplication
     */
    public static void syncIsDebug(Application pApplication) {
        ApplicationInfo lApplicationInfo = pApplication.getApplicationInfo();
        if (null == lApplicationInfo) {
            return;
        }
        isDeBug = (lApplicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}
