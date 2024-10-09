package icu.nullptr.hidemyapplist.common;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class RefUtils {

    private static final String TAG = RefUtils.class.getName();

    public static void hookFiled(Class<?> cls, Object obj, String name, Object value) {
        Field field = null;
        try {
            field = cls.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getFiled(Object obj, String filedName) {
        try {
            Field field = obj.getClass().getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object setFiled(Object obj, String filedName, String value) {
        try {
            Field field = obj.getClass().getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getStaticFiled(Class<?> cls, String filedName) {
        try {
            Field field = cls.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static final List<String> list = new ArrayList<>();

    public static void quickHook(String className, String method, ClassLoader classLoader) {
        RefUtils.hookAllMethod(className, classLoader, (HookAfterCallback) param -> {
            if (TextUtils.isEmpty(method)) {
                Log.d(TAG, "[" + className + "]: " + param.method.getName() + " : " + Arrays.toString(param.args) + " result: " + param.getResult());
                return;
            }
            if (TextUtils.equals(method, param.method.getName())) {
                Log.w(TAG, "[" + className + "]: " + param.method.getName() + " : " + Arrays.toString(param.args) + " result: " + param.getResult());
            }
        });
    }

    public static void hookAllMethod(String clazz, ClassLoader classLoader, HookCallback callback) {
        try {
            hookAllMethod(Class.forName(clazz, true, classLoader), callback);
        } catch (Throwable e) {
            LogUtils.w(TAG, "Error: hookAllMethod " + clazz, e);
        }
    }

    public static void hookAllMethod(Class<?> clazz, HookCallback callback) {
        if (list.contains(clazz.toString())) {
            return;
        }
        list.add(clazz.toString());
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (!Modifier.isAbstract(method.getModifiers())) {
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (callback instanceof HookAfterCallback) {
                                ((HookAfterCallback) callback).afterHook(param);
                            }
                        }

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (callback instanceof HookBeforeCallback) {
                                ((HookBeforeCallback) callback).beforeHook(param);
                            }
                        }
                    });
                }
            } catch (Throwable e) {
                LogUtils.w(TAG, "Error " + clazz.getName());
            }
        }
    }

    public static boolean isExtendsClass(Class<?> clazz, String superClass) {
        while (clazz != null) {
            if (clazz.getName().equals(superClass)) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public interface HookCallback {
    }

    public interface HookBeforeCallback extends HookCallback {
        void beforeHook(XC_MethodHook.MethodHookParam param);
    }

    public interface HookAfterCallback extends HookCallback {
        void afterHook(XC_MethodHook.MethodHookParam param);
    }
}
