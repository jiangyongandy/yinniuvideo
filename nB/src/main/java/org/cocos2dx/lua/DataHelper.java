package org.cocos2dx.lua;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataHelper {
    private static SharedPreferences mSharedPreferences;
    public static final String SP_NAME = "boyi_info_config";

    /**
     * 存储重要信息到sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void setStringSF(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 返回存在sharedPreferences的信息
     *
     * @param key
     * @return
     */
    public static String getStringSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(key, null);
    }

    public static void setBoolSF(Context context, String key, boolean value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolSp(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key, false);
    }

    public static boolean getBoolSp(Context context, String key, boolean def) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key, def);
    }

    /**
     * 存储重要信息到sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void setIntergerSF(Context context, String key, int value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 返回存在sharedPreferences的信息
     *
     * @param key
     * @return
     */
    public static int getIntergerSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, -1);
    }

    /**
     * 返回存在sharedPreferences的信息
     *
     * @param key
     * @return
     */
    public static int getIntergerSF(Context context, String key,int def) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, def);
    }

    /**
     * 清除某个内容
     */
    public static void removeSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(key).commit();
    }

    /**
     * 清除Shareprefrence
     */
    public static void clearShareprefrence(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().clear().commit();
    }

    /**
     * 将对象储存到sharepreference
     *
     * @param key
     * @param device
     * @param <T>
     */
    public static <T> boolean saveDeviceData(Context context, String key, T device) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(device);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            mSharedPreferences.edit().putString(key, oAuth_Base64).commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将对象从shareprerence中取出来
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getDeviceData(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        T device = null;
        String productBase64 = mSharedPreferences.getString(key, null);

        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);

            // 读取对象
            device = (T) bis.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return device;
    }

    /**
     * 返回缓存文件夹
     * rxcache 缓存会自己清理文件夹（程序异常退出时，被系统强制杀死） 所以不能与图片缓存公用一个文件夹
     */
    public static File getCacheFile(Context context) {
/*        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                file = new File(getCacheFilePath(context));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return file;
        } else {
            return context.getCacheDir();
        }*/

            String cachePath = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            return new File(cachePath);
    }

    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡路径
     * <p>先用shell，shell失败再普通方法获取，一般是/storage/emulated/0/</p>
     *
     * @return SD卡路径
     */
    public static String getSDCardPath() {
        if (!isSDCardEnable()) return null;
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context) {

        String cachePath = null;
        String packageName = context.getPackageName();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getAbsolutePath() + packageName;
        } else {
            cachePath = context.getCacheDir().getPath() + packageName;
        }
        File file = new File(cachePath);
        if(!file.exists())
            file.mkdir();
        return cachePath;

    }


    /**
     * 使用递归获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 使用递归删除文件夹
     *
     * @param dir
     * @return
     */
    public static boolean DeleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                DeleteDir(file); // 递归调用继续删除
            }
        }
        return true;
    }


    public static String BytyToString(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int num = 0;
        while ((num = in.read(buf)) != -1) {
            out.write(buf, 0, buf.length);
        }
        String result = out.toString();
        out.close();
        return result;
    }

}
