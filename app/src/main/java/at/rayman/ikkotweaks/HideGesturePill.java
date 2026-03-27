package at.rayman.ikkotweaks;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.graphics.Canvas;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

//using https://github.com/DanGLVK/Hide-Navbar instead
public class HideGesturePill implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("hide gesture pill module loaded");
        if (!lpparam.packageName.equals("com.android.systemui")) {
            return;
        }
        XposedBridge.log("inside systemui resources");
        findAndHookMethod("com.android.systemui.navigationbar.gestural.NavigationHandle", lpparam.classLoader, "onDraw", Canvas.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) {
                return null;
            }
        });
    }

}
