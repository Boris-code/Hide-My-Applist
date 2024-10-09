package icu.nullptr.hidemyapplist.xposed.accessibility;

import android.text.TextUtils;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import icu.nullptr.hidemyapplist.common.LogUtils;
import icu.nullptr.hidemyapplist.common.RefUtils;

/**
 * 模块功能：隐藏无障碍服务、miui优化、开发者选项、adb调试
 * 进程：com.android.providers.settings
 */
public class SettingsProviderHook {

    public static final String TAG = "SettingsProviderModule";

    public static void start(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!TextUtils.equals(lpparam.packageName, "com.android.providers.settings")) {
            return;
        }
        LogUtils.e(TAG, "[SettingsProvider]: start hook SettingsProvider: " + lpparam.packageName + " ; processName : " + lpparam.processName);
        hookSettingsProvider(lpparam);
    }

    private static void hookSettingsProvider(XC_LoadPackage.LoadPackageParam lpparam) {
        RefUtils.hookAllMethod("com.android.providers.settings.SettingsProvider", lpparam.classLoader, (RefUtils.HookAfterCallback) param -> {
            switch (param.method.getName()) {
                case "getSecureSetting":
                    handleSecureSetting(param);
                    break;
                case "getGlobalSetting":
                    handleGlobalSetting(param);
                    break;
                default:
                    break;
            }
        });
    }

    private static void handleGlobalSetting(XC_MethodHook.MethodHookParam param) {
        String key = (String) param.args[0];

        String caller = CommonUtils.getCaller();
        if (!CommonUtils.shouldHide(caller)) return;

        // 环境隐藏
        switch (key) {
            case "development_settings_enabled":
            case "adb_enabled":
                if (TextUtils.isEmpty(caller)) return;
                XposedHelpers.setObjectField(param.getResult(), "value", "0");
                LogUtils.e(TAG, "[SettingsProvider]: " + param.method.getName() + " caller: " + caller + "a : " + Arrays.toString(param.args) + " res: " + param.getResult());
                break;
        }
    }

    private static void handleSecureSetting(XC_MethodHook.MethodHookParam param) {
        String key = (String) param.args[0];

        String caller = CommonUtils.getCaller();
        if (!CommonUtils.shouldHide(caller)) return;

        switch (key) {
            case "enabled_accessibility_services":
                XposedHelpers.setObjectField(param.getResult(), "value", "");
                LogUtils.e(TAG, "[SettingsProvider]mocked: " + param.method.getName() + ", caller: " + caller + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                break;
            case "accessibility_enabled":
                XposedHelpers.setObjectField(param.getResult(), "value", "0");
                LogUtils.e(TAG, "[SettingsProvider]mocked: " + param.method.getName() + ", caller: " + caller + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                break;
            case "miui_optimization":
                XposedHelpers.setObjectField(param.getResult(), "value", "1");
                LogUtils.i(TAG, "[SettingsProvider]: " + param.method.getName() + ", caller: " + caller + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                break;
        }
    }
}
