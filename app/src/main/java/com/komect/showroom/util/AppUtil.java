package com.komect.showroom.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * App相关信息的获取
 * Author by hf
 * Create on 16/11/16
 */
public class AppUtil {
    /**
     * 获取当前客户端版本信息
     */
    public static String getCurrentVersionName(Context context) {
        String curVersionName = "?.?.?";
        try {
            PackageInfo info = context.getPackageManager()
                                      .getPackageInfo(context.getPackageName(), 0);
            curVersionName = info.versionName;
            curVersionName = curVersionName.substring(0, curVersionName.lastIndexOf("."));
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return curVersionName;
    }

    /**
     * 获得当前ap的包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        String pkgName = "";

        ApplicationInfo appInfo = context.getApplicationInfo();

        if (appInfo != null) {
            pkgName = appInfo.packageName;
        }
        return pkgName;
    }

    public static String getPhoneIp() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIPAddre = intf.getInetAddresses(); enumIPAddre
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddre.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toLowerCase();
                    }
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return "127.0.0.1";
    }


    /**
     * 活的当前的网络状态， 精确的子类型。
     *
     * @param context
     * @return
     */
    public static String getSubNetWorkType(Context context) {
        String netType;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        manager.getActiveNetworkInfo();
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;

        if (info != null) {
            wifiState = info.getState();
        }

        info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (info != null) {
            Log.d("network ", " info.getSubtype()  " + info.getSubtype() + " info.getSubtypeName" +
                    "() =  " + info.getSubtypeName());
            mobileState = info.getState();
        }
        if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            TelephonyManager teleMan = (TelephonyManager) context.getSystemService(Context
                    .TELEPHONY_SERVICE);
            int networkType = teleMan.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return "1xRTT";
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return "CDMA";
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return "EDGE";
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return "eHRPD";
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return "EVDO rev. 0";
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return "EVDO rev. A";
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return "EVDO rev. B";
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return "GPRS";
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return "HSDPA";
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return "HSPA";
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "HSPA+";
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return "HSUPA";
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "iDen";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "LTE";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return "UMTS";
                default:
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return "Unknown";
            }

        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED !=
                wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            return "offline";
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            return "wifi";
        }
        return "offline";
    }


    public static Point getScreenSize(Context context) {
        Point p = new Point();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        p.set(width, height);

        return p;
    }

    public static double getScreenInches(Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double x = Math.pow(width, 2);
        double y = Math.pow(height, 2);
        double diagonal = Math.sqrt(x + y);

        int dens = dm.densityDpi;
        double screenInches = diagonal / (double) dens;

        Log.d("context", "The screenInches " + screenInches);

        return screenInches;
    }

    /**
     * 获得当前app的名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = "";
        PackageManager pm = context.getPackageManager();
        try {
            appName = pm.getApplicationInfo(context.getPackageName(), 0).loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            appName = context.getPackageName();
        }
        return appName;
    }

    private static String sApkChannel = null;

    /**
     * 从apk中获取版本信息
     * 依次按照如下顺序查找渠道名 没有则用下一个策略，有则返回。
     * 1、 默认从apk包下的"META-INF 查找 dtchannels 开头的文件。
     * 2、  DUOTIN_CHANNEL
     * 3、 UMENG_CHANNEL
     *
     * @param context 上下文
     * @return
     * @see #getChannelFromApk(Context, String, String, String)
     */
    public static String getChannelFromApk(Context context) {
        return getChannelFromApk(context, null, null, null);
    }

    /**
     * 从apk中获取版本信息
     * 依次按照如下顺序查找渠道名 没有则用下一个策略，有则返回。
     * 1、 默认从apk包下的"META-INF 查找keyStr （为空用默认 dtchannels) 开头的文件。采用 splitStr 分割  splitStr 后的作为渠道名
     * 2、 清单里的参数  defaultMetaDataKeyString的值，如果为空采用默认值 DUOTIN_CHANNEL
     * 3、 UMENG_CHANNEL
     *
     * @param context                  上下文
     * @param keyStr                   识别符
     * @param splitStr                 分隔符
     * @param defaultMetaDataKeyString 默认的清单meta data数据键字符串
     * @return
     */
    public static String getChannelFromApk(Context context, String keyStr, String splitStr,
                                           String defaultMetaDataKeyString) {
        long sT = System.nanoTime();

        if (TextUtils.isEmpty(sApkChannel)) {

            if (TextUtils.isEmpty(keyStr)) {
                keyStr = "dtchannels";
            }
            //从apk包中获取
            ApplicationInfo appinfo = context.getApplicationInfo();
            String sourceDir = appinfo.sourceDir;
            //注意这里：默认放在meta-inf/里， 所以需要再拼接一下
            String key = "META-INF/" + keyStr;
            String ret = "";
            ZipFile zipfile = null;
            try {
                zipfile = new ZipFile(sourceDir);
                Enumeration<?> entries = zipfile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();

                    if (entryName.startsWith(key)) {
                        ret = entryName;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (zipfile != null) {
                    try {
                        zipfile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (TextUtils.isEmpty(splitStr)) {
                splitStr = "_";
            }

            String[] split = ret.split(splitStr);

            if (split != null && split.length >= 2) {
                sApkChannel = ret.substring(split[0].length() + 1);
            }

            if (TextUtils.isEmpty(defaultMetaDataKeyString)) {
                defaultMetaDataKeyString = "DUOTIN_CHANNEL";
            }

            if (TextUtils.isEmpty(sApkChannel)) {
                sApkChannel = AppUtil.getAppMetaData(context, defaultMetaDataKeyString);
            }

            if (TextUtils.isEmpty(sApkChannel)) {
                sApkChannel = AppUtil.getAppMetaData(context, "UMENG_CHANNEL");
            }
        }

        return sApkChannel;
    }

    /**
     * 获得app meta data 参数
     *
     * @param context
     * @param metaTagStr
     * @return
     */
    public static String getAppMetaData(Context context, String metaTagStr) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String value = "";

        if (appInfo != null) {
            value = appInfo.metaData.getString(metaTagStr);
        }
        return value;
    }

    /**
     * 获取版本号
     */
    public static int getCurrentVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo info = context.getPackageManager()
                                      .getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return versionCode;
    }


    /**
     * 获得本地MAC地址
     */
    public static String getWifiMac(Context context) {
        String wifiMac = "";
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            wifiMac = info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiMac;
    }


    public static String getDeviceId(Context context) {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }


    public static String getDeviceKey(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("deviceInfo", Context.MODE_PRIVATE);
//        String key = sp.getString("device_key", "");
        String key = "";
        if (!checkDeviceKey(key)) {
            key = getDeviceId(context);
            if (!checkDeviceKey(key)) {
                key = getWifiMac(context);
            }
            if (!checkDeviceKey(key)) {
                key = UUID.randomUUID().toString();
            }
//            sp.edit().putString("device_key", key).commit();
        }
        return key;
    }

    /**
     * 检查设备ID 是否合法
     */
    private static boolean checkDeviceKey(String key) {
        if ("00:00:00:00:00:00".equals(key)) {
            return false;
        }
        return !TextUtils.isEmpty(key) && key.length() >= 6;
    }

    /**
     * 判断apk是否开启了debug功能
     */
    public static boolean isApkDebugable(Context context, String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_ACTIVITIES);
            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
