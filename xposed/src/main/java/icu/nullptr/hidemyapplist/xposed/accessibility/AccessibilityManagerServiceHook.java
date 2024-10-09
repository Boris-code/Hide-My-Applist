package icu.nullptr.hidemyapplist.xposed.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import icu.nullptr.hidemyapplist.common.LogUtils;
import icu.nullptr.hidemyapplist.common.RefUtils;

public class AccessibilityManagerServiceHook {
    public static final String TAG = "AccessibilityManagerServiceHook";

    public static void start(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!TextUtils.equals(lpparam.packageName, "android")) {
            return;
        }

        LogUtils.e(TAG, "[AccessibilityManagerServiceHook]: start hook AccessibilityManagerService: " + lpparam.packageName + " ; processName : " + lpparam.processName);
        hookAccessibilityServer(lpparam);
    }

    private static void hookAccessibilityServer(XC_LoadPackage.LoadPackageParam lpparam) {
        RefUtils.hookAllMethod("com.android.server.accessibility.AccessibilityManagerService", lpparam.classLoader, (RefUtils.HookAfterCallback) param -> {
            String caller = CommonUtils.getCaller();
            if (!CommonUtils.shouldHide(caller)) return;

            switch (param.method.getName()) {
                case "getEnabledAccessibilityServiceList":
                    if (param.getResult() == null) break;
                    param.setResult(new ArrayList<AccessibilityServiceInfo>());
                    LogUtils.i(TAG, "[AccessibilityManagerService]: " + caller + " method " + param.method.getName() + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                    break;
                default:
                    // LogUtils.v(TAG, "[AccessibilityManagerService]: " + caller + " method: " + param.method.getName() + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                    break;
            }
        });

        RefUtils.hookAllMethod("com.android.server.accessibility.AccessibilityUserState", lpparam.classLoader, (RefUtils.HookAfterCallback) param -> {
            String caller = CommonUtils.getCaller();
            if (!CommonUtils.shouldHide(caller)) return;

            switch (param.method.getName()) {
                case "getClientStateLocked":
                    param.setResult(0);
                    LogUtils.i(TAG, "[AccessibilityUserState]: " + caller + " method " + param.method.getName() + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                    break;
                case "isHandlingAccessibilityEventsLocked":
                    param.setResult(false);
                    LogUtils.i(TAG, "[AccessibilityUserState]: " + caller + " method " + param.method.getName() + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                    break;
                default:
                    // LogUtils.v(TAG, "[AccessibilityUserState]: " + caller + " method: " + param.method.getName() + " param: " + Arrays.toString(param.args) + " res: " + param.getResult());
                    break;
            }
        });
    }
}
