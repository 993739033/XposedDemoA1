package com.bbny.qifengwlw.xposeddemoa1;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by ZWX on 2019/2/13.
 */

public class Demo1 implements IXposedHookLoadPackage ,IXposedHookInitPackageResources{
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        // 打印装载的apk程序包名
        ///data/data/de.robv.android.xposed.installer/log/debug.log
        XposedBridge.log("Launch app: " + loadPackageParam.packageName);

        if (!loadPackageParam.packageName.equals("com.bbny.qifengwlw"))//判断运行的应用包名
            return;
        //通过类地址和方法名进行监听  可在方法触发前后注入运行代码  方法名后面记得加参数类型
        findAndHookMethod("com.bbny.qifengwlw.Ui.Activity.LoginActivity",
                    loadPackageParam.classLoader, "attempLogin",String.class,String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String arges = ((String) param.args[0]) + param.args[1];
                XposedBridge.log(" app userArges " + arges);
                // this will be called before the clock was updated by the original method
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
            }
        });


        if (loadPackageParam.packageName.equals("com.wrbug.xposeddemo")) {
            XposedHelpers.findAndHookMethod("com.wrbug.xposeddemo.MainActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    //不能通过Class.forName()来获取Class ，在跨应用时会失效
                    Class c=loadPackageParam.classLoader.loadClass("com.wrbug.xposeddemo.MainActivity");
                    Field field=c.getDeclaredField("textView");
                    field.setAccessible(true);//操作私有变量时   私有变量Accessible值默认为false  设置为true才可进行操作
                    //param.thisObject 为执行该方法的对象，在这里指MainActivity
                    TextView textView= (TextView) field.get(param.thisObject);//传入父类实例获取实例
                    textView.setText("Hello Xposed");
                }
            });
        }

        //该实现用于替换hook的方法
//        new XC_MethodReplacement() {
//            @Override
//            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
//                return null;
//            }
//        };



    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) throws Throwable {
        if (initPackageResourcesParam.packageName.equals("com.wrbug.xposeddemo")){
            initPackageResourcesParam.res.hookLayout(initPackageResourcesParam.packageName, "layout", "layout_name", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {//此方法再 setcontentview（） 及inflate（）方法调用时调用
                    View view= layoutInflatedParam.view;//这个为加载时获取的父view
//                    XposedHelpers.findClass()  获取实例
//                    XposedHelpers.findField()  获取实例中的成员变量  只能获取静态的字段？
//                    XposedHelpers.findMethodExact()  获取实例中的方法  只能获取静态的字段？
//                    XposedHelpers.findConstructorExact()  获取Constructor 构造方法 Constructor.newInstance(new Object[]{}); 来创建实例方法

//                    XposedHelpers.getStaticBooleanField()
                    //设置/获取实例的变量的值
//                    public static void setXXXField(Object obj, String fieldName, XXX value)
//                    public static void setStaticXXXField(Class<?> clazz, String fieldName, XXX value)
                    ////设置/获取类型静态的变量的值
//                    public static Xxx getXxxField(Object obj, String fieldName)
//                    public static Xxx getStaticXxxField(Class<?> clazzj, String fieldName)

                    //调用实例方法  Object 表示返回值
//                    public static Object callMethod(Object obj, String methodName, Object... args)
//                    public static Object callMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args)
//
                    //调用类型中的静态方法  Object 表示返回值
//                    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args)
//                    public static Object callStaticMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object... args)
                    //hook监听的方法 最后一位参数为XC_MethodHook对象或其子类
//                    public static XC_MethodHook.Unhook findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback)
//                    //通过className和classLoader获取Class<?> ，再调用上面的方法
//                    public static XC_MethodHook.Unhook findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback)

                    //以下为XC_MethodHook及相关类
//                    public abstract class XC_MethodHook extends XCallback {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            //方法调用前的回调
//                            super.beforeHookedMethod(param);
//                        }
//
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            //方法调用后的回调
//                            super.afterHookedMethod(param);
//                        }
//                    }
//
//                    public abstract class XC_MethodReplacement extends XC_MethodHook{
//                        @Override
//                        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                            //带返回值的方法执行时调用
//                            return null;
//                        }
//                    }

                }
            });
        }


    }
}
