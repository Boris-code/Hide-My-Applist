package icu.nullptr.hidemyapplist.common;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {
    private static final String LOG_PREFIX = "HideEnv.";
    public static String identifer = "";
    public static boolean DEBUG = true;

    public static void setDebug(boolean z) {
        DEBUG = z;
    }

    public static void setIdentifer(String str) {
        String[] split = str.split("\\.");
        if (split.length > 0) {
            identifer = split[split.length - 1] + ".";
            return;
        }
        identifer = str;
    }

    public static void v(String str, String str2) {
        if (DEBUG) {
            if (TextUtils.equals(lastLog, str2)) {
                return;
            }
            lastLog = str2;
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(2, str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (DEBUG) {
            if (TextUtils.equals(lastLog, str2)) {
                return;
            }
            lastLog = str2;
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(4, str, str2);
        }
    }

    public static void i(String str, String str2, Throwable th) {
        if (DEBUG) {
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(4, str, str2 + ":" + errInfo(th));
        }
    }

    public static void d(String str, String str2) {
        if (DEBUG) {
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(3, str, str2);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (DEBUG) {
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(3, str, str2 + ":" + errInfo(th));
        }
    }

    public static void w(String str, String str2) {
        if (DEBUG) {
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(5, str, str2);
        }
    }

    public static void w(String str, String str2, Throwable th) {
        if (DEBUG) {
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(5, str, str2 + ":" + errInfo(th));
        }
    }

    private static String lastLog = "";

    public static void e(String str, String str2) {
        if (DEBUG) {
            if (TextUtils.equals(lastLog, str2)) {
                return;
            }
            lastLog = str2;
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(6, str, str2);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (DEBUG) {
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(6, str, str2 + ":" + errInfo(th));
        }
    }

    private static String errInfo(Throwable th) {
        Throwable th2;
        PrintWriter printWriter;
        StringWriter stringWriter = null;
        try {
            StringWriter stringWriter2 = new StringWriter();
            try {
                printWriter = new PrintWriter(stringWriter2);
                try {
                    th.printStackTrace(printWriter);
                    printWriter.flush();
                    stringWriter2.flush();
                    try {
                        stringWriter2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    printWriter.close();
                    return stringWriter2.toString();
                } catch (Throwable th3) {
                    th2 = th3;
                    stringWriter = stringWriter2;
                    if (stringWriter != null) {
                        try {
                            stringWriter.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (printWriter != null) {
                        printWriter.close();
                    }
                    throw th2;
                }
            } catch (Throwable th4) {
                th2 = th4;
                printWriter = null;
            }
        } catch (Throwable th5) {
            th2 = th5;
            printWriter = null;
        }
        return "";
    }

    public static void showLongLog(int i, String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            int length = str2.length();
            int i2 = 2000;
            int i3 = 0;
            int i4 = 0;
            while (i3 < 1000) {
                if (length > i2) {
                    Log.println(i, str + ":" + i3, str2.substring(i4, i2));
                    i4 = i2;
                    i2 += 1000;
                    i3++;
                } else {
                    Log.println(i, str, str2.substring(i4, length));
                    return;
                }
            }
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        if (DEBUG) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            if (str2 == null) {
                str2 = "";
            }
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(3, str, str2);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (DEBUG) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            if (str2 == null) {
                str2 = "";
            }
            String str3 = LOG_PREFIX;
            if (!str.startsWith(str3)) {
                str = str3 + identifer + str;
            }
            showLongLog(6, str, str2);
        }
    }

    public static void writeToFile(String str, String str2) {
        if (DEBUG) {
            try {
                String str3 = Environment.getExternalStorageDirectory().getPath() + File.separator + "qin";
                String str4 = str3 + File.separator + System.currentTimeMillis() + "_" + str + ".txt";
                e("LogUtils", "writeToFile log path:" + str4);
                File file = new File(str3);
                e("LogUtils", "writeToFile logFileDir:exist=" + file.exists());
                if (!file.exists()) {
                    e("LogUtils", "writeToFile logFileDir:not exist mkdirs=" + file.mkdirs());
                }
                FileOutputStream fileOutputStream = new FileOutputStream(str4);
                fileOutputStream.write(str2.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e("LogUtils", "writeToFile", e);
            }
        }
    }

    public static void www(String filePath, String log) {
        writeFileStr("/data/user/0/com.jd.jdhealth/files/run.log", log);
    }

    @SuppressLint("NewApi")
    public static String writeFileStr(String path, String data) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileWriter fileWritter = new FileWriter(path, true);
                 BufferedWriter bufferWritter = new BufferedWriter(fileWritter)) {
                bufferWritter.write(data + "\n");
            }
        } catch (Exception e) {
            e("LogUtils", "writeFileStr", e);
        }
        return "";
    }
}
