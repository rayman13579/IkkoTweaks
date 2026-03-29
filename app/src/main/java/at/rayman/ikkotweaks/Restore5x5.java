package at.rayman.ikkotweaks;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

//Wasn't able to replace the whole device_profiles.xml with magisk or xpsoed
public class Restore5x5 implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("launcher grid module loaded");
        if (!lpparam.packageName.equals("com.android.launcher3")) {
            return;
        }
        XposedBridge.log("inside launcher");
        XposedHelpers.findAndHookConstructor("com.android.launcher3.InvariantDeviceProfile.GridOption", lpparam.classLoader,
            "android.content.Context", "android.util.AttributeSet",
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    XposedBridge.log("inside GridOption constructor");
                    //5x5 still exists but the xml is just modified to be 4x4, revert that modification
                    if ("5_by_5".equals(XposedHelpers.getObjectField(param.thisObject, "name"))) {
                        XposedHelpers.setIntField(param.thisObject, "numRows", 5);
                        XposedHelpers.setIntField(param.thisObject, "numColumns", 5);
                        XposedHelpers.setIntField(param.thisObject, "numHotseatIcons", 5);
                        XposedHelpers.setIntField(param.thisObject, "numDatabaseHotseatIcons", 5);
                        XposedHelpers.setIntField(param.thisObject, "numAllAppsColumns", 5);
                        XposedHelpers.setIntField(param.thisObject, "numDatabaseAllAppsColumns", 5);
                        XposedHelpers.setIntField(param.thisObject, "numSearchContainerColumns", 5);
                    }
                }
            }
        );
    }

}
