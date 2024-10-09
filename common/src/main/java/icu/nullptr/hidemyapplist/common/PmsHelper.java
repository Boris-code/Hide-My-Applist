package icu.nullptr.hidemyapplist.common;

import android.content.pm.IPackageManager;
import android.os.IBinder;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class PmsHelper {

    private static final PmsHelper INSTANCE = new PmsHelper();

    private final Map<Integer, String[]> cache = new HashMap<>();

    public IPackageManager pms;

    public static PmsHelper getInstance() {
        return INSTANCE;
    }

    public void register(IPackageManager pms) {
        this.pms = pms;
    }

    public void register(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("android.os.ServiceManager", classLoader,
                "addService", String.class, IBinder.class, boolean.class, int.class, new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) {
                        String tag = (String) param.args[0];
                        if (TextUtils.equals(tag, "package")) {
                            PmsHelper.getInstance().pms = (IPackageManager) param.args[1];
                        }
                    }
                });
    }

    public String[] getPackagesForUid(int uid) {
        if (pms == null) return null;
        if (cache.containsKey(uid)) {
            return cache.get(uid);
        }
        try {
            String[] result = pms.getPackagesForUid(uid);
            if (result != null && result.length > 0) {
                cache.put(uid, result);
            }
            return result;
        } catch (Exception e) {
            // nothing
        }
        return null;
    }

}
