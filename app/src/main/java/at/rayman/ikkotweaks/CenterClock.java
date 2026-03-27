package at.rayman.ikkotweaks;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

//To crash/restart systemui: adb shell am crash com.android.systemui
//To uninstall module/app: adb shell pm uninstall at.rayman.ikkotweaks
public class CenterClock implements IXposedHookInitPackageResources {

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        XposedBridge.log("center clock module loaded");
        if (!resparam.packageName.equals("com.android.systemui")) {
            return;
        }
        XposedBridge.log("inside systemui resources");
        resparam.res.hookLayout("com.android.systemui", "layout", "status_bar", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) {
                XposedBridge.log("inflated status bar layout");
                try {
                    LinearLayout statusBar = (LinearLayout) getView(liparam, "status_bar_contents");

                    View cutoutSpace = getView(liparam, "cutout_space_view");
                    statusBar.removeView(cutoutSpace);

                    View clock = getView(liparam, "clock");
                    ((LinearLayout) clock.getParent()).removeView(clock);

                    LinearLayout linearLayout = new LinearLayout(statusBar.getContext());
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    linearLayout.addView(clock);
                    statusBar.addView(linearLayout, 1);
                } catch (Exception e) {
                    XposedBridge.log(e);
                    XposedBridge.log("Error centering clock: " + e.getMessage());
                }
            }
        });
    }

    private View getView(XC_LayoutInflated.LayoutInflatedParam liparam, String id) {
        return liparam.view.findViewById(liparam.res.getIdentifier(id, "id", "com.android.systemui"));
    }

}
