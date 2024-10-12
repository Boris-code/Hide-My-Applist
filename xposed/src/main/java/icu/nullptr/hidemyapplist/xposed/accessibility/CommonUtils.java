package icu.nullptr.hidemyapplist.xposed.accessibility;


import android.os.Binder;
import android.text.TextUtils;

import icu.nullptr.hidemyapplist.common.Constants;
import icu.nullptr.hidemyapplist.common.PmsHelper;

public class CommonUtils {
    public static String getCaller() {
        int uid = Binder.getCallingUid();
        String[] callers = PmsHelper.getInstance().getPackagesForUid(uid);
        return callers == null ? null : callers[0];
    }

    public static boolean shouldHide(String caller) {
        if (TextUtils.isEmpty(caller)) return false;

        int uid = Binder.getCallingUid();
        if (uid < 10000) return false;

        if (Constants.globalHiddenApps.contains(caller)) return false;
        if (Constants.packagesShouldNotHide.contains(caller)) return false;

        if (caller.contains("miui") || caller.contains("xiaomi") || caller.contains("youdao") || caller.contains("autojs")) {
            return false;
        }

        return true;
    }
}
