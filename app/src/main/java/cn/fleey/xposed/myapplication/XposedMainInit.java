package cn.fleey.xposed.myapplication;

import cn.fleey.xposed.myapplication.wechat.WeChatHook;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class XposedMainInit implements IXposedHookLoadPackage {

    private WeChatHook mWeChatHookClass;

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.processName.equals("com.tencent.mm")) {
//            XposedBridge.log("init hook wechat：" + lpparam.processName);
            mWeChatHookClass = WeChatHook.instance();
        }
        if (mWeChatHookClass != null && !mWeChatHookClass.isReadyForUse()) {
            mWeChatHookClass.handleLoadPackage(lpparam);
        }
    }


}